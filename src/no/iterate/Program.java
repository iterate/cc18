package no.iterate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

class Program {

    private CompilationUnit compilationUnit = new CompilationUnit("no.iterate");
    private ClassOrInterfaceDeclaration currentClass;
    private MethodDeclaration currentMethod;

    public String toString() {
        return compilationUnit.toString();
    }

    public Program addClass(String className) {
        currentClass = compilationUnit.addClass(className);
        return this;
    }

    public Program addMethod(String methodName) {
        currentMethod = currentClass.addMethod(methodName);
        return this;
    }

    public Program addParameter(String parameterType, String parameterName) {
        currentMethod.addParameter(JavaParser.parseTypeParameter(parameterType), parameterName);
        return this;
    }

    public Program addReturnType(String returnType) {
        currentMethod.setType(JavaParser.parseTypeParameter(returnType));
        return this;
    }

    public Program addMethodBody(String s) {
        final BlockStmt block = new BlockStmt();
        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        call.addArgument(new StringLiteralExpr("Hello World!"));
        ReturnStmt returnStmt = new ReturnStmt("1");

        block.addStatement(call);
        block.addStatement(returnStmt);
        currentMethod.setBody(block);
        return this;
    }

    public Program addMethodReturnStmt(String returnString) {
        final BlockStmt block = new BlockStmt();
        ReturnStmt returnStmt = new ReturnStmt(returnString);
        block.addStatement(returnStmt);
        currentMethod.setBody(block);
        return this;
    }

    public String run() {
        System.out.println(this.toString());
        try (PrintWriter out = new PrintWriter("FizzBuzz.java")) {
            out.println(toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            runProcess("pwd");
            System.out.println("**********");
            runProcess("javac -cp FizzBuzz.java");
            System.out.println("**********");
            runProcess("java -cp FizzBuzz");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //compiler.run(new ByteArrayInputStream(toString().getBytes()), System.out, System.err);
        return this.toString();
    }


    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    public static Program script(String script){
        Program program = new Program();
        //script.lines();
        return null;
    };
}

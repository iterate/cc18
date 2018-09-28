package no.iterate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.EnumSet;

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

    public Program addMethodToClass(int node, String methodName) {
        currentClass = (ClassOrInterfaceDeclaration) compilationUnit.getChildNodes().get(node);

        System.out.println("------");

        for (int i = 0; i < compilationUnit.getChildNodes().size(); i++) {
            System.out.println(compilationUnit.getChildNodes().get(i));
        }

        System.out.println("------");

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

    public Program addMethodReturnStmt(String returnString) {
        final BlockStmt block = new BlockStmt();
        ReturnStmt returnStmt = new ReturnStmt(returnString);
        block.addStatement(returnStmt);
        currentMethod.setBody(block);
        return this;
    }

    public Program makeStatic() {
        return getProgram(Modifier.STATIC);
    }

    public Program makePublic() {
        return getProgram(Modifier.PUBLIC);
    }

    private Program getProgram(Modifier modifier) {
        final EnumSet<Modifier> modifiers = currentMethod.getModifiers();
        modifiers.add(modifier);

        currentMethod.setModifiers(modifiers);
        return this;
    }

    public String run() {
        System.out.println(this.toString());
        try (PrintWriter out = new PrintWriter("src/no/iterate/FizzBuzz.java")) {
            out.println(toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            runProcess("pwd");
            System.out.println("**********");
            runProcess("javac -classpath javaparser-symbol-solver-core-3.6.23.jar:javaparser-core-3.6.23.jar src/no/iterate/ProgramTests.java src/no/iterate/Program.java src/no/iterate/Tester.java src/no/iterate/Reporter.java src/no/iterate/Testable.java src/no/iterate/TestResults.java src/no/iterate/Tests.java src/no/iterate/CodeCamp.java -d out/production/cc18/ && \\\n");
            System.out.println("**********");
            runProcess("java -cp FizzBuzz");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}

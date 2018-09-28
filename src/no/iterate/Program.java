package no.iterate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.github.javaparser.ast.type.PrimitiveType.*;

class Program {

    private CompilationUnit compilationUnit = new CompilationUnit();
    private ClassOrInterfaceDeclaration currentClass;
    private MethodDeclaration currentMethod;

    public String toString() {
        return compilationUnit.toString();
    }

    public Program addClass(String className) {
        currentClass = compilationUnit.addClass(className);
        return this;
    }

    public Program setPackage(String packageName){
        compilationUnit.setPackageDeclaration(packageName);
        return this;}

    public Program addMethod(String methodName) {
        currentMethod = currentClass.addMethod(methodName);
        return this;
    }

    public Program selectMethod(String methodName) {
        currentMethod = currentClass.getMethodsByName(methodName).get(0);
        return this;
    }

    public Program addMethodToClass(int node, String methodName) {
        currentClass = (ClassOrInterfaceDeclaration) compilationUnit.getChildNodes().get(node);
        currentMethod = currentClass.addMethod(methodName);
        return this;
    }

    public void printChildrenRecursively() {
        System.out.println("Nodes:");
        printNodes(compilationUnit.getChildNodes());
    }

    void printNodes(List<Node> nodes){
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            if(node instanceof ClassOrInterfaceDeclaration){
                System.out.println("" + i + "\t"+((ClassOrInterfaceDeclaration)node).getName());
            }
            printNodes(node.getChildNodes());
        }
    }

    public Program addParameter(String parameterType, String parameterName, boolean isVarArgs) {
        final TypeParameter type = JavaParser.parseTypeParameter(parameterType);
        return addParameter(type, parameterName, isVarArgs);
    }

    private Program addParameter(Type type, String parameterName, boolean isVarArgs) {
        final Parameter parameter = new Parameter(type, parameterName);
        parameter.setVarArgs(isVarArgs);

        currentMethod.addParameter(parameter);
        return this;
    }

    public Program addPrimitiveParameter(Primitive parameterType, String parameterName) {
        final PrimitiveType primitiveType = new PrimitiveType(parameterType);

        addParameter(primitiveType, parameterName, false);
        return this;
    }


    public Program changeSignatureAddParameter(Type type, String name, Object defaultValue) {
        addParameter(type, name, false);

        return this;
    }

    public Program addReturnType(String returnType) {
        currentMethod.setType(JavaParser.parseTypeParameter(returnType));
        return this;
    }

    public Program printMethodResult(String methodName) {
        final BlockStmt block = currentMethod.getBody().orElse(new BlockStmt());

        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        call.addArgument(new MethodCallExpr(methodName));

        block.addStatement(call);
        currentMethod.setBody(block);


        return this;
    }

    public Program callMethod(String methodName) {
        final BlockStmt block = currentMethod.getBody().orElse(new BlockStmt());
        block.addStatement(new MethodCallExpr(methodName));
        currentMethod.setBody(block);

        return this;
    }

    public Program addMethodReturnStmt(String returnString) {
        final BlockStmt block = currentMethod.getBody().orElse(new BlockStmt());
        ReturnStmt returnStmt = new ReturnStmt(returnString);
        block.addStatement(returnStmt);
        currentMethod.setBody(block);
        return this;
    }

    public Program makeStatic() {
        return addParameter(Modifier.STATIC);
    }

    public Program makePublic() {
        return addParameter(Modifier.PUBLIC);
    }

    private Program addParameter(Modifier modifier) {
        final EnumSet<Modifier> modifiers = currentMethod.getModifiers();
        modifiers.add(modifier);
        currentMethod.setModifiers(modifiers);
        return this;
    }

    public String run() {
        System.out.println(this.toString());
        String packageName = compilationUnit.getPackageDeclaration().get().getNameAsString();
        String className = currentClass.getNameAsString();
        String fileName = "src/" + packageName.replace(".", "/") + "/" + className + ".java";
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String returnString = "";

        try {
            System.out.println("**********");
            runProcess("javac "+ fileName + " -d out/production/cc18/");
            System.out.println("**********");
            returnString = runProcess("java -classpath out/production/cc18 "+ packageName + "." + className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return returnString;
    }

    public Program peek(Consumer<Program> lambda) {
        lambda.accept(this);
        return this;
    }

    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }

    private static String runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);

        InputStream inputStream = pro.getInputStream();

        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());

        if (pro.exitValue() != 0) {
            throw new RuntimeException("runProcess() terminated with a non zero exitcode");

        }

        String result = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        System.out.println(result);

        return result;
    }
}

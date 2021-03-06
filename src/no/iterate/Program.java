package no.iterate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;

import java.io.*;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.github.javaparser.ast.type.PrimitiveType.Primitive;

class Program {
    public CompilationUnit compilationUnit = new CompilationUnit();
    public Node cursor;
    public Node cursor2;

    public String toString() {
        return compilationUnit.toString();
    }

    public Program addClass(String className) {
        cursor = compilationUnit.addClass(className);
        cursor2 = cursor;
        return this;
    }

    public Program addClass(CompilationUnit cu, String className) {
        cu.addClass(className);
        return this;
    }

    public Program setPackage(String packageName){
        setPackage(compilationUnit, packageName);
        return this;
    }

    public Program setPackage(CompilationUnit cu, String packageName){
        cu.setPackageDeclaration(packageName);
        return this;
    }

    public Program addMethod(String methodName) {
            cursor2 = ((ClassOrInterfaceDeclaration) cursor).addMethod(methodName);
        return this;
    }

    public Program selectMethod(String methodName) {
            cursor2 = ((ClassOrInterfaceDeclaration) cursor).getMethodsByName(methodName).get(0);
        return this;
    }

    public Program setCursorToCurrentClass(){
        while (cursor2 instanceof ClassOrInterfaceDeclaration){
            cursor2 = cursor2.getParentNode().get();
        }
        return this;
    }

    public Program addMethodToClass(int node, String methodName) {
        cursor2 = compilationUnit.getChildNodes().get(node);

        cursor2 = ((ClassOrInterfaceDeclaration) cursor2).addMethod(methodName);
        return this;
    }

    public void printChildrenRecursively() {
        printChildrenRecursively(this);
    }

    public void printChildrenRecursively(Program program) {
        System.out.println("Nodes:");
        ProgramPrinter.printNodes(program.compilationUnit.getChildNodes(), 0);
    }

    public Program addParameter(String parameterType, String parameterName, boolean isVarArgs) {
        final TypeParameter type = JavaParser.parseTypeParameter(parameterType);
        return addParameter(type, parameterName, isVarArgs);
    }

    private Program addParameter(Type type, String parameterName, boolean isVarArgs) {
        final Parameter parameter = new Parameter(type, parameterName);
        parameter.setVarArgs(isVarArgs);

        ((MethodDeclaration) cursor2).addParameter(parameter);

        return this;
    }

    public Program addPrimitiveParameter(Primitive parameterType, String parameterName) {
        final PrimitiveType primitiveType = new PrimitiveType(parameterType);

        addParameter(primitiveType, parameterName, false);
        return this;
    }


    public Program changeSignatureAddParameter(Type type, String name, Object defaultValue) {
        addParameter(type, name, false);

        final List<MethodCallExpr> allMethodCalls = cursor
                .findAll(
                        MethodCallExpr.class,
                        methodCallExpr -> methodCallExpr
                                .getName()
                                .asString()
                                .equals(((MethodDeclaration) cursor2).getName().toString())
                );
        
        allMethodCalls.stream().map(call -> call.asMethodCallExpr()).forEach(c -> {
            final NodeList<Expression> arguments = c.getArguments();
            arguments.addLast(new IntegerLiteralExpr((Integer) defaultValue));
            c.setArguments(arguments);
        });

        return this;
    }

    public Program setReturnType(String returnType) {
        ((MethodDeclaration) cursor2).setType(JavaParser.parseTypeParameter(returnType));
        return this;
    }

    public Program printMethodResult(String methodName) {

        final BlockStmt block = ((MethodDeclaration) cursor2).getBody().orElse(new BlockStmt());

        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        call.addArgument(new MethodCallExpr(methodName));

        block.addStatement(call);
        ((MethodDeclaration) cursor2).setBody(block);

        return this;
    }

    public Program printMethodResult(String methodName, int argument) {

        if(cursor2 instanceof MethodDeclaration) {

            final BlockStmt block = ((MethodDeclaration) cursor2).getBody().orElse(new BlockStmt());

            NameExpr clazz = new NameExpr("System");
            FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
            MethodCallExpr call = new MethodCallExpr(field, "println");
            call.addArgument(new MethodCallExpr(methodName).addArgument(new IntegerLiteralExpr(argument)));

            block.addStatement(call);
            ((MethodDeclaration) cursor2).setBody(block);

        }
        return this;
    }

    public Program setMethodReturnStmt(String returnString) {
        final BlockStmt block = ((MethodDeclaration) cursor2).getBody().orElse(new BlockStmt());
        ReturnStmt returnStmt = new ReturnStmt(returnString);
        block.addStatement(returnStmt);
        ((MethodDeclaration) cursor2).setBody(block);
        return this;
    }


    public Program setMethodReturnStmt(MethodCallExpr methodCallExpr) {
        final BlockStmt block = new BlockStmt();
        ReturnStmt returnStmt = new ReturnStmt(methodCallExpr);

        final NodeList<Statement> statements = block.getStatements()
                .stream()
                .filter(statement -> !statement.isReturnStmt())
                .collect(NodeList.toNodeList());

        statements.add(returnStmt);
        block.setStatements(statements);

        ((MethodDeclaration) cursor2).setBody(block);

        return this;
    }

    public Program makeStatic() {
        return addParameter(Modifier.STATIC);
    }

    public Program makePublic() {
        return addParameter(Modifier.PUBLIC);
    }

    private Program addParameter(Modifier modifier) {
        final EnumSet<Modifier> modifiers = ((MethodDeclaration) cursor2).getModifiers();
        modifiers.add(modifier);
        ((MethodDeclaration) cursor2).setModifiers(modifiers);
        return this;
    }

    public String run() {
        System.out.println(this.toString());
        String packageName = compilationUnit.getPackageDeclaration().get().getNameAsString();


        String className = null;

        if(cursor instanceof ClassOrInterfaceDeclaration)
             className = ((ClassOrInterfaceDeclaration) cursor).getNameAsString();


        String fileName = "src/" + packageName.replace(".", "/") + "/" + className + ".java";
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String returnString;

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
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        in.lines().map(line -> cmd + " " + line).forEach(System.out::println);
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

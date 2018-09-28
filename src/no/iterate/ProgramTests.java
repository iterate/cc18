package no.iterate;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.PrimitiveType;

import static no.iterate.Tests.assume;

public class ProgramTests {
    public static final Runnable INTEGRATION_TEST = () -> assume(new Program()
            .setPackage("no.iterate")
            .addClass("FizzBuzz")
            .addMethod("main")
            .peek(p -> System.out.println(p.cursor.getClass()))
            .makeStatic()
            .makePublic()
            .addParameter("String", "args", true)
            .printMethodResult("calculate")
            .setCursorToCurrentClass()
            .addMethod("calculate")
            .makeStatic()
            .setReturnType("String")
            .setMethodReturnStmt("\"1\"")
            .selectMethod("calculate")
            .changeSignatureAddParameter(new PrimitiveType(PrimitiveType.Primitive.INT), "input", 1)
            .setMethodReturnStmt(new MethodCallExpr(new NameExpr("String"), "valueOf").addArgument("input"))
            .selectMethod("main")
            .printMethodResult("calculate", 2)
            .run()
            .equals("1\n2"), "Method should return what we give as input");


    public static final Runnable PROGRAM_SHOULD_CONTAIN_CLASS_FIZZBUZZ = () -> assume(new Program()
            .addClass("FizzBuzz") // sets cursor to class
            .toString()
            .contains("class FizzBuzz"), "Program should contain class FizzBuzz");

    public static final Runnable ADD_METHOD = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate") // sets cursor to method
            .toString()
            .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");

    public static final Runnable ADD_PARAMETER = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .addPrimitiveParameter(PrimitiveType.Primitive.INT, "input")
            .toString()
            .contains("calculate(int input)"));

    public static final Runnable ADD_SECOND_CLASS = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addClass("AnotherClass")
            .toString()
            .contains("class FizzBuzz"), "Program should contain class FizzBuzz even when AnotherClass has been added");

    public static final Runnable SET_RETURN_TYPE = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .setReturnType("String")
            .toString()
            .contains("String calculate("));

    public static final Runnable ADD_RETURN = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .setMethodReturnStmt("\"1\"")
            .toString()
            .contains("return \"1\";"));

    public static final Runnable ADD_MAIN_METHOD = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("main")
            .makeStatic()
            .makePublic()
            .addParameter("String", "args", true)
            .toString()
            .contains("public static void main(String... args"));

    public static final Runnable ADD_METHOD_TO_SPECIFIC_CLASS = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethodToClass(0, "calculate")
            .toString()
            .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");

    public static final Runnable ADD_MULTIPLE_METHODS = () -> {
        final String program = new Program()
                .addClass("FizzBuzz")
                .addMethod("method1")
                .setCursorToCurrentClass()
                .addMethod("method2")
                .toString();

        assume(program.contains("method1()"), "Adding multiple methods, the first one is still there");
        assume(program.contains("method2()"), "Adding multiple methods, the last one is added");
    };

    public static final Runnable ADD_MULTIPLE_METHODS_CHAGE_FIRST = () -> {
        final String program = new Program()
                .addClass("FizzBuzz")
                .addMethod("method1")
                .setCursorToCurrentClass()
                .addMethod("method2")
                .toString();

        assume(program.contains("method1()"), "Adding multiple methods, the first one is still there");
        assume(program.contains("method2()"), "Adding multiple methods, the last one is added");
    };
}

package no.iterate;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.PrimitiveType;

import static no.iterate.Tests.assume;

public class ProgramTests {
    public static final Runnable PROGRAM_SHOULD_CONTAIN_CLASS_FIZZBUZZ = () -> assume(new Program()
            .addClass("FizzBuzz")
            .toString()
            .contains("class FizzBuzz"), "Program should contain class FizzBuzz");
    public static final Runnable ADD_METHOD = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
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

    MethodCallExpr returnStatment = new MethodCallExpr(new NameExpr("String"), "valueOf").addArgument("input");

    public static final Runnable RUN_THE_CODE = () -> assume(new Program()
            .setPackage("no.iterate")
            .addClass("FizzBuzz")
            .addMethod("main")
            .makeStatic()
            .makePublic()
            .addParameter("String", "args", true)
            .printMethodResult("calculate")
            .addMethod("calculate")
            .makeStatic()
            .setReturnType("String")
            .setMethodReturnStmt("\"2\"")
            .peek(Program::printChildrenRecursively)
            .changeSignatureAddParameter(new PrimitiveType(PrimitiveType.Primitive.INT), "input", 0)
            .setMethodReturnStmt(new MethodCallExpr(new NameExpr("String"), "valueOf").addArgument("input"))
            .run()
            .equals("0"), "Method should return what we give as input");

    public static final Runnable ADD_MAIN_METHOD = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("main")
            .makeStatic()
            .makePublic()
            .addParameter("String", "args", true)
            .peek(Program::printChildrenRecursively)
            .toString()
            .contains("public static void main(String... args"));

    public static final Runnable ADD_METHOD_TO_SPECIFIC_CLASS = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethodToClass(0, "calculate")
            .toString()
            .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");
}

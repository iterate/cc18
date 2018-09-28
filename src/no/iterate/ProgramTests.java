package no.iterate;

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
            .addReturnType("String")
            .toString()
            .contains("String calculate("));
    public static final Runnable ADD_RETURN = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .addMethodReturnStmt("\"1\"")
            .toString()
            .contains("return \"1\";"));
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
            .addReturnType("String")
            .addMethodReturnStmt("\"2\"")
            .changeSignatureAddParameter(new PrimitiveType(PrimitiveType.Primitive.INT))
            //.addPrimitiveParameter(PrimitiveType.Primitive.INT, "input")
            .selectMethod("main")
            .run()
            .equals("2"), "Method should return \"2\" like we said");

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
            .peek(Program::printChildrenRecursively)
            .toString()
            .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");
}

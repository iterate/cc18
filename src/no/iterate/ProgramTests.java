package no.iterate;

import com.github.javaparser.ast.CompilationUnit;

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
            .addParameter("INT", "input")
            .toString()
            .contains("calculate(INT input)"));
    public static final Runnable ADD_SECOND_CLASS = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addClass("AnotherClass")
            .toString()
            .contains("class FizzBuzz"), "Program should contain class FizzBuzz even when AnotherClass has been added");
    public static final Runnable SET_RETURN_TYPE = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .addReturnType("STRING")
            .toString()
            .contains("STRING calculate("));
    public static final Runnable ADD_RETURN = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .addMethodReturnStmt("1")
            .toString()
            .contains("return 1;"));
    public static final Runnable RUN_THE_CODE = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .addReturnType("STRING")
            .addMethodReturnStmt("1")
            .run()
            .contains("return 1;"), "Method should return 1 like we said");


}

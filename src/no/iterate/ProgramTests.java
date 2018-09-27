package no.iterate;

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
}

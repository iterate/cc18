package no.iterate;

import java.util.ArrayList;
import java.util.List;

import static no.iterate.Tests.assume;

public class CodeCamp {

    public static final Runnable RUNNABLE = () -> assume(new Program()
            .addClass("FizzBuzz")
            .addMethod("calculate")
            .toString()
            .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.FizzBuzz());

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> {});
        functionTests.add(Tests.correctErrorMessage);
        functionTests.add(Tests.correctAssertErrorMessage2);

        functionTests.add(ProgramTests.PROGRAM_SHOULD_CONTAIN_CLASS_FIZZBUZZ);

        functionTests.add(RUNNABLE);

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .addMethod("calculate")
                .addParameter("INT", "input")
                .toString()
                .contains("calculate(INT input)")));

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .addClass("AnotherClass")
                .toString()
                .contains("class FizzBuzz"), "Program should contain class FizzBuzz even when AnotherClass has been added"));

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .addMethod("calculate")
                .addReturnType("STRING")
                .toString()
                .contains("STRING calculate(")));
        

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .addMethod("calculate")
                .addMethodReturnStmt("1")
                .toString()
                .contains("return 1;")));

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .addMethod("calculate")
                .addReturnType("STRING")
                .addMethodReturnStmt("1")
                .run()
                .contains("return 1;"), "Method should return 1 like we said"));


        Reporter.report(Tester.runTests(tests, functionTests));
    }

}

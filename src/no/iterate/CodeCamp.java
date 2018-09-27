package no.iterate;

import java.util.ArrayList;
import java.util.List;

import static no.iterate.Tests.assume;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.FizzBuzz());

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> {});
        functionTests.add(Tests.correctErrorMessage);
        functionTests.add(Tests.correctAssertErrorMessage2);

        functionTests.add(ProgramTests.PROGRAM_SHOULD_CONTAIN_CLASS_FIZZBUZZ);
        functionTests.add(ProgramTests.ADD_METHOD);

        functionTests.add(ProgramTests.ADD_PARAMETER);

        functionTests.add(ProgramTests.ADD_SECOND_CLASS);

        functionTests.add(ProgramTests.SET_RETURN_TYPE);
        

        functionTests.add(ProgramTests.ADD_RETURN);

        functionTests.add(ProgramTests.RETURN_VALUE);


        Reporter.report(Tester.runTests(tests, functionTests));
    }

}

package no.iterate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static no.iterate.ProgramTests.*;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.FizzBuzz());

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> {});
        functionTests.add(Tests.correctErrorMessage);
        functionTests.add(Tests.correctAssertErrorMessage2);

        functionTests.add(PROGRAM_SHOULD_CONTAIN_CLASS_FIZZBUZZ);
        functionTests.add(ADD_METHOD);
        functionTests.add(ADD_PARAMETER);
        functionTests.add(ADD_SECOND_CLASS);
        functionTests.add(SET_RETURN_TYPE);
        functionTests.add(ADD_RETURN);
        functionTests.add(RETURN_VALUE);

        functionTests.addAll(testClass());

        Reporter.report(Tester.runTests(tests, functionTests));
        testClass();
    }

    static List<Runnable> testClass() {
        Field[] fields = ProgramTests.class.getFields();
        List<Runnable> tests = new ArrayList<>();
        for (Field field : fields) {
            try {
                tests.add((Runnable) field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return tests;
    }

}

package no.iterate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.FizzBuzz());

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> {});
        functionTests.add(Tests.correctErrorMessage);
        functionTests.add(Tests.correctAssertErrorMessage2);

        functionTests.addAll(testClass(ProgramTests.class));

        Reporter.report(Tester.runTests(tests, functionTests));
        testClass(ProgramTests.class);
    }

    static List<Runnable> testClass(Class<ProgramTests> aClass) {
        Field[] fields = aClass.getFields();
        List<Runnable> tests = new ArrayList<>();
        stream(fields).forEach(field -> {
            try {
                tests.add((Runnable) field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return tests;
    }

}

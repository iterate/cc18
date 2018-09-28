package no.iterate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.FizzBuzz());

        List<Runnable> functionalTest = testClass(ProgramTests.class);
        functionalTest.addAll(List.of(
                () -> {},
                Tests.correctErrorMessage,
                Tests.correctAssertErrorMessage2));

        Reporter.report(Tester.runTests(tests, functionalTest));
        testClass(ProgramTests.class);
    }

    static List<Runnable> testClass(Class<ProgramTests> aClass) {
        List<Runnable> tests = new ArrayList<>();
        stream(aClass.getFields()).forEach(field -> {
            try {
                tests.add((Runnable) field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return tests;
    }

}

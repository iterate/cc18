package no.iterate;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    public static Runnable integrationTest = () -> {};

    public static class FizzBuzz implements Testable {


        public void invoke() {
            assume(fizzBuzz(1).equals("1"));
            assume(fizzBuzz(2).equals("2"));
            assume(fizzBuzz(3).equals("Fizz!"));
            assume(fizzBuzz(5).equals("Buzz!"));
            assume(fizzBuzz(15).equals("FizzBuzz!"));
        }

        private String fizzBuzz(int i) {
            if (i % 3 == 0 && i % 5 == 0) {
                return "FizzBuzz!";
            } else if (i % 3 == 0) {
                return "Fizz!";
            } else if (i % 5 == 0) {
                return "Buzz!";
            }

            return String.valueOf(i);
        }
    }

    public static Runnable correctErrorMessage = () -> {
        List<Testable> tests2 = new ArrayList<>();

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> { throw new RuntimeException("MyMessage"); });

        TestResults testResults = CodeCamp.runTests(tests2, functionTests);
        assume(testResults.summary().contains("MyMessage"), "Test results should contain \"MyMessage\"");
    };

    public static Runnable correctAssertErrorMessage2 = () -> {
        List<Testable> tests2 = new ArrayList<>();

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> { assume(false); });


        TestResults testResults = CodeCamp.runTests(tests2, functionTests);

        assume(testResults.summary().contains("Tests.java"));
        assume(testResults.summary().contains("(assume)"));
    };

    public static Runnable correctAssertErrorMessage = () -> {
        List<Testable> tests2 = new ArrayList<>();

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> { assume(false); });

        TestResults testResults = CodeCamp.runTests(tests2, functionTests);

        assume(testResults.summary().contains("CodeCamp.java"), "test result summary should contain \"CodeCamp.java\"");
        assume(testResults.summary().contains("(invoke)"), "test result summary should contain \"(invoke)\"");
    };

    public static void assume(boolean b) {
        assume(b, "");
    }

    public static void assume(boolean b, String message) {
        if(!b) throw new RuntimeException(message);
    }
}

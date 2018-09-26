package no.iterate;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    public static Runnable integrationTest = () -> {
        List<Testable> tests = new ArrayList<>();

        Runnable correctErrorMessage = () -> {
            List<Testable> tests2 = new ArrayList<>();

            List<Runnable> functionTests = new ArrayList<>();
            functionTests.add(() -> { throw new RuntimeException("MyMessage"); });

            TestResults testResults = CodeCamp.runTests(tests2, functionTests);
            assume(testResults.summary().contains("MyMessage"), "Test results should contain \"MyMessage\"");
        };

        Runnable correctAssertErrorMessage = () -> {
            List<Testable> tests2 = new ArrayList<>();

            List<Runnable> functionTests = new ArrayList<>();
            functionTests.add(() -> { assume(false); });


            TestResults testResults = CodeCamp.runTests(tests2, functionTests);
            assume(testResults.summary().contains("CodeCamp.java"));
            assume(testResults.summary().contains("(invoke)"));
        };

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> { throw new RuntimeException("MyMessage"); });
        functionTests.add(() -> {});
        functionTests.add(() -> { assume(false); });
        functionTests.add(correctErrorMessage);
        functionTests.add(correctAssertErrorMessage);


        TestResults testResults = CodeCamp.runTests(tests, functionTests);

        assert(testResults.numberOfTests == tests.size() + functionTests.size());
        assume(testResults.numberOfTestsFailed == 2);
    };

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

    public static class IntegrationTest implements Testable {

        @Override
        public void invoke() {
            List<Testable> tests = new ArrayList<>();
            tests.add(new CodeCamp.AnonymousFunction(() -> {return;}));

            Runnable correctErrorMessage = () -> {
                List<Testable> tests2 = new ArrayList<>();

                List<Runnable> functionTests = new ArrayList<>();
                functionTests.add(() -> { throw new RuntimeException("MyMessage"); });

                TestResults testResults = CodeCamp.runTests(tests2, functionTests);
                assume(testResults.summary().contains("MyMessage"), "Test results should contain \"MyMessage\"");
            };

            Runnable correctAssertErrorMessage = () -> {
                List<Testable> tests2 = new ArrayList<>();

                List<Runnable> functionTests = new ArrayList<>();
                functionTests.add(() -> { assume(false); });


                TestResults testResults = CodeCamp.runTests(tests2, functionTests);
                assume(testResults.summary().contains("CodeCamp.java"));
                assume(testResults.summary().contains("(invoke)"));
            };

            List<Runnable> functionTests = new ArrayList<>();
            functionTests.add(() -> { throw new RuntimeException("MyMessage"); });
            functionTests.add(() -> {});
            functionTests.add(() -> { assume(false); });
            functionTests.add(correctErrorMessage);
            functionTests.add(correctAssertErrorMessage);


            TestResults testResults = CodeCamp.runTests(tests, functionTests);

            assert(testResults.numberOfTests == tests.size() + functionTests.size());
            assume(testResults.numberOfTestsFailed == 2);
        }

    }

    public static void assume(boolean b) {
        assert (b);
    }

    public static void assume(boolean b, String message) {
        if(!b) throw new RuntimeException(message);
    }
}

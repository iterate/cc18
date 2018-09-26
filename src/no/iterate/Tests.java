package no.iterate;

import java.util.ArrayList;
import java.util.List;

public class Tests {
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
            tests.add(new CodeCamp.AnonymousFunction(() -> { throw new RuntimeException("MyMessage"); }));
            tests.add(new PassingTest());
            tests.add(new AssertFailedTest());
            tests.add(new EmptyTestResult());
            tests.add(new CorrectErrorMessage());
            tests.add(new CorrectAssertErrorMessage());
            tests.add(new CodeCamp.AnonymousFunction(() -> {return;}));

            List<Runnable> functionTests = new ArrayList<>();
            functionTests.add(() -> { throw new RuntimeException("MyMessage"); });

            TestResults testResults = CodeCamp.runTests(tests, functionTests);

            assert(testResults.numberOfTests == tests.size());
            assume(testResults.numberOfTestsFailed == 2);
        }

        private static class CorrectAssertErrorMessage implements Testable {
            @Override
            public void invoke() {
                List<Testable> tests = new ArrayList<>();
                tests.add(new AssertFailedTest());

                TestResults testResults = CodeCamp.runTests(tests, null);
                assume(testResults.summary().contains("CodeCamp.java"));
                assume(testResults.summary().contains("(invoke)"));

            }
        }

        private static class EmptyTestResult implements Testable {

            @Override
            public void invoke() {
                TestResults sample = new TestResults();

                assume(sample.summary().isEmpty());
            }
        }

        private static class CorrectErrorMessage implements Testable {
            @Override
            public void invoke() {
                List<Testable> tests = new ArrayList<>();
                tests.add(new CodeCamp.AnonymousFunction(() -> { throw new RuntimeException("MyMessage"); }));

                TestResults testResults = CodeCamp.runTests(tests, null);
                assume(testResults.summary().contains("MyMessage"), "Test results should contain \"MyMessage\"");

            }
        }

        private static class AssertFailedTest implements Testable {
            public void invoke() {
                assume(false);
            }
        }

        private static class PassingTest implements Testable {
            public void invoke() {
            }
        }
    }

    public static void assume(boolean b) {
        assert (b);
    }

    public static void assume(boolean b, String message) {
        if(!b) throw new RuntimeException();
        assert (b);
    }
}

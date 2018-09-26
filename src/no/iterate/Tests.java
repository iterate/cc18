package no.iterate;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    public static class FizzBuzz implements Testable {
        public void invoke() {
            assert(fizzBuzz(1).equals("1"));
            assert(fizzBuzz(2).equals("2"));
            assert(fizzBuzz(3).equals("Fizz!"));
            assert(fizzBuzz(5).equals("Buzz!"));
            assert(fizzBuzz(15).equals("FizzBuzz!"));
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
            tests.add(new FailingTest());
            tests.add(new PassingTest());
            tests.add(new AssertFailedTest());
            tests.add(new EmptyTestResult());
            tests.add(new CorrectErrorMessage());
            tests.add(new CorrectAssertErrorMessage());
            tests.add(new CodeCamp.AnonymousFunction(() -> {return;}));

            TestResults testResults = CodeCamp.runTests(tests);

            assert(testResults.numberOfTests == tests.size());
            assert(testResults.numberOfTestsFailed == 2);
        }

        private static class CorrectAssertErrorMessage implements Testable {
            @Override
            public void invoke() {
                List<Testable> tests = new ArrayList<>();
                tests.add(new AssertFailedTest());

                TestResults testResults = CodeCamp.runTests(tests);
                assert(testResults.summary().contains("CodeCamp.java"));
                assert(testResults.summary().contains("(invoke)"));

            }
        }

        private static class FailingTest implements Testable {
            public void invoke() {
                throw new RuntimeException("MyMessage");
            }
        }

        private static class EmptyTestResult implements Testable {

            @Override
            public void invoke() {
                TestResults sample = new TestResults();

                assert(sample.summary().isEmpty());
            }
        }

        private static class CorrectErrorMessage implements Testable {
            @Override
            public void invoke() {
                List<Testable> tests = new ArrayList<>();
                tests.add(new FailingTest());

                TestResults testResults = CodeCamp.runTests(tests);
                assert(testResults.summary().contains("MyMessage"));

            }
        }

        private static class AssertFailedTest implements Testable {
            public void invoke() {
                assert(false);
            }
        }

        private static class PassingTest implements Testable {
            public void invoke() {
            }
        }
    }
}

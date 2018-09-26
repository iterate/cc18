package no.iterate;

import java.util.ArrayList;
import java.util.List;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new IntegrationTest());
        tests.add(new FizzBuzz());

        report(runTests(tests));
    }

    public static void report(TestResults testResults) {
        System.out.println("Tests run: " + testResults.numberOfTests);
        System.out.println("Tests failed: " + testResults.numberOfTestsFailed);

        if (testResults.numberOfTestsFailed > 0) {
            System.out.println("Exceptions: " + testResults.summary());
            System.exit(1);
        }
    }

    public static TestResults runTests(List<Testable> tests) {
        TestResults testResults = new TestResults();
        for (Testable test : tests) {
            try {
                testResults.numberOfTests++;
                test.invoke();
            } catch (Throwable e) {
                testResults.numberOfTestsFailed++;
                testResults.exceptions.add(buildErrorMessage(e));
            }
        }
        return testResults;
    }

    private static String buildErrorMessage(Throwable throwable) {
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        StringBuilder message = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            String filename = stackTraceElement.getFileName();
            int linenumber = stackTraceElement.getLineNumber();
            String methodName = stackTraceElement.getMethodName();
            
            message.append(filename + ":" + linenumber + " " + methodName + " \n");
        }
        return message + " " + throwable.getMessage();
    }

    public interface Testable {
        void invoke();
    }

    private static class TestResults {
        public int numberOfTests;
        public int numberOfTestsFailed;

        public List<String> exceptions = new ArrayList<>();

        public String summary() {
            return exceptions
                    .stream()
                    .reduce("", (exceptionsString, exception) -> exceptionsString.concat(exception));
        }
    }

    public static class FailingTest implements Testable{
        public void invoke() {
            throw new RuntimeException("MyMessage");
        }
    }

    public static class PassingTest implements Testable{
        public void invoke() {
        }
    }

    public static class AssertFailedTest implements Testable {
        public void invoke() {
            assert(false);
        }
    }

    private static class CorrectErrorMessage implements Testable {
        @Override
        public void invoke() {
            List<Testable> tests = new ArrayList<>();
            tests.add(new FailingTest());

            TestResults testResults = runTests(tests);
            assert(testResults.summary().contains("MyMessage"));

        }
    }

    public static class EmptyTestResult implements Testable {

        @Override
        public void invoke() {
            TestResults sample = new TestResults();

            assert(sample.summary().isEmpty());
        }
    }

    private static class CorrectAssertErrorMessage implements Testable {
        @Override
        public void invoke() {
            List<Testable> tests = new ArrayList<>();
            tests.add(new AssertFailedTest());

            TestResults testResults = runTests(tests);
            System.out.println(testResults.summary());
            assert(testResults.summary().contains("CodeCamp.java"));

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


            TestResults testResults = runTests(tests);

            assert(testResults.numberOfTests == tests.size());
            assert(testResults.numberOfTestsFailed == 2);
        }
    }

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
}

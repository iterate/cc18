package no.iterate;

import java.util.ArrayList;
import java.util.List;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new IntegrationTest());
        tests.add(new Tests.FizzBuzz());

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

        message.append("\n!!!\nTest failed at: " + printStackTraceMessage(stackTraceElements[0]));
        message.append("\nStack trace:\n");

        for (StackTraceElement stackTraceElement : stackTraceElements) {
            message.append(printStackTraceMessage(stackTraceElement));
        }

        return message + throwable.getMessage() + "\n!!!\n";
    }

    private static String printStackTraceMessage(StackTraceElement stackTraceElement) {
        String filename = stackTraceElement.getFileName();
        int lineNumber = stackTraceElement.getLineNumber();
        String methodName = stackTraceElement.getMethodName();

        return filename + ":" + lineNumber + " (" + methodName + ") \n";
    }

    public interface Testable {
        void invoke();
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
            tests.add(new IntegrationTest.FailingTest());

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

        private static class CorrectAssertErrorMessage implements Testable {
            @Override
            public void invoke() {
                List<Testable> tests = new ArrayList<>();
                tests.add(new AssertFailedTest());

                TestResults testResults = runTests(tests);
                assert(testResults.summary().contains("CodeCamp.java"));
                assert(testResults.summary().contains("(invoke)"));

            }
        }

        private static class FailingTest implements Testable{
            public void invoke() {
                throw new RuntimeException("MyMessage");
            }
        }
    }

}

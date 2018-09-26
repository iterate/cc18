package no.iterate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.IntegrationTest());
        tests.add(new Tests.FizzBuzz());

        report(runTests(tests));
    }

    public static class AnonymousFunction implements Testable {
        private final BiFunction<Void, ?, Void> function;

        public AnonymousFunction(BiFunction<Void, ?, Void> function) {
            this.function = function;
        }

        public void invoke() {

        }
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

}

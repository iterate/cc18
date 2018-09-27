package no.iterate;

import java.util.List;

public interface Testable {
    static TestResults runTests(List<Testable> tests, List<Runnable> testFunctions) {
        TestResults testResults = new TestResults();
        for (Testable test : tests) {
            try {
                testResults.numberOfTests++;
                test.invoke();
            } catch (Throwable e) {
                testResults.numberOfTestsFailed++;
                testResults.exceptions.add(CodeCamp.buildErrorMessage(e));
            }
        }

        for (Runnable test : testFunctions) {
            try {
                testResults.numberOfTests++;
                test.run();
            } catch (Throwable e) {
                testResults.numberOfTestsFailed++;
                testResults.exceptions.add(CodeCamp.buildErrorMessage(e));
            }
        }

        return testResults;
    }

    void invoke();
}

package no.iterate;

public class Reporter {
    public static void report(TestResults testResults) {
        System.out.println("Tests run: " + testResults.numberOfTests);
        System.out.println("Tests failed: " + testResults.numberOfTestsFailed);

        System.out.println();

        if (testResults.numberOfTestsFailed > 0) {
            System.out.println(testResults.summary());
            System.exit(1);
        }
    }
}

package no.iterate;

public class Reporter {
    private static final String LOGO = "_________            .___     _________                       ____  ______  \n" +
            "[31m\\_   ___ \\  ____   __| _/____ \\_   ___ \\_____    _____ ______/_   |/  __  \\ \n" +
            "/    \\  \\/ /  _ \\ / __ |/ __ \\/    \\  \\/\\__  \\  /     \\\\____ \\|   |>      < \n" +
            "\\     \\___(  <_> ) /_/ \\  ___/\\     \\____/ __ \\|  Y Y  \\  |_> >   /   --   \\\n" +
            " \\______  /\\____/\\____ |\\___  >\\______  (____  /__|_|  /   __/|___\\______  /\n" +
            "        \\/            \\/    \\/        \\/     \\/      \\/|__|              \\/ ";

    public static void report(TestResults testResults) {
        System.out.println(LOGO);
        System.out.println("Tests run: " + testResults.numberOfTests);
        System.out.println("Tests failed: " + testResults.numberOfTestsFailed);

        System.out.println();

        if (testResults.numberOfTestsFailed > 0) {
            System.out.println(testResults.summary());
            System.exit(1);
        }
    }
}

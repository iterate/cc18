package no.iterate;

public class Reporter {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final String LOGO = "_________            .___     _________                       ____  ______  \n" +
            "\\_   ___ \\  ____   __| _/____ \\_   ___ \\_____    _____ ______/_   |/  __  \\ \n" +
            "/    \\  \\/ /  _ \\ / __ |/ __ \\/    \\  \\/\\__  \\  /     \\\\____ \\|   |>      < \n" +
            "\\     \\___(  <_> ) /_/ \\  ___/\\     \\____/ __ \\|  Y Y  \\  |_> >   /   --   \\\n" +
            " \\______  /\\____/\\____ |\\___  >\\______  (____  /__|_|  /   __/|___\\______  /\n" +
            "        \\/            \\/    \\/        \\/     \\/      \\/|__|              \\/ ";

    public static void report(TestResults testResults) {
        boolean failure = testResults.numberOfTestsFailed > 0;  
        System.out.print(failure ? ANSI_RED : ANSI_GREEN);
        System.out.println(LOGO);
        System.out.print(ANSI_RESET);
        System.out.println("Tests run: " + testResults.numberOfTests);
        System.out.println("Tests failed: " + testResults.numberOfTestsFailed);

        System.out.println();

        if (failure) {
            System.out.println(testResults.summary());
            System.exit(1);
        }
    }
}

package no.iterate;

public class Reporter {
    static final String LOGO = "_________            .___     _________                       ____  ______  \\r\\n\\\\_   ___ \\\\  ____   __| _/____ \\\\_   ___ \\\\_____    _____ ______/_   |/  __  \\\\ \\r\\n/    \\\\  \\\\/ /  _ \\\\ / __ |/ __ \\\\/    \\\\  \\\\/\\\\__  \\\\  /     \\\\\\\\____ \\\\|   |>      < \\r\\n\\\\     \\\\___(  <_> ) /_/ \\\\  ___/\\\\     \\\\____/ __ \\\\|  Y Y  \\\\  |_> >   /   --   \\\\\\r\\n \\\\______  /\\\\____/\\\\____ |\\\\___  >\\\\______  (____  /__|_|  /   __/|___\\\\______  /\\r\\n        \\\\/            \\\\/    \\\\/        \\\\/     \\\\/      \\\\/|__|              \\\\/ ";
    static final String LOGO2 = "_________            .___     _________                       ____  ______  \n" +
            "\\_   ___ \\  ____   __| _/____ \\_   ___ \\_____    _____ ______/_   |/  __  \\ \n" +
            "/    \\  \\/ /  _ \\ / __ |/ __ \\/    \\  \\/\\__  \\  /     \\\\____ \\|   |>      < \n" +
            "\\     \\___(  <_> ) /_/ \\  ___/\\     \\____/ __ \\|  Y Y  \\  |_> >   /   --   \\\n" +
            " \\______  /\\____/\\____ |\\___  >\\______  (____  /__|_|  /   __/|___\\______  /\n" +
            "        \\/            \\/    \\/        \\/     \\/      \\/|__|              \\/ ";

    public static void report(TestResults testResults) {
        System.out.println(LOGO2);
        System.out.println("Tests run: " + testResults.numberOfTests);
        System.out.println("Tests failed: " + testResults.numberOfTestsFailed);

        System.out.println();

        if (testResults.numberOfTestsFailed > 0) {
            System.out.println(testResults.summary());
            System.exit(1);
        }
    }
}

package no.iterate;

import java.util.ArrayList;
import java.util.List;

class TestResults {
    public int numberOfTests;
    public int numberOfTestsFailed;

    public List<String> exceptions = new ArrayList<>();

    public String summary() {
        new StringBuilder();
        return exceptions
                .stream()
                .reduce("", (exceptionsString, exception) -> exceptionsString.concat(exception));
    }
}

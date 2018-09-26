package no.iterate;

import java.util.ArrayList;
import java.util.List;

class TestResults {
    public int numberOfTests;
    public int numberOfTestsFailed;

    public List<String> exceptions = new ArrayList<>();

    public String summary() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String exception : exceptions) {
            
        }
        return exceptions
                .stream()
                .reduce("", (exceptionsString, exception) -> exceptionsString.concat(exception));
    }
}

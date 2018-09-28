package no.iterate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TestResults {
    public int numberOfTests;
    public int numberOfTestsFailed;

    public List<String> exceptions = new ArrayList<>();

    public String summary() {


        System.out.println(Arrays.asList("ole", "petter").stream().collect(Collectors.joining()));

        StringBuilder stringBuilder = new StringBuilder();
        for (String exception : exceptions) {
            stringBuilder.append(exception);
        }
        return stringBuilder.toString();
    }
}

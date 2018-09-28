package no.iterate;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProgramPrinter {
    static String indent(int depth){
        return IntStream
                .range(0, depth + 1)
                .mapToObj(i -> "   ")
                .collect(Collectors.joining());
    }
}

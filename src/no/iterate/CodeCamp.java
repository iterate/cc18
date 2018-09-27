package no.iterate;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import static com.github.javaparser.JavaParser.parseClassOrInterfaceType;
import static no.iterate.Tests.assume;

public class CodeCamp {

    public static void main(String[] args) {
        List<Testable> tests = new ArrayList<>();
        tests.add(new Tests.FizzBuzz());

        List<Runnable> functionTests = new ArrayList<>();
        functionTests.add(() -> {});
        functionTests.add(Tests.correctErrorMessage);
        functionTests.add(Tests.correctAssertErrorMessage2);

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .toString()
                    .contains("class FizzBuzz"), "Program should contain class FizzBuzz");
        });

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addMethod("calculate", "FizzBuzz")
                    .toString()
                    .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");
        });

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addMethod("calculate", "FizzBuzz")
                    .addParameter("FizBuzz", "calculate", "int", "input")
                    .toString()
                    .contains("calculate"));
        });

        report(runTests(tests, functionTests));
    }

    private static class Program {

        private CompilationUnit compilationUnit;

        public String toString() {
            return compilationUnit.toString();
        }

        public Program addClass(String className) {
            compilationUnit = new CompilationUnit();
            compilationUnit.addClass(className);
            return this;
        }

        public Program addMethod(String methodName, String contaningClassName) {
            compilationUnit.getClassByName(contaningClassName)
                    .map((klass -> klass.addMethod(methodName)));
            return this;
        }

        public Program addParameter(String containingClass, String containingMethod, String parameterType, String parameterName) {
            compilationUnit.getClassByName(containingClass);

            // WE WERE DOING THIS
            return this;
        }
    }

    public static void report(TestResults testResults) {
        System.out.println("Tests run: " + testResults.numberOfTests);
        System.out.println("Tests failed: " + testResults.numberOfTestsFailed);

        System.out.println("");

        if (testResults.numberOfTestsFailed > 0) {
            System.out.println(testResults.summary());
            System.exit(1);
        }
    }

    public static TestResults runTests(List<Testable> tests, List<Runnable> testFunctions) {
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

        for (Runnable test : testFunctions) {
            try {
                testResults.numberOfTests++;
                test.run();
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
        
        message.append(throwable.getMessage());

        message.append("\n\nStack trace:\n");

        for (StackTraceElement stackTraceElement : stackTraceElements) {
            message.append("\t" + printStackTraceMessage(stackTraceElement));
        }

        return message.toString();
    }

    private static String printStackTraceMessage(StackTraceElement stackTraceElement) {
        String filename = stackTraceElement.getFileName();
        int lineNumber = stackTraceElement.getLineNumber();
        String methodName = stackTraceElement.getMethodName();

        return filename + ":" + lineNumber + " (" + methodName + ") \n";
    }
}

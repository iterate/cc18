package no.iterate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

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
                    .addMethod("calculate")
                    .toString()
                    .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'");
        });

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addMethod("calculate")
                    .addParameter("FizzBuzz", "INT", "input")
                    .toString()
                    .contains("calculate"));
        });

        report(runTests(tests, functionTests));
    }

    private static class Program {

        private CompilationUnit compilationUnit;
        private ClassOrInterfaceDeclaration currentClass;
        private MethodDeclaration currentMethod;

        public String toString() {
            return compilationUnit.toString();
        }

        public Program addClass(String className) {
            compilationUnit = new CompilationUnit();
            currentClass = compilationUnit.addClass(className);
            return this;
        }

        public Program addMethod(String methodName) {
            currentMethod = currentClass.addMethod(methodName);
            return this;
        }

        public Program addParameter(String containingClass, String parameterType, String parameterName) {
            currentMethod.addParameter(JavaParser.parseTypeParameter(parameterType), parameterName);

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

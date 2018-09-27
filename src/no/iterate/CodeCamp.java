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

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .toString()
                .contains("class FizzBuzz"), "Program should contain class FizzBuzz"));

        functionTests.add(() -> assume(new Program()
                .addClass("FizzBuzz")
                .addMethod("calculate")
                .toString()
                .contains("calculate()"), "Adding a method, 'calculate' should make THE STRING contain 'calculate()'"));

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addMethod("calculate")
                    .addParameter("INT", "input")
                    .toString()
                    .contains("calculate(INT input)"));
        });

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addClass("AnotherClass")
                    .toString()
                    .contains("class FizzBuzz"), "Program should contain class FizzBuzz even when AnotherClass has been added");
        });

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addMethod("calculate")
                    .addReturnType("STRING")
                    .toString()
                    .contains("STRING calculate("));
        });

        functionTests.add(() -> {
            assume(new Program()
                    .addClass("FizzBuzz")
                    .addMethod("calculate")
                    .toString()
                    .contains("calculate("));
        });

        Reporter.report(Tester.runTests(tests, functionTests));
    }

    private static class Program {

        private CompilationUnit compilationUnit = new CompilationUnit();
        private ClassOrInterfaceDeclaration currentClass;
        private MethodDeclaration currentMethod;

        public String toString() {
            return compilationUnit.toString();
        }

        public Program addClass(String className) {
            currentClass = compilationUnit.addClass(className);
            return this;
        }

        public Program addMethod(String methodName) {
            currentMethod = currentClass.addMethod(methodName);
            return this;
        }

        public Program addParameter(String parameterType, String parameterName) {
            currentMethod.addParameter(JavaParser.parseTypeParameter(parameterType), parameterName);
            return this;
        }

        public Program addReturnType(String returnType) {
            currentMethod.setType(JavaParser.parseTypeParameter(returnType));
            return this;
        }
    }

}

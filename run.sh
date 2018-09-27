#!/usr/bin/env zsh

javac -classpath javaparser-symbol-solver-core-3.6.23.jar:javaparser-core-3.6.23.jar src/no/iterate/Compiler.java src/no/iterate/ProgramTests.java src/no/iterate/Program.java src/no/iterate/Tester.java src/no/iterate/Reporter.java src/no/iterate/Testable.java src/no/iterate/TestResults.java src/no/iterate/Tests.java src/no/iterate/CodeCamp.java -d out/production/cc18/ && \
java -classpath out/production/cc18:javaparser-symbol-solver-core-3.6.23.jar:javaparser-core-3.6.23.jar no.iterate.CodeCamp && \
git commit -am working || git reset --hard

exit 0
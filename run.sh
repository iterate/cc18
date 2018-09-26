#!/usr/bin/env bash
FILES = find -name "*.java"
javac src/no/iterate/CodeCamp.java -d out/production/cc18/ \
    && java -ea -classpath out/production/cc18 no.iterate.CodeCamp \
    && (git commit -am working \
        && git push) \
    || git reset --hard
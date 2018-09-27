#!/usr/bin/env zsh
FILES=$(ls -lrt -d -1 $PWD/**/{*,.*} | grep ".java$" | tr '\n' ' ')

echo "_________            .___     _________                       ____  ______  "
echo "\_   ___ \  ____   __| _/____ \_   ___ \_____    _____ ______/_   |/  __  \ "
echo "/    \  \/ /  _ \ / __ |/ __ \/    \  \/\__  \  /     \\____ \|   |>      < "
echo '\     \___(  <_> ) /_/ \  ___/\     \____/ __ \|  Y Y  \  |_> >   /   --   \'
echo " \______  /\____/\____ |\___  >\______  (____  /__|_|  /   __/|___\______  /"
echo "        \/            \/    \/        \/     \/      \/|__|              \/ "

function class() {
    $1
}

echo $(class "Testable")

javac -classpath javaparser-symbol-solver-core-3.6.23.jar:javaparser-core-3.6.23.jar src/no/iterate/Testable.java src/no/iterate/TestResults.java src/no/iterate/Tests.java src/no/iterate/CodeCamp.java -d out/production/cc18/ && \
java -classpath out/production/cc18:javaparser-symbol-solver-core-3.6.23.jar:javaparser-core-3.6.23.jar no.iterate.CodeCamp && \
git commit -am working || git reset --hard

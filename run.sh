FILES=$(ls -lrt -d -1 $PWD/**/{*,.*} | grep ".java$")

echo "-----"
echo $FILES
echo "-----"

javac src/no/iterate/CodeCamp.java -d out/production/cc18/ \
    && java -ea -classpath out/production/cc18 no.iterate.CodeCamp \
    && (git commit -am working \
        && git push) \
    || git reset --hard
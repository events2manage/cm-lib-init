#!/bin/sh

java_use 1.7
echo "Switched to Java 1.7"

mvn -X exec:java -Dexec.mainClass=com.newlinecode.libraryInit.App -Dexec.args="$1"

if [ ! -z "$2" ]; then
    echo "Moving files to project $2"
    mv -v libraries/* ~/Documents/htdocs/$2/libraries
fi


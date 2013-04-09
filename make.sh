#!/bin/sh

java -jar precompiler.jar
javac *.java
echo "Kompilierung abgeschlossen"

#!/bin/bash

#Parameter 1 should be the name of a scala file; 
#Parameter 2 is the name of the class that scala generates from it, which can 
#  at the very least be derived by looking at the .class files scalac creates.

#Compile to the build directory:
scalac -d build $1 || exit 1

#This runs in the java vm. So all you need is just one extra scala jar file:
java -classpath "build;C:\Programs\Scala\lib\scala-library.jar" $2
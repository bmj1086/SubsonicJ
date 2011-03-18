#!/bin/bash

clear

#Pre checks
[ -e subsonicJ.jar ] && rm subsonicJ.jar
[ ! -e bin ] && mkdir bin

#Compile app
javac -d bin -classpath lib/jl1.0.1.jar:lib/JTattoo.jar:src src/subsonicj/*

#Copy res folder
cp -r src/res bin/

#Create Manifest
echo "Main-Class: subsonicj.Main" >> manifest.txt
echo "Class-Path: lib/JTattoo.jar lib/jl1.0.1.jar" >> manifest.txt

#Compile Jar
jar -cfm subsonicJ.jar manifest.txt -C bin/ .

#Clean up
[ -e manifest.txt ] && rm manifest.txt
[ -e bin ] && rm -r bin

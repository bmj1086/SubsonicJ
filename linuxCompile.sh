#!/bin/bash

[ ! -e bin ] && mkdir bin
javac -d bin -classpath lib/jl1.0.1.jar:lib/JTattoo.jar:src src/subsonicj/*

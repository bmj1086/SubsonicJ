#!/bin/bash

if [ -e subsonicJ.jar ]; then
    java -jar subsonicJ.jar
else
    echo "No jar found. Please compile app first!"
fi

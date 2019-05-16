#!/bin/bash

printf "Building OutsideWorld jar...\n"

# make classes directory
mkdir classes

# compile project
javac -d classes/ -cp ./src/ ./src/*.java

# copy manifest into classes
cp manifest.txt classes/

# go inside classes directory
cd classes/

# generate jar
jar -cmf manifest.txt ../outsideworld.jar *.class ./*/*.class ./*/*/*.class

printf "Done.\n\n"
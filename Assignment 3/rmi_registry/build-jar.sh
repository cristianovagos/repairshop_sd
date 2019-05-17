#!/bin/bash

printf "Building RMI_REGISTRY jar...\n"

# make classes directory
mkdir classes

# compile project
javac -d classes/ -cp ./src/ ./src/*.java

# copy manifest into classes
cp manifest.txt classes/

# go inside classes directory
cd classes/

# generate jar
jar -cmf manifest.txt ../rmi_registry.jar *.class ./*/*.class ./*/*/*.class

printf "Done.\n\n"
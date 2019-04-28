#!/bin/bash

printf "Building Customer jar...\n"

# make classes directory
mkdir classes

# compile project
javac -d classes/ -cp ./repairshop_sd/src/ ./repairshop_sd/src/*.java

# copy manifest into classes
cp manifest.txt classes/

# go inside classes directory
cd classes/

# generate jar
jar -cmf manifest.txt ../customer.jar *.class ./*/*.class

printf "Done.\n\n"

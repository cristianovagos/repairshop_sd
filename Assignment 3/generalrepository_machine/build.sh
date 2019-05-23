#!/bin/bash

printf "Building Repository project...\n"

# make dir_generalrepository_machine directory
mkdir dir_generalrepository_machine

# compile project
javac -d dir_generalrepository_machine/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
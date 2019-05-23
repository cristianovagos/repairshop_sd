#!/bin/bash

printf "Building Park project...\n"

# make dir_park_machine directory
mkdir dir_park_machine

# compile project
javac -d dir_park_machine/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
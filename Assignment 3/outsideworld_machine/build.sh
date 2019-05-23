#!/bin/bash

printf "Building OutsideWorld project...\n"

# make dir_outsideworld_machine directory
mkdir dir_outsideworld_machine

# compile project
javac -d dir_outsideworld_machine/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
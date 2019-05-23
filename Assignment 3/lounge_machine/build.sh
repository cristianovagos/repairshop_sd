#!/bin/bash

printf "Building Lounge project...\n"

# make dir_lounge_machine directory
mkdir dir_lounge_machine

# compile project
javac -d dir_lounge_machine/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
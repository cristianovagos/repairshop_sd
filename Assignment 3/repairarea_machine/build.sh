#!/bin/bash

printf "Building RepairArea project...\n"

# make dir_repairarea_machine directory
mkdir dir_repairarea_machine

# compile project
javac -d dir_repairarea_machine/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
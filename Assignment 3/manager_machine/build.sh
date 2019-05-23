#!/bin/bash

printf "Building Manager project...\n"

# make dir_manager_machine directory
mkdir dir_manager_machine

# compile project
javac -d dir_manager_machine/ -cp ./repairshop_sd/repairshop_sd/src/ ./repairshop_sd/repairshop_sd/src/*.java

printf "Done.\n\n"
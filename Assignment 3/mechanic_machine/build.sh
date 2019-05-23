#!/bin/bash

printf "Building Mechanic project...\n"

# make dir_mechanic_machine directory
mkdir dir_mechanic_machine

# compile project
javac -d dir_mechanic_machine/ -cp ./repairshop_sd/src/ ./repairshop_sd/src/*.java

printf "Done.\n\n"
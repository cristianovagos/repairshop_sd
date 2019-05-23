#!/bin/bash

printf "Building Customer project...\n"

# make dir_customer_machine directory
mkdir dir_customer_machine

# compile project
javac -d dir_customer_machine/ -cp ./repairshop_sd/src/ ./repairshop_sd/src/*.java

printf "Done.\n\n"

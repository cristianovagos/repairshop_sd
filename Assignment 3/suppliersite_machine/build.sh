#!/bin/bash

printf "Building SupplierSite project...\n"

# make dir_suppliersite_machine directory
mkdir dir_suppliersite_machine

# compile project
javac -d dir_suppliersite_machine/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
#!/bin/bash

printf "Building RMI_REGISTRY project...\n"

# make dir_rmi_registry directory
mkdir dir_rmi_registry

# compile project
javac -d dir_rmi_registry/ -cp ./src/ ./src/*.java

printf "Done.\n\n"
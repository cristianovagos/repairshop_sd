#!/bin/bash

printf "SD - Assignment 2 (Repair Shop Activities - Message Passing)\nTurma 1, Grupo 1 - Cristiano Vagos, Miguel Bras\n\n"
printf "Cleaning all project folders... "

cd ./customer_machine/
bash clean.sh

cd ../generalrepository_machine/
bash clean.sh

cd ../lounge_machine/
bash clean.sh

cd ../manager_machine/
bash clean.sh

cd ../mechanic_machine/
bash clean.sh

cd ../park_machine/
bash clean.sh

cd ../outsideworld_machine/
bash clean.sh

cd ../suppliersite_machine/
bash clean.sh

cd ../repairarea_machine/
bash clean.sh

printf "Done.\n"
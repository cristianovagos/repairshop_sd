#!/bin/bash

cd ./customer_machine/
bash build-jar.sh

cd ../generalrepository_machine/
bash build-jar.sh

cd ../lounge_machine/
bash build-jar.sh

cd ../manager_machine/
bash build-jar.sh

cd ../mechanic_machine/
bash build-jar.sh

cd ../park_machine/
bash build-jar.sh

cd ../outsideworld_machine/
bash build-jar.sh

cd ../suppliersite_machine/
bash build-jar.sh

cd ../repairarea_machine/
bash build-jar.sh

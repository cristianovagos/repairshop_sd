#!/bin/bash

cd ./rmi_registry/
bash build.sh

cd ../customer_machine/
bash build.sh

cd ../generalrepository_machine/
bash build.sh

cd ../lounge_machine/
bash build.sh

cd ../manager_machine/
bash build.sh

cd ../mechanic_machine/
bash build.sh

cd ../park_machine/
bash build.sh

cd ../outsideworld_machine/
bash build.sh

cd ../suppliersite_machine/
bash build.sh

cd ../repairarea_machine/
bash build.sh
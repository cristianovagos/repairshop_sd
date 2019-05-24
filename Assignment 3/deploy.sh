#!/bin/bash

printf "SD - Assignment 3 (Repair Shop Activities - RMI)\nTurma 1, Grupo 1 - Cristiano Vagos, Miguel Bras\n\n"

# variables
user=sd0101

rmi_registry=$user@l040101-ws01.ua.pt
repository=$user@l040101-ws01.ua.pt
lounge=$user@l040101-ws02.ua.pt
outsideworld=$user@l040101-ws03.ua.pt
park=$user@l040101-ws04.ua.pt
suppliersite=$user@l040101-ws05.ua.pt
repairarea=$user@l040101-ws06.ua.pt
customer=$user@l040101-ws07.ua.pt
mechanic=$user@l040101-ws09.ua.pt
manager=$user@l040101-ws10.ua.pt

printf "Compiling all projects...\n"

# build all projects
bash build.sh

printf "Transferring the files into each machine...\n"

printf "\nTransferring the files into RMI_REGISTRY server...\n"
ssh $rmi_registry 'mkdir -p rmi_registry; mkdir -p Public/classes/utils'
scp -r ./rmi_registry/dir_rmi_registry/interfaces/ $rmi_registry:/home/$user/Public/classes/
scp -r ./lounge_machine/dir_lounge_machine/interfaces/ ./lounge_machine/dir_lounge_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./outsideworld_machine/dir_outsideworld_machine/interfaces/ ./outsideworld_machine/dir_outsideworld_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./park_machine/dir_park_machine/interfaces/ ./park_machine/dir_park_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./suppliersite_machine/dir_suppliersite_machine/interfaces/ ./suppliersite_machine/dir_suppliersite_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./repairarea_machine/dir_repairarea_machine/interfaces/ ./repairarea_machine/dir_repairarea_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./customer_machine/dir_customer_machine/interfaces/ ./customer_machine/dir_customer_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./mechanic_machine/dir_mechanic_machine/interfaces/ ./mechanic_machine/dir_mechanic_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./manager_machine/dir_manager_machine/interfaces/ ./manager_machine/dir_manager_machine/model/ $rmi_registry:/home/$user/Public/classes/
scp -r ./rmi_registry/dir_rmi_registry/ ./rmi_registry/set_rmi_registry.sh $rmi_registry:/home/$user/rmi_registry
scp ./lounge_machine/dir_lounge_machine/utils/Pair.class $rmi_registry:/home/$user/Public/classes/utils/
scp ./java.policy ./rmi_registry/registry_com.sh $rmi_registry:/home/$user/rmi_registry/dir_rmi_registry

printf "\nTransferring the files into GeneralRepository server...\n"
ssh $repository 'mkdir -p repository'
scp -r ./generalrepository_machine/dir_generalrepository_machine/ $repository:/home/$user/repository/
scp ./java.policy ./generalrepository_machine/repository.sh $repository:/home/$user/repository/dir_generalrepository_machine

printf "\nTransferring the files into Lounge server...\n"
ssh $lounge 'mkdir -p lounge'
scp -r ./lounge_machine/dir_lounge_machine/ $lounge:/home/$user/lounge
scp ./java.policy ./lounge_machine/lounge.sh $lounge:/home/$user/lounge/dir_lounge_machine

printf "\nTransferring the files into OutsideWorld server...\n"
ssh $outsideworld 'mkdir -p outsideworld'
scp -r ./outsideworld_machine/dir_outsideworld_machine/ $outsideworld:/home/$user/outsideworld
scp ./java.policy ./outsideworld_machine/outsideworld.sh $outsideworld:/home/$user/outsideworld/dir_outsideworld_machine

printf "\nTransferring the files into Park server...\n"
ssh $park 'mkdir -p park'
scp -r ./park_machine/dir_park_machine/ $park:/home/$user/park
scp ./java.policy ./park_machine/park.sh $park:/home/$user/park/dir_park_machine

printf "\nTransferring the files into SupplierSite server...\n"
ssh $suppliersite 'mkdir -p suppliersite'
scp -r ./suppliersite_machine/dir_suppliersite_machine/ $suppliersite:/home/$user/suppliersite
scp ./java.policy ./suppliersite_machine/suppliersite.sh $suppliersite:/home/$user/suppliersite/dir_suppliersite_machine

printf "\nTransferring the files into RepairArea server...\n"
ssh $repairarea 'mkdir -p repairarea'
scp -r ./repairarea_machine/dir_repairarea_machine/ $repairarea:/home/$user/repairarea
scp ./java.policy ./repairarea_machine/repairarea.sh $repairarea:/home/$user/repairarea/dir_repairarea_machine

printf "\nTransferring the files into Customer server...\n"
ssh $customer 'mkdir -p customer'
scp -r ./customer_machine/dir_customer_machine/ $customer:/home/$user/customer
scp ./customer_machine/customer.sh $customer:/home/$user/customer/dir_customer_machine

printf "\nTransferring the files into Mechanic server...\n"
ssh $mechanic 'mkdir -p mechanic'
scp -r ./mechanic_machine/dir_mechanic_machine/ $mechanic:/home/$user/mechanic
scp ./mechanic_machine/mechanic.sh $mechanic:/home/$user/mechanic/dir_mechanic_machine

printf "\nTransferring the files into Manager server...\n"
ssh $manager 'mkdir -p manager'
scp -r ./manager_machine/dir_manager_machine/ $manager:/home/$user/manager
scp ./manager_machine/manager.sh $manager:/home/$user/manager/dir_manager_machine

printf "\nLaunching a terminal window from each machine...\n"

# open each terminal
xterm -e bash -c "ssh -t $rmi_registry 'cd rmi_registry/; bash set_rmi_registry.sh 22110; bash';" &
sleep 2
xterm -e bash -c "ssh -t $rmi_registry 'cd rmi_registry/dir_rmi_registry/; bash registry_com.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $repository 'cd repository/dir_generalrepository_machine/; bash repository.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $lounge 'cd lounge/dir_lounge_machine/; bash lounge.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $outsideworld 'cd outsideworld/dir_outsideworld_machine/; bash outsideworld.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $park 'cd park/dir_park_machine/; bash park.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $suppliersite 'cd suppliersite/dir_suppliersite_machine/; bash suppliersite.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $repairarea 'cd repairarea/dir_repairarea_machine/; bash repairarea.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $customer 'cd customer/dir_customer_machine/; bash customer.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $mechanic 'cd mechanic/dir_mechanic_machine/; bash mechanic.sh; bash';" &
sleep 2
xterm -e bash -c "ssh -t $manager 'cd manager/dir_manager_machine/; bash manager.sh; bash';"
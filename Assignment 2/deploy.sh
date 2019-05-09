#!/bin/bash

printf "SD - Assignment 2 (Repair Shop Activities - Message Passing)\nTurma 1, Grupo 1 - Cristiano Vagos, Miguel Bras\n\n"

# OPTIONAL (copy ssh key to all machines) - uncomment this if needed
# (needs sshpass installed)
# 
# printf "Copy ssh key to machines...\n"
# for ((i=1;i<=9;i++)); do \
# sshpass -f password.txt ssh-copy-id sd0101@l040101-ws0$i.ua.pt; \
# done;

printf "Building jars...\n"

# build project jars
bash build-jars.sh

# OPTIONAL (copy all jars using Ansible)
# printf "Transferring jars into each machine using Ansible...\n"

# # transfer jars to UA machines 
# ansible-playbook playbook.yml

printf "Transferring jars into each machine...\n"

ssh sd0101@l040101-ws01.ua.pt 'mkdir -p repository'
scp ./generalrepository_machine/repository.jar sd0101@l040101-ws01.ua.pt:/home/sd0101/repository/
ssh sd0101@l040101-ws02.ua.pt 'mkdir -p lounge'
scp ./lounge_machine/lounge.jar sd0101@l040101-ws02.ua.pt:/home/sd0101/lounge/
ssh sd0101@l040101-ws03.ua.pt 'mkdir -p outsideworld'
scp ./outsideworld_machine/outsideworld.jar sd0101@l040101-ws03.ua.pt:/home/sd0101/outsideworld/
ssh sd0101@l040101-ws04.ua.pt 'mkdir -p park'
scp ./park_machine/park.jar sd0101@l040101-ws04.ua.pt:/home/sd0101/park/
ssh sd0101@l040101-ws05.ua.pt 'mkdir -p suppliersite'
scp ./suppliersite_machine/suppliersite.jar sd0101@l040101-ws05.ua.pt:/home/sd0101/suppliersite/
ssh sd0101@l040101-ws06.ua.pt 'mkdir -p repairarea'
scp ./repairarea_machine/repairarea.jar sd0101@l040101-ws06.ua.pt:/home/sd0101/repairarea/
ssh sd0101@l040101-ws07.ua.pt 'mkdir -p customer'
scp ./customer_machine/customer.jar sd0101@l040101-ws07.ua.pt:/home/sd0101/customer/
ssh sd0101@l040101-ws08.ua.pt 'mkdir -p mechanic'
scp ./mechanic_machine/mechanic.jar sd0101@l040101-ws08.ua.pt:/home/sd0101/mechanic/
ssh sd0101@l040101-ws08.ua.pt 'mkdir -p manager'
scp ./manager_machine/manager.jar sd0101@l040101-ws08.ua.pt:/home/sd0101/manager/


printf "Launching a terminal window from each machine...\n"

# open each terminal
xterm -e bash -c "ssh -t sd0101@l040101-ws01.ua.pt 'cd repository/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws02.ua.pt 'cd lounge/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws03.ua.pt 'cd outsideworld/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws04.ua.pt 'cd park/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws05.ua.pt 'cd suppliersite/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws06.ua.pt 'cd repairarea/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws07.ua.pt 'cd customer/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws08.ua.pt 'cd mechanic/; bash';" &
xterm -e bash -c "ssh -t sd0101@l040101-ws08.ua.pt 'cd manager/; bash';"
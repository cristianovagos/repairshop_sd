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

printf "Transferring jars into each machine using Ansible...\n"

# transfer jars to UA machines 
ansible-playbook playbook.yml

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
xterm -e bash -c "ssh -t sd0101@l040101-ws09.ua.pt 'cd manager/; bash';"
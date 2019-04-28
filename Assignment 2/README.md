# Repair Shop Activities (Assignment 2 - Message Passing)

Project made for SD (Sistemas Distribuídos / Distributed Systems) course of MIECT, at DETI-UA (University of Aveiro), throughout the semester, with different purposes and approaches. The goal is to put to practise the concepts learned, about concurrency and communication between shared regions.

The main goal of the project is to manage the activities of a Auto Repair Shop, which has a Manager, two Mechanics and 30 Customers who need to repair their car.

## Assignment 2

For this assignment 2, we were required to have the same problem, but instead using message passing as the way to communicate.

### How to run

- Install Ansible and xterm

- Copy hosts file to /etc/ansible _(if you don't have it yet, otherwise append contents to existing file)_

- Run the deploy script
```sh
$ bash deploy.sh
```
It will:
- Build all machines jar files
- Create a folder, copy their jar file in each remote machine using a _Ansible playbook_
- Launch terminal for each machine via ssh using _xterm_

_NOTE:_ to clean jar files and .class files:
```sh
$ bash clean-all.sh
```

### Machines info

|      Machine      |  Role  |        Host        |  Port |
|:-----------------:|:------:|:------------------:|:-----:|
| GeneralRepository | Server | l040101-ws01.ua.pt | 22100 |
| Lounge            | Server | l040101-ws02.ua.pt | 22101 |
| OutsideWorld      | Server | l040101-ws03.ua.pt | 22102 |
| Park              | Server | l040101-ws04.ua.pt | 22103 |
| SupplierSite      | Server | l040101-ws05.ua.pt | 22104 |
| RepairArea        | Server | l040101-ws06.ua.pt | 22105 |
| Customer          | Client | l040101-ws07.ua.pt | 22106 |
| Mechanic          | Client | l040101-ws08.ua.pt | 22107 |
| Manager           | Client | l040101-ws09.ua.pt | 22108 |


### Developers and collaborators:
- [Cristiano Vagos](http://github.com/cristianovagos)
- [Miguel Bras](http://github.com/miguelbras)

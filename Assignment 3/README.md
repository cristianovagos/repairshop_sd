# Repair Shop Activities (Assignment 3 - RMI)

Project made for SD (Sistemas Distribuídos / Distributed Systems) course of MIECT, at DETI-UA (University of Aveiro), throughout the semester, with different purposes and approaches. The goal is to put to practise the concepts learned, about concurrency and communication between shared regions.

The main goal of the project is to manage the activities of a Auto Repair Shop, which has a Manager, two Mechanics and 30 Customers who need to repair their car.

## Assignment 3

For this assignment 3, we were required to have the same problem, but instead using Java RMI as the way to communicate.

### How to run

- Copy your ssh keys to each server using _ssh-pass_

- Install xterm

- Run the deploy script
```sh
$ bash deploy.sh
```
It will:
- Compile code for all machines, generating .class files
- Create a folder, copy the .class files for each remote machine using good'ol _scp_
- Launch terminal and subsequently the program (RMI_REGISTRY / Server / Client) for each machine via ssh using _xterm_

_NOTE:_ to clean jar files and .class files:
```sh
$ bash clean-all.sh
```

### Machines info

|      Machine      |   Role   |        Host        |  Port |
|:-----------------:|:--------:|:------------------:|:-----:|
| GeneralRepository |  Server  | l040101-ws01.ua.pt | 22100 |
| Lounge            |  Server  | l040101-ws02.ua.pt | 22101 |
| OutsideWorld      |  Server  | l040101-ws03.ua.pt | 22102 |
| Park              |  Server  | l040101-ws04.ua.pt | 22103 |
| SupplierSite      |  Server  | l040101-ws05.ua.pt | 22104 |
| RepairArea        |  Server  | l040101-ws06.ua.pt | 22105 |
| Customer          |  Client  | l040101-ws07.ua.pt | 22106 |
| Mechanic          |  Client  | l040101-ws08.ua.pt | 22107 |
| Manager           |  Client  | l040101-ws09.ua.pt | 22108 |
| RMI_REGISTRY      | Registry | l040101-ws01.ua.pt | 22110 |

### Developers and collaborators:
- [Cristiano Vagos](http://github.com/cristianovagos)
- [Miguel Bras](http://github.com/miguelbras)

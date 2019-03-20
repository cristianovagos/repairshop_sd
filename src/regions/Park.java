package regions;

import entities.Customer;
import entities.CustomerState;
import entities.Mechanic;
import entities.MechanicState;

import java.util.Random;

public class Park {

    /**
     * Customer cars at the Park
     */
    private boolean[] customerCars;

    /**
     * Replacement cars at the Park
     */
    private boolean[] replacementCars;

    /**
     * Reference to {@link GeneralRepository}
     */
    private GeneralRepository repository;

    /**
     * Number of different parts available
     */
    private int numOfPartsAvailable;

    public Park(int nCustomers, int nReplacementCars, int nParts, GeneralRepository repo) {
        this.customerCars = new boolean[nCustomers];
        this.replacementCars = new boolean[nReplacementCars];
        this.repository = repo;
        this.numOfPartsAvailable = nParts;

        // initially all replacementCars are at the shop
        for (int i = 0; i < replacementCars.length; i++) {
            replacementCars[i] = true;
        }

        // initially there are no customerCars at the shop
        for (int i = 0; i < customerCars.length; i++) {
            customerCars[i] = false;
        }
    }

    /**
     * Operação goToRepairShop (chamada pelo {@link Customer})
     *
     * O Cliente chega à RepairShop, e estaciona a sua viatura no Park.
     */
    public synchronized void goToRepairShop() {
        // update Customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.PARK);

        // Customer car is now on the Park
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        customerCars[customerId] = true;

        // todo update repository
    }

    /**
     * Operação findCar (chamada pelo {@link Customer})
     *
     * O Cliente recolhe a viatura de substituição do Park.
     */
    public synchronized void findCar(int replacementCar) {
        // update Customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.PARK);

        // Replacement car is not on Park
        replacementCars[replacementCar] = false;

        // todo update repository
    }

    /**
     * Operação collectCar (chamada pelo {@link Customer})
     *
     * O Cliente recolhe a sua viatura própria do Park.
     */
    public synchronized void collectCar() {
        // update Customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.PARK);

        // Customer car is not on Park
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        customerCars[customerId] = false;

        // todo update repository
    }

    /**
     * Operação getVehicle (chamada pelo {@link Mechanic})
     *
     * O Mecânico dirige-se ao Park para obter a viatura do {@link Customer}
     * e avalia qual a peça que deve substituir para a reparar.
     */
    public synchronized int getVehicle() {
        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setState(MechanicState.FIXING_THE_CAR);

        // Customer car is not on Park
        int customerId = ((Mechanic) Thread.currentThread()).getCurrentCarFixingId();
        customerCars[customerId] = false;

        // todo update repository

        // select randomly the part that needs to be replaced
        return new Random().nextInt(numOfPartsAvailable);
    }

    /**
     * Operação returnVehicle (chamada pelo {@link Mechanic})
     *
     * O Mecânico devolve a viatura já reparada ao Park.
     */
    public synchronized void returnVehicle() {
        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setState(MechanicState.FIXING_THE_CAR);

        // Customer car is now on Park
        int customerId = ((Mechanic) Thread.currentThread()).getCurrentCarFixingId();
        customerCars[customerId] = true;

        // todo update repository
    }
}

package regions;

import entities.CarState;
import entities.CustomerState;
import entities.ManagerState;
import entities.MechanicState;
import utils.MemFIFO;

import java.util.ArrayList;
import java.util.List;

public class GeneralRepository {

    /**
     * Repository
     *
     * repositoryState              - estado interno do repositorio
     */
//    public int repositoryState;

    /**
     * ------------------------------------------
     * ENTIDADES
     * ------------------------------------------
     */

    /**
     * Customer
     *
     * customersState               - estado dos clientes
     * requireReplacementVehicle    - se cada cliente requer uma viatura de substituição
     * carsState                    - estado das viaturas dos clientes
     */
    private CustomerState[] customersState;
    private boolean[] requireReplacementVehicle;
    private CarState[] carsState;

    /**
     * Manager
     *
     * managerState                 - estado do Manager
     */
    private ManagerState managerState;

    /**
     * Mechanics
     *
     * mechanicsState               - estado dos mecânicos
     */
    private MechanicState[] mechanicsState;

    /**
     * ------------------------------------------
     * REGIÕES PARTILHADAS DE INFORMAÇÃO
     * ------------------------------------------
     */

    /**
     * Park
     *
     * customerCars                 - viaturas dos clientes
     * replacementCars              - viaturas de substituição
     */
    private int[] customerCars;
    private int[] replacementCars;


    /**
     * Lounge
     *
     * customersQueue               - fila de espera de clientes
     * mechanicsQueue               - fila de espera de mecânicos
     */
    private MemFIFO customersQueue;
    private MemFIFO mechanicsQueue;

    /**
     * Repair Area
     *
     * customerFirstRepairQueue     - fila de espera de clientes à espera de reparação (primeira fila)
     * customerMissingPartQueue     - lista de clientes à espera de uma peça para reparação
     * taskDescription              - lista de tarefas a decorrer para cada mecânico (viatura a ser reparada, etc)
     * stockParts                   - número de peças em stock
     */
    private MemFIFO customerFirstRepairQueue;
    private List<List<Integer>> customerMissingPartQueue;
    private int[] taskDescription;
    private int[] stockParts;

    /**
     * Supplier Site
     *
     * soldParts                    - número de peças vendidas
     */
    private int[] soldParts;


    public GeneralRepository(int nCustomers, int nMechanics, int nParts, int nReplacementCars) {
        if(nCustomers > 0 && nMechanics > 0 && nParts > 0 && nReplacementCars > 0) {
            this.customersState = new CustomerState[nCustomers];
            this.mechanicsState = new MechanicState[nMechanics];
            this.requireReplacementVehicle = new boolean[nCustomers];
            this.carsState = new CarState[nCustomers];
            this.customerCars = new int[nCustomers];
            this.replacementCars = new int[nReplacementCars];
            this.customersQueue = new MemFIFO(nCustomers);
            this.mechanicsQueue = new MemFIFO(nMechanics);
            this.customerFirstRepairQueue = new MemFIFO(nCustomers);
            this.customerMissingPartQueue = new ArrayList<>();
            this.taskDescription = new int[nMechanics];
            this.stockParts = new int[nParts];
            this.soldParts = new int[nParts];
        }
    }

    public CustomerState getCustomersState(int i) {
        return this.customersState[i];
    }

    public void setCustomerState(int i, CustomerState customerState) {
        this.customersState[i] = customerState;
    }

    public ManagerState getManagerState() {
        return this.managerState;
    }

    public void setManagerState(ManagerState managerState) {
        this.managerState = managerState;
    }

    public MechanicState getMechanicState(int i) {
        return this.mechanicsState[i];
    }

    public void setMechanicsState(int i, MechanicState mechanicState) {
        this.mechanicsState[i] = mechanicState;
    }

    public boolean getRequireReplacementVehicle(int i) {
        return requireReplacementVehicle[i];
    }

    public void setRequireReplacementVehicle(int i, boolean require) {
        this.requireReplacementVehicle[i] = require;
    }

    public CarState getCarsState(int i) {
        return this.carsState[i];
    }

    public void setCarsState(int i, CarState carState) {
        this.carsState[i] = carState;
    }

    public int getCustomerCar(int i) {
        return this.customerCars[i];
    }

    public void setCustomerCar(int i, int val) {
        this.customerCars[i] = val;
    }

    public int getReplacementCar(int i) {
        return this.replacementCars[i];
    }

    public void setReplacementCar(int i, int val) {
        this.replacementCars[i] = val;
    }

    public MemFIFO getCustomersQueue() {
        return customersQueue;
    }

    public void setCustomersQueue(MemFIFO customersQueue) {
        this.customersQueue = customersQueue;
    }

    public MemFIFO getMechanicsQueue() {
        return mechanicsQueue;
    }

    public void setMechanicsQueue(MemFIFO mechanicsQueue) {
        this.mechanicsQueue = mechanicsQueue;
    }

    public MemFIFO getCustomerFirstRepairQueue() {
        return customerFirstRepairQueue;
    }

    public void setCustomerFirstRepairQueue(MemFIFO customerFirstRepairQueue) {
        this.customerFirstRepairQueue = customerFirstRepairQueue;
    }

    public List<List<Integer>> getCustomerMissingPartQueue() {
        return customerMissingPartQueue;
    }

    public void setCustomerMissingPartQueue(List<List<Integer>> customerMissingPartQueue) {
        this.customerMissingPartQueue = customerMissingPartQueue;
    }

    public int getTask(int i) {
        return this.taskDescription[i];
    }

    public void setTask(int i, int val) {
        this.taskDescription[i] = val;
    }

    public int getStockPart(int i) {
        return this.stockParts[i];
    }

    public void setStockPart(int i, int val) {
        this.stockParts[i] = val;
    }

    public int getSoldPart(int i) {
        return this.soldParts[i];
    }

    public void setSoldParts(int i, int val) {
        this.soldParts[i] = val;
    }
}

package regions;

import entities.*;
import utils.MemFIFO;

/**
 * TODO javadoc
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Lounge {
    /**
     * Número de clientes
     */
    private final int N_CUSTOMERS;

    /**
     *  Keys for the Replacement Cars
     */
    private int[] replacementKeys = {100, 101, 102};

    /**
     * The current customer state
     */
    private CustomerState currentCustomerState;

    /**
     * Indicia a chave da viatura de substituição que está a ser dada
     * @serialField carKeyToHandle
     */
    private int carKeyToHandle;

    /**
     * Indicia o numero total de carros reparados no dia
     * @serialField nTotalRepairedCars
     */
    private int nTotalRepairedCars;

    /**
    *  number of Customer waiting for keys
    *
    *    @serialField nCostumerForKey
    */
    private int nCustomerForKey;

    /**
    *  Customer waiting for keys
    *
    *    @serialField clientsWaitingForKey
    */
    private MemFIFO customersWaitingForKey;

    /**
     * Repositorio que armazena informação atual do sistema
     * @serialField repository
     */
    private GeneralRepository repository;

      /**
     * Variáveis para interação entre manager e cliente
     * @serialField repository
     */
    private boolean isCustomerTaskComplete;

    // todo javadoc
    private boolean isManagerTaskComplete;

    /**
    *  Cliente a ser atendido
    *
    *    @serialField nextCostumer
    */
    private int nextCustomer;

    /**
    *  Customer Queue
    *
    *    @serialField customerQueue
    */
    private MemFIFO customerQueue;

    /**
    *  Customer List Registry
    *
    *    @serialField customerRegistry
    */
    private MemFIFO repairedCars;

    /**
    *  Number of Requests
    *
    *    @serialField nRequests
    */
    private int nRequests;

    /**
    *  Customer Requests
    *
    *    @serialField costumerInQueue
    */
    private int nCustomerInQueue;

    /**
    *  Indica a quantidade de carros de substituição disponivel.
    *
    *    @serialField replacementCarAvailable
    */
    private int nReplacementCarAvailable;

    /**
    *  Indica se há peças que necessitem de supply.
    *
    *    @serialField replacementCarAvailable
    */
    private boolean needsResupplyParts;

    /**
    *  Indica o numero de carros reparados.
    *
    *    @serialField nRepairArea
    */
    private int nRepairedCar;

    private boolean[] customerWantsReplacementCar;

    /**
    *  Instanciação do Lounge.
    *
    */
    public Lounge(int nCustomers, int nReplacementCars, GeneralRepository repo) {
        this.N_CUSTOMERS = nCustomers;
        this.nextCustomer = -1;
        this.isCustomerTaskComplete = this.isManagerTaskComplete = false;
        this.repository = repo;
        this.nReplacementCarAvailable = nReplacementCars;
        this.needsResupplyParts = false;
        this.carKeyToHandle = -1;
        this.nCustomerInQueue = 0;
        this.customerQueue = new MemFIFO(nCustomers);
        this.customersWaitingForKey = new MemFIFO(nCustomers);
        this.repairedCars = new MemFIFO(nCustomers);
        this.customerWantsReplacementCar = new boolean[nCustomers];
    }

    /**
     * Operação queueIn (chamada pelo {@link Customer})
     *
     * Aqui, o cliente vai entrar na fila.
     * O {@link Manager} avisa quando chegar a vez de o cliente ser atendido
     */
    public synchronized void queueIn() {
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();

        // add customer to the queue
        customerQueue.write(customerId);
        nCustomerInQueue++;

        int currentCarID = ((Customer) Thread.currentThread()).getCarId();

        //if currentKey is equal to the customer id, then customer arrives with its car and wants to repair it
        ((Customer) Thread.currentThread()).setState(currentCarID == customerId ?
                CustomerState.RECEPTION_REPAIR : CustomerState.RECEPTION_PAYING);
        repository.setCustomerState(customerId, currentCarID == customerId ?
                CustomerState.RECEPTION_REPAIR : CustomerState.RECEPTION_PAYING);
        repository.setCustomersInQueue(nCustomerInQueue);

        //increase the number of requests and notify
        nRequests++;
        notifyAll();

        // block on condition variable
        while (customerId != nextCustomer) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        nCustomerInQueue--;
        repository.setCustomersInQueue(nCustomerInQueue);
    }

    /**
     * Operação collectKey (chamada pelo {@link Customer})
     *
     * Aqui, o cliente vai receber a chave da viatura de substituição
     * dada pelo {@link Manager}.
     * Devolve um int que representa a chave do carro.
     */
    public synchronized int collectKey() {
        int carKey;

        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        // change customer state, update repository
        ((Customer) Thread.currentThread()).setState(CustomerState.WAITING_FOR_REPLACE_CAR);
        repository.setCustomerState(customerId, CustomerState.WAITING_FOR_REPLACE_CAR);

        // client wait until manager searches for keys
        while (!isManagerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }

        if(carKeyToHandle != -1) {
            // manager has already a replacement car key
            carKey = carKeyToHandle;
            carKeyToHandle = -1;

            isCustomerTaskComplete = true;
            notifyAll();

            return carKey;
        }

        /* client needs to wait for a replacement car key
           there are no replacement cars available
         */

        // add customer to the queue waiting for key, update repository
        customersWaitingForKey.write(customerId);
        nCustomerForKey++;
        repository.setCustomersInQueueForKey(nCustomerForKey);

        //increase the number of requests and notify
        nRequests++;
        notifyAll();

        // block on condition variable, waiting for a replacement car key
        while ((nextCustomer == customerId) && (carKeyToHandle == -1)) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }

        // client has a replacement key, updating repository and notify manager
        nCustomerForKey--;
        repository.setCustomersInQueueForKey(nCustomerForKey);
        carKey = carKeyToHandle;
        carKeyToHandle = -1;

        isCustomerTaskComplete = true;
        notifyAll();

        return carKey;
    }

    /**
     * Operação talkWithManager (chamada pelo {@link Customer})
     *
     * Aqui, o cliente vai falar com o {@link Manager}.
     */
    public synchronized void talkWithManager() {
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        currentCustomerState = ((Customer) Thread.currentThread()).getCustomerState();
        customerWantsReplacementCar[customerId] = ((Customer) Thread.currentThread()).getWantsReplacementCar();

        // update customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION_TALK_WITH_MANAGER);
        repository.setCustomerState(customerId, CustomerState.RECEPTION_TALK_WITH_MANAGER);

        // block on condition variable
        while (!isManagerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }

        isManagerTaskComplete = false;

        ((Customer) Thread.currentThread()).setCarId(-1); //customer hands over the key
        repository.setCustomerCar(customerId, -1);

        //Customer hands key
        isCustomerTaskComplete = true;
        notifyAll();
    }


    /**
     * Operação payForTheService (chamada pelo {@link Customer})
     *
     * Aqui, o cliente vai paga o serviço esperando pela confirmação do {@link Manager}.
     * Após confirmação o {@link Customer} vai receber a chave.
     */
	public synchronized void payForTheService() {
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        // Customer pays
        isCustomerTaskComplete = true;
        notifyAll();

        // update customer state and repository
        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION_PAYING);
        repository.setCustomerState(customerId, CustomerState.RECEPTION_PAYING);

        // block until manager confirms payment
        while (!isManagerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isManagerTaskComplete = false;

        // if the customer used a replacement car, it's now available
        int tempCarID = ((Customer) Thread.currentThread()).getCarId();
        if(tempCarID != customerId && tempCarID != -1) {
            nReplacementCarAvailable++;
            replacementKeys[tempCarID - 100] = tempCarID;
        }

        ((Customer) Thread.currentThread()).setCarId(carKeyToHandle);
        repository.setCustomerCar(customerId, carKeyToHandle);
	}

    /**
     * Operação getNextTask (chamada pelo {@link entities.Manager})
     * Aqui o manager fica à espera da próxima tarefa.
     * Se o dia chegar ao fim, o manager avisa os mecanicos que o dia acabou.
     */
    public synchronized boolean getNextTask() {
        // update state
        ((Manager) Thread.currentThread()).setState(ManagerState.CHECKING_WHAT_TO_DO);
        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO);

        if(nTotalRepairedCars >= N_CUSTOMERS)
            return false;

        // block on condition variable
        while (nRequests == 0) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        nRequests--;
        return true;
    }

    /**
     * Operação appraiseSit chamada pelo {@link entities.Manager})
     * Aqui o manager verifica qual a sua próxima tarefa a realizar.
     * Prioridade: 1º reabastecer stock, 2º atribuir chave, 3º atender cliente
     */
    public synchronized ManagerTask appraiseSit() {
        // update Manager state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.CHECKING_WHAT_TO_DO);
        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO);

        if (needsResupplyParts) {
            needsResupplyParts = false;
            return ManagerTask.GET_PARTS;
        }
        else if (nCustomerForKey > 0 && nReplacementCarAvailable > 0)
            return ManagerTask.HAND_CAR_KEY;
        else if (nRepairedCar > 0) {
            nRepairedCar--;
            return ManagerTask.PHONE_CUSTOMER;
        }
        else if (nCustomerInQueue > 0)
            return ManagerTask.TALK_CUSTOMER;
        return ManagerTask.NONE;
    }

    /**
     * Operação talkToCustomer chamada pelo {@link entities.Manager})
     * Aqui o manager verifica qual o próximo cliente a chamar.
     * Devolve o próximo "customer"
     */
    public synchronized CustomerState talkToCustomer(ManagerTask task) {
        // update Manager state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        boolean clientWantsToRepair = false;

        switch (task) {
            case TALK_CUSTOMER:
                nextCustomer = (int) customerQueue.read();
                clientWantsToRepair = true;
                break;
            case HAND_CAR_KEY:
                nextCustomer = (int) customersWaitingForKey.read();
                clientWantsToRepair = false;
                break;
        }

        // avisa os customers que estão em fila
        ((Manager) Thread.currentThread()).setCurrentlyAttendingCustomer(nextCustomer);
        isManagerTaskComplete = true;
        notifyAll();

        if(clientWantsToRepair) {
            // wait for customer to respond
            while (!isCustomerTaskComplete) {
                try {
                    wait();
                } catch (InterruptedException e) { }
            }
            isCustomerTaskComplete = false;
            return currentCustomerState;
        }
        return null;
    }

    /**
     *  Operação receivePayment Chamada pelo {@link Manager})
     *  Recebe pagamento por parte do cliente
     */
    public synchronized void receivePayment() {
        // update Manager state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        // waits for customer to pay
        while (!isCustomerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isCustomerTaskComplete = false;

        //Manager confirms receivement of payment
        isManagerTaskComplete = true;
        notifyAll();
    }

    /**
     *  Operação handCarKey (chamada pelo {@link Manager})
     *  Devolve a chave ao cliente
     *
     *  @param returningKey identificação do cliente que recebe a chave
     */
    public synchronized void handCarKey(boolean returningKey) {
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        //Manager searches for keys
        if (returningKey) {
            //Manager wants to return the car to its owner
            carKeyToHandle = nextCustomer;
            nTotalRepairedCars++;
            repository.setTotalRepairedCars(nTotalRepairedCars);
        }
        else {
            //client wants a replacement car
            int temp = -1;
            if(nReplacementCarAvailable > 0) {
                for (int i = 0; i < replacementKeys.length; i++) {
                    if(replacementKeys[i] != -1) {
                        temp = replacementKeys[i];
                        replacementKeys[i] = -1;
                        nReplacementCarAvailable--;
                        break;
                    }
                }
            }
            carKeyToHandle = temp;
        }

        //Manager signals that he found the key.
        isManagerTaskComplete = true;
        notifyAll();

        //waits until client receives the key
        while (!isCustomerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isCustomerTaskComplete = false;
    }

    /**
    *  Operação letManagerKnow Chamada pelo {@link Mechanic})
    *  Notifica o manager que uma peça está em falta, se ainda não está registada
    *  @param partRequired identificação da parte em falta
    */
    public synchronized void letManagerKnow(int partRequired) {
        // update Mechanic state and repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        ((Mechanic) Thread.currentThread()).setState(MechanicState.ALERTING_MANAGER);
        repository.setMechanicState(mechanicId, MechanicState.ALERTING_MANAGER);
        repository.setPartMissingAlert(partRequired, true);

        // notify Manager that needs to resupply parts
        needsResupplyParts = true;
        nRequests++;
        notifyAll();
    }

    /**
    *  Operação repairConcluded Chamada pelo {@link entities.Mechanic})
    *  Notifica o manager que a reparação de um carro foi concluida.
    */
    public synchronized void repairConcluded() {
        // update Mechanic state and repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        ((Mechanic) Thread.currentThread()).setState(MechanicState.ALERTING_MANAGER);
        repository.setMechanicState(mechanicId, MechanicState.ALERTING_MANAGER);

        // add repaired car to queue of repaired cars, update repository
        int repairedCarId = ((Mechanic) Thread.currentThread()).getCurrentCarFixingId();
        repairedCars.write(repairedCarId);
        nRepairedCar++;
        repository.setCustomerRepairConcluded(repairedCarId, true);

        // Mechanic is not fixing any car
        ((Mechanic) Thread.currentThread()).setCurrentCarFixingId(-1);

        // notify Manager, repair concluded
        notifyAll();
	}

    /**
     * Função auxiliar para obter o numero de um cliente.
     * Chamada pelo {@link entities.Manager})
     */
    public synchronized int getClientNumber() {
        return (int) this.repairedCars.read();
    }

    public synchronized boolean wantReplacementCar(int customerId) {
        return this.customerWantsReplacementCar[customerId];
    }

    public synchronized int getCurrentCustomerID() {
        return this.nextCustomer;
    }
}

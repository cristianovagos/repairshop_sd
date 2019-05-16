package service;

import model.CustomerState;
import model.ManagerState;
import model.ManagerTask;
import model.MechanicState;
import interfaces.IGeneralRepository;
import utils.MemFIFO;
import utils.Pair;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe Lounge (Recepção)<br>
 *
 * Esta classe é responsável pela criação da Recepção da Oficina, uma
 * das entidades passivas do problema.<br>
 *
 * É neste local que o maior número de interações acontece, uma vez que
 * é o principal local de trabalho do Gerente (Manager) da Oficina,
 * que a vai gerindo consoante as necessidades e tarefas que aparecerem.
 * Atender os Clientes (Customer) quando estes chegam, fornecer
 * chaves de substituição, entre outros.<br>
 * Os Clientes irão ter aqui um ponto de contacto com a Oficina, onde
 * estão na fila de espera para serem atendidos, ou estarão a aguardar uma
 * chave de substituição para a eventualmente receber, ou irão efetuar o
 * pagamento do serviço de reparação.<br>
 * Já os Mecânicos (Mechanic) irão aqui informar o Gerente tanto da
 * falta de peças para as reparações como para a conclusão de reparações por
 * ele pedidas anteriormente.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Lounge {

    /**
     * Numero de clientes que existem no total.
     */
    private final int NCUSTOMERS;

    /**
     * Referência para o Repositório
     */
    private IGeneralRepository repository;

    /**
     * Fila de espera de clientes na Recepção
     */
    private MemFIFO customerQueue;

    /**
     * Número de clientes em espera na Recepção
     */
    private int numCustomerQueue;

    /**
     * Fila de espera de clientes à espera de chave
     * para viatura de substituição
     */
    private MemFIFO customerReplacementCarKeyQueue;

    /**
     * Número de clientes à espera de chave para
     * viatura de substituição
     */
    private int numCustomerReplacementCarKeyQueue;

    /**
     * Indicação se os Mecânicos precisam de reabastecimento de peças
     */
    private AtomicBoolean mechanicsNeedParts;

    /**
     * Fila de espera de viaturas reparadas aguardando
     * alerta ao cliente
     */
    private MemFIFO carsRepairedQueue;

    /**
     * Número de viaturas reparadas aguardando
     * alerta ao cliente
     */
    private int numCarsRepairedQueue;

    /**
     * Fila de espera de chaves de viaturas de substituição
     */
    private MemFIFO replacementCarKeys;

    /**
     * Número de viaturas de substituição disponíveis
     */
    private int numReplacementCarsAvailable;

    /**
     * Número total de carros reparados
     */
    private int numCarsRepaired;

    /**
     * Número de pedidos ao Gerente
     */
    private int managerRequests;

    /**
     * Lista de clientes à espera de reparação
     */
    private boolean[] waitForRepair;

    /**
     * Lista de clientes à espera de pagamento
     */
    private boolean[] waitForPayment;

    /**
     * Lista de clientes com chave de substituição
     * e chave respetiva
     */
    private int[] customerWithReplacementKey;

    /**
     * Numero de clientes servidos hoje
     */
    private int numCustomersServedToday;

    /**
     * Construtor de uma Recepção<br>
     *
     * Aqui será construído o objeto referente a uma Recepção.<br>
     *
     * @param nCustomers número total de clientes
     * @param nReplacementCars número total de viaturas de substituição
     * @param repo referência para o Repositório
     */
    public Lounge(int nCustomers, int nReplacementCars, IGeneralRepository repo) {
        this.NCUSTOMERS = nCustomers;
        this.repository = repo;
        this.customerQueue = new MemFIFO(nCustomers);
        this.numCustomerQueue = 0;
        this.customerReplacementCarKeyQueue = new MemFIFO(nCustomers);
        this.numCustomerReplacementCarKeyQueue = 0;
        this.mechanicsNeedParts = new AtomicBoolean(); // defaults to false
        this.carsRepairedQueue = new MemFIFO(nCustomers);
        this.numCarsRepairedQueue = 0;
        this.replacementCarKeys = new MemFIFO(nReplacementCars);
        this.numReplacementCarsAvailable = nReplacementCars;
        this.numCarsRepaired = 0;
        this.managerRequests = 0;
        this.waitForRepair = new boolean[nCustomers];
        this.waitForPayment = new boolean[nCustomers];
        this.customerWithReplacementKey = new int[nCustomers];
        this.numCustomersServedToday = 0;

        // initialize all wait conditions
        for (int i = 0; i < nCustomers; i++) {
            this.waitForRepair[i] = false;
            this.waitForPayment[i] = false;
            this.customerWithReplacementKey[i] = -1;
        }

        // initially all replacement cars and keys are available
        for (int i = 0; i < nReplacementCars; i++)
            this.replacementCarKeys.write(100 + i);
    }

    /* CUSTOMER */

    /**
     * Operação queueIn (chamada pelo Customer)<br>
     *
     * O Cliente chega à Recepção e aguarda na fila, para ser
     * atendido pelo Gerente (Manager). Tanto poderá colocar a sua
     * viatura para reparação ou para proceder ao pagamento do mesmo.<br>
     *
     * @param repairCompleted indicação se a reparação já foi feita
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void queueIn(boolean repairCompleted, int customerId) throws RemoteException {
        // customer is in repair shop queue
//        int customerId = ((ClientProxy) Thread.currentThread()).getCustomerId();
        customerQueue.write(customerId);
        numCustomerQueue++;
        repository.setCustomersInQueue(numCustomerQueue, false);

        // manager has work to do
        managerRequests++;
        notifyAll();

        // customer is at reception
//        ((ClientProxy) Thread.currentThread()).setCustomerState(CustomerState.RECEPTION);
        repository.setCustomerState(customerId, CustomerState.RECEPTION, true);

        if (repairCompleted) {
            // repair has completed, customer wants to pay

            // waits for its turn to pay
            waitForPayment[customerId] = true;
            while (waitForPayment[customerId]) {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }
        } else {
            // customer wants to repair their car

            // waits for its turn to repair
            waitForRepair[customerId] = true;
            while (waitForRepair[customerId]) {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }
        }
    }

    /**
     * Operação talkWithManager (chamada pelo Customer)<br>
     *
     * O Cliente fala com o Gerente para reparar
     * a sua viatura e entrega-lhe a chave da mesma.<br>
     *
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void talkWithManager(int customerId) throws RemoteException {
        // talk with manager

        // update state and interfaces
//        int customerId = ((ClientProxy) Thread.currentThread()).getCustomerId();
//        ((ClientProxy) Thread.currentThread()).setCustomerState(CustomerState.RECEPTION_TALK_WITH_MANAGER);
        repository.setCustomerState(customerId, CustomerState.RECEPTION_TALK_WITH_MANAGER, false);

        // give manager the customers car key
//        ((ClientProxy) Thread.currentThread()).setCustomerCarId(-1);
        repository.setCustomerCar(customerId, -1, true);
    }

    /**
     * Operação collectKey (chamada pelo Customer)<br>
     *
     * O Cliente pretende uma viatura de substituição enquanto a sua
     * viatura própria é reparada, e aguarda a entrega de uma chave de
     * uma das viaturas de substituição disponíveis por parte do Gerente.<br>
     *
     * @param customerId id do cliente
     * @return chave da viatura de substituição
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized int collectKey(int customerId) throws RemoteException {
        // update state and interfaces
//        int customerId = ((ClientProxy) Thread.currentThread()).getCustomerId();
//        ((ClientProxy) Thread.currentThread()).setCustomerState(CustomerState.WAITING_FOR_REPLACE_CAR);
        repository.setCustomerState(customerId, CustomerState.WAITING_FOR_REPLACE_CAR, false);

        // customer is on the queue waiting for a replacement car key
        customerReplacementCarKeyQueue.write(customerId);
        numCustomerReplacementCarKeyQueue++;
        repository.setCustomersInQueueForKey(numCustomerReplacementCarKeyQueue);

        // manager has work to do
        managerRequests++;
        notifyAll();

        // wait until manager has a replacement car key available
        while (customerWithReplacementKey[customerId] == -1) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        // get the replacement car assigned
        int replacementCarKey = customerWithReplacementKey[customerId];
//        ((ClientProxy) Thread.currentThread()).setCustomerCarId(replacementCarKey);
        repository.setCustomerCar(customerId, replacementCarKey, true);

        return replacementCarKey;
    }

    /**
     * Operação payForTheService (chamada pelo Customer)<br>
     *
     * O Cliente procede ao pagamento do serviço prestado pela Oficina.
     * Caso este tenha utilizado uma viatura de substituição devolve a
     * chave da mesma ao Gerente.<br>
     *
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void payForTheService(int customerId) throws RemoteException {
        // update state and interfaces
//        int customerId = ((ClientProxy) Thread.currentThread()).getCustomerId();
//        ((ClientProxy) Thread.currentThread()).setCustomerState(CustomerState.RECEPTION_PAYING);
        repository.setCustomerState(customerId, CustomerState.RECEPTION_PAYING, true);
    }

    /* MANAGER */

    /**
     * Operação getNextTask (chamada pelo Manager)<br>
     *
     * Caso hajam pedidos ao Gerente, este procede à realização
     * das tarefas pretendidas. Assim que não haja mais pedidos,
     * a Oficina é fechada, e os Mecânicos poderão ir embora.<br>
     *
     * @param firstRun indicação se é a primeira execução
     * @return indicação se ainda há trabalho para fazer na Oficina
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized boolean getNextTask(boolean firstRun) throws RemoteException {
        // update state and interfaces
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.CHECKING_WHAT_TO_DO);
//        boolean firstRun = ((ClientProxy) Thread.currentThread()).getFirstRun();
//        if (firstRun) ((ClientProxy) Thread.currentThread()).setFirstRun(false);
        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO, !firstRun);

        //manager will mark end of the day
        if(numCustomersServedToday >= NCUSTOMERS)
            return false;

        while (managerRequests == 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if (managerRequests > 0)
            managerRequests--;

        // there is still work left to do
        return true;
    }

    /**
     * Operação appraiseSit (chamada pelo Manager)<br>
     *
     * O Gerente avalia a próxima tarefa a desempenhar, de acordo
     * com a seguinte prioridade:<br>
     * <ul>
     *     <li>Reabastecimento de peças para os Mechanic
     *     poderem trabalhar na RepairArea através da SupplierSite</li>
     *     <li>Caso haja clientes em fila de espera para obter uma chave para
     *     viatura de substituição.</li>
     *     <li>Caso haja viaturas reparadas, o Gerente vai chamar os clientes
     *     para as virem buscar.</li>
     *     <li>Atender os clientes em fila de espera na Recepção</li>
     * </ul>
     *
     * @return par de objetos com a próxima tarefa a ser executada pelo Gerente e/ou
     *         id do cliente a chamar para levantar a viatura
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized Pair appraiseSit() throws RemoteException {
        if (mechanicsNeedParts.getAndSet(false))
            return new Pair<>(ManagerTask.GET_PARTS, null);
        else if (numCustomerReplacementCarKeyQueue > 0 && numReplacementCarsAvailable > 0)
            return new Pair<>(ManagerTask.TALK_CUSTOMER, null);
        else if (numCarsRepairedQueue > 0) {
            int customerToCall = (int) carsRepairedQueue.read();
            numCarsRepairedQueue--;
//            ((ClientProxy) Thread.currentThread()).setCurrentlyAttendingCustomer(customerToCall);
            return new Pair<>(ManagerTask.PHONE_CUSTOMER, customerToCall);
        }
        else if (numCustomerQueue > 0)
            return new Pair<>(ManagerTask.TALK_CUSTOMER, null);
        managerRequests++;
        return new Pair<>(ManagerTask.NONE, null);
    }

    /**
     * Operação talkToCustomer (chamada pelo Manager)<br>
     *
     * O Gerente irá atender o cliente para saber o que
     * este pretende, de uma das seguintes opções:<br>
     * <ul>
     *     <li>Reparar a sua viatura</li>
     *     <li>Obter uma viatura de substituição</li>
     *     <li>Efetuar pagamento do serviço</li>
     * </ul>
     *
     * @return par de objetos com o estado do cliente atual e/ou o id do cliente a atender
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized Pair talkToCustomer() throws RemoteException {
        // update state and interfaces
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER, false);

        if (numReplacementCarsAvailable > 0 && numCustomerReplacementCarKeyQueue > 0) {
            /* there are replacement cars available, and
             * customers waiting for replacement car keys
             */
            return new Pair<>(CustomerState.WAITING_FOR_REPLACE_CAR, null);
        } else if (!customerQueue.empty()) {
            /* talk to the first customer in queue
             * and figure out what he wants
             */
            int customerToAttend = (int) customerQueue.read();
            numCustomerQueue--;
            repository.setCustomersInQueue(numCustomerQueue, true);

            if (waitForPayment[customerToAttend]) {
                // customer wants to pay
                waitForPayment[customerToAttend] = false;
//                ((ClientProxy) Thread.currentThread()).setCurrentlyAttendingCustomer(customerToAttend);
                notifyAll();
                return new Pair<>(CustomerState.RECEPTION_PAYING, customerToAttend);
            } else if (waitForRepair[customerToAttend]) {
                // customer wants to repair his car
                waitForRepair[customerToAttend] = false;
//                ((ClientProxy) Thread.currentThread()).setCurrentlyAttendingCustomer(customerToAttend);
                notifyAll();
                return new Pair<>(CustomerState.RECEPTION_REPAIR, customerToAttend);
            }
        }
        return new Pair<>(CustomerState.NONE, null);
    }

    /**
     * Operação handCarKey (chamada pelo Manager)<br>
     *
     * O Gerente irá dar uma das chaves das viaturas de substituição
     * disponíveis ao Customer.<br>
     *
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void handCarKey() throws RemoteException {
        // update state and interfaces
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER, false);

        // get first customer in queue waiting for replacement car
        int customerToAttend = (int) customerReplacementCarKeyQueue.read();
        numCustomerReplacementCarKeyQueue--;
        repository.setCustomersInQueueForKey(numCustomerReplacementCarKeyQueue);

        // let customer know there is a key available
        customerWithReplacementKey[customerToAttend] = (int)replacementCarKeys.read();
        numReplacementCarsAvailable--;
        notifyAll();
    }

    /**
     * Operação receivePayment (chamada pelo Manager)<br>
     *
     * O Gerente recebe o pagamento do serviço por parte do Customer<br>
     *
     * @param customerToAttend o id do cliente a fazer pagamento
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void receivePayment(int customerToAttend) throws RemoteException {
        // update state and interfaces
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER, true);

        // let customer know that he needs to pay
        waitForPayment[customerToAttend] = false;
        if (customerWithReplacementKey[customerToAttend] != -1) {
            // if this customer have used a replacement car, that car it's available to others
            replacementCarKeys.write(customerWithReplacementKey[customerToAttend]);
            numReplacementCarsAvailable++;
            customerWithReplacementKey[customerToAttend] = -1;
        }

        notifyAll();
        numCustomersServedToday++;
    }

    /* MECHANIC */

    /**
     * Operação letManagerKnow (chamada pelo Mechanic)<br>
     *
     * O Mecânico informa o Gerente (Manager) de que não existem
     * peças para que este possa reparar uma viatura.<br>
     *
     * @param partRequired peça em falta
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void letManagerKnow(int partRequired, int mechanicId) throws RemoteException {
        // update state and interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.ALERTING_MANAGER_FOR_PARTS);
        repository.setMechanicState(mechanicId, MechanicState.ALERTING_MANAGER_FOR_PARTS, false);

        // mark part as needed to replenish
        repository.setPartMissingAlert(partRequired, true, true);
        mechanicsNeedParts.set(true);

        // manager has work to do
        managerRequests++;
        notifyAll();
    }

    /**
     * Operação repairConcluded (chamada pelo Mechanic)<br>
     *
     * O Mecânico terminou de reparar uma viatura, e irá informar o Gerente
     * que esta está pronta a ser levantada pelo seu proprietário,
     * o Cliente.<br>
     *
     * @param mechanicId id do mecânico
     * @param carFixed indicação se a viatura já está pronta
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void repairConcluded(int mechanicId, int carFixed) throws RemoteException {
        // update state and interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.ALERTING_MANAGER_REPAIR_CONCLUDED);
        repository.setMechanicState(mechanicId, MechanicState.ALERTING_MANAGER_REPAIR_CONCLUDED, false);

        // add car to the repaired cars queue
//        int carFixed = ((ClientProxy) Thread.currentThread()).getCurrentCarFixingId();
        repository.setCustomerCarRepaired(carFixed);
        carsRepairedQueue.write(carFixed);
        numCarsRepairedQueue++;

        // update number of total repaired cars
        numCarsRepaired++;
        repository.setTotalRepairedCars(numCarsRepaired);

        // manager has work to do
        managerRequests++;
        notifyAll();
    }
}

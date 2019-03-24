package regions;

import entities.*;
import utils.MemFIFO;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe Lounge (Recepção)
 *
 * Esta classe é responsável pela criação da Recepção da Oficina, uma
 * das entidades passivas do problema.
 *
 * É neste local que o maior número de interações acontece, uma vez que
 * é o principal local de trabalho do Gerente {@link Manager} da Oficina,
 * que a vai gerindo consoante as necessidades e tarefas que aparecerem.
 * Atender os Clientes {@link Customer} quando estes chegam, fornecer
 * chaves de substituição, entre outros.
 * Os Clientes irão ter aqui um ponto de contacto com a Oficina, onde
 * estão na fila de espera para serem atendidos, ou estarão a aguardar uma
 * chave de substituição para a eventualmente receber, ou irão efetuar o
 * pagamento do serviço de reparação.
 * Já os Mecânicos {@link Mechanic} irão aqui informar o Gerente tanto da
 * falta de peças para as reparações como para a conclusão de reparações por
 * ele pedidas anteriormente.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class NewLounge {

    /**
     * Referência para o Repositório
     * @see GeneralRepository
     */
    private GeneralRepository repository;

    /**
     * Fila de espera de clientes na Recepção
     * {@link Customer}
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
     * Indicação se os Mecânicos {@link Mechanic}
     * precisam de reabastecimento de peças
     */
    private AtomicBoolean mechanicsNeedParts;

    /**
     * Fila de espera de viaturas reparadas aguardando
     * alerta ao cliente {@link Customer}
     */
    private MemFIFO carsRepairedQueue;

    /**
     * Número de viaturas reparadas aguardando
     * alerta ao cliente {@link Customer}
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
     * {@link Manager}
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
     * Lista de clientes à espera de chave para viatura
     * de substituição
     */
    private boolean[] waitForReplacementCarKey;

    /**
     * Lista de clientes com chave de substituição
     * e chave respetiva
     */
    private int [] customerWithReplacementKey;

    /**
     * Numero de clientes servidos hoje
     */
    private int numCustomersServedToday;

    /**
     * Numero de clientes que existem no total.
     */
    private final int NCUSTOMERS;

    /**
     * Clientes que pagaram.
     * Variável de bloqueio para confirmação de pagamento
     */
    private boolean[] customerPaid;

    /**
     * Construtor de uma Recepção
     *
     * Aqui será construído o objeto referente a uma Recepção.
     *
     * @param nCustomers número total de clientes {@link Customer}
     * @param nReplacementCars número total de viaturas de substituição
     * @param repo referência para o Repositório {@link GeneralRepository}
     */
    public NewLounge(int nCustomers, int nReplacementCars, GeneralRepository repo) {
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
        this.waitForReplacementCarKey = new boolean[nCustomers];

        // initialize all wait conditions
        for (int i = 0; i < nCustomers; i++) {
            this.waitForRepair[i] = false;
            this.waitForPayment[i] = false;
            this.waitForReplacementCarKey[i] = false;
        }

        // initially all replacement cars and keys are available
        for (int i = 0; i < nReplacementCars; i++)
            this.replacementCarKeys.write(100 + i);

        this.customerWithReplacementKey = new int[nCustomers];
        //NEW
        for(int i = 0; i<nCustomers;i++)
        {
            customerWithReplacementKey[i] = -1;
        }
        this.numCustomersServedToday = 0;
        this.NCUSTOMERS = nCustomers;

        this.customerPaid = new boolean[nCustomers];
    }

    /* CUSTOMER */

    /**
     * Operação queueIn (chamada pelo {@link Customer})
     *
     * O Cliente chega à Recepção e aguarda na fila, para ser
     * atendido pelo Gerente {@link Manager}. Tanto poderá colocar a sua
     * viatura para reparação ou para proceder ao pagamento do mesmo.
     *
     * @param repairCompleted indicação se a reparação já foi feita
     */
    public synchronized void queueIn(boolean repairCompleted) {
        // customer is in repair shop queue
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        customerQueue.write(customerId);
        numCustomerQueue++;
        repository.setCustomersInQueue(numCustomerQueue);

        // manager has work to do
        managerRequests++;
        notifyAll();

        // customer is at reception
        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION);
        repository.setCustomerState(customerId, CustomerState.RECEPTION);

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
     * Operação talkWithManager (chamada pelo {@link Customer})
     *
     * O Cliente fala com o Gerente {@link Manager} para reparar
     * a sua viatura e entrega-lhe a chave da mesma.
     */
    public synchronized void talkWithManager() {
        // talk with manager

        // update state and repository
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION_TALK_WITH_MANAGER);
        repository.setCustomerState(customerId, CustomerState.RECEPTION_TALK_WITH_MANAGER);

        // give manager the customers car key
        ((Customer) Thread.currentThread()).setCarId(-1);
        repository.setCustomerCar(customerId, -1);
    }

    /**
     * Operação collectKey (chamada pelo {@link Customer})
     *
     * O Cliente pretende uma viatura de substituição enquanto a sua
     * viatura própria é reparada, e aguarda a entrega de uma chave de
     * uma das viaturas de substituição disponíveis por parte do Gerente
     * {@link Manager}.
     *
     * @return chave da viatura de substituição
     */
    public synchronized int collectKey() {
        // update state and repository
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        ((Customer) Thread.currentThread()).setState(CustomerState.WAITING_FOR_REPLACE_CAR);
        repository.setCustomerState(customerId, CustomerState.WAITING_FOR_REPLACE_CAR);

        // customer is on the queue waiting for a replacement car key
        customerReplacementCarKeyQueue.write(customerId);
        numCustomerReplacementCarKeyQueue++;
        repository.setCustomersInQueueForKey(numCustomerReplacementCarKeyQueue);

        // manager has work to do
        managerRequests++;
        notifyAll();

        // wait until manager has a replacement car key available
        //waitForReplacementCarKey[customerId] = true;
        while (customerWithReplacementKey[customerId]==-1) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        // get the first replacement car available
        //int replacementCarKey = (int) replacementCarKeys.read();
        //NEW
        int replacementCarKey = customerWithReplacementKey[customerId];
        //numReplacementCarsAvailable--;
        ((Customer) Thread.currentThread()).setCarId(replacementCarKey);
        repository.setCustomerCar(customerId, replacementCarKey);

        return replacementCarKey;
    }

    /**
     * Operação payForTheService (chamada pelo {@link Customer})
     *
     * O Cliente procede ao pagamento do serviço prestado pela Oficina.
     * Caso este tenha utilizado uma viatura de substituição devolve a
     * chave da mesma ao Gerente {@link Manager}.
     */
    public synchronized void payForTheService() {
        // update state and repository
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION_PAYING);
        repository.setCustomerState(customerId, CustomerState.RECEPTION_PAYING);

        customerPaid[customerId] = true;
    }

    /* MANAGER */

    /**
     * Operação getNextTask (chamada pelo {@link Manager})
     *
     * Caso hajam pedidos ao Gerente, este procede à realização
     * das tarefas pretendidas. Assim que não haja mais pedidos,
     * a Oficina é fechada, e os Mecânicos {@link Mechanic} poderão
     * ir embora.
     *
     * @return indicação se ainda há trabalho para fazer na Oficina
     */
    public synchronized boolean getNextTask() {
        // update state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.CHECKING_WHAT_TO_DO);
        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO);


        //manager will mark end of the day
        if(numCustomersServedToday >= NCUSTOMERS)
            return false;

        while (managerRequests == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                // manager will mark end of the day
                //return false;
            }
        }
        if (managerRequests > 0)
            managerRequests--;


        // there is still work left to do
        return true;
    }

    /**
     * Operação appraiseSit (chamada pelo {@link Manager})
     *
     * O Gerente avalia a próxima tarefa a desempenhar, de acordo
     * com a seguinte prioridade:
     * <ul>
     *     <li>Reabastecimento de peças para os {@link Mechanic}
     *     poderem trabalhar na {@link RepairArea} através da {@link SupplierSite}</li>
     *     <li>Caso haja clientes em fila de espera para obter uma chave para
     *     viatura de substituição.</li>
     *     <li>Caso haja viaturas reparadas, o Gerente vai chamar os clientes
     *     para as virem buscar.</li>
     *     <li>Atender os clientes em fila de espera na Recepção</li>
     * </ul>
     *
     * @return a próxima tarefa a ser executada pelo Gerente
     */
    public synchronized ManagerTask appraiseSit() {
        // update state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.CHECKING_WHAT_TO_DO);
        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO);

        if (mechanicsNeedParts.getAndSet(false))
            return ManagerTask.GET_PARTS;
        else if (numCustomerReplacementCarKeyQueue > 0 && numReplacementCarsAvailable > 0)
            return ManagerTask.TALK_CUSTOMER;
        else if (numCarsRepairedQueue > 0) {
            int customerToCall = (int) carsRepairedQueue.read();
            numCarsRepairedQueue--;
            ((Manager) Thread.currentThread()).setCurrentlyAttendingCustomer(customerToCall);
            return ManagerTask.PHONE_CUSTOMER;
        }
        else if (numCustomerQueue > 0)
            return ManagerTask.TALK_CUSTOMER;
        managerRequests++;
        return ManagerTask.NONE;
    }

    /**
     * Operação talkToCustomer (chamada pelo {@link Manager})
     *
     * O Gerente irá atender o cliente {@link Customer} para saber o que
     * este pretende, de uma das seguintes opções:
     * <ul>
     *     <li>Reparar a sua viatura</li>
     *     <li>Obter uma viatura de substituição</li>
     *     <li>Efetuar pagamento do serviço</li>
     * </ul>
     *
     * @return estado do cliente atual
     */
    public synchronized CustomerState talkToCustomer() {
        // update state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        if (numReplacementCarsAvailable > 0 && numCustomerReplacementCarKeyQueue > 0) {
            /* there are replacement cars available, and
             * customers waiting for replacement car keys
             */
             return CustomerState.WAITING_FOR_REPLACE_CAR;
        } else if (!customerQueue.empty()) {
            /* talk to the first customer in queue
             * and figure out what he wants
             */
            int customerToAttend = (int) customerQueue.read();
            numCustomerQueue--;
            repository.setCustomersInQueue(numCustomerQueue);

            if (waitForPayment[customerToAttend]) {
                // customer wants to pay
                waitForPayment[customerToAttend] = false;
                ((Manager) Thread.currentThread()).setCurrentlyAttendingCustomer(customerToAttend);
                notifyAll();
                return CustomerState.RECEPTION_PAYING;
            } else if (waitForRepair[customerToAttend]) {
                // customer wants to repair his car
                waitForRepair[customerToAttend] = false;
                ((Manager) Thread.currentThread()).setCurrentlyAttendingCustomer(customerToAttend);
                notifyAll();
                return CustomerState.RECEPTION_REPAIR;
            }
        }
        return CustomerState.NONE;
    }

    /**
     * Operação handCarKey (chamada pelo {@link Manager})
     *
     * O Gerente irá dar uma das chaves das viaturas de substituição
     * disponíveis ao {@link Customer}.
     */
    public synchronized void handCarKey() {
        // update state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        // get first customer in queue waiting for replacement car
        int customerToAttend = (int) customerReplacementCarKeyQueue.read();
        numCustomerReplacementCarKeyQueue--;
        repository.setCustomersInQueueForKey(numCustomerReplacementCarKeyQueue);

        // let customer know there is a key available
        //waitForReplacementCarKey[customerToAttend] = false;

        customerWithReplacementKey[customerToAttend] = (int)replacementCarKeys.read();

        numReplacementCarsAvailable--;
        notifyAll();
    }

    /**
     * Operação receivePayment (chamada pelo {@link Manager})
     *
     * O Gerente recebe o pagamento do serviço por parte do {@link Customer}
     *
     * @param customerToAttend o id do cliente a fazer pagamento
     */
    public synchronized void receivePayment(int customerToAttend) {
        // update state and repository
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        // let customer know that he needs to pay
        waitForPayment[customerToAttend] = false;
        if (customerWithReplacementKey[customerToAttend] != -1)
        {
            replacementCarKeys.write(customerWithReplacementKey[customerToAttend]);
            numReplacementCarsAvailable++;

            customerWithReplacementKey[customerToAttend] = -1;
        }

        notifyAll();
        while (customerPaid[customerToAttend]) {
            try {
                wait();
            } catch (InterruptedException e) {
                // manager will mark end of the day
                //return false;
            }
        }
        numCustomersServedToday++;

    }

    /* MECHANIC */

    /**
     * Operação letManagerKnow (chamada pelo {@link Mechanic})
     *
     * O Mecânico informa o Gerente {@link Manager} de que não existem
     * peças para que este possa reparar uma viatura.
     *
     * @param partRequired peça em falta
     */
    public synchronized void letManagerKnow(int partRequired) {
        // update state and repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        ((Mechanic) Thread.currentThread()).setState(MechanicState.ALERTING_MANAGER_FOR_PARTS);
        repository.setMechanicState(mechanicId, MechanicState.ALERTING_MANAGER_FOR_PARTS);

        // mark part as needed to replenish
        repository.setPartMissingAlert(partRequired, true);
        mechanicsNeedParts.set(true);

        // manager has work to do
        managerRequests++;
        notifyAll();
    }

    /**
     * Operação repairConcluded (chamada pelo {@link Mechanic})
     *
     * O Mecânico terminou de reparar uma viatura, e irá informar o Gerente
     * {@link Manager} que esta está pronta a ser levantada pelo seu proprietário,
     * o Cliente {@link Customer}.
     */
    public synchronized void repairConcluded() {
        // update state and repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        ((Mechanic) Thread.currentThread()).setState(MechanicState.ALERTING_MANAGER_REPAIR_CONCLUDED);
        repository.setMechanicState(mechanicId, MechanicState.ALERTING_MANAGER_REPAIR_CONCLUDED);

        // add car to the repaired cars queue
        int carFixed = ((Mechanic) Thread.currentThread()).getCurrentCarFixingId();
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

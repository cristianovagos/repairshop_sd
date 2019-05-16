package service;

import interfaces.IGeneralRepository;
import model.ManagerState;
import model.MechanicState;
import utils.Constants;
import utils.MemFIFO;

import java.rmi.RemoteException;

/**
 * Classe RepairArea (Área de Reparação)<br>
 *
 * Esta classe é responsável pela criação da Área de Reparação, uma das
 * entidades passivas do problema.<br>
 *
 * A Área de Reparação é o local onde decorrem as reparações das viaturas,
 * por parte dos Mecânicos, sendo que é aqui onde eles irão
 * estar a maior parte do tempo, seja a reparar viaturas, seja à espera da
 * indicação do Gerente de que há trabalho a fazer. Nesta área
 * existe um número de peças em stock para que os Mecânicos possam substituir
 * nas viaturas que necessitem de reparar, e caso estas porventura faltem, é
 * responsabilidade do Gerente obter mais peças e por sua vez restabelecer o
 * stock para que os Mecânicos voltem ao trabalho.<br>
 * Quando o Gerente dá o dia por terminado, todos os Mecânicos vão embora.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class RepairArea {

    /**
     * Repositório Geral de dados
     */
    private IGeneralRepository repository;

    /**
     * Fila de espera de clientes para o seu carro ser reparado
     */
    private MemFIFO customerFirstRepairQueue;

    /**
     * Fila de espera de carros que necessitam de uma dada peça
     */
    private MemFIFO[] customerMissingPartQueue;

    /**
     * Número de pedidos de serviço para os Mechanic
     */
    private int nRequestedServices;

    /**
     * Peças em stock
     */
    private int[] stockParts;

    /**
     * Indicação se o dia de trabalho terminou
     */
    private boolean endOfDay;

    /**
     * Registo dos Mecânicos a dormir
     */
    private Thread[] mechanicsRegistry;

    /**
     * Construtor da Área de Reparação (Repair Area)<br>
     *
     * Aqui será construído o objeto referente à Área de Reparação.<br>
     *
     * @param nCustomers número de clientes
     * @param nParts número de tipos de peças
     * @param repo referência para o Repositório ({@link IGeneralRepository})
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public RepairArea(int nCustomers, int nParts, IGeneralRepository repo) throws RemoteException {
        this.repository = repo;
        this.customerFirstRepairQueue = new MemFIFO(nCustomers);
        this.customerMissingPartQueue = new MemFIFO[nParts];
        this.nRequestedServices = 0;
        this.stockParts = new int[nParts];
        this.endOfDay = false;

        // initialize data structures
        for (int i = 0; i < nParts; i++) {
            this.customerMissingPartQueue[i] = new MemFIFO(nCustomers);
            this.stockParts[i] = (int) (10 * Math.random());
        }

        repository.setStockParts(stockParts, false);

        mechanicsRegistry = new Thread[Constants.NUM_MECHANICS];
        for (int i = 0; i < Constants.NUM_MECHANICS; i++)
            mechanicsRegistry[i] = null;
    }

    /**
     * Operação readThePaper (chamada pelo Mechanic)<br>
     *
     * O Mecânico irá estar bloqueado até que lhe seja atribuído um serviço<br>
     *
     * @param mechanicId id do mecânico
     * @param firstRun indicação se é a primeira execução
     * @return estado do dia de trabalho
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized boolean readThePaper(int mechanicId, boolean firstRun) throws RemoteException {
        // update mechanic state and interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
//        boolean firstRun = ((ClientProxy) Thread.currentThread()).getFirstRun();
//        if (firstRun) ((ClientProxy) Thread.currentThread()).setFirstRun(false);
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.WAITING_FOR_WORK);
        repository.setMechanicState(mechanicId, MechanicState.WAITING_FOR_WORK, !firstRun);

        if(!endOfDay){
            while (nRequestedServices == 0) {
                mechanicsRegistry[mechanicId] = Thread.currentThread();     // regista o thread mecânico
                try {
                    wait();
                } catch (InterruptedException e) {
                    mechanicsRegistry[mechanicId] = null;                   // elimina o registo
                    return false;
                }
            }
            nRequestedServices--;
        }
        mechanicsRegistry[mechanicId] = null;                               // elimina o registo
        return !endOfDay;
    }

    /**
     * Operação markEndOfDay (chamada pelo Manager)<br>
     *
     * Marca o encerramento do dia para o Mechanic.<br>
     */
    public synchronized void markEndOfDay() {
        endOfDay = true;
        nRequestedServices = 1;
        notifyAll();
    }

    /**
     * Operação startRepairProcedure (chamada pelo Mechanic)<br>
     *
     * O Mecânico vai à lista de espera dos clientes para saber qual a viatura
     * que vai arranjar.<br>
     *
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized int startRepairProcedure(int mechanicId) throws RemoteException {
        // get the first customer vehicle in the queue
        int carId = (int) customerFirstRepairQueue.read();

        // update Mechanic state
//        ((ClientProxy) Thread.currentThread()).setCurrentCarFixingId(carId);
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.FIXING_THE_CAR);

        // update interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
        repository.setMechanicState(mechanicId, MechanicState.FIXING_THE_CAR, true);
        return carId;
    }

    /**
     * Operação getRequiredPart (chamada pelo Mechanic)<br>
     *
     * O Mecânico irá verificar se a peça pretendida para o arranjo da viatura
     * está disponível.<br>
     *
     * @param partId peça pretendida
     * @param mechanicId id do mecânico
     * @param carId id do carro a ser arranjado
     * @return indicação se a peça pretendida existe em stock
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized boolean getRequiredPart(int partId, int mechanicId, int carId) throws RemoteException {
        // update Mechanic state
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.CHECKING_STOCK);

        // update interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
        repository.setMechanicState(mechanicId, MechanicState.CHECKING_STOCK, false);

        // check if part is in stock
        if (stockParts[partId] > 0)
            return true;

        // add Customer car to the queue of cars with missing parts
//        int carId = ((ClientProxy) Thread.currentThread()).getCurrentCarFixingId();
        customerMissingPartQueue[partId].write(carId);

        // update interfaces with missing part
        repository.addMissingPart(partId);

        // Mechanic now is not fixing any car
//        ((ClientProxy) Thread.currentThread()).setCurrentCarFixingId(-1);

        return false;
    }

    /**
     * Operação partAvailable (chamada pelo Mechanic)<br>
     *
     * Obtém a peça para proceder ao arranjo.<br>
     *
     * @param partId a peça a obter
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void partAvailable(int partId, int mechanicId) throws RemoteException {
        stockParts[partId]--;

        // update state and interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.CHECKING_STOCK);
        repository.setMechanicState(mechanicId, MechanicState.CHECKING_STOCK, false);
        repository.setStockParts(stockParts, true);
    }

    /**
     * Operação resumeRepairProcedure (chamada pelo Mechanic)<br>
     *
     * Recomeço da reparação, agora que o Mecânico tem a peça pretendida para
     * substituição.<br>
     *
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void resumeRepairProcedure(int mechanicId) throws RemoteException {
        // update Mechanic state
//        ((ClientProxy) Thread.currentThread()).setMechanicState(MechanicState.FIXING_THE_CAR);

        // update interfaces
//        int mechanicId = ((ClientProxy) Thread.currentThread()).getMechanicId();
        repository.setMechanicState(mechanicId, MechanicState.FIXING_THE_CAR, true);
    }

    /**
     * Operação storePart (chamada pelo Manager)<br>
     *
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * SupplierSite, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o Mechanic.<br>
     *
     * @param newParts peças novas a serem incluídas no stock
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void storePart(int[] newParts) throws RemoteException {
        // update Manager state and interfaces
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.REPLENISH_STOCK);
        repository.setManagerState(ManagerState.REPLENISH_STOCK, false);

        for (int part = 0; part < stockParts.length; part++) {
            // replenishing stock of parts
            stockParts[part] += newParts[part];

            // update interfaces
            repository.setPartMissingAlert(part, false, false);
            for (int i = 0; i < newParts[part]; i++)
                repository.removeMissingPart(part);

            /* traverse the queue of Cars waiting for each part, add them
               into the repair queue, and notify Mechanics there's work to do
             */
            while (!customerMissingPartQueue[part].empty()) {
                int customerId = (int) customerMissingPartQueue[part].read();
                customerFirstRepairQueue.write(customerId);
                nRequestedServices++;
            }
        }

        // update interfaces
        repository.setStockParts(stockParts, true);

        notifyAll();
    }

    /**
     * Operação registerService (chamada pelo Manager)<br>
     *
     * O Manager irá registar um pedido de reparação da viatura do Customer,
     * e notificar os Mechanic de que existe um serviço disponível.<br>
     *
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void registerService(int customerId) throws RemoteException {
        // add Customer to the repair queue
        customerFirstRepairQueue.write(customerId);

        // update Manager state
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.POSTING_JOB);

        // update interfaces
        repository.setManagerState(ManagerState.POSTING_JOB, false);
        repository.managerRequestedService();

        // mechanics have one more service to do
        nRequestedServices++;

        // notify them
        notifyAll();
    }

    /**
     * Acordar intempestivamente um mecânico para sinalizar fim de operações.
     * Apenas o faz quando o Manager dá como encerrado o dia de trabalho.<br>
     *
     * @param mechanicId id do Mecânico
     */
    public synchronized void endOperation(int mechanicId) {
        if (mechanicsRegistry[mechanicId] != null && endOfDay)
            mechanicsRegistry[mechanicId].interrupt();
    }
}

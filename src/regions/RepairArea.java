package regions;

import entities.*;
import utils.MemFIFO;

/**
 * Classe RepairArea (Área de Reparação)<br>
 *
 * Esta classe é responsável pela criação da Área de Reparação, uma das
 * entidades passivas do problema.<br>
 *
 * A Área de Reparação é o local onde decorrem as reparações das viaturas,
 * por parte dos Mecânicos ({@link Mechanic}), sendo que é aqui onde eles irão
 * estar a maior parte do tempo, seja a reparar viaturas, seja à espera da
 * indicação do Gerente ({@link Manager}) de que há trabalho a fazer. Nesta área
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
    private GeneralRepository repository;

    /**
     * Fila de espera de clientes para o seu carro ser reparado
     */
    private MemFIFO customerFirstRepairQueue;

    /**
     * Fila de espera de carros que necessitam de uma dada peça
     */
    private MemFIFO[] customerMissingPartQueue;

    /**
     * Número de pedidos de serviço para os {@link Mechanic}
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
     * Construtor da Área de Reparação (Repair Area)<br>
     *
     * Aqui será construído o objeto referente à Área de Reparação.<br>
     *
     * @param nCustomers número de clientes
     * @param nParts número de tipos de peças
     * @param repo referência para o Repositório ({@link GeneralRepository})
     */
    public RepairArea(int nCustomers, int nParts, GeneralRepository repo) {
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
    }

    /**
     * Operação readThePaper (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico irá estar bloqueado até que lhe seja atribuído um serviço<br>
     *
     * @return estado do dia de trabalho
     */
    public synchronized boolean readThePaper() {
        // update mechanic state and repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        ((Mechanic) Thread.currentThread()).setState(MechanicState.WAITING_FOR_WORK);
        repository.setMechanicState(mechanicId, MechanicState.WAITING_FOR_WORK);

        if(!endOfDay){
            while (nRequestedServices == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return false;
                }
            }
            nRequestedServices--;
        }
        return !endOfDay;
    }

    /**
     * Operação markEndOfDay (chamada pelo {@link Manager})<br>
     *
     * Marca o encerramento do dia para o {@link Mechanic}.<br>
     */
    public synchronized void markEndOfDay() {
        endOfDay = true;
        nRequestedServices = 1;
        notifyAll();
    }

    /**
     * Operação startRepairProcedure (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico vai à lista de espera dos clientes para saber qual a viatura
     * que vai arranjar.<br>
     */
    public synchronized void startRepairProcedure() {
        // get the first customer vehicle in the queue
        int carId = (int) customerFirstRepairQueue.read();

        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setCurrentCarFixingId(carId);
        ((Mechanic) Thread.currentThread()).setState(MechanicState.FIXING_THE_CAR);

        // update repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        repository.setMechanicState(mechanicId, MechanicState.FIXING_THE_CAR);
    }

    /**
     * Operação getRequiredPart (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico irá verificar se a peça pretendida para o arranjo da viatura
     * está disponível.<br>
     *
     * @param partId peça pretendida
     * @return indicação se a peça pretendida existe em stock
     */
    public synchronized boolean getRequiredPart(int partId) {
        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setState(MechanicState.CHECKING_STOCK);

        // update repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        repository.setMechanicState(mechanicId, MechanicState.CHECKING_STOCK);

        // check if part is in stock
        if (stockParts[partId] > 0)
            return true;

        // add Customer car to the queue of cars with missing parts
        int carId = ((Mechanic) Thread.currentThread()).getCurrentCarFixingId();
        customerMissingPartQueue[partId].write(carId);

        // update repository with missing part
        repository.addMissingPart(partId);

        // Mechanic now is not fixing any car
        ((Mechanic) Thread.currentThread()).setCurrentCarFixingId(-1);

        return false;
    }

    /**
     * Operação partAvailable (chamada pelo {@link Mechanic})<br>
     *
     * Obtém a peça para proceder ao arranjo.<br>
     *
     * @param partId a peça a obter
     */
    public synchronized void partAvailable(int partId) {
        stockParts[partId]--;

        // update state and repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        ((Mechanic) Thread.currentThread()).setState(MechanicState.CHECKING_STOCK);
        repository.setMechanicState(mechanicId, MechanicState.CHECKING_STOCK);
        repository.setStockParts(stockParts);
    }

    /**
     * Operação resumeRepairProcedure (chamada pelo {@link Mechanic})<br>
     *
     * Recomeço da reparação, agora que o Mecânico tem a peça pretendida para
     * substituição.<br>
     */
    public synchronized void resumeRepairProcedure() {
        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setState(MechanicState.FIXING_THE_CAR);

        // update repository
        int mechanicId = ((Mechanic) Thread.currentThread()).getMechanicId();
        repository.setMechanicState(mechanicId, MechanicState.FIXING_THE_CAR);
    }

    /**
     * Operação storePart (chamada pelo {@link Manager})<br>
     *
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * {@link SupplierSite}, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o {@link Mechanic}.<br>
     *
     * @param newParts peças novas a serem incluídas no stock
     */
    public synchronized void storePart(int[] newParts) {
        // update Manager state
        ((Manager) Thread.currentThread()).setState(ManagerState.REPLENISH_STOCK);

        // update repository
        repository.setManagerState(ManagerState.REPLENISH_STOCK);

        for (int part = 0; part < stockParts.length; part++) {
            // replenishing stock of parts
            stockParts[part] += newParts[part];

            // update repository
            repository.setPartMissingAlert(part, false);
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

        // update repository
        repository.setStockParts(stockParts);

        notifyAll();
    }

    /**
     * Operação registerService (chamada pelo {@link Manager})<br>
     *
     * O Manager irá registar um pedido de reparação da viatura do {@link Customer},
     * e notificar os {@link Mechanic} de que existe um serviço disponível.<br>
     *
     * @param customerId id do cliente
     */
    public synchronized void registerService(int customerId) {
        // add Customer to the repair queue
        customerFirstRepairQueue.write(customerId);

        // update Manager state
        ((Manager) Thread.currentThread()).setState(ManagerState.POSTING_JOB);

        // update repository
        repository.setManagerState(ManagerState.POSTING_JOB);
        repository.managerRequestedService();

        // mechanics have one more service to do
        nRequestedServices++;

        // notify them
        notifyAll();
    }
}

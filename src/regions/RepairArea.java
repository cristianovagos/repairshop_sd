package regions;

import entities.*;
import utils.MemFIFO;

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

    public RepairArea(int nCustomers, int nParts, GeneralRepository repo) {
        this.repository = repo;
        this.customerFirstRepairQueue = new MemFIFO(nCustomers);
        this.customerMissingPartQueue = new MemFIFO[nParts];
        this.nRequestedServices = 0;
        this.stockParts = new int[nParts];
        this.endOfDay = false;

        for (int i = 0; i < nParts; i++) {
            this.customerMissingPartQueue[i] = new MemFIFO(nCustomers);
            this.stockParts[i] = (int) (10 * Math.random());
        }
    }

    /**
     * Operação readThePaper (chamada pelo {@link Mechanic})
     *
     * O Mecânico irá estar bloqueado até que lhe seja atribuído um serviço
     *
     * @return estado do dia de trabalho
     */
    public synchronized boolean readThePaper() {
        if(!endOfDay){
            while (nRequestedServices == 0) {
                try {
                    wait();
                } catch (InterruptedException e) { }
            }
            nRequestedServices--;
        }
        return !endOfDay;
    }

    /**
     * Operação markEndOfDay (chamada pelo {@link Manager})
     *
     * Marca o encerramento do dia para o {@link Mechanic}.
     */
    public synchronized void markEndOfDay() {
        endOfDay = true;
    }

    /**
     * Operação startRepairProcedure (chamada pelo {@link Mechanic})
     *
     * O Mecânico vai à lista de espera dos clientes para saber qual a viatura
     * que vai arranjar.
     */
    public synchronized void startRepairProcedure() {
        // get the first customer vehicle in the queue
        int carId = (int) customerFirstRepairQueue.read();

        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setCurrentCarFixingId(carId);
        ((Mechanic) Thread.currentThread()).setState(MechanicState.FIXING_THE_CAR);

        // todo update repository
    }

    /**
     * Operação getRequiredPart (chamada pelo {@link Mechanic})
     *
     * O Mecânico irá verificar se a peça pretendida para o arranjo da viatura
     * está disponível.
     */
    public synchronized boolean getRequiredPart(int partId) {
        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setState(MechanicState.CHECKING_STOCK);

        // todo update repository

        // check if part is in stock
        if (stockParts[partId] > 0)
            return true;

        // add Customer car to the queue of cars with missing parts
        int carId = ((Mechanic) Thread.currentThread()).getCurrentCarFixingId();
        customerMissingPartQueue[partId].write(carId);

        // Mechanic now is not fixing any car
        ((Mechanic) Thread.currentThread()).setCurrentCarFixingId(-1);

        return false;
    }

    /**
     * Operação partAvailable (chamada pelo {@link Mechanic})
     *
     * Obtém a peça para proceder ao arranjo.
     */
    public synchronized void partAvailable(int partId) {
        stockParts[partId]--;
        // todo ??
    }

    /**
     * Operação resumeRepairProcedure (chamada pelo {@link Mechanic})
     *
     */
    public synchronized void resumeRepairProcedure(int carId) {
        // update Mechanic state
        ((Mechanic) Thread.currentThread()).setState(MechanicState.FIXING_THE_CAR);
        ((Mechanic) Thread.currentThread()).setCurrentCarFixingId(carId);

        // todo update repository
    }

    /**
     * Operação storePart (chamada pelo {@link Manager})
     *
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * {@link SupplierSite}, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o {@link Mechanic}.
     */
    public synchronized void storePart(int partId) {
        // update Manager state
        ((Manager) Thread.currentThread()).setState(ManagerState.REPLENISH_STOCK);

        // replenishing stock of part
        stockParts[partId] = (int) (10 * Math.random());

        /* traverse the queue of Cars waiting for this part, add them
           into the work queue, and notify Mechanics there's work to do
         */
        while (!customerMissingPartQueue[partId].empty()) {
            int customerId = (int) customerMissingPartQueue[partId].read();
            customerFirstRepairQueue.write(customerId);
            nRequestedServices++;
        }

        // todo update repository
        
        notifyAll();
    }

    /**
     * Operação registerService (chamada pelo {@link Manager})
     *
     * O Manager irá registar um pedido de reparação da viatura do {@link Customer},
     * e notificar os {@link Mechanic} de que existe um serviço disponível.
     */
    public synchronized void registerService(int customerId) {
        // add Customer to the repair queue
        customerFirstRepairQueue.write(customerId);

        // update Manager state
        ((Manager) Thread.currentThread()).setState(ManagerState.POSTING_JOB);

        // todo update repository

        // mechanics have one more service to do
        nRequestedServices++;

        // notify them
        notifyAll();
    }
}

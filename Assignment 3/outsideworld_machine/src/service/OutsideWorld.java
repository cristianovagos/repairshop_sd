package service;

import interfaces.IGeneralRepository;
import model.CustomerState;
import model.ManagerState;

import java.rmi.RemoteException;

/**
 * Classe OutsideWorld (Mundo Exterior)<br>
 *
 * Esta classe é responsável pela criação do Mundo Exterior, uma das entidades
 * passivas do problema.<br>
 *
 * É aqui que os Clientes estão no início do problema, a fazer
 * a sua vida normal com a sua viatura. Após um dado tempo, a sua viatura avaria
 * e necessita de ser reparada. Aí, o Cliente dirige-se à Oficina.<br>
 * Quer queira uma viatura de substituição ou não, o Cliente volta a fazer a sua
 * vida normal assim que deixa a sua viatura na Oficina, e é notificado pelo
 * Gerente assim que a reparação esteja concluída.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class OutsideWorld {

    /**
     * Variável de condição (waitForCarRepair)<br>
     *
     * Assinala se o Customer está à espera de reparação da viatura<br>
     */
    private boolean[] waitForCarRepair;

    /**
     * Repositório Geral de dados
     */
    private IGeneralRepository repository;

    public OutsideWorld(int nCustomers, IGeneralRepository repo) {
        if(repo != null) this.repository = repo;
        if(nCustomers > 0) this.waitForCarRepair = new boolean[nCustomers];

        for (int i = 0; i < nCustomers; i++)
            waitForCarRepair[i] = true;
    }

    /**
     * Operação backToWorkByBus (chamada pelo Customer)<br>
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     *
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void backToWorkByBus(int customerId) throws RemoteException {
//        int customerId = ((ClientProxy) Thread.currentThread()).getCustomerId();

        // change customer state
//        ((ClientProxy) Thread.currentThread()).setCustomerState(CustomerState.NORMAL_LIFE_WITHOUT_CAR);

        // update interfaces
        repository.setCustomerState(customerId, CustomerState.NORMAL_LIFE_WITHOUT_CAR, true);

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Operação backToWorkByCar (chamada pelo Customer)<br>
     *
     * Aqui, o cliente vai fazer a sua vida normal.
     * No caso de ter colocado a sua viatura para reparação na Oficina, fica à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     *
     * @param carRepaired indicação se a sua viatura já foi reparada
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void backToWorkByCar(boolean carRepaired, int customerId) throws RemoteException {
//        int customerId = ((ClientProxy) Thread.currentThread()).getCustomerId();

        // change customer state, and interfaces
//        ((ClientProxy) Thread.currentThread()).setCustomerState(CustomerState.NORMAL_LIFE_WITH_CAR);
        repository.setCustomerState(customerId, CustomerState.NORMAL_LIFE_WITH_CAR, true);

        if(carRepaired)
            return;

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }

    }

    /**
     * Operação phoneCustomer (chamada pelo Manager)<br>
     *
     * Aqui, o Manager irá notificar o Customer de que
     * o seu carro está pronto, ligando-lhe.<br>
     *
     * @param customerId o id do Customer
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void phoneCustomer(int customerId) throws RemoteException {
        // change manager state
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.ALERTING_CUSTOMER);

        // update interfaces
        repository.setManagerState(ManagerState.ALERTING_CUSTOMER, true);

        // signal condition variable
        waitForCarRepair[customerId] = false;
        notifyAll();
    }
}

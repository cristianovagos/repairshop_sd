package service;

import model.CustomerState;
import model.ManagerState;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Este tipo de dados define o interface ao OutsideWorld, uma das regiões partilhadas do problema Repair Shop Activities.
 * A comunicação é feita com Java RMI.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IOutsideWorld extends Remote {
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
    void backToWorkByBus(int customerId) throws RemoteException;

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
    void backToWorkByCar(boolean carRepaired, int customerId) throws RemoteException;

    /**
     * Operação phoneCustomer (chamada pelo Manager)<br>
     *
     * Aqui, o Manager irá notificar o Customer de que
     * o seu carro está pronto, ligando-lhe.<br>
     *
     * @param customerId o id do Customer
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void phoneCustomer(int customerId) throws RemoteException;
}

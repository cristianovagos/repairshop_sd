package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Este tipo de dados define o interface ao Park, uma das regiões partilhadas do problema Repair Shop Activities.
 * A comunicação é feita com Java RMI.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IPark extends Remote {
    /**
     * Operação goToRepairShop (chamada pelo Customer)<br>
     *
     * O Cliente chega à RepairShop, e estaciona a sua viatura ou
     * a viatura de substituição que lhe foi atribuída no Park.<br>
     *
     * @param customerId o id do cliente
     * @param carId o id do carro do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void goToRepairShop(int customerId, int carId) throws RemoteException;

    /**
     * Operação findCar (chamada pelo Customer)<br>
     *
     * O Cliente recolhe a viatura de substituição do Park.<br>
     *
     * @param replacementCar viatura de substituição
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void findCar(int replacementCar, int customerId) throws RemoteException;

    /**
     * Operação collectCar (chamada pelo Customer)<br>
     *
     * O Cliente recolhe a sua viatura própria do Park.<br>
     *
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void collectCar(int customerId) throws RemoteException;

    /**
     * Operação getVehicle (chamada pelo Mechanic)<br>
     *
     * O Mecânico dirige-se ao Park para obter a viatura do Customer
     * e avalia qual a peça que deve substituir para a reparar.<br>
     *
     * @param customerId o id do cliente
     * @return a peça que necessita de substituição
     * @exception RemoteException se a invocação do método remoto falhar
     */
    int getVehicle(int customerId) throws RemoteException;

    /**
     * Operação returnVehicle (chamada pelo Mechanic)<br>
     *
     * O Mecânico devolve a viatura já reparada ao Park.<br>
     *
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void returnVehicle(int customerId) throws RemoteException;
}

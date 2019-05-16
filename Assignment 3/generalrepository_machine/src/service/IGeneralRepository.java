package service;

import model.CustomerState;
import model.ManagerState;
import model.MechanicState;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Este tipo de dados define o interface ao GeneralRepository, uma das regiões partilhadas do problema Repair Shop Activities.
 * A comunicação é feita com Java RMI.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IGeneralRepository extends Remote {

    /**
     * Operação printInitialState<br>
     *
     * Irá imprimir a linha de estado, e será chamada pelo programa inicial quando este começa.<br>
     */
    void printInitialState() throws RemoteException;

    /**
     * Operação initializeCustomer<br>
     *
     * Inicializa o estado de cada um dos Customers<br>
     *
     * @param index índice do cliente
     * @param requiresReplacement indicação se requer viatura de substituição
     */
    void initializeCustomer(int index, boolean requiresReplacement) throws RemoteException;

    /**
     * Operação setCustomerState<br>
     *
     * Altera o estado de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param newState o estado novo do cliente
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setCustomerState(int index, CustomerState newState, boolean print) throws RemoteException;

    /**
     * Operação setCustomerCar<br>
     *
     * Altera a viatura atual de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param carId o identificador da viatura
     *              (-1 se nenhuma, id do customer se a própria, ou 100..102 para viatura de substituição)
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setCustomerCar(int index, int carId, boolean print) throws RemoteException;

    /**
     * Operação setManagerState<br>
     *
     * Altera o estado do Manager<br>
     *
     * @param state estado do Manager
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setManagerState(ManagerState state, boolean print) throws RemoteException;

    /**
     * Operação setMechanicState<br>
     *
     * Altera o estado de um mecânico em específico<br>
     *
     * @param index o índice do mecânico em questão
     * @param newState o estado novo do mecânico
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setMechanicState(int index, MechanicState newState, boolean print) throws RemoteException;

    /**
     * Operação customerCarEntersPark<br>
     *
     * Indica que uma viatura de um cliente entrou no park<br>
     */
    void customerCarEntersPark() throws RemoteException;

    /**
     * Operação customerCarLeavesPark<br>
     *
     * Indica que uma viatura de um cliente saiu do park<br>
     */
    void customerCarLeavesPark() throws RemoteException;

    /**
     * Operação replacementCarEntersPark<br>
     *
     * Indica que uma viatura de substituição entrou no park<br>
     */
    void replacementCarEntersPark() throws RemoteException;

    /**
     * Operação replacementCarLeavesPark<br>
     *
     * Indica que uma viatura de substituição saiu do park<br>
     */
    void replacementCarLeavesPark() throws RemoteException;

    /**
     * Operação setCustomersInQueue<br>
     *
     * Altera o valor dos clientes na fila no Lounge<br>
     *
     * @param customersInQueue número de clientes na fila no Lounge
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setCustomersInQueue(int customersInQueue, boolean print) throws RemoteException;

    /**
     * Operação setCustomersInQueueForKey<br>
     *
     * Altera o valor dos clientes na fila para obter uma chave para viatura de substituição no Lounge<br>
     *
     * @param customersInQueueForKey número de clientes na fila à espera de chave para viatura de substituição
     */
    void setCustomersInQueueForKey(int customersInQueueForKey) throws RemoteException;

    /**
     * Operação setTotalRepairedCars<br>
     *
     * Altera o valor do total de carros reparados<br>
     *
     * @param repairedCars total de carros reparados
     */
    void setTotalRepairedCars(int repairedCars) throws RemoteException;

    /**
     * Operação managerRequestedService<br>
     *
     * Incrementa o número de serviços pedidos pelo Manager na RepairArea<br>
     */
    void managerRequestedService() throws RemoteException;

    /**
     * Operação setStockParts<br>
     *
     * Altera o array referente ao número de peças em stock na RepairArea<br>
     *
     * @param stockParts lista com número de peças em stock
     */
    void setStockParts(int[] stockParts, boolean print) throws RemoteException;

    /**
     * Operação addMissingPart<br>
     *
     * Marca uma dada peça como inexistente no stock da RepairArea<br>
     *
     * @param partIndex o índice da peça
     */
    void addMissingPart(int partIndex) throws RemoteException;

    /**
     * Operação removeMissingPart<br>
     *
     * Marca uma dada peça como existente no stock da RepairArea<br>
     *
     * @param partIndex o índice da peça
     */
    void removeMissingPart(int partIndex) throws RemoteException;

    /**
     * Operação setPartMissingAlert<br>
     *
     * Altera o valor de um dado índice do array de alerta de peças em falta<br>
     *
     * @param partIndex o índice da peça
     * @param value o novo valor
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setPartMissingAlert(int partIndex, boolean value, boolean print) throws RemoteException;

    /**
     * Operação setSoldParts<br>
     *
     * Altera o valor do array referente ao número de peças vendidas pela SupplierSite<br>
     *
     * @param soldParts array com o total de peças vendidas
     */
    void setSoldParts(int[] soldParts) throws RemoteException;

    /**
     * Operação setCustomerCarRepaired<br>
     *
     * Marca um dado carro como reparado
     *
     * @param carIndex o índice do carro
     */
    void setCustomerCarRepaired(int carIndex) throws RemoteException;
}

package service;

import interfaces.IGeneralRepository;
import interfaces.IPark;
import model.CustomerState;

import java.rmi.RemoteException;
import java.util.Random;

/**
 * Classe Park (Parque de Estacionamento)<br>
 *
 * Esta classe é responsável pela criação do Parque de Estacionamento, uma
 * das entidades passivas do problema.<br>
 *
 * É aqui que estão inicialmente localizadas as viaturas de substituição, a
 * serem disponibilizadas aos Clientes que queiram. Também
 * é o local onde os Clientes vão estacionar as suas próprias viaturas quando
 * se dirigem à Oficina para posterior reparação, onde após estar concluída
 * será novamente estacionada pelo Mecânico.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Park implements IPark {

    /**
     * Número de tipos de peças disponíveis
     */
    private final int N_PARTS;

    /**
     * Indicação de existência dos carros dos Clientes
     * no Parque de Estacionamento da Oficina.
     */
    private boolean[] customerCars;

    /**
     * Indicação de existência de carros de substituição
     * no Parque de Estacionamento da Oficina.
     */
    private boolean[] replacementCars;

    /**
     * Referência para o Repositório
     * @see IGeneralRepository
     */
    private IGeneralRepository repository;

    /**
     * Construtor de um Parque de Estacionamento<br>
     *
     * Aqui será construído o objeto referente a um Parque de Estacionamento.<br>
     *
     * @param nCustomers número de clientes
     * @param nReplacementCars número de viaturas de substituição
     * @param nParts número de tipos de peças
     * @param repo referência para o Repositório {@link IGeneralRepository}
     */
    public Park(int nCustomers, int nReplacementCars, int nParts, IGeneralRepository repo) {
        this.N_PARTS = nParts;
        this.customerCars = new boolean[nCustomers];
        this.replacementCars = new boolean[nReplacementCars];
        this.repository = repo;

        // initially all replacementCars are at the shop
        for (int i = 0; i < nReplacementCars; i++)
            replacementCars[i] = true;

        // initially there are no customerCars at the shop
        for (int i = 0; i < nCustomers; i++)
            customerCars[i] = false;
    }

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
    public synchronized void goToRepairShop(int customerId, int carId) throws RemoteException {
        // update Customer state
        repository.setCustomerState(customerId, CustomerState.PARK, false);

        if(customerId != carId) {
            // replacement car is on park
            replacementCars[carId - 100] = true;
            repository.replacementCarEntersPark();
        }
        else {
            // customer car is on park
            customerCars[customerId] = true;
            repository.customerCarEntersPark();
        }
    }

    /**
     * Operação findCar (chamada pelo Customer)<br>
     *
     * O Cliente recolhe a viatura de substituição do Park.<br>
     *
     * @param replacementCar viatura de substituição
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void findCar(int replacementCar, int customerId) throws RemoteException {
        // Replacement car is not on Park
        replacementCars[replacementCar - 100] = false;

        // update Customer state
        repository.setCustomerState(customerId, CustomerState.PARK, false);
        repository.setCustomerCar(customerId, replacementCar, false);
        repository.replacementCarLeavesPark();
    }

    /**
     * Operação collectCar (chamada pelo Customer)<br>
     *
     * O Cliente recolhe a sua viatura própria do Park.<br>
     *
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void collectCar(int customerId) throws RemoteException {
        // Customer car is not on Park
        customerCars[customerId] = false;

        // update Customer state
        repository.setCustomerState(customerId, CustomerState.PARK, false);
        repository.setCustomerCar(customerId, customerId, false);
        repository.customerCarLeavesPark();
    }

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
    public synchronized int getVehicle(int customerId) throws RemoteException {
        // Customer car is not on Park
        customerCars[customerId] = false;

        // update interfaces
        repository.customerCarLeavesPark();

        // select randomly the part that needs to be replaced
        return new Random().nextInt(N_PARTS);
    }

    /**
     * Operação returnVehicle (chamada pelo Mechanic)<br>
     *
     * O Mecânico devolve a viatura já reparada ao Park.<br>
     *
     * @param customerId o id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    public synchronized void returnVehicle(int customerId) throws RemoteException {
        // Customer car is now on Park
        customerCars[customerId] = true;

        // update interfaces
        repository.customerCarEntersPark();
    }
}

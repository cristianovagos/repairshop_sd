package regions;

import entities.CustomerState;
import entities.ManagerState;

public class OutsideWorld {

    /**
     * Variável de condição (waitForCarRepair)
     *
     * Assinala se o {@link entities.Customer} está à espera de reparação da viatura
     */
    private boolean[] waitForCarRepair;

    /**
     * Repositório Geral de dados
     */
    private GeneralRepository repository;

    public OutsideWorld(int nCustomers, GeneralRepository repo) {
        if(repo != null) this.repository = repo;
        if(nCustomers > 0) this.waitForCarRepair = new boolean[nCustomers];

        for (int i = 0; i < nCustomers; i++) {
            waitForCarRepair[i] = false;
        }
    }

    /**
     * Operação backToWorkByBus (chamada pelo {@link entities.Customer})
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do {@link entities.Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.
     *
     * @param customerId o id do {@link entities.Customer}
     */
    public synchronized void backToWorkByBus(int customerId) {
        waitForCarRepair[customerId] = true;

        // change customer state
        repository.setCustomerState(customerId, CustomerState.NORMAL_LIFE_WITHOUT_CAR);

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Operação backToWorkByCar (chamada pelo {@link entities.Customer})
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do {@link entities.Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.
     *
     * @param customerId o id do {@link entities.Customer}
     */
    public synchronized void backToWorkByCar(int customerId, int replaceCarId) {
        waitForCarRepair[customerId] = true;

        // change customer state
        repository.setCustomerState(customerId, CustomerState.NORMAL_LIFE_WITH_CAR);
        repository.setReplacementCar(replaceCarId, customerId);

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Operação phoneCustomer (chamada pelo {@link entities.Manager})
     *
     * Aqui, o {@link entities.Manager} irá notificar o {@link entities.Customer} de que
     * o seu carro está pronto, ligando-lhe.
     *
     * @param customerId o id do {@link entities.Customer}
     */
    public synchronized void phoneCustomer(int customerId) {
        // change manager state
        repository.setManagerState(ManagerState.ALERTING_CUSTOMER);

        // signal condition variable
        waitForCarRepair[customerId] = false;
        notifyAll();
    }
}

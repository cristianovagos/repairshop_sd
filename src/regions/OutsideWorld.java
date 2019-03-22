package regions;

import entities.Customer;
import entities.CustomerState;
import entities.Manager;
import entities.ManagerState;

/**
 * Classe OutsideWorld (Mundo Exterior)
 *
 * Esta classe é responsável pela criação do Mundo Exterior, uma das entidades
 * passivas do problema.
 *
 * É aqui que os Clientes {@link Customer} estão no início do problema, a fazer
 * a sua vida normal com a sua viatura. Após um dado tempo, a sua viatura avaria
 * e necessita de ser reparada. Aí, o Cliente dirige-se à Oficina.
 * Quer queira uma viatura de substituição ou não, o Cliente volta a fazer a sua
 * vida normal assim que deixa a sua viatura na Oficina, e é notificado pelo
 * Gerente {@link Manager} assim que a reparação esteja concluída.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class OutsideWorld {

    /**
     * Variável de condição (waitForCarRepair)
     *
     * Assinala se o {@link Customer} está à espera de reparação da viatura
     */
    private boolean[] waitForCarRepair;

    /**
     * Repositório Geral de dados
     */
    private GeneralRepository repository;

    public OutsideWorld(int nCustomers, GeneralRepository repo) {
        if(repo != null) this.repository = repo;
        if(nCustomers > 0) this.waitForCarRepair = new boolean[nCustomers];

        for (int i = 0; i < nCustomers; i++)
            waitForCarRepair[i] = false;
    }

    /**
     * Operação backToWorkByBus (chamada pelo {@link Customer})
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do {@link Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.
     */
    public synchronized void backToWorkByBus() {
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();

        waitForCarRepair[customerId] = true;

        // change customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.NORMAL_LIFE_WITHOUT_CAR);

        // update repository
        repository.setCustomerState(customerId, CustomerState.NORMAL_LIFE_WITHOUT_CAR);

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Operação backToWorkByCar (chamada pelo {@link Customer})
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do {@link Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.
     */
    public synchronized void backToWorkByCar() {
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();

        waitForCarRepair[customerId] = true;

        // change customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.NORMAL_LIFE_WITH_CAR);

        // update repository
        repository.setCustomerState(customerId, CustomerState.NORMAL_LIFE_WITH_CAR);

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Operação phoneCustomer (chamada pelo {@link Manager})
     *
     * Aqui, o {@link Manager} irá notificar o {@link Customer} de que
     * o seu carro está pronto, ligando-lhe.
     *
     * @param customerId o id do {@link Customer}
     */
    public synchronized void phoneCustomer(int customerId) {
        // change manager state
        ((Manager) Thread.currentThread()).setState(ManagerState.ALERTING_CUSTOMER);

        // update repository
        repository.setManagerState(ManagerState.ALERTING_CUSTOMER);

        // signal condition variable
        waitForCarRepair[customerId] = false;
        notifyAll();
    }
}

package regions;

public class OutsideWorld {

    /**
     *
     */
    private boolean[] waitForCarRepair;


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


        // update repository

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


        // update repository

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
        // update repository

        // block on condition variable
        waitForCarRepair[customerId] = false;
        notifyAll();
    }
}

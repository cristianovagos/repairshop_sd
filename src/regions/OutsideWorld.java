package regions;

public class OutsideWorld {

    /**
     * Operação backToWorkByBus (chamada pelo Customer)
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.
     *
     * @param customerId
     */
    public synchronized void backToWorkByBus(int customerId) {
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
     * Operação backToWorkByCar (chamada pelo Customer)
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.
     *
     * @param customerId
     */
    public synchronized void backToWorkByCar(int customerId, int replaceCarId) {
        // change customer state
        // update repository

        // block on condition variable
        while (waitForCarRepair[customerId]) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }

}

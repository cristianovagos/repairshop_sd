package service.proxy;

import model.ManagerState;

public interface IManagerAtt {
    /**
     * Altera o estado interno do Manager
     * @param managerState estado novo do Manager
     */
    void setManagerState(ManagerState managerState);

    /**
     * Obtém o estado interno do Manager
     * @return estado interno do Manager
     */
    ManagerState getManagerState();

    /**
     * Altera a indicação de primeira execução
     * @param firstRun indicação de primeira execução
     */
    void setFirstRun(boolean firstRun);

    /**
     * Obtém a indicação de primeira execução
     * @return indicação de primeira execução
     */
    boolean getFirstRun();

    /**
     * Altera o cesto de compras do Manager
     * @param partsBasket novo cesto de compras do Manager
     */
    void setPartsBasket(int[] partsBasket);

    /**
     * Obtém o cesto de compras do Manager
     * @return cesto de compras do Manager
     */
    int[] getPartsBasket();

    /**
     * Altera o Cliente a ser atendido de momento pelo Manager
     * @param customer id do cliente
     */
    void setCurrentlyAttendingCustomer(int customer);

    /**
     * Obtém o id do cliente que está a ser atendido de momento pelo Manager
     * @return id do cliente
     */
    int getCurrentlyAttendingCustomer();
}

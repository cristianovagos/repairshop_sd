package regions;

import model.ManagerState;

public interface IGeneralRepository {

    /**
     * Operação setManagerState<br>
     *
     * Altera o estado do Manager<br>
     *
     * @param state estado do Manager
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setManagerState(ManagerState state, boolean print);
}

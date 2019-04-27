package service.proxy;

import model.ManagerState;

/**
 * Interface IManagerAtt (atributos do Manager)<br>
 *
 * Esta interface contém todos os métodos do Manager, que
 * serão invocados assim que necessário, durante a troca de mensagens,
 * para manter os estados da entidade antes e após a troca de mensagens.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
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
}

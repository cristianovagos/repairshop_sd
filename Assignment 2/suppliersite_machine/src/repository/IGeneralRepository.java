package repository;

import model.ManagerState;

/**
 * Interface IGeneralRepository (ligação ao Repositório Geral)<br>
 *
 * Esta interface contém todos os métodos do serviço GeneralRepository, que
 * serão invocados assim que necessário.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
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

    /**
     * Operação setSoldParts<br>
     *
     * Altera o valor do array referente ao número de peças vendidas pela SupplierSite<br>
     *
     * @param soldParts array com o total de peças vendidas
     */
    void setSoldParts(int[] soldParts);
}

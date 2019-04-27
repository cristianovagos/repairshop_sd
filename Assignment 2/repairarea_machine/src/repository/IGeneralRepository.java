package repository;

import model.ManagerState;
import model.MechanicState;

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
     * Operação setMechanicState<br>
     *
     * Altera o estado de um mecânico em específico<br>
     *
     * @param index o índice do mecânico em questão
     * @param newState o estado novo do mecânico
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setMechanicState(int index, MechanicState newState, boolean print);

    /**
     * Operação managerRequestedService<br>
     *
     * Incrementa o número de serviços pedidos pelo Manager na RepairArea<br>
     */
    void managerRequestedService();

    /**
     * Operação setStockParts<br>
     *
     * Altera o array referente ao número de peças em stock na RepairArea<br>
     *
     * @param stockParts lista com número de peças em stock
     */
    void setStockParts(int[] stockParts, boolean print);

    /**
     * Operação addMissingPart<br>
     *
     * Marca uma dada peça como inexistente no stock da RepairArea<br>
     *
     * @param partIndex o índice da peça
     */
    void addMissingPart(int partIndex);

    /**
     * Operação removeMissingPart<br>
     *
     * Marca uma dada peça como existente no stock da RepairArea<br>
     *
     * @param partIndex o índice da peça
     */
    void removeMissingPart(int partIndex);

    /**
     * Operação setPartMissingAlert<br>
     *
     * Altera o valor de um dado índice do array de alerta de peças em falta<br>
     *
     * @param partIndex o índice da peça
     * @param value o novo valor
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setPartMissingAlert(int partIndex, boolean value, boolean print);
}

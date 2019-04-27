package repository;

import model.CustomerState;
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
     * Operação setCustomerState<br>
     *
     * Altera o estado de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param newState o estado novo do cliente
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setCustomerState(int index, CustomerState newState, boolean print);

    /**
     * Operação setCustomerCar<br>
     *
     * Altera a viatura atual de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param carId o identificador da viatura
     *              (-1 se nenhuma, id do customer se a própria, ou 100..102 para viatura de substituição)
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    void setCustomerCar(int index, int carId, boolean print);

    /**
     * Operação customerCarEntersPark<br>
     *
     * Indica que uma viatura de um cliente entrou no park<br>
     */
    void customerCarEntersPark();

    /**
     * Operação customerCarLeavesPark<br>
     *
     * Indica que uma viatura de um cliente saiu do park<br>
     */
    void customerCarLeavesPark();

    /**
     * Operação replacementCarEntersPark<br>
     *
     * Indica que uma viatura de substituição entrou no park<br>
     */
    void replacementCarEntersPark();

    /**
     * Operação replacementCarLeavesPark<br>
     *
     * Indica que uma viatura de substituição saiu do park<br>
     */
    void replacementCarLeavesPark();
}

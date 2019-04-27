package regions;

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
     * Operação initializeCustomer<br>
     *
     * Inicializa o estado de cada um dos Customers<br>
     *
     * @param index índice do cliente
     * @param requiresReplacement indicação se requer viatura de substituição
     */
    void initializeCustomer(int index, boolean requiresReplacement);
}

package service.proxy;

import model.CustomerState;

/**
 * Interface ICustomerAtt (atributos do Customer)<br>
 *
 * Esta interface contém todos os métodos do Cliente, que
 * serão invocados assim que necessário, durante a troca de mensagens,
 * para manter os estados da entidade antes e após a troca de mensagens.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface ICustomerAtt {
    /**
     * Altera o estado interno do Cliente
     * @param customerState o novo estado do Cliente
     */
    void setCustomerState(CustomerState customerState);

    /**
     * Obtém o estado interno do Cliente
     * @return o estado interno do Cliente
     */
    CustomerState getCustomerState();

    /**
     * Altera o id do Cliente
     * @param customerId novo id do Cliente
     */
    void setCustomerId(int customerId);

    /**
     * Obtém o id do Cliente
     * @return o id do Cliente
     */
    int getCustomerId();

    /**
     * Altera o id do carro do Cliente
     * @param carId novo id do carro do Cliente
     */
    void setCustomerCarId(int carId);

    /**
     * Obtém o id do carro do Cliente
     * @return id do carro do Cliente
     */
    int getCustomerCarId();
}

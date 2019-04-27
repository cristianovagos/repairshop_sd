package regions;
import entities.*;

/**
 * Interface IOutsideWorld (ligação ao Mundo Exterior)<br>
 *
 * Esta interface contém todos os métodos do serviço OutsideWorld, que
 * serão invocados assim que necessário.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IOutsideWorld {

    /**
     * Operação phoneCustomer (chamada pelo {@link Manager})<br>
     *
     * Aqui, o {@link Manager} irá notificar o Customer de que
     * o seu carro está pronto, ligando-lhe.<br>
     *
     * @param customerId o id do Customer
     */
    void phoneCustomer(int customerId);
}

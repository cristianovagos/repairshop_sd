package regions;
import entities.*;

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

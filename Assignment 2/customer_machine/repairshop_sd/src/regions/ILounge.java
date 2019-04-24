package regions;

import entities.Customer;
import entities.CustomerState;

public interface ILounge {

    /* CUSTOMER */

    /**
     * Operação queueIn (chamada pelo {@link Customer})<br>
     *
     * O Cliente chega à Recepção e aguarda na fila, para ser
     * atendido pelo Gerente ({@link Manager}). Tanto poderá colocar a sua
     * viatura para reparação ou para proceder ao pagamento do mesmo.<br>
     *
     * @param repairCompleted indicação se a reparação já foi feita
     */
    public void queueIn(boolean repairCompleted);

    /**
     * Operação talkWithManager (chamada pelo {@link Customer})<br>
     *
     * O Cliente fala com o Gerente {@link Manager} para reparar
     * a sua viatura e entrega-lhe a chave da mesma.<br>
     */
    public void talkWithManager();

    /**
     * Operação collectKey (chamada pelo {@link Customer})<br>
     *
     * O Cliente pretende uma viatura de substituição enquanto a sua
     * viatura própria é reparada, e aguarda a entrega de uma chave de
     * uma das viaturas de substituição disponíveis por parte do Gerente
     * ({@link Manager}).<br>
     *
     * @return chave da viatura de substituição
     */
    public int collectKey();

    /**
     * Operação payForTheService (chamada pelo {@link Customer})<br>
     *
     * O Cliente procede ao pagamento do serviço prestado pela Oficina.
     * Caso este tenha utilizado uma viatura de substituição devolve a
     * chave da mesma ao Gerente ({@link Manager}).<br>
     */
    public void payForTheService();


}

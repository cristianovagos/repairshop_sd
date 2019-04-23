package regions;

/**
 * Enumerado ManagerTask<br>
 *
 * Tarefas do Manager ({@link entities.Manager}).<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum ManagerTask {
    /**
     * Telefonar (chamar) a um Cliente, indicando que a sua viatura está pronta.
     */
    PHONE_CUSTOMER,

    /**
     * Obter novas peças para reabastecer a {@link RepairArea}.
     */
    GET_PARTS,

    /**
     * Falar com o Cliente.
     */
    TALK_CUSTOMER,

    /**
     * Nenhuma.
     */
    NONE
}

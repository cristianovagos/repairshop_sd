package entities;

/**
 * Enumerado CustomerState
 *
 * Estado interno do Cliente {@link Customer}.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum CustomerState {
    NORMAL_LIFE_WITH_CAR ("NLC"),
    NORMAL_LIFE_WITHOUT_CAR ("NLW"),
    PARK ("PRK"),
    RECEPTION ("RCP"),
    RECEPTION_REPAIR ("RPR"),
    RECEPTION_PAYING ("PAY"),
    RECEPTION_TALK_WITH_MANAGER ("TLK"),
    WAITING_FOR_REPLACE_CAR ("WAI"),
    NONE ("---");

    /**
     * Descrição do estado interno do cliente
     */
    private final String state;

    /**
     * Construtor privado do estado interno do cliente
     *
     * Será criado um objeto referente ao estado interno do cliente.
     *
     * @param state a descrição do estado interno do cliente
     */
    CustomerState(String state) {
        this.state = state;
    }

    /**
     * Operação state
     *
     * Obtém a descrição do estado interno do cliente
     *
     * @return a descrição do estado interno do cliente
     */
    public String state() { return state; }
}

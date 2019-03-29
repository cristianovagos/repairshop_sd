package entities;

/**
 * Enumerado CustomerState<br>
 *
 * Estado interno do Cliente ({@link Customer}).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum CustomerState {
    /**
     * O Cliente está a fazer a sua vida normal e
     * tem na sua posse uma viatura, seja a sua, seja uma viatura
     * de substituição que lhe foi atribuída quando colocou a sua
     * viatura na Oficina para reparação.
     */
    NORMAL_LIFE_WITH_CAR ("NLC"),

    /**
     * O Cliente está a fazer a sua vida normal,
     * no entanto não possui nenhuma viatura.
     */
    NORMAL_LIFE_WITHOUT_CAR ("NLW"),

    /**
     * O Cliente está no parque, ou a estacionar a viatura que possui,
     * ou a levantar a sua viatura já reparada.
     */
    PARK ("PRK"),

    /**
     * O Cliente encontra-se na recepção da Oficina, e está na fila para
     * ser atendido.
     */
    RECEPTION ("RCP"),

    /**
     * O Cliente está na recepção da Oficina e pretende reparar a sua viatura.
     */
    RECEPTION_REPAIR ("RPR"),

    /**
     * O Cliente está na recepção da Oficina e pretende pagar pelo serviço de
     * reparação.
     */
    RECEPTION_PAYING ("PAY"),

    /**
     * O Cliente está na recepção da Oficina e está a falar com o Gerente.
     */
    RECEPTION_TALK_WITH_MANAGER ("TLK"),

    /**
     * O Cliente está à espera de uma viatura de substituição
     */
    WAITING_FOR_REPLACE_CAR ("WAI"),

    /**
     * Estado vazio
     */
    NONE ("---");

    /**
     * Descrição do estado interno do cliente
     */
    private final String state;

    /**
     * Construtor privado do estado interno do cliente<br>
     *
     * Será criado um objeto referente ao estado interno do cliente.<br>
     *
     * @param state a descrição do estado interno do cliente
     */
    CustomerState(String state) {
        this.state = state;
    }

    /**
     * Operação state<br>
     *
     * Obtém a descrição do estado interno do cliente<br>
     *
     * @return a descrição do estado interno do cliente
     */
    public String state() { return state; }
}

package entities;

/**
 * Enumerado MechanicState<br>
 *
 * Estado interno do Mecânico ({@link Mechanic}).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum MechanicState {
    WAITING_FOR_WORK ("WAI"),
    FIXING_THE_CAR ("FIX"),
    ALERTING_MANAGER_FOR_PARTS ("ALP"),
    ALERTING_MANAGER_REPAIR_CONCLUDED("ARP"),
    CHECKING_STOCK ("CHK");

    /**
     * Descrição do estado interno do mecânico
     */
    private final String state;

    /**
     * Construtor privado do estado interno do mecânico<br>
     *
     * Será criado um objeto referente ao estado interno do mecânico.<br>
     *
     * @param state a descrição do estado interno do mecânico
     */
    MechanicState(String state) {
        this.state = state;
    }

    /**
     * Operação state<br>
     *
     * Obtém a descrição do estado interno do mecânico<br>
     *
     * @return a descrição do estado interno do mecânico
     */
    public String state() { return state; }
}

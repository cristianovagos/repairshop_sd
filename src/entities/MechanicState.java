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
    /**
     * O Mecânico está à espera de novas viaturas para reparar, trazidas pelo Cliente.
     */
    WAITING_FOR_WORK ("WAI"),

    /**
     * O Mecânico encontra-se a reparar a viatura.
     */
    FIXING_THE_CAR ("FIX"),

    /**
     * O Mecânico está a alertar o Gerente de que é preciso peças na {@link regions.RepairArea}.
     */
    ALERTING_MANAGER_FOR_PARTS ("ALP"),

    /**
     * O Mecânico está a alertar o Gerente de que acabou de reparar uma viatura.
     */
    ALERTING_MANAGER_REPAIR_CONCLUDED("ARP"),

    /**
     * O Mecânico está a verificar o stock de peças na {@link regions.RepairArea}.
     */
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

package entities;

/**
 * Enumerado ManagerState
 *
 * Estado interno do Gerente {@link Manager}.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum ManagerState {
    CHECKING_WHAT_TO_DO ("TODO"),
    ATTENDING_CUSTOMER ("ATTN"),
    POSTING_JOB ("POST"),
    ALERTING_CUSTOMER ("ALRT"),
    GETTING_NEW_PARTS ("GETT"),
    REPLENISH_STOCK ("RPLN");

    /**
     * Descrição do estado interno do Gerente
     */
    private final String state;

    /**
     * Construtor privado do estado interno do gerente
     *
     * Será criado um objeto referente ao estado interno do gerente.
     *
     * @param state a descrição do estado interno do gerente
     */
    ManagerState(String state) {
        this.state = state;
    }

    /**
     * Operação state
     *
     * Obtém a descrição do estado interno do gerente
     *
     * @return a descrição do estado interno do gerente
     */
    public String state() { return state; }
}

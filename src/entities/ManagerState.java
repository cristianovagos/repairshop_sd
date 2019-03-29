package entities;

/**
 * Enumerado ManagerState<br>
 *
 * Estado interno do Gerente {@link Manager}.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum ManagerState {
    /**
     * O Gerente está a averiguar se existem tarefas para fazer na Oficina.
     */
    CHECKING_WHAT_TO_DO ("TODO"),

    /**
     * O Gerente está a atender um Cliente.
     */
    ATTENDING_CUSTOMER ("ATTN"),

    /**
     * O Gerente está a registar um novo trabalho de reparação na Oficina.
     */
    POSTING_JOB ("POST"),

    /**
     * O Gerente está a alertar o Cliente de que a sua viatura está pronta.
     */
    ALERTING_CUSTOMER ("ALRT"),

    /**
     * O Gerente está a abastecer-se de novas peças na {@link regions.SupplierSite}.
     */
    GETTING_NEW_PARTS ("GETT"),

    /**
     * O Gerente está a abastecer a {@link regions.RepairArea} com as peças
     * novas adquiridas.
     */
    REPLENISH_STOCK ("RPLN");

    /**
     * Descrição do estado interno do Gerente
     */
    private final String state;

    /**
     * Construtor privado do estado interno do gerente<br>
     *
     * Será criado um objeto referente ao estado interno do gerente.<br>
     *
     * @param state a descrição do estado interno do gerente
     */
    ManagerState(String state) {
        this.state = state;
    }

    /**
     * Operação state<br>
     *
     * Obtém a descrição do estado interno do gerente<br>
     *
     * @return a descrição do estado interno do gerente
     */
    public String state() { return state; }
}

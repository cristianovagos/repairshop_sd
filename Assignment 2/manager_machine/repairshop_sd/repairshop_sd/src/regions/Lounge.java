package regions;
import entities.*;

public interface Lounge {
    /* MANAGER */

    /**
     * Operação getNextTask (chamada pelo {@link Manager})<br>
     *
     * Caso hajam pedidos ao Gerente, este procede à realização
     * das tarefas pretendidas. Assim que não haja mais pedidos,
     * a Oficina é fechada, e os Mecânicos ({@link Mechanic}) poderão
     * ir embora.<br>
     *
     * @return indicação se ainda há trabalho para fazer na Oficina
     */
    public boolean getNextTask();

    /**
     * Operação appraiseSit (chamada pelo {@link Manager})<br>
     *
     * O Gerente avalia a próxima tarefa a desempenhar, de acordo
     * com a seguinte prioridade:<br>
     * <ul>
     *     <li>Reabastecimento de peças para os {@link Mechanic}
     *     poderem trabalhar na {@link RepairArea} através da {@link SupplierSite}</li>
     *     <li>Caso haja clientes em fila de espera para obter uma chave para
     *     viatura de substituição.</li>
     *     <li>Caso haja viaturas reparadas, o Gerente vai chamar os clientes
     *     para as virem buscar.</li>
     *     <li>Atender os clientes em fila de espera na Recepção</li>
     * </ul>
     *
     * @return a próxima tarefa a ser executada pelo Gerente
     */
    public ManagerTask appraiseSit();

    /**
     * Operação talkToCustomer (chamada pelo {@link Manager})<br>
     *
     * O Gerente irá atender o cliente {@link Customer} para saber o que
     * este pretende, de uma das seguintes opções:<br>
     * <ul>
     *     <li>Reparar a sua viatura</li>
     *     <li>Obter uma viatura de substituição</li>
     *     <li>Efetuar pagamento do serviço</li>
     * </ul>
     *
     * @return estado do cliente atual
     */
    public CustomerState talkToCustomer();

    /**
     * Operação handCarKey (chamada pelo {@link Manager})<br>
     *
     * O Gerente irá dar uma das chaves das viaturas de substituição
     * disponíveis ao {@link Customer}.<br>
     */
    public void handCarKey();

    /**
     * Operação receivePayment (chamada pelo {@link Manager})<br>
     *
     * O Gerente recebe o pagamento do serviço por parte do {@link Customer}<br>
     *
     * @param customerToAttend o id do cliente a fazer pagamento
     */
    public void receivePayment(int customerToAttend);
}

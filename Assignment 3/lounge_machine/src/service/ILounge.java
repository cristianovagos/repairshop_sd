package service;

import utils.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Este tipo de dados define o interface ao Lounge, uma das regiões partilhadas do problema Repair Shop Activities.
 * A comunicação é feita com Java RMI.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface ILounge extends Remote {

    /**
     * Operação queueIn (chamada pelo Customer)<br>
     *
     * O Cliente chega à Recepção e aguarda na fila, para ser
     * atendido pelo Gerente (Manager). Tanto poderá colocar a sua
     * viatura para reparação ou para proceder ao pagamento do mesmo.<br>
     *
     * @param repairCompleted indicação se a reparação já foi feita
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void queueIn(boolean repairCompleted, int customerId) throws RemoteException;

    /**
     * Operação talkWithManager (chamada pelo Customer)<br>
     *
     * O Cliente fala com o Gerente para reparar
     * a sua viatura e entrega-lhe a chave da mesma.<br>
     *
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void talkWithManager(int customerId) throws RemoteException;

    /**
     * Operação collectKey (chamada pelo Customer)<br>
     *
     * O Cliente pretende uma viatura de substituição enquanto a sua
     * viatura própria é reparada, e aguarda a entrega de uma chave de
     * uma das viaturas de substituição disponíveis por parte do Gerente.<br>
     *
     * @param customerId id do cliente
     * @return chave da viatura de substituição
     * @exception RemoteException se a invocação do método remoto falhar
     */
    int collectKey(int customerId) throws RemoteException;

    /**
     * Operação payForTheService (chamada pelo Customer)<br>
     *
     * O Cliente procede ao pagamento do serviço prestado pela Oficina.
     * Caso este tenha utilizado uma viatura de substituição devolve a
     * chave da mesma ao Gerente.<br>
     *
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void payForTheService(int customerId) throws RemoteException;

    /* MANAGER */

    /**
     * Operação getNextTask (chamada pelo Manager)<br>
     *
     * Caso hajam pedidos ao Gerente, este procede à realização
     * das tarefas pretendidas. Assim que não haja mais pedidos,
     * a Oficina é fechada, e os Mecânicos poderão ir embora.<br>
     *
     * @param firstRun indicação se é a primeira execução
     * @return indicação se ainda há trabalho para fazer na Oficina
     * @exception RemoteException se a invocação do método remoto falhar
     */
    boolean getNextTask(boolean firstRun) throws RemoteException;

    /**
     * Operação appraiseSit (chamada pelo Manager)<br>
     *
     * O Gerente avalia a próxima tarefa a desempenhar, de acordo
     * com a seguinte prioridade:<br>
     * <ul>
     *     <li>Reabastecimento de peças para os Mechanic
     *     poderem trabalhar na RepairArea através da SupplierSite</li>
     *     <li>Caso haja clientes em fila de espera para obter uma chave para
     *     viatura de substituição.</li>
     *     <li>Caso haja viaturas reparadas, o Gerente vai chamar os clientes
     *     para as virem buscar.</li>
     *     <li>Atender os clientes em fila de espera na Recepção</li>
     * </ul>
     *
     * @return par de objetos com a próxima tarefa a ser executada pelo Gerente e/ou
     *         id do cliente a chamar para levantar a viatura
     * @exception RemoteException se a invocação do método remoto falhar
     */
    Pair appraiseSit() throws RemoteException;

    /**
     * Operação talkToCustomer (chamada pelo Manager)<br>
     *
     * O Gerente irá atender o cliente para saber o que
     * este pretende, de uma das seguintes opções:<br>
     * <ul>
     *     <li>Reparar a sua viatura</li>
     *     <li>Obter uma viatura de substituição</li>
     *     <li>Efetuar pagamento do serviço</li>
     * </ul>
     *
     * @return par de objetos com o estado do cliente atual e/ou o id do cliente a atender
     * @exception RemoteException se a invocação do método remoto falhar
     */
    Pair talkToCustomer() throws RemoteException;

    /**
     * Operação handCarKey (chamada pelo Manager)<br>
     *
     * O Gerente irá dar uma das chaves das viaturas de substituição
     * disponíveis ao Customer.<br>
     *
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void handCarKey() throws RemoteException;

    /**
     * Operação receivePayment (chamada pelo Manager)<br>
     *
     * O Gerente recebe o pagamento do serviço por parte do Customer<br>
     *
     * @param customerToAttend o id do cliente a fazer pagamento
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void receivePayment(int customerToAttend) throws RemoteException;

    /* MECHANIC */

    /**
     * Operação letManagerKnow (chamada pelo Mechanic)<br>
     *
     * O Mecânico informa o Gerente (Manager) de que não existem
     * peças para que este possa reparar uma viatura.<br>
     *
     * @param partRequired peça em falta
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void letManagerKnow(int partRequired, int mechanicId) throws RemoteException;

    /**
     * Operação repairConcluded (chamada pelo Mechanic)<br>
     *
     * O Mecânico terminou de reparar uma viatura, e irá informar o Gerente
     * que esta está pronta a ser levantada pelo seu proprietário,
     * o Cliente.<br>
     *
     * @param mechanicId id do mecânico
     * @param carFixed indicação se a viatura já está pronta
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void repairConcluded(int mechanicId, int carFixed) throws RemoteException;
}

package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Este tipo de dados define o interface ao RepairArea, uma das regiões partilhadas do problema Repair Shop Activities.
 * A comunicação é feita com Java RMI.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IRepairArea extends Remote {
    /**
     * Operação readThePaper (chamada pelo Mechanic)<br>
     *
     * O Mecânico irá estar bloqueado até que lhe seja atribuído um serviço<br>
     *
     * @param mechanicId id do mecânico
     * @param firstRun indicação se é a primeira execução
     * @return estado do dia de trabalho
     * @exception RemoteException se a invocação do método remoto falhar
     */
    boolean readThePaper(int mechanicId, boolean firstRun) throws RemoteException;

    /**
     * Operação markEndOfDay (chamada pelo Manager)<br>
     *
     * Marca o encerramento do dia para o Mechanic.<br>
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void markEndOfDay() throws RemoteException;

    /**
     * Operação startRepairProcedure (chamada pelo Mechanic)<br>
     *
     * O Mecânico vai à lista de espera dos clientes para saber qual a viatura
     * que vai arranjar.<br>
     *
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    int startRepairProcedure(int mechanicId) throws RemoteException;

    /**
     * Operação getRequiredPart (chamada pelo Mechanic)<br>
     *
     * O Mecânico irá verificar se a peça pretendida para o arranjo da viatura
     * está disponível.<br>
     *
     * @param partId peça pretendida
     * @param mechanicId id do mecânico
     * @param carId id do carro a ser arranjado
     * @return indicação se a peça pretendida existe em stock
     * @exception RemoteException se a invocação do método remoto falhar
     */
    boolean getRequiredPart(int partId, int mechanicId, int carId) throws RemoteException;

    /**
     * Operação partAvailable (chamada pelo Mechanic)<br>
     *
     * Obtém a peça para proceder ao arranjo.<br>
     *
     * @param partId a peça a obter
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void partAvailable(int partId, int mechanicId) throws RemoteException;

    /**
     * Operação resumeRepairProcedure (chamada pelo Mechanic)<br>
     *
     * Recomeço da reparação, agora que o Mecânico tem a peça pretendida para
     * substituição.<br>
     *
     * @param mechanicId id do mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void resumeRepairProcedure(int mechanicId) throws RemoteException;

    /**
     * Operação storePart (chamada pelo Manager)<br>
     *
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * SupplierSite, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o Mechanic.<br>
     *
     * @param newParts peças novas a serem incluídas no stock
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void storePart(int[] newParts) throws RemoteException;

    /**
     * Operação registerService (chamada pelo Manager)<br>
     *
     * O Manager irá registar um pedido de reparação da viatura do Customer,
     * e notificar os Mechanic de que existe um serviço disponível.<br>
     *
     * @param customerId id do cliente
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void registerService(int customerId) throws RemoteException;

    /**
     * Acordar intempestivamente um mecânico para sinalizar fim de operações.
     * Apenas o faz quando o Manager dá como encerrado o dia de trabalho.<br>
     *
     * @param mechanicId id do Mecânico
     * @exception RemoteException se a invocação do método remoto falhar
     */
    void endOperation(int mechanicId) throws RemoteException;
}

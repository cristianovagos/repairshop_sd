package entities;

import genclass.GenericIO;
import interfaces.IGeneralRepository;
import interfaces.ILounge;
import interfaces.IPark;
import interfaces.IRepairArea;
import model.MechanicState;

import java.rmi.RemoteException;

/**
 * Classe Mechanic (Mecânico)<br>
 *
 * Esta classe é responsável pela criação de um Mecânico, que é uma
 * das entidades ativas do problema.<br>
 *
 * O Mecânico, quando chega ao local de trabalho, fica a aguardar
 * que haja tarefas para fazer, nomeadamente viaturas para reparar.
 * Assim que uma tarefa de reparação seja registada pelo Gerente (Manager),
 * o Mecânico é notificado de que existe trabalho para fazer, e de seguida
 * dirige ao Parque de Estacionamento ({@link IPark}) para avaliar a viatura
 * e diagnosticar qual a peça danificada que necessita de substituição.
 * De seguida, verifica se essa peça existe em stock na Área de Reparação
 * ({@link IRepairArea}), e caso esta não exista, informa o Gerente para que este
 * possa reabastecer a Área de Reparação com mais peças, necessárias para a
 * reparação. Caso a peça esteja disponível, o Mecânico repara a viatura com a
 * peça, e assim que esta esteja reparada, coloca a viatura no Parque de
 * Estacionamento e informa o Gerente de que esta está pronta a ser levantada
 * pelo Cliente (Customer).<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Mechanic extends Thread {

    /** O id do Mecânico */
    private int mechanicId;

    /**
     * O id da viatura que o Mecânico está a reparar de momento.<br>
     * Poderá ter os seguintes valores:<br>
     * <ul>
     *  <li>o id do cliente</li>
     *  <li>-1, se nenhuma</li>
     * </ul>
     */
    private int currentCarFixingId;

    /**
     * Indicação se é a primeira execução
     */
    private boolean firstRun;

    /**
     * Referência para o Repositório Geral
     * @see IGeneralRepository
     */
    private IGeneralRepository repository;

    /**
     * O estado interno do Mecânico
     * @see MechanicState
     */
    private MechanicState state;

    /**
     * Referência para a Área de Reparação
     * @see IRepairArea
     */
    private IRepairArea repairArea;

    /**
     * Referência para o Parque de Estacionamento
     * @see IPark
     */
    private IPark park;

    /**
     * Referência para a Recepção (Lounge)
     * @see ILounge
     */
    private ILounge lounge;

    /**
     * Construtor de um Mecânico<br>
     *
     * Aqui será construído o objeto referente a um Mecânico.
     * De seguida, o repositório {@link IGeneralRepository} será informado de
     * que o Mecânico foi criado, mostrando o seu estado inicial.<br>
     *
     * @param id o id do Mecânico
     * @param repo referência para o {@link IGeneralRepository}
     * @param repairArea referência para a {@link IRepairArea}
     * @param park referência para o {@link IPark}
     * @param lounge referência para o {@link ILounge}
     */
    public Mechanic(int id, IGeneralRepository repo, IRepairArea repairArea, IPark park, ILounge lounge) throws RemoteException {
        this.mechanicId = id;
        this.state = MechanicState.WAITING_FOR_WORK;
        this.repository = repo;
        this.repairArea = repairArea;
        this.park = park;
        this.lounge = lounge;
        this.currentCarFixingId = -1;
        this.firstRun = true;

        repository.setMechanicState(id, MechanicState.WAITING_FOR_WORK, false);
    }

    /**
     * Ciclo de vida de um Mecânico, conforme descrito na descrição da classe.
     */
    @Override
    public void run() {
        try {
            while (repairArea.readThePaper(mechanicId, firstRun)){
                currentCarFixingId = repairArea.startRepairProcedure(mechanicId);
                setState(MechanicState.FIXING_THE_CAR);
                int partRequired = park.getVehicle(currentCarFixingId);

                boolean hasPart = repairArea.getRequiredPart(partRequired, mechanicId, currentCarFixingId);
                setState(MechanicState.CHECKING_STOCK);
                if(!hasPart) {
                    currentCarFixingId = -1;
                    lounge.letManagerKnow(partRequired, mechanicId);
                    setState(MechanicState.ALERTING_MANAGER_FOR_PARTS);
                    setState(MechanicState.WAITING_FOR_WORK);
                    firstRun = false;
                    continue;
                }

                repairArea.partAvailable(partRequired, mechanicId);
                setState(MechanicState.CHECKING_STOCK);
                repairArea.resumeRepairProcedure(mechanicId);
                setState(MechanicState.FIXING_THE_CAR);
                fixIt();
                park.returnVehicle(currentCarFixingId);
                lounge.repairConcluded(mechanicId, currentCarFixingId);
                setState(MechanicState.ALERTING_MANAGER_REPAIR_CONCLUDED);
                setState(MechanicState.WAITING_FOR_WORK);
                firstRun = false;
            }
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception thrown on Mechanic lifecycle: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Operação getMechanicId<br>
     *
     * Obtém o id do Mecânico<br>
     *
     * @return o id do mecânico
     */
    public int getMechanicId() {
        return this.mechanicId;
    }

    /**
     * Operação getFirstRun<br>
     *
     * Obtém a indicação se é a primeira execução<br>
     *
     * @return o valor da primeira execução
     */
    public boolean getFirstRun() {
        return this.firstRun;
    }

    /**
     * Operação setFirstRun<br>
     *
     * Altera a indicação se é a primeira execução<br>
     *
     * @param firstRun o novo valor
     */
    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    /**
     * Operação setState<br>
     *
     * Modifica o estado interno do Mecânico<br>
     *
     * @param state o estado interno do Mecânico
     */
    public void setState(MechanicState state) {
        this.state = state;
    }

    /**
     * Operação getCurrentCarFixingId<br>
     *
     * Marca a viatura que o Mecânico estiver a reparar de momento,
     * o id da viatura caso esteja a reparar uma viatura, -1 se nenhuma.<br>
     *
     * @param carId o id da viatura (o mesmo que o do Customer)
     */
    public void setCurrentCarFixingId(int carId) {
        this.currentCarFixingId = carId;
    }

    /**
     * Operação getCurrentCarFixingId<br>
     *
     * Obtém a viatura que o Mecânico estiver a reparar de momento.
     * Devolve o id da viatura caso esteja a reparar uma viatura, -1 se nenhuma.<br>
     *
     * @return o id da viatura (o mesmo que o do Customer)
     */
    public int getCurrentCarFixingId() {
        return this.currentCarFixingId;
    }

    /**
     * Operação interna fixIt<br>
     *
     * O Mecânico irá reparar a viatura durante um período aleatório de tempo.<br>
     */
    private void fixIt() {
        try {
            sleep ((long) (1 + 30 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

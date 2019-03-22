package entities;

import regions.GeneralRepository;
import regions.Lounge;
import regions.Park;
import regions.RepairArea;

/**
 * Classe Mechanic (Mecânico)
 *
 * Esta classe é responsável pela criação de um Mecânico, que é uma
 * das entidades ativas do problema.
 *
 * O Mecânico, quando chega ao local de trabalho, fica a aguardar
 * que haja tarefas para fazer, nomeadamente viaturas para reparar.
 * Assim que uma tarefa de reparação seja registada pelo Gerente {@link Manager},
 * o Mecânico é notificado de que existe trabalho para fazer, e de seguida
 * dirige ao Parque de Estacionamento {@link Park} para avaliar a viatura
 * e diagnosticar qual a peça danificada que necessita de substituição.
 * De seguida, verifica se essa peça existe em stock na Área de Reparação
 * {@link RepairArea}, e caso esta não exista, informa o Gerente para que este
 * possa reabastecer a Área de Reparação com mais peças, necessárias para a
 * reparação. Caso a peça esteja disponível, o Mecânico repara a viatura com a
 * peça, e assim que esta esteja reparada, coloca a viatura no Parque de
 * Estacionamento e informa o Gerente de que esta está pronta a ser levantada
 * pelo Cliente {@link Customer}.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Mechanic extends Thread {

    /** O id do Mecânico */
    private int mechanicId;

    /**
     * O id da viatura que o Mecânico está a reparar de momento.
     * Poderá ter os seguintes valores:
     * <ul>
     *  <li>o id do cliente {@link Customer}</li>
     *  <li>-1, se nenhuma</li>
     * </ul>
     */
    private int currentCarFixingId;

    /**
     * Referência para o Repositório Geral
     * @see GeneralRepository
     */
    private GeneralRepository repository;

    /**
     * O estado interno do Mecânico
     * @see MechanicState
     */
    private MechanicState state;

    /**
     * Referência para a Área de Reparação
     * @see RepairArea
     */
    private RepairArea repairArea;

    /**
     * Referência para o Parque de Estacionamento
     * @see Park
     */
    private Park park;

    /**
     * Referência para a Recepção (Lounge)
     * @see Lounge
     */
    private Lounge lounge;

    /**
     * Construtor de um Mecânico
     *
     * Aqui será construído o objeto referente a um Mecânico.
     * De seguida, o repositório {@link GeneralRepository} será informado de
     * que o Mecânico foi criado, mostrando o seu estado inicial.
     *
     * @param id o id do Mecânico
     * @param repo referência para o {@link GeneralRepository}
     * @param repairArea referência para a {@link RepairArea}
     * @param park referência para o {@link Park}
     * @param lounge referência para o {@link Lounge}
     */
    public Mechanic(int id, GeneralRepository repo, RepairArea repairArea, Park park, Lounge lounge) {
        this.mechanicId = id;
        this.state = MechanicState.WAITING_FOR_WORK;
        this.repository = repo;
        this.repairArea = repairArea;
        this.park = park;
        this.lounge = lounge;
        this.currentCarFixingId = -1;

        repository.setMechanicState(id, MechanicState.WAITING_FOR_WORK);
    }

    /**
     * Ciclo de vida de um Mecânico, conforme descrito na descrição da classe.
     */
    @Override
    public void run() {
        while (repairArea.readThePaper()){
            repairArea.startRepairProcedure();
            int partRequired = park.getVehicle();

            boolean hasPart = repairArea.getRequiredPart(partRequired);
            if(!hasPart) {
                lounge.letManagerKnow(partRequired);
                continue;
            }

            repairArea.partAvailable(partRequired);
            repairArea.resumeRepairProcedure();
            fixIt();
            park.returnVehicle();
            lounge.repairConcluded();
        }
    }

    /**
     * Operação getMechanicId
     *
     * Obtém o id do Mecânico
     *
     * @return o id do mecânico
     */
    public int getMechanicId() {
        return this.mechanicId;
    }

    /**
     * Operação setState
     *
     * Modifica o estado interno do Mecânico
     *
     * @param state o estado interno do Mecânico
     */
    public void setState(MechanicState state) {
        this.state = state;
    }

    /**
     * Operação getCurrentCarFixingId
     *
     * Marca a viatura que o Mecânico estiver a reparar de momento,
     * o id da viatura caso esteja a reparar uma viatura, -1 se nenhuma.
     *
     * @param carId o id da viatura (o mesmo que o do {@link Customer})
     */
    public void setCurrentCarFixingId(int carId) {
        this.currentCarFixingId = carId;
    }

    /**
     * Operação getCurrentCarFixingId
     *
     * Obtém a viatura que o Mecânico estiver a reparar de momento.
     * Devolve o id da viatura caso esteja a reparar uma viatura, -1 se nenhuma.
     *
     * @return o id da viatura (o mesmo que o do {@link Customer})
     */
    public int getCurrentCarFixingId() {
        return this.currentCarFixingId;
    }

    /**
     * Operação interna fixIt
     *
     * O Mecânico irá reparar a viatura durante um período aleatório de tempo.
     */
    private void fixIt() {
        try {
            sleep ((long) (1 + 10 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

package entities;

import genclass.GenericIO;
import interfaces.*;
import model.CustomerState;
import model.ManagerState;
import model.ManagerTask;
import utils.Pair;

import java.rmi.RemoteException;

/**
 * Classe Manager (Gerente)<br>
 *
 * Esta classe é responsável pela criação de um Gerente, que é uma
 * das entidades ativas do problema.<br>
 *
 * O Gerente, como o nome indica, é o responsável pela gestão da Oficina.
 * Quando o Gerente chega à Oficina fica a aguardar tarefas, que começam com
 * a chegada de Clientes (Customer), que esperam ser atendidos pelo Gerente
 * assim que chegam à Recepção (Lounge) para reparar a sua viatura. Caso os
 * Clientes queiram, poderão obter uma das viaturas de substituição à disposição,
 * cuja chave é entregue pelo Gerente assim que disponível. Assim que a viatura
 * esteja reparada, o Gerente informa o respectivo cliente, que está algures
 * a fazer a sua vida normal (OutsideWorld) para a vir buscar,
 * o que é feito após o pagamento da reparação. Caso os Mecânicos (Mechanic)
 * necessitem de peças para a reparação das viaturas, o Gerente também é responsável
 * por se dirigir ao Fornecedor (SupplierSite) comprar as peças e colocá-las
 * na Área de Reparação (RepairArea) para que os Mecânicos possam continuar o
 * trabalho. Assim que não haja mais trabalho para fazer, o Gerente fecha a
 * Oficina e marca o dia como terminado.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Manager extends Thread {

    /**
     * O estado interno do Gerente
     * @see ManagerState
     */
    private ManagerState state;

    /**
     * Indicação se é a primeira execução
     */
    private boolean firstRun;

    /**
     * Cesto de compras do Gerente.<br>
     * Para reabastecer-se de peças no Fornecedor {@link ISupplierSite}.
     */
    private int[] partsBasket;

    /**
     * O cliente que o Gerente está a atender no momento.
     */
    private int currentCustomerAttending;

    /**
     * Referência para o Repositório Geral
     * @see IGeneralRepository
     */
    private IGeneralRepository repository;

    /**
     * Referência para a Recepção (Lounge)
     * @see ILounge
     */
    private ILounge lounge;

    /**
     * Referência para a Área de Reparação (Repair Area)
     * @see IRepairArea
     */
    private IRepairArea repairArea;

    /**
     * Referência para o Mundo Exterior (Outside World)
     * @see IOutsideWorld
     */
    private IOutsideWorld outsideWorld;

    /**
     * Referência para o Fornecedor (SupplierSite)
     * @see ISupplierSite
     */
    private ISupplierSite supplierSite;

    /**
     * Construtor de um Gerente<br>
     *
     * Aqui será construído o objeto referente a um Gerente.
     * De seguida, o repositório {@link IGeneralRepository} será informado de
     * que o Gerente foi criado, mostrando o seu estado inicial.<br>
     *
     * @param nParts número total de peças existentes
     * @param repo referência para o {@link IGeneralRepository}
     * @param lounge referência para o {@link ILounge}
     * @param repairArea referência para a {@link IRepairArea}
     * @param outsideWorld referência para o {@link IOutsideWorld}
     * @param supplierSite referência para o {@link ISupplierSite}
     */
    public Manager(int nParts, IGeneralRepository repo, ILounge lounge, IRepairArea repairArea,
                   IOutsideWorld outsideWorld, ISupplierSite supplierSite) throws RemoteException {
        this.state = ManagerState.CHECKING_WHAT_TO_DO;
        this.currentCustomerAttending = -1;
        this.repository = repo;
        this.lounge = lounge;
        this.repairArea = repairArea;
        this.outsideWorld = outsideWorld;
        this.supplierSite = supplierSite;
        this.partsBasket = new int[nParts];
        this.firstRun = true;

        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO, false);
    }

    /**
     * Ciclo de vida de um Gerente, conforme descrito na descrição da classe.
     */
    @Override
    public void run() {
        try {
            while (lounge.getNextTask(firstRun)) {
                Pair<ManagerTask, Integer> pair = lounge.appraiseSit();
                ManagerTask task = pair.getLeft();
                switch (task) {
                    case GET_PARTS:
                        partsBasket = supplierSite.goToSupplier();
                        setState(ManagerState.GETTING_NEW_PARTS);
                        repairArea.storePart(partsBasket);
                        setState(ManagerState.REPLENISH_STOCK);
                        break;
                    case PHONE_CUSTOMER:
                        currentCustomerAttending = pair.getRight();
                        outsideWorld.phoneCustomer(currentCustomerAttending);
                        setState(ManagerState.ALERTING_CUSTOMER);
                        break;
                    case TALK_CUSTOMER:
                        setState(ManagerState.ATTENDING_CUSTOMER);
                        Pair<CustomerState, Integer> pair1 = lounge.talkToCustomer();
                        switch (pair1.getLeft()) {
                            case WAITING_FOR_REPLACE_CAR:
                                lounge.handCarKey();
                                break;
                            case RECEPTION_PAYING:
                                currentCustomerAttending = pair1.getRight();
                                lounge.receivePayment(currentCustomerAttending);
                                break;
                            case RECEPTION_REPAIR:
                                currentCustomerAttending = pair1.getRight();
                                repairArea.registerService(currentCustomerAttending);
                                setState(ManagerState.POSTING_JOB);
                                break;
                            default:
                                continue;
                        }
                        break;
                }
                setState(ManagerState.CHECKING_WHAT_TO_DO);
                firstRun = false;
            }
            repairArea.markEndOfDay();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception thrown at Manager lifecycle: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Operação setState<br>
     *
     * Altera o estado interno do Gerente.<br>
     *
     * @param state o novo estado do Gerente
     */
    public void setState(ManagerState state) {
        this.state = state;
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
     * Operação setCurrentlyAttendingCustomer<br>
     *
     * Altera o id do cliente a ser atendido no momento<br>
     *
     * @param customerId id do Cliente
     */
    public void setCurrentlyAttendingCustomer(int customerId) {
        this.currentCustomerAttending = customerId;
    }
}

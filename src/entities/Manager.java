package entities;

import regions.*;

/**
 * Classe Manager (Gerente)<br>
 *
 * Esta classe é responsável pela criação de um Gerente, que é uma
 * das entidades ativas do problema.<br>
 *
 * O Gerente, como o nome indica, é o responsável pela gestão da Oficina.
 * Quando o Gerente chega à Oficina fica a aguardar tarefas, que começam com
 * a chegada de Clientes ({@link Customer}), que esperam ser atendidos pelo Gerente
 * assim que chegam à Recepção ({@link Lounge}) para reparar a sua viatura. Caso os
 * Clientes queiram, poderão obter uma das viaturas de substituição à disposição,
 * cuja chave é entregue pelo Gerente assim que disponível. Assim que a viatura
 * esteja reparada, o Gerente informa o respectivo cliente, que está algures
 * a fazer a sua vida normal ({@link OutsideWorld}) para a vir buscar,
 * o que é feito após o pagamento da reparação. Caso os Mecânicos ({@link Mechanic})
 * necessitem de peças para a reparação das viaturas, o Gerente também é responsável
 * por se dirigir ao Fornecedor ({@link SupplierSite}) comprar as peças e colocá-las
 * na Área de Reparação ({@link RepairArea}) para que os Mecânicos possam continuar o
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
     * Cesto de compras do Gerente.<br>
     * Para reabastecer-se de peças no Fornecedor {@link SupplierSite}.
     */
    private int[] partsBasket;

    /**
     * O cliente que o Gerente está a atender no momento.
     */
    private int currentCustomerAttending;

    /**
     * Referência para o Repositório Geral
     * @see GeneralRepository
     */
    private GeneralRepository repository;

    /**
     * Referência para a Recepção (Lounge)
     * @see Lounge
     */
    private Lounge lounge;

    /**
     * Referência para a Área de Reparação (Repair Area)
     * @see RepairArea
     */
    private RepairArea repairArea;

    /**
     * Referência para o Mundo Exterior (Outside World)
     * @see OutsideWorld
     */
    private OutsideWorld outsideWorld;

    /**
     * Referência para o Fornecedor (SupplierSite)
     * @see SupplierSite
     */
    private SupplierSite supplierSite;

    /**
     * Construtor de um Gerente<br>
     *
     * Aqui será construído o objeto referente a um Gerente.
     * De seguida, o repositório {@link GeneralRepository} será informado de
     * que o Gerente foi criado, mostrando o seu estado inicial.<br>
     *
     * @param nParts número total de peças existentes
     * @param repo referência para o {@link GeneralRepository}
     * @param lounge referência para o {@link Lounge}
     * @param repairArea referência para a {@link RepairArea}
     * @param outsideWorld referência para o {@link OutsideWorld}
     * @param supplierSite referência para o {@link SupplierSite}
     */
    public Manager(int nParts, GeneralRepository repo, Lounge lounge, RepairArea repairArea, OutsideWorld outsideWorld, SupplierSite supplierSite) {
        this.state = ManagerState.CHECKING_WHAT_TO_DO;
        this.currentCustomerAttending = -1;
        this.repository = repo;
        this.lounge = lounge;
        this.repairArea = repairArea;
        this.outsideWorld = outsideWorld;
        this.supplierSite = supplierSite;
        this.partsBasket = new int[nParts];

        repository.setManagerState(ManagerState.CHECKING_WHAT_TO_DO);
    }

    /**
     * Ciclo de vida de um Gerente, conforme descrito na descrição da classe.
     */
    @Override
    public void run() {
        while (lounge.getNextTask()) {
            ManagerTask task = lounge.appraiseSit();
            switch (task) {
                case GET_PARTS:
                    partsBasket = supplierSite.goToSupplier();
                    repairArea.storePart(partsBasket);
                    break;
                case PHONE_CUSTOMER:
                    outsideWorld.phoneCustomer(currentCustomerAttending);
                    break;
                case TALK_CUSTOMER:
                    switch (lounge.talkToCustomer()) {
                        case WAITING_FOR_REPLACE_CAR:
                            lounge.handCarKey();
                            break;
                        case RECEPTION_PAYING:
                            lounge.receivePayment(currentCustomerAttending);
                            break;
                        case RECEPTION_REPAIR:
                            repairArea.registerService(currentCustomerAttending);
                            break;
                        default:
                            continue;
                    }
                    break;
            }
        }
        repairArea.markEndOfDay();
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

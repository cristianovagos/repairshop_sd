package entities;

import regions.*;

/**
 * Classe Customer (Cliente)
 *
 * Esta classe é responsável pela criação de um Cliente, que é uma
 * das entidades ativas do problema.
 *
 * Inicialmente, o Cliente irá decidir por si próprio se pretende reparar a sua
 * viatura, e quando achar oportuno, dirige-se à Oficina, estacionando a sua viatura
 * no Parque de Estacionamento {@link Park}, e dirige-se à Recepção {@link Lounge},
 * onde aguarda a sua vez para ser atendido pelo Gerente {@link Manager}, que o
 * irá atender tendo em vista a reparação da sua viatura própria. Caso queira, o
 * cliente poderá ter acesso a uma viatura de substituição, que caso esteja disponível,
 * ser-lhe-à dado uma chave, para que a possa levantar ao Parque de Estacionamento
 * e prosseguir com a sua vida normal no Mundo Exterior {@link OutsideWorld}.
 * Assim que a reparação esteja concluída, o Gerente irá notificar o Cliente de que
 * a sua viatura está pronta a ser levantada. O Cliente assim volta à Oficina,
 * para pagar a reparação e levantar a sua viatura.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Customer extends Thread {

    /** O id do Cliente */
    private int customerId;

    /**
     * Id da viatura que o cliente está a conduzir de momento
     * <ul>
     *  <li>o id do cliente</li>
     *  <li>uma das viaturas de substituição</li>
     *  <ul>
     *      <li>100, se viatura de substituição 0</li>
     *      <li>101, se viatura de substituição 1</li>
     *      <li>etc...</li>
     *  </ul>
     *  <li>-1, se nenhuma</li>
     * </ul>
     */
    private int carId;

    /**
     * Estado interno do Cliente
     * @see CustomerState
     */
    private CustomerState state;

    /**
     * Indicação se pretende viatura de substituição ou não
     */
    private boolean wantsReplacementCar;

    /**
     * Chave da viatura de substituição
     */
    private int key;

    /**
     * Referência para o Repositório
     * @see GeneralRepository
     */
    private GeneralRepository repository;

    /**
     * Referência para a Recepção (Lounge)
     * @see Lounge
     */
    private NewLounge lounge;

    /**
     * Referência para o Parque de Estacionamento (Park)
     * @see Park
     */
    private Park park;

    /**
     * Referência para o Mundo Exterior (Outside World)
     * @see OutsideWorld
     */
    private OutsideWorld outsideWorld;

    /**
     * Construtor de um Cliente
     *
     * Aqui será construído o objeto referente a um Cliente.
     * De seguida, o repositório {@link GeneralRepository} será informado de
     * que o Cliente foi criado, mostrando o seu estado inicial.
     *
     * @param id id do cliente
     * @param repo referência para o Repositório {@link GeneralRepository}
     * @param lounge referência para o Lounge {@link Lounge}
     * @param park referência para o Park {@link Park}
     * @param outsideWorld referência para o Outside World {@link OutsideWorld}
     */
    public Customer(int id, GeneralRepository repo, NewLounge lounge, Park park, OutsideWorld outsideWorld) {
        this.customerId = id;
        this.carId = id;
        this.state = CustomerState.NORMAL_LIFE_WITH_CAR;
        this.wantsReplacementCar = Math.random() >= 0.6;
        this.repository = repo;
        this.lounge = lounge;
        this.park = park;
        this.outsideWorld = outsideWorld;

        repository.initializeCustomer(id, wantsReplacementCar);
    }

    /**
     * Ciclo de vida de um Cliente, conforme descrito na descrição da classe.
     */
    @Override
    public void run() {
        decideOnRepair();
        park.goToRepairShop();
        lounge.queueIn(false);
        lounge.talkWithManager();
        if(wantsReplacementCar) {
            key = lounge.collectKey();
            park.findCar(key);
            outsideWorld.backToWorkByCar(false);
            park.goToRepairShop();
        } else
            outsideWorld.backToWorkByBus();
        lounge.queueIn(true);
        lounge.payForTheService();
        park.collectCar();
        outsideWorld.backToWorkByCar(true);
    }

    /**
     * Operação getCustomerId
     *
     * Obtém o id do Cliente
     *
     * @return o id do cliente
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Operação setCarId
     *
     * Altera o id da viatura que o cliente possui
     *
     * @param carId o id da viatura
     */
    public void setCarId(int carId) {
        this.carId = carId;
    }

    /**
     * Operação getCarId
     *
     * Obtém o id da viatura atual que o cliente possui
     *
     * @return o id da viatura
     */
    public int getCarId() {
        return this.carId;
    }

    /**
     * Operação getWantsReplacementCar
     *
     * Obtém a indicação se o cliente pretende ou não viatura de substituição
     *
     * @return se o cliente pretende ou não viatura de substituição
     */
    public boolean getWantsReplacementCar() {
        return this.wantsReplacementCar;
    }

    /**
     * Operação setState
     *
     * Altera o estado interno do cliente
     *
     * @param state o novo estado do cliente
     */
    public void setState(CustomerState state) {
        this.state = state;
    }

    /**
     * Operação getCustomerState
     *
     * Obtém o estado interno do cliente
     *
     * @return o estado interno do cliente
     */
    public CustomerState getCustomerState() {
        return this.state;
    }

    /**
     * Operação interna decideOnRepair
     *
     * O cliente irá decidir se pretende reparar a viatura durante um período de tempo
     */
    private void decideOnRepair() {
        try {
            sleep ((long) (1 + 40 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

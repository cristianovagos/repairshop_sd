package service.proxy;

import comm.*;
import genclass.GenericIO;
import comm.ServerCom;
import model.CustomerState;
import model.ManagerState;
import model.MechanicState;
import service.OutsideWorldInterface;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para o OutsideWorld, uma das áreas partilhadas do
 *   problema da Repair Shop, que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */
public class ClientProxy extends Thread implements IManagerAtt, ICustomerAtt, IMechanicAtt {
    /**
     *  Contador de threads lançados
     */
    private static int nProxy;

    /**
     *  Canal de comunicação
     */
    private ServerCom sconi;

    /**
     *  Interface do OutsideWorld
     */
    private OutsideWorldInterface outsideWorldInterface;

    /**
     * Estado interno do Cliente
     * @see CustomerState
     */
    private CustomerState customerState;

    /** O id do Cliente */
    private int customerId;

    /**
     * Id da viatura que o cliente está a conduzir de momento<br>
     * <ul>
     *  <li>o id do cliente</li>
     *  <li>uma das viaturas de substituição</li>
     *  <li>
     *      <ul>
     *          <li>100, se viatura de substituição 0</li>
     *          <li>101, se viatura de substituição 1</li>
     *          <li>etc...</li>
     *      </ul>
     *  </li>
     *  <li>-1, se nenhuma</li>
     * </ul>
     */
    private int customerCarId;

    /**
     * Indicação se o Cliente pretende viatura de substituição ou não
     */
    private boolean wantsReplacementCar;

    /**
     * Chave de viatura de substituição do Cliente
     */
    private int key;

    /**
     * O estado interno do Gerente
     * @see ManagerState
     */
    private ManagerState managerState;

    /**
     * Indicação se é a primeira execução
     */
    private boolean firstRun;

    /**
     * Cesto de compras do Gerente.<br>
     * Para reabastecer-se de peças no Fornecedor.
     */
    private int[] partsBasket;

    /**
     * O cliente que o Gerente está a atender no momento.
     */
    private int currentlyAttendingCustomer;

    /**
     * O estado interno do Mecânico
     * @see MechanicState
     */
    private MechanicState mechanicState;

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
     *  Instanciação do interface ao RepairArea.
     *
     *  @param sconi canal de comunicação
     *  @param outsideWorldInterface interface ao RepairArea
     */
    public ClientProxy (ServerCom sconi, OutsideWorldInterface outsideWorldInterface) {
        super ("OutsideWorldProxy_" + getProxyId ());
        this.sconi = sconi;
        this.outsideWorldInterface = outsideWorldInterface;
    }

    /**
     *  Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        Message inMessage = null,                                      // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
        try {
            outMessage = outsideWorldInterface.processAndReply(inMessage);         // processá-lo
        } catch (MessageException e) {
            GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
            GenericIO.writelnString (e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject (outMessage);                                // enviar resposta ao cliente
        sconi.close ();                                                // fechar canal de comunicação
    }

    /**
     *  Geração do identificador da instanciação.
     *
     *  @return identificador da instanciação
     */
    private static int getProxyId() {
        Class<ClientProxy> cl = null;               // representação do tipo de dados service.proxy.ClientProxy na máquina
                                                    //   virtual de Java
        int proxyId;                                // identificador da instanciação

        try {
            cl = (Class<ClientProxy>) Class.forName ("service.proxy.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString ("O tipo de dados service.proxy.ClientProxy não foi encontrado!");
            e.printStackTrace ();
            System.exit (1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }

    /**
     * Altera o estado interno do Cliente
     * @param customerState o novo estado do Cliente
     */
    @Override
    public void setCustomerState(CustomerState customerState) {
        this.customerState = customerState;
    }

    /**
     * Obtém o estado interno do Cliente
     * @return o estado interno do Cliente
     */
    @Override
    public CustomerState getCustomerState() {
        return customerState;
    }

    /**
     * Altera o id do Cliente
     * @param customerId novo id do Cliente
     */
    @Override
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Obtém o id do Cliente
     * @return o id do Cliente
     */
    @Override
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Altera o id do carro do Cliente
     * @param carId novo id do carro do Cliente
     */
    @Override
    public void setCustomerCarId(int carId) {
        this.customerCarId = carId;
    }

    /**
     * Obtém o id do carro do Cliente
     * @return id do carro do Cliente
     */
    @Override
    public int getCustomerCarId() {
        return customerCarId;
    }

    /**
     * Altera a indicação se o Cliente pretende viatura de substituição
     * @param wantsReplacementCar indicação se o Cliente pretende viatura de substituição
     */
    @Override
    public void setWantsReplacementCar(boolean wantsReplacementCar) {
        this.wantsReplacementCar = wantsReplacementCar;
    }

    /**
     * Obtém a indicação se o Cliente pretende viatura de substituição
     * @return indicação se o Cliente pretende viatura de substituição
     */
    @Override
    public boolean getWantsReplacementCar() {
        return wantsReplacementCar;
    }

    /**
     * Altera a chave do carro do cliente
     * @param key chave do carro do cliente
     */
    @Override
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * Obtém a chave do carro do cliente
     * @return chave do carro do cliente
     */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * Altera o estado interno do Manager
     * @param managerState estado novo do Manager
     */
    @Override
    public void setManagerState(ManagerState managerState) {
        this.managerState = managerState;
    }

    /**
     * Obtém o estado interno do Manager
     * @return estado interno do Manager
     */
    @Override
    public ManagerState getManagerState() {
        return managerState;
    }

    /**
     * Altera a indicação de primeira execução
     * @param firstRun indicação de primeira execução
     */
    @Override
    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    /**
     * Obtém a indicação de primeira execução
     * @return indicação de primeira execução
     */
    @Override
    public boolean getFirstRun() {
        return firstRun;
    }

    /**
     * Altera o cesto de compras do Manager
     * @param partsBasket novo cesto de compras do Manager
     */
    @Override
    public void setPartsBasket(int[] partsBasket) {
        this.partsBasket = partsBasket;
    }

    /**
     * Obtém o cesto de compras do Manager
     * @return cesto de compras do Manager
     */
    @Override
    public int[] getPartsBasket() {
        return partsBasket;
    }

    /**
     * Altera o Cliente a ser atendido de momento pelo Manager
     * @param customer id do cliente
     */
    @Override
    public void setCurrentlyAttendingCustomer(int customer) {
        this.currentlyAttendingCustomer = customer;
    }

    /**
     * Obtém o id do cliente que está a ser atendido de momento pelo Manager
     * @return id do cliente
     */
    @Override
    public int getCurrentlyAttendingCustomer() {
        return currentlyAttendingCustomer;
    }

    /**
     * Altera o estado interno do Mecanico
     * @param state novo estado do Mecanico
     */
    @Override
    public void setMechanicState(MechanicState state) {
        this.mechanicState = state;
    }

    /**
     * Obtém o estado interno do Mecanico
     * @return estado interno do Mecanico
     */
    @Override
    public MechanicState getMechanicState() {
        return mechanicState;
    }

    /**
     * Altera o id do Mecanico
     * @param mechanicId novo id do Mecanico
     */
    @Override
    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
    }

    /**
     * Obtém o id do Mecanico
     * @return id do Mecanico
     */
    @Override
    public int getMechanicId() {
        return mechanicId;
    }

    /**
     * Altera o id do carro que o Mecanico esta a arranjar de momento
     * @param carId id do carro
     */
    @Override
    public void setCurrentCarFixingId(int carId) {
        this.currentCarFixingId = carId;
    }

    /**
     * Obtém o id do carro que o Mecanico esta a arranjar de momento
     * @return id do carro
     */
    @Override
    public int getCurrentCarFixingId() {
        return currentCarFixingId;
    }
}

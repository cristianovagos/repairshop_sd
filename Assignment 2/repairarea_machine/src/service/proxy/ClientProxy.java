package service.proxy;

import comm.*;
import genclass.GenericIO;
import comm.ServerCom;
import model.ManagerState;
import model.MechanicState;
import service.RepairAreaInterface;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para o RepairArea, uma das áreas partilhadas do
 *   problema da Repair Shop, que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */
public class ClientProxy extends Thread implements IManagerAtt, IMechanicAtt {
    /**
     *  Contador de threads lançados
     */
    private static int nProxy;

    /**
     *  Canal de comunicação
     */
    private ServerCom sconi;

    /**
     *  Interface da RepairArea
     */
    private RepairAreaInterface repairAreaInterface;

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
     *  @param repairAreaInterface interface ao RepairArea
     */
    public ClientProxy (ServerCom sconi, RepairAreaInterface repairAreaInterface) {
        super ("RepairAreaProxy_" + getProxyId ());
        this.sconi = sconi;
        this.repairAreaInterface = repairAreaInterface;
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
            outMessage = repairAreaInterface.processAndReply(inMessage);         // processá-lo
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

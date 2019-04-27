package service.proxy;

import comm.*;
import genclass.GenericIO;
import comm.ServerCom;
import model.ManagerState;
import service.SupplierSiteInterface;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para o SupplierSite, uma das áreas partilhadas do
 *   problema da Repair Shop, que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */
public class ClientProxy extends Thread implements IManagerAtt {
    /**
     *  Contador de threads lançados
     */
    private static int nProxy;

    /**
     *  Canal de comunicação
     */
    private ServerCom sconi;

    /**
     *  Interface do SupplierSite
     */
    private SupplierSiteInterface supplierSiteInterface;

    /**
     * O estado interno do Gerente
     * @see ManagerState
     */
    private ManagerState managerState;

    /**
     *  Instanciação do interface ao SupplierSite.
     *
     *  @param sconi canal de comunicação
     *  @param supplierSiteInterface interface ao SupplierSite
     */
    public ClientProxy (ServerCom sconi, SupplierSiteInterface supplierSiteInterface) {
        super ("SupplierSiteProxy_" + getProxyId ());
        this.sconi = sconi;
        this.supplierSiteInterface = supplierSiteInterface;
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
            outMessage = supplierSiteInterface.processAndReply(inMessage);         // processá-lo
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
}

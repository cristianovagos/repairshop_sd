package regions;

import entities.Manager;
import utils.ClientCom;
import utils.Message;
import utils.MessageType;

public class SupplierSiteStub implements SupplierSite{
    /**
     *  Nome do sistema computacional onde está localizado o servidor.
     */
    private String serverHostName;

    /**
     *  Número do port de escuta do servidor.
     */
    private int serverPortNumb;


    /**
     *  Instanciação do stub.
     *
     *    @param hostName nome do sistema computacional onde está localizado o servidor
     *    @param port número do port de escuta do servidor
     */
    public SupplierSiteStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação goToSupplier (chamada pelo {@link Manager}
     * <p>
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a {@link RepairArea} de peças, necessárias para a reparação das viaturas.
     *
     * @return carrinho de compras do Gerente
     */
    @Override
    public int[] goToSupplier() {
        Object fromServer;      //input
        int [] parts;
        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING,"goToSupplier");

        //wait to receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
        parts = ((Message) fromServer).getGenericIntegerArray();

        return parts;
    }

    private Object communicationWithServer(Message messageToSend)
    {
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);
        Object fromServer;      //input
        Object fromUser;       //output

        //enquanto a ligação não estiver establecida
        //a thread vai "dormir" até establecer a ligação
        while(!com.open())
        {
            try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        //Message to Send
        //CREATE MESSAGE
        fromUser = messageToSend;

        //Send Message
        com.writeObject(fromUser);

        //receive message
        fromServer = com.readObject();

        //close communications
        com.close();

        //return object
        return fromServer;
    }
}

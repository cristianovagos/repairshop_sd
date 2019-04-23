package regions;

import entities.Manager;
import utils.ClientCom;
import utils.Message;
import utils.MessageType;

public class OutsideWorldStub implements OutsideWorld {

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
    public OutsideWorldStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação phoneCustomer (chamada pelo {@link Manager})<br>
     * <p>
     * Aqui, o {@link Manager} irá notificar o {@link Customer} de que
     * o seu carro está pronto, ligando-lhe.<br>
     *
     * @param customerId o id do {@link Customer}
     */
    @Override
    public void phoneCustomer(int customerId) {
        Object fromServer;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING,"phoneCustomer", customerId);

        //wait to receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
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

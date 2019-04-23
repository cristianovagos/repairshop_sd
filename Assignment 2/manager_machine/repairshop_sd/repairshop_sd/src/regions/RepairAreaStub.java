package regions;

import entities.Manager;
import utils.ClientCom;
import utils.Message;
import utils.MessageType;

public class RepairAreaStub implements RepairArea {

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
    public RepairAreaStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação markEndOfDay (chamada pelo {@link Manager})<br>
     * <p>
     * Marca o encerramento do dia para o {@link Mechanic}.<br>
     */
    @Override
    public void markEndOfDay() {
        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING,"markEndOfDay");

        //wait to receive message
        communicationWithServer(messageToSend);
    }

    /**
     * Operação storePart (chamada pelo {@link Manager})<br>
     * <p>
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * {@link SupplierSite}, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o {@link Mechanic}.<br>
     *
     * @param newParts peças novas a serem incluídas no stock
     */
    @Override
    public void storePart(int[] newParts) {
        Object fromServer;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING,"storePart", newParts);

        //wait to receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
    }

    /**
     * Operação registerService (chamada pelo {@link Manager})<br>
     * <p>
     * O Manager irá registar um pedido de reparação da viatura do {@link Customer},
     * e notificar os {@link Mechanic} de que existe um serviço disponível.<br>
     *
     * @param customerId id do cliente
     */
    @Override
    public void registerService(int customerId) {
        Object fromServer;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING,"registerService", customerId);

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

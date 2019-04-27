package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import entities.Manager;
import genclass.GenericIO;

public class RepairArea implements IRepairArea {

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
    public RepairArea(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação markEndOfDay (chamada pelo {@link Manager})<br>
     * <p>
     * Marca o encerramento do dia para o Mechanic.<br>
     */
    @Override
    public void markEndOfDay() {
        Message inMessage;      //input
        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.REPAIR_AREA_MARK_END_OF_DAY_REQ);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_MARK_END_OF_DAY_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
    }

    /**
     * Operação storePart (chamada pelo {@link Manager})<br>
     * <p>
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * SupplierSite, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o Mechanic.<br>
     *
     * @param newParts peças novas a serem incluídas no stock
     */
    @Override
    public void storePart(int[] newParts) {
        Message inMessage;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.REPAIR_AREA_STORE_PART_REQ, newParts);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_STORE_PART_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState(inMessage.getManagerState());
    }

    /**
     * Operação registerService (chamada pelo {@link Manager})<br>
     * <p>
     * O Manager irá registar um pedido de reparação da viatura do Customer,
     * e notificar os Mechanic de que existe um serviço disponível.<br>
     *
     * @param customerId id do cliente
     */
    @Override
    public void registerService(int customerId) {
        Message inMessage;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.REPAIR_AREA_REGISTER_SERVICE_REQ, customerId);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_REGISTER_SERVICE_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( inMessage.getManagerState());
    }

    private Message communicationWithServer(Message messageToSend)
    {
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);
        Message fromServer;      //input
        Message fromUser;       //output

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
        fromServer = (Message) com.readObject();

        //close communications
        com.close();

        //return object
        return fromServer;
    }

}

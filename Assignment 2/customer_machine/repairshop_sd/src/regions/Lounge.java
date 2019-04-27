package regions;

import entities.*;
import genclass.GenericIO;
import comm.ClientCom;
import comm.Message;
import comm.MessageType;

/**
 * Classe Lounge (ligação com o Lounge)<br>
 *
 * Esta classe é responsável pela comunicação com o servidor do serviço da Recepção,
 * uma região partilhada do problema, feita através de passagem de mensagens, atuando
 * como um Stub para a classe real, sendo que são implementados os métodos do serviço
 * propriamente dito, através da sua interface.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Lounge implements ILounge {

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
    public Lounge(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação queueIn (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente chega à Recepção e aguarda na fila, para ser
     * atendido pelo Gerente (Manager). Tanto poderá colocar a sua
     * viatura para reparação ou para proceder ao pagamento do mesmo.<br>
     *
     * @param repairCompleted indicação se a reparação já foi feita
     */
    @Override
    public void queueIn(boolean repairCompleted) {
        Message inMessage, outMessage;
        int customerID;

        customerID = ((Customer)Thread.currentThread()).getCustomerId();

        //criar mensagem
        outMessage = new Message(MessageType.LOUNGE_QUEUE_IN_REQ, customerID, repairCompleted);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_QUEUE_IN_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
    }

    /**
     * Operação talkWithManager (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente fala com o Gerente para reparar
     * a sua viatura e entrega-lhe a chave da mesma.<br>
     */
    @Override
    public void talkWithManager() {
        Message inMessage, outMessage;
        int customerID;

        customerID = ((Customer)Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.LOUNGE_TALK_WITH_MANAGER_REQ, customerID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_TALK_WITH_MANAGER_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
        ((Customer) Thread.currentThread()).setCarId(inMessage.getIntegerParam1());
    }

    /**
     * Operação collectKey (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente pretende uma viatura de substituição enquanto a sua
     * viatura própria é reparada, e aguarda a entrega de uma chave de
     * uma das viaturas de substituição disponíveis por parte do Gerente.<br>
     *
     * @return chave da viatura de substituição
     */
    @Override
    public int collectKey() {
        Message inMessage, outMessage;
        int carKey;
        int customerID;

        customerID = ((Customer) Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.LOUNGE_COLLECT_KEY_REQ, customerID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_COLLECT_KEY_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
        ((Customer) Thread.currentThread()).setCarId(inMessage.getIntegerParam1());
        //get car key from the message
        carKey = inMessage.getIntegerParam1();

        return carKey;
    }

    /**
     * Operação payForTheService (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente procede ao pagamento do serviço prestado pela Oficina.
     * Caso este tenha utilizado uma viatura de substituição devolve a
     * chave da mesma ao Gerente.<br>
     */
    @Override
    public void payForTheService() {
        Message inMessage, outMessage;
        int carKey = -1;
        int customerID;

        customerID = ((Customer)Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.LOUNGE_PAY_FOR_THE_SERVICE_REQ, customerID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_PAY_FOR_THE_SERVICE_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
    }

    /**
     * Comunicação com o servidor do Lounge.
     * Envia e recebe mensagem de resposta
     *
     * @param messageToSend mensagem a ser enviada para o servidor
     * @return mensagem de resposta vinda do servidor
     */
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
        fromServer = (Message)com.readObject();

        //close communications
        com.close();

        //return object
        return fromServer;
    }
}

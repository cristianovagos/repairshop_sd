package regions;

import entities.Customer;
import genclass.GenericIO;
import comm.ClientCom;
import comm.Message;
import comm.MessageType;

/**
 * Classe Park (ligação com o Parque de Estacionamento)<br>
 *
 * Esta classe é responsável pela comunicação com o servidor do serviço do Park,
 * uma região partilhada do problema, feita através de passagem de mensagens, atuando
 * como um Stub para a classe real, sendo que são implementados os métodos do serviço
 * propriamente dito, através da sua interface.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Park implements IPark {

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
     *  @param hostName nome do sistema computacional onde está localizado o servidor
     *  @param port número do port de escuta do servidor
     */
    public Park(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação goToRepairShop (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente chega à RepairShop, e estaciona a sua viatura ou
     * a viatura de substituição que lhe foi atribuída no Park.<br>
     */
    @Override
    public void goToRepairShop() {
        Message inMessage, outMessage;
        int customerID, carID;

        customerID = ((Customer)Thread.currentThread()).getCustomerId();
        carID = ((Customer)Thread.currentThread()).getCarId();

        //criar mensagem
        outMessage = new Message(MessageType.PARK_GO_TO_REPAIR_SHOP_REQ, customerID, carID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.PARK_GO_TO_REPAIR_SHOP_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
    }

    /**
     * Operação findCar (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente recolhe a viatura de substituição do Park.<br>
     *
     * @param replacementCar viatura de substituição
     */
    @Override
    public void findCar(int replacementCar) {
        Message inMessage, outMessage;
        int customerID;

        customerID = ((Customer)Thread.currentThread()).getCustomerId();

        //criar mensagem
        outMessage = new Message(MessageType.PARK_FIND_CAR_REQ, customerID, replacementCar);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.PARK_FIND_CAR_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
        //set the current car ID
        ((Customer) Thread.currentThread()).setCarId(inMessage.getIntegerParam1());
    }

    /**
     * Operação collectCar (chamada pelo {@link Customer})<br>
     * <p>
     * O Cliente recolhe a sua viatura própria do Park.<br>
     */
    @Override
    public void collectCar() {
        Message inMessage, outMessage;
        int customerID;

        customerID = ((Customer)Thread.currentThread()).getCustomerId();

        //criar mensagem
        outMessage = new Message(MessageType.PARK_COLLECT_CAR_REQ, customerID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.PARK_COLLECT_CAR_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
        //set the current car ID
        ((Customer) Thread.currentThread()).setCarId(inMessage.getIntegerParam1());
    }

    /**
     * Comunicação com o servidor do Park.
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

package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import entities.Mechanic;
import genclass.GenericIO;

/**
 * Classe Park (Parque de Estacionamento)<br>
 *
 * Esta classe é responsável pela criação do Parque de Estacionamento, uma
 * das entidades passivas do problema.<br>
 *
 * É aqui que estão inicialmente localizadas as viaturas de substituição, a
 * serem disponibilizadas aos Clientes (Customer) que queiram. Também
 * é o local onde os Clientes vão estacionar as suas próprias viaturas quando
 * se dirigem à Oficina para posterior reparação, onde após estar concluída
 * será novamente estacionada pelo Mecânico ({@link Mechanic}).<br>
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
     *    @param hostName nome do sistema computacional onde está localizado o servidor
     *    @param port número do port de escuta do servidor
     */
    public Park(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }


    /**
     * Operação getVehicle (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico dirige-se ao Park para obter a viatura do Customer
     * e avalia qual a peça que deve substituir para a reparar.<br>
     *
     * @return a peça que necessita de substituição
     */
    @Override
    public int getVehicle() {
        Message inMessage, outMessage;
        int currentCarFixingID, partID;

        currentCarFixingID = ((Mechanic)Thread.currentThread()).getCurrentCarFixingId();
        //criar mensagem
        outMessage = new Message(MessageType.PARK_GET_VEHICLE_REQ, currentCarFixingID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.PARK_GET_VEHICLE_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
        partID = inMessage.getIntegerParam1();

        return partID;
    }

    /**
     * Operação returnVehicle (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico devolve a viatura já reparada ao Park.<br>
     */
    @Override
    public void returnVehicle() {
        Message inMessage, outMessage;
        int currentCarFixingID, partID;

        currentCarFixingID = ((Mechanic)Thread.currentThread()).getCurrentCarFixingId();
        //criar mensagem
        outMessage = new Message(MessageType.PARK_RETURN_VEHICLE_REQ, currentCarFixingID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.PARK_RETURN_VEHICLE_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());

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
        fromServer = (Message)com.readObject();

        //close communications
        com.close();

        //return object
        return fromServer;
    }
}

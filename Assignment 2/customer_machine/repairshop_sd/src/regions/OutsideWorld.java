package regions;

import entities.Customer;
import genclass.GenericIO;
import comm.ClientCom;
import comm.Message;
import comm.MessageType;

/**
 * Classe OutsideWorld (ligação com o MundoExterior)<br>
 *
 * Esta classe é responsável pela comunicação com o servidor do serviço do OutsideWorld,
 * uma região partilhada do problema, feita através de passagem de mensagens, atuando
 * como um Stub para a classe real, sendo que são implementados os métodos do serviço
 * propriamente dito, através da sua interface.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class OutsideWorld implements IOutsideWorld {

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
    public OutsideWorld(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação backToWorkByBus (chamada pelo {@link Customer})<br>
     * <p>
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     */
    @Override
    public void backToWorkByBus() {
        Message inMessage, outMessage;

        int customerID = ((Customer)Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_REQ, customerID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
    }

    /**
     * Operação backToWorkByCar (chamada pelo {@link Customer})<br>
     * <p>
     * Aqui, o cliente vai fazer a sua vida normal.
     * No caso de ter colocado a sua viatura para reparação na Oficina, fica à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     *
     * @param carRepaired indicação se a sua viatura já foi reparada
     */
    @Override
    public void backToWorkByCar(boolean carRepaired) {
        Message inMessage, outMessage;

        int customerID = ((Customer)Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_REQ, customerID, carRepaired);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_RESP)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
    }

    /**
     * Comunicação com o servidor do OutsideWorld.
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

package regions;

import entities.Customer;
import genclass.GenericIO;
import comm.ClientCom;
import comm.Message;
import comm.MessageType;

/**
 * Classe OutsideWorld (Mundo Exterior)<br>
 *
 * Esta classe é responsável pela criação do Mundo Exterior, uma das entidades
 * passivas do problema.<br>
 *
 * É aqui que os Clientes ({@link Customer}) estão no início do problema, a fazer
 * a sua vida normal com a sua viatura. Após um dado tempo, a sua viatura avaria
 * e necessita de ser reparada. Aí, o Cliente dirige-se à Oficina.<br>
 * Quer queira uma viatura de substituição ou não, o Cliente volta a fazer a sua
 * vida normal assim que deixa a sua viatura na Oficina, e é notificado pelo
 * Gerente ({@link Manager}) assim que a reparação esteja concluída.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class OutsideWorld implements IOutsideWorld{

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
    public OutsideWorld(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação backToWorkByBus (chamada pelo {@link Customer})<br>
     * <p>
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do {@link Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     */
    @Override
    public void backToWorkByBus() {
        Message inMessage, outMessage;

        int customerID = ((Customer)Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.BACK_TO_WORK_BY_BUS_REQUEST, customerID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.BACK_TO_WORK_BY_BUS_RESPONSE)
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
     * de novidades por parte do {@link Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     *
     * @param carRepaired indicação se a sua viatura já foi reparada
     */
    @Override
    public void backToWorkByCar(boolean carRepaired) {
        Message inMessage, outMessage;

        int customerID = ((Customer)Thread.currentThread()).getCustomerId();
        //criar mensagem
        outMessage = new Message(MessageType.BACK_TO_WORK_BY_CAR_REQUEST, customerID, carRepaired);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.BACK_TO_WORK_BY_CAR_RESPONSE)
        {
            GenericIO.writelnString ("Thread " + ((Customer) Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Customer) Thread.currentThread()).setState(inMessage.getCustomerState());
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

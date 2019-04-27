package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import entities.*;
import genclass.GenericIO;

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
     * Operação letManagerKnow (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico informa o Gerente (Manager) de que não existem
     * peças para que este possa reparar uma viatura.<br>
     *
     * @param partRequired peça em falta
     */
    @Override
    public void letManagerKnow(int partRequired) {
        Message inMessage, outMessage;
        int mechanicID;

        mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();
        //criar mensagem
        outMessage = new Message(MessageType.LOUNGE_LET_MANAGER_KNOW_REQ, partRequired, mechanicID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_LET_MANAGER_KNOW_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
    }

    /**
     * Operação repairConcluded (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico terminou de reparar uma viatura, e irá informar o Gerente
     * (Manager) que esta está pronta a ser levantada pelo seu proprietário,
     * o Cliente (Customer).<br>
     */
    @Override
    public void repairConcluded() {
        Message inMessage, outMessage;
        int mechanicID, carFixed;

        mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();
        carFixed = ((Mechanic)Thread.currentThread()).getCurrentCarFixingId();

        //criar mensagem
        outMessage = new Message(MessageType.LOUNGE_REPAIR_CONCLUDED_REQ, carFixed, mechanicID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_REPAIR_CONCLUDED_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
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

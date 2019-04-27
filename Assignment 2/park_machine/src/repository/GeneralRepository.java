package repository;

import comm.*;
import genclass.GenericIO;
import model.CustomerState;
import model.ManagerState;
import model.MechanicState;

/**
 * Classe GeneralRepository (Repositório Geral)<br>
 *
 * Esta classe é responsável pela comunicação com o servidor do serviço do Repositório Geral de Dados,
 * uma região partilhada do problema, feita através de passagem de mensagens, atuando
 * como um Stub para a classe real, sendo que são implementados os métodos do serviço
 * propriamente dito, através da sua interface.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class GeneralRepository implements IGeneralRepository {
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
    public GeneralRepository(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação setCustomerState<br>
     *
     * Altera o estado de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param newState o estado novo do cliente
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setCustomerState(int index, CustomerState newState, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_STATE_REQ, index, newState, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMER_STATE_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setCustomerCar<br>
     *
     * Altera a viatura atual de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param carId o identificador da viatura
     *              (-1 se nenhuma, id do customer se a própria, ou 100..102 para viatura de substituição)
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setCustomerCar(int index, int carId, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_CAR_REQ, index, carId, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMER_CAR_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação customerCarEntersPark<br>
     *
     * Indica que uma viatura de um cliente entrou no park<br>
     */
    @Override
    public void customerCarEntersPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação customerCarLeavesPark<br>
     *
     * Indica que uma viatura de um cliente saiu do park<br>
     */
    @Override
    public void customerCarLeavesPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação replacementCarEntersPark<br>
     *
     * Indica que uma viatura de substituição entrou no park<br>
     */
    @Override
    public void replacementCarEntersPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação replacementCarLeavesPark<br>
     *
     * Indica que uma viatura de substituição saiu do park<br>
     */
    @Override
    public void replacementCarLeavesPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Comunicação com o servidor do Repository.
     * Envia e recebe mensagem de resposta
     *
     * @param messageToSend mensagem a ser enviada para o servidor
     * @return mensagem de resposta vinda do servidor
     */
    private Message communicationWithServer(Message messageToSend) {
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);
        Message fromServer;      //input
        Message fromUser;       //output

        //enquanto a ligação não estiver establecida
        //a thread vai "dormir" até establecer a ligação
        while(!com.open()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        //Message to Send
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

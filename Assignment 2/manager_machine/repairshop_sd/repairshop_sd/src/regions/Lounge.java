package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import entities.*;
import genclass.GenericIO;
import model.CustomerState;
import model.ManagerTask;


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
     * Operação getNextTask (chamada pelo {@link Manager})<br>
     *
     * Caso hajam pedidos ao Gerente, este procede à realização
     * das tarefas pretendidas. Assim que não haja mais pedidos,
     * a Oficina é fechada, e os Mecânicos ({@link Mechanic}) poderão
     * ir embora.<br>
     *
     * @return indicação se ainda há trabalho para fazer na Oficina
     */
    @Override
    public boolean getNextTask() {
        Message inMessage;      //input
        boolean hasNextTask = false; //manager tem tarefa a realizar
        boolean firstRun = ((Manager) Thread.currentThread()).getFirstRun();

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.LOUNGE_GET_NEXT_TASK_REQ, firstRun);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_GET_NEXT_TASK_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }


        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState(inMessage.getManagerState());
        ((Manager) Thread.currentThread()).setFirstRun(inMessage.getBooleanParam1());
        hasNextTask = inMessage.getBooleanParam2();

        //return if manager has next task
        return hasNextTask;
    }

    /**
     * Operação appraiseSit (chamada pelo {@link Manager})<br>
     *
     * O Gerente avalia a próxima tarefa a desempenhar, de acordo
     * com a seguinte prioridade:<br>
     * <ul>
     *     <li>Reabastecimento de peças para os {@link Mechanic}
     *     poderem trabalhar na {@link IRepairArea} através da {@link ISupplierSite}</li>
     *     <li>Caso haja clientes em fila de espera para obter uma chave para
     *     viatura de substituição.</li>
     *     <li>Caso haja viaturas reparadas, o Gerente vai chamar os clientes
     *     para as virem buscar.</li>
     *     <li>Atender os clientes em fila de espera na Recepção</li>
     * </ul>
     *
     * @return a próxima tarefa a ser executada pelo Gerente
     */
    @Override
    public ManagerTask appraiseSit() {
        Message inMessage;      //input
        ManagerTask nextTask = ManagerTask.NONE; //manager tem tarefa a realizar

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.LOUNGE_APPRAISE_SIT_REQ);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);


        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_APPRAISE_SIT_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }


        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState(  inMessage.getManagerState());
        nextTask =  inMessage.getManagerTask();

        //return if manager's next task
        return nextTask;
    }

    /**
     * Operação talkToCustomer (chamada pelo {@link Manager})<br>
     *
     * O Gerente irá atender o cliente {@link Customer} para saber o que
     * este pretende, de uma das seguintes opções:<br>
     * <ul>
     *     <li>Reparar a sua viatura</li>
     *     <li>Obter uma viatura de substituição</li>
     *     <li>Efetuar pagamento do serviço</li>
     * </ul>
     *
     * @return estado do cliente atual
     */
    @Override
    public CustomerState talkToCustomer() {
        Message inMessage;      //input
        CustomerState customerState= CustomerState.NONE; //manager tem tarefa a realizar

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.LOUNGE_TALK_TO_CUSTOMER_REQ);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_TALK_TO_CUSTOMER_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState(inMessage.getManagerState());
        customerState =  inMessage.getCustomerState();

        if(customerState == CustomerState.RECEPTION_REPAIR || customerState == CustomerState.RECEPTION_PAYING)
            ((Manager) Thread.currentThread()).setCurrentlyAttendingCustomer(inMessage.getIntegerParam1());

        //return if customer's state
        return customerState;
    }

    /**
     * Operação handCarKey (chamada pelo {@link Manager})<br>
     *
     * O Gerente irá dar uma das chaves das viaturas de substituição
     * disponíveis ao {@link Customer}.<br>
     */
    @Override
    public void handCarKey() {
        Message inMessage;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message( MessageType.LOUNGE_HAND_CAR_KEY_REQ);

        //receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_HAND_CAR_KEY_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState(  inMessage.getManagerState());
    }

    @Override
    public void receivePayment(int customerToAttend) {
        Message inMessage;

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.LOUNGE_RECEIVE_PAYMENT_REQ, customerToAttend);

        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.LOUNGE_RECEIVE_PAYMENT_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }


        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState(  inMessage.getManagerState());
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

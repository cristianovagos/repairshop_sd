package regions;

import entities.CustomerState;
import entities.*;
import utils.ClientCom;
import utils.Message;
import utils.MessageType;

public class LoungeStub implements Lounge {

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
    public LoungeStub (String hostName, int port)
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
        Object fromServer;      //input
        boolean hasNextTask = false; //manager tem tarefa a realizar

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING,"getNextTask");

        //wait to receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
        hasNextTask = ((Message) fromServer).getGenericBoolean();

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
     *     poderem trabalhar na {@link RepairArea} através da {@link SupplierSite}</li>
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
        Object fromServer;      //input
        ManagerTask nextTask = ManagerTask.NONE; //manager tem tarefa a realizar

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING, "appraiseSit");

        //wait to receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
        nextTask = ((Message) fromServer).getManagerTask();

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
        Object fromServer;      //input
        CustomerState customerState= CustomerState.NONE; //manager tem tarefa a realizar

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING, "talkToCustomer");

        //wait to receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
        customerState = ((Message) fromServer).getCustomerState();

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
        Object fromServer;      //input

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message( MessageType.METHOD_CALLING,"handCarKey");

        //receive message
        fromServer = communicationWithServer(messageToSend);

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
    }

    @Override
    public void receivePayment(int customerToAttend) {
        Object fromServer;

        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.METHOD_CALLING, "receivePayment", customerToAttend);

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

/*TODO: BEFORE SENDING WORK DELETE THIS! ONLY FOR BACKUP PURPOSES*/
/*
    @Override
    public ManagerTask appraiseSit() {
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);
        Object fromServer;      //input
        Object fromUser;       //output
        ManagerTask nextTask = ManagerTask.NONE; //manager tem tarefa a realizar

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
        Message messageToSend = new Message(MessageType.METHOD_CALLING, "appraiseSit");
        fromUser = messageToSend;

        //Send Message
        com.writeObject(fromUser);

        //wait to receive message
        fromServer = com.readObject();

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( ((Message) fromServer).getManagerState());
        nextTask = ((Message) fromServer).getManagerTask();

        //close communications
        com.close();

        //return if manager's next task
        return nextTask;
    }
 */
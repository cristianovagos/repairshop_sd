package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import entities.*;
import genclass.GenericIO;

/**
 * Classe RepairArea (Área de Reparação)<br>
 *
 * Esta classe é responsável pela criação da Área de Reparação, uma das
 * entidades passivas do problema.<br>
 *
 * A Área de Reparação é o local onde decorrem as reparações das viaturas,
 * por parte dos Mecânicos ({@link Mechanic}), sendo que é aqui onde eles irão
 * estar a maior parte do tempo, seja a reparar viaturas, seja à espera da
 * indicação do Gerente ({@link Manager}) de que há trabalho a fazer. Nesta área
 * existe um número de peças em stock para que os Mecânicos possam substituir
 * nas viaturas que necessitem de reparar, e caso estas porventura faltem, é
 * responsabilidade do Gerente obter mais peças e por sua vez restabelecer o
 * stock para que os Mecânicos voltem ao trabalho.<br>
 * Quando o Gerente dá o dia por terminado, todos os Mecânicos vão embora.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class RepairArea implements  IRepairArea{

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
     * Operação readThePaper (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico irá estar bloqueado até que lhe seja atribuído um serviço<br>
     *
     * @return estado do dia de trabalho
     */
    @Override
    public boolean readThePaper() {
        Message inMessage, outMessage;
        boolean notEndOfDay = false;

        int mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();
        boolean firstRun = ((Mechanic) Thread.currentThread()).getFirstRun();
         //criar mensagem
        outMessage = new Message(MessageType.REPAIR_AREA_READ_THE_PAPER_REQ, mechanicID, firstRun);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_READ_THE_PAPER_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
        notEndOfDay = inMessage.getBooleanParam1();
        ((Mechanic)Thread.currentThread()).setFirstRun(inMessage.getBooleanParam2());
        return notEndOfDay;
    }

    /**
     * Operação startRepairProcedure (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico vai à lista de espera dos clientes para saber qual a viatura
     * que vai arranjar.<br>
     */
    @Override
    public void startRepairProcedure() {
        Message inMessage, outMessage;

        int mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();
        //criar mensagem
        outMessage = new Message(MessageType.REPAIR_AREA_START_REPAIR_PROCEDURE_REQ, mechanicID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_START_REPAIR_PROCEDURE_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
    }

    /**
     * Operação getRequiredPart (chamada pelo {@link Mechanic})<br>
     * <p>
     * O Mecânico irá verificar se a peça pretendida para o arranjo da viatura
     * está disponível.<br>
     *
     * @param partId peça pretendida
     * @return indicação se a peça pretendida existe em stock
     */
    @Override
    public boolean getRequiredPart(int partId) {
        Message inMessage, outMessage;
        boolean hasPart = false;
        int mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();
        int currentCarFixingID = ((Mechanic)Thread.currentThread()).getCurrentCarFixingId();
        //criar mensagem
        outMessage = new Message(MessageType.REPAIR_AREA_GET_REQUIRED_PART_REQ, mechanicID, partId, currentCarFixingID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_GET_REQUIRED_PART_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
        ((Mechanic)Thread.currentThread()).setCurrentCarFixingId(inMessage.getIntegerParam1());

        hasPart = inMessage.getBooleanParam1();
        return hasPart;
    }

    /**
     * Operação partAvailable (chamada pelo {@link Mechanic})<br>
     * <p>
     * Obtém a peça para proceder ao arranjo.<br>
     *
     * @param partId a peça a obter
     */
    @Override
    public void partAvailable(int partId) {
        Message inMessage, outMessage;

        int mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();

        //criar mensagem
        outMessage = new Message(MessageType.REPAIR_AREA_PART_AVAILABLE_REQ, mechanicID, partId);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_PART_AVAILABLE_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //set customer state
        ((Mechanic)Thread.currentThread()).setState(inMessage.getMechanicState());
    }

    /**
     * Operação resumeRepairProcedure (chamada pelo {@link Mechanic})<br>
     * <p>
     * Recomeço da reparação, agora que o Mecânico tem a peça pretendida para
     * substituição.<br>
     */
    @Override
    public void resumeRepairProcedure() {
        Message inMessage, outMessage;

        int mechanicID = ((Mechanic)Thread.currentThread()).getMechanicId();

        //criar mensagem
        outMessage = new Message(MessageType.REPAIR_AREA_RESUME_REPAIR_PROCEDURE_REQ, mechanicID);
        //enviar e receber mensagem de resposta
        inMessage = communicationWithServer(outMessage);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.REPAIR_AREA_RESUME_REPAIR_PROCEDURE_RESP)
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

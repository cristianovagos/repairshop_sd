package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import entities.Manager;
import genclass.GenericIO;

/**
 * Classe SupplierSite (ligação com a SupplierSite)<br>
 *
 * Esta classe é responsável pela comunicação com o servidor do serviço do Fornecedor,
 * uma região partilhada do problema, feita através de passagem de mensagens, atuando
 * como um Stub para a classe real, sendo que são implementados os métodos do serviço
 * propriamente dito, através da sua interface.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class SupplierSite implements ISupplierSite {

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
    public SupplierSite(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação goToSupplier (chamada pelo {@link Manager}
     * <p>
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a {@link IRepairArea} de peças, necessárias para a reparação das viaturas.
     *
     * @return carrinho de compras do Gerente
     */
    @Override
    public int[] goToSupplier() {
        Message inMessage;      //input
        int [] parts;
        //Message to Send
        //CREATE MESSAGE
        Message messageToSend = new Message(MessageType.SUPPLIER_SITE_GO_TO_SUPPLIER_REQ);

        //wait to receive message
        inMessage = communicationWithServer(messageToSend);

        //check if received message is valid
        if(inMessage.getMessageType() != MessageType.SUPPLIER_SITE_GO_TO_SUPPLIER_RESP)
        {
            GenericIO.writelnString ("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //retrieve state, and True/False from the message
        ((Manager) Thread.currentThread()).setState( inMessage.getManagerState());
        parts =  inMessage.getIntegerArrayParam();

        return parts;
    }

    /**
     * Comunicação com o servidor do SupplierSite.
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
        fromServer = (Message) com.readObject();

        //close communications
        com.close();

        //return object
        return fromServer;
    }
}

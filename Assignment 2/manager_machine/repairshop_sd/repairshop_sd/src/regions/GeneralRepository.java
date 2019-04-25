package regions;

import comm.ClientCom;
import comm.Message;
import comm.MessageType;
import genclass.GenericIO;
import model.ManagerState;

/**
 * Classe GeneralRepository (Repositório Geral)<br>
 *
 * Esta classe é reponsável pela criação e posterior atualização de um ficheiro
 * de logging que irá permitir visualizar os diferentes estados e valores
 * decorrentes da execução do problema.<br>
 *
 * Assim que necessário e pedido pelas diferentes entidades e regiões partilhadas
 * de dados deste problema, será acrescentado ao ficheiro de logging os valores
 * mais recentes para posterior acompanhamento, que serão atualizados nas variáveis
 * e estruturas de dados que nada mais são de que "fotocópias" dos dados que estão
 * a ser alterados e atualizados nas diferentes entidades ativas e passivas do problema.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class GeneralRepository implements  IGeneralRepository{


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
    public GeneralRepository(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação setManagerState<br>
     * <p>
     * Altera o estado do Manager<br>
     *
     * @param state estado do Manager
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setManagerState(ManagerState state, boolean print) {
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
        fromUser = new Message(MessageType.REPOSITORY_SET_MANAGER_STATE_REQ, state, print);

        //Send Message
        com.writeObject(fromUser);

        //receive message
        fromServer = (Message)com.readObject();

        if ((fromServer.getMessageType () != MessageType.REPOSITORY_SET_MANAGER_STATE_RESP))
        { GenericIO.writelnString ("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            GenericIO.writelnString (fromServer.toString ());
            System.exit (1);
        }

        //close communications
        com.close();
    }

}

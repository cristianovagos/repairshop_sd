import genclass.GenericIO;
import repository.GeneralRepository;
import service.proxy.ClientProxy;
import service.Lounge;
import service.LoungeInterface;
import utils.Constants;
import comm.ServerCom;

/**
 * Servidor do Lounge <br>
 *
 * Classe responsável pela criação do Servidor do Lounge, uma das regiões partilhadas
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ServerLounge {

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        ServerCom scon, sconi;
        Lounge lounge;
        LoungeInterface loungeInter;
        ClientProxy cliProxy;
        GeneralRepository repository = new GeneralRepository(Constants.REPOSITORY_SERVER_HOST, Constants.REPOSITORY_SERVER_PORT);

        scon = new ServerCom(Constants.LOUNGE_SERVER_PORT_NUMBER);         // criação do canal de escuta e sua associação
        scon.start();                                        // com o endereço público
        lounge = new Lounge(Constants.NUM_CUSTOMERS, Constants.NUM_REPLACEMENT_CARS, repository); // activação do serviço
        loungeInter = new LoungeInterface(lounge);          // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, loungeInter);   // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}

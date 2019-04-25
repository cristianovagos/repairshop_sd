import genclass.GenericIO;
import service.GeneralRepository;
import service.proxy.ClientProxy;
import service.GeneralRepositoryInterface;
import utils.Constants;
import comm.ServerCom;

/**
 * Servidor do GeneralRepository<br>
 *
 * Classe responsável pela criação do Servidor do GeneralRepository, uma das regiões partilhadas
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ServerGeneralRepository {

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        ServerCom scon, sconi;
        GeneralRepository generalRepository;
        GeneralRepositoryInterface generalRepositoryInter;
        ClientProxy cliProxy;

        scon = new ServerCom(Constants.GENERAL_REPOSITORY_SERVER_PORT_NUMBER);         // criação do canal de escuta e sua associação
        scon.start();                                                                  // com o endereço público
        generalRepository = new GeneralRepository(Constants.FILE_NAME, Constants.NUM_CUSTOMERS, Constants.NUM_MECHANICS,
                Constants.NUM_PARTS, Constants.NUM_REPLACEMENT_CARS);                  // activação do serviço
        generalRepository.printInitialState();                                         // impressao da primeira linha no ficheiro de logging
        generalRepositoryInter = new GeneralRepositoryInterface(generalRepository);    // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                                                     // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, generalRepositoryInter);                 // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}

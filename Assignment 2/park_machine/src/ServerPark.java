import genclass.GenericIO;
import repository.GeneralRepository;
import service.proxy.ClientProxy;
import service.ParkInterface;
import utils.Constants;
import comm.ServerCom;

/**
 * Servidor do Lounge <br>
 *
 * Classe responsável pela criação do Servidor do RepairArea, uma das regiões partilhadas
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ServerPark {

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        ServerCom scon, sconi;
        RepairArea repairArea;
        ParkInterface repairAreaInter;
        ClientProxy cliProxy;
        GeneralRepository repository = new GeneralRepository(Constants.REPOSITORY_SERVER_HOST, Constants.REPOSITORY_SERVER_PORT);

        scon = new ServerCom(Constants.REPAIR_AREA_SERVER_PORT_NUMBER);         // criação do canal de escuta e sua associação
        scon.start();                                        // com o endereço público
        repairArea = new RepairArea(Constants.NUM_CUSTOMERS, Constants.NUM_PARTS, repository); // activação do serviço
        repairAreaInter = new ParkInterface(repairArea);          // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, repairAreaInter);   // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}

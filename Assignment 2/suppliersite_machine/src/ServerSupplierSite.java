import genclass.GenericIO;
import repository.GeneralRepository;
import service.SupplierSite;
import service.proxy.ClientProxy;
import service.SupplierSiteInterface;
import utils.Constants;
import comm.ServerCom;

/**
 * Servidor do SupplierSite <br>
 *
 * Classe responsável pela criação do Servidor do SupplierSite, uma das regiões partilhadas
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ServerSupplierSite {

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        ServerCom scon, sconi;
        SupplierSite supplierSite;
        SupplierSiteInterface supplierSiteInter;
        ClientProxy cliProxy;
        GeneralRepository repository = new GeneralRepository(Constants.REPOSITORY_SERVER_HOST, Constants.REPOSITORY_SERVER_PORT);

        scon = new ServerCom(Constants.SUPPLIER_SITE_SERVER_PORT_NUMBER);         // criação do canal de escuta e sua associação
        scon.start();                                                             // com o endereço público
        supplierSite = new SupplierSite(Constants.NUM_PARTS, Constants.NUM_MAX_PARTS, repository); // activação do serviço
        supplierSiteInter = new SupplierSiteInterface(supplierSite);              // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                                                // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, supplierSiteInter);                 // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}

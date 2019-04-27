import entities.Manager;
import genclass.GenericIO;
import regions.*;
import utils.Constants;

/**
 * Cliente do Manager<br>
 *
 * Classe responsável pela criação do Cliente do {@link Manager}, uma das entidades
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ClientManager {

    /**
     * Operação main<br>
     *
     * Aqui será inicializada a simulação do problema.
     *
     * @param args argumentos da função (não usados)
     */
    public static void main(String[] args) {

        Manager manager;

        /*Criar Stubs das regioes partilhadas*/
        //Criar General Repository
        GeneralRepository generalRepository = new GeneralRepository(Constants.REPOSITORY_SERVER_HOST, Constants.REPOSITORY_SERVER_PORT);
        //Criar Lounge Area
        Lounge lounge = new Lounge(Constants.LOUNGE_SERVER_HOST, Constants.LOUNGE_SERVER_PORT);
        //Criar Repair Area
        RepairArea repairArea = new RepairArea(Constants.REPAIR_SERVER_HOST, Constants.REPAIR_AREA_SERVER_PORT_NUMBER);
        //Criar Outside World
        OutsideWorld outsideWorld= new OutsideWorld(Constants.OUTSIDE_WORLD_HOST, Constants.OUTSIDE_WORLD_PORT);
        //Criar Supplier Area
        SupplierSite supplierSite = new SupplierSite(Constants.SUPPLIER_SITE_HOST, Constants.SUPPLIER_SITE_PORT);

        /*Criar thread do manager*/
        manager = new Manager(
                Constants.NUM_PARTS,
                generalRepository,
                lounge,
                repairArea,
                outsideWorld,
                supplierSite
        );

        //Iniciar a thread
        manager.start();

        //Aguarda pelo fim da simulação
        while (manager.isAlive()) {
            manager.interrupt();
            Thread.yield();
        }
        try {
            manager.join();
        } catch (InterruptedException e) { }
        GenericIO.writelnString("O gerente terminou.");
        GenericIO.writelnString();
    }
}

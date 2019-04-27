import entities.Customer;
import genclass.GenericIO;
import regions.*;
import utils.Constants;

/**
 * Cliente do Cliente<br>
 *
 * Classe responsável pela criação do Cliente do {@link Customer}, uma das entidades
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ClientCustomer {

    /**
     * Operação main<br>
     *
     * Aqui será inicializada a simulação do problema.
     *
     * @param args argumentos da função (não usados)
     */
    public static void main(String[] args) {

        Customer[] customers = new Customer[ Constants.NUM_CUSTOMERS];

        /*Criar Stubs das regioes partilhadas*/
        //Criar General Repository
        GeneralRepository generalRepository = new GeneralRepository(Constants.REPOSITORY_SERVER_HOST, Constants.REPOSITORY_SERVER_PORT);
        //Criar Lounge Area
        Lounge lounge = new Lounge(Constants.LOUNGE_SERVER_HOST, Constants.LOUNGE_SERVER_PORT);
        //Criar Park Area
        Park park = new Park(Constants.PARK_SERVER_HOST, Constants.PARK_SERVER_PORT);
        //Criar OutsideWorld
        OutsideWorld outsideWorld = new OutsideWorld(Constants.OUTSIDE_WORLD_HOST, Constants.OUTSIDE_WORLD_PORT);

        /*Criar threads dos clientes */
        for (int i = 0; i < Constants.NUM_CUSTOMERS; i++) {
            customers[i] = new Customer(
                    i,
                    generalRepository,
                    lounge,
                    park,
                    outsideWorld
            );
        }

        //Iniciar as threads
        for (int i = 0; i < Constants.NUM_CUSTOMERS; i++) {
            customers[i].start();
        }
        /* aguardar o fim da simulação */

        for (int i = 0; i < Constants.NUM_CUSTOMERS; i++) {
            try {
                customers[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString("O cliente " + i + " terminou.");
        }
        GenericIO.writelnString();
    }

}

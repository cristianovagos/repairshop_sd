import entities.Mechanic;
import genclass.GenericIO;
import regions.*;
import utils.Constants;

/**
 * Cliente do Mechanic<br>
 *
 * Classe responsável pela criação do Cliente do {@link Mechanic}, uma das entidades
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ClientMechanic {

    /**
     * Operação main<br>
     *
     * Aqui será inicializada a simulação do problema.
     *
     * @param args argumentos da função (não usados)
     */
    public static void main(String[] args) {

        Mechanic[] mechanics = new Mechanic[Constants.NUM_MECHANICS];

        /*Criar Stubs das regioes partilhadas*/
        //Criar General Repository
        GeneralRepository generalRepository = new GeneralRepository(Constants.REPOSITORY_SERVER_HOST, Constants.REPOSITORY_SERVER_PORT);
        //Criar Lounge Area
        Lounge lounge = new Lounge(Constants.LOUNGE_SERVER_HOST, Constants.LOUNGE_SERVER_PORT);
        //Criar Repair Area
        RepairArea repairArea = new RepairArea(Constants.REPAIR_SERVER_HOST, Constants.REPAIR_AREA_SERVER_PORT_NUMBER);
        //Criar Park Area
        Park park = new Park(Constants.PARK_SERVER_HOST, Constants.PARK_SERVER_PORT);

        /*Criar threads dos mecanicos*/
        for(int i = 0; i < Constants.NUM_MECHANICS; i++)
        {
            mechanics[i] = new Mechanic(
                    i,
                    generalRepository,
                    repairArea,
                    park,
                    lounge
            );
        }

        //Iniciar as threads
        for(int i = 0; i < Constants.NUM_MECHANICS; i++)
            mechanics[i].start();

        //Aguarda pelo fim da simulação
        for(int i = 0; i < Constants.NUM_MECHANICS; i++) {
            while (mechanics[i].isAlive ()) {
                Thread.yield ();
            }
            try {
                mechanics[i].join();
            }
            catch (InterruptedException e) {}
            GenericIO.writelnString("O mecânico " + i + " terminou.");
        }
        GenericIO.writelnString();
    }
}

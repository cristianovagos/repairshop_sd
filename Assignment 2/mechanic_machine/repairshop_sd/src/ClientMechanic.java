import entities.Mechanic;
import genclass.GenericIO;
import regions.*;
import utils.Constants;

/**
 * Classe RepairShop (Oficina)<br>
 *
 * Este tipo de dados simula o problema descrito no âmbito deste projeto,
 * que é as actividades de uma Oficina de Reparação de Automóveis.<br>
 * Aqui foi implementada uma solução concorrente baseada em monitores como
 * elementos de sincronização entre as entidades ativas (Cliente ({@link Customer}),
 * Mecânico ({@link Mechanic}) e Gerente ({@link Manager})) e as entidades passivas
 * (Mundo Exterior ({@link OutsideWorld}), Recepção ({@link Lounge}), Parque de
 * Estacionamento ({@link Park}), Área de Reparação ({@link RepairArea}) e Fornecedor
 * ({@link SupplierSite})).<br>
 * Durante a execução da simulação, todas as entidades passivas irão atualizar um
 * Repositório Geral de Dados ({@link GeneralRepository}), que irá escrever num ficheiro
 * de logging para um acompanhamento de todos os estados e transições do problema.
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

        Mechanic [] mechanics = new Mechanic[Constants.NUM_MECHANICS];

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
                mechanics[i].sendInterrupt();
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

import entities.Customer;
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

        /*Criar threads dos mecanicos*/

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
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O cliente " + i + " terminou.");
        }
        GenericIO.writelnString();
    }

}

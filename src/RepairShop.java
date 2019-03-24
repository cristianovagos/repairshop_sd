import entities.Customer;
import entities.Manager;
import entities.Mechanic;
import genclass.FileOp;
import genclass.GenericIO;
import regions.*;

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
public class RepairShop {

    /**
     * Flag constante que indica se pretende executar a simulação em modo DEBUG,
     * o que irá executar a simulação do problema sem intervenção do utilizador,
     * escrevendo num ficheiro de logging com o nome "log-debug.txt" por defeito.
     */
    private static final boolean DEBUG_MODE = true;

    /**
     * Constante indicando o número de clientes a serem instanciados
     * @see Customer
     */
    private static final int N_CUSTOMERS = 30;

    /**
     * Constante indicando o número de mecânicos a serem instanciados
     * @see Mechanic
     */
    private static final int N_MECHANICS = 2;

    /**
     * Constante indicando o número de tipos de peças distintos do problema
     */
    private static final int N_PARTS = 3;

    /**
     * Constante indicando o número total de viaturas de substituição
     */
    private static final int N_REPLACEMENT_CARS = 3;

    /**
     * Constante indicando o número máximo de peças a serem obtidas pelo
     * Gerente ({@link Manager}) no Fornecedor ({@link SupplierSite})
     */
    private static final int N_MAX_PARTS = 20;

    /**
     * Operação main<br>
     *
     * Aqui será inicializada a simulação do problema.
     *
     * @param args argumentos da função (não usados)
     */
    public static void main(String[] args) {
        String fileName;
        char opt;
        boolean success;

        GeneralRepository repository;
        OutsideWorld outsideWorld;
        Park park;
        Lounge lounge;
        RepairArea repairArea;
        SupplierSite supplierSite;

        Manager manager;
        Customer[] customers = new Customer[N_CUSTOMERS];
        Mechanic[] mechanics = new Mechanic[N_MECHANICS];

        /* inicialização */

        GenericIO.writeString ("\n" + "      Repair Shop Activities ");
        if(!DEBUG_MODE) {
            do {
                GenericIO.writeString("Nome do ficheiro de armazenamento da simulação? ");
                fileName = GenericIO.readlnString();
                if (FileOp.exists(".", fileName)) {
                    do {
                        GenericIO.writeString("Já existe um directório/ficheiro com esse nome. Quer apagá-lo (s - sim; n - não)? ");
                        opt = GenericIO.readlnChar();
                    } while ((opt != 's') && (opt != 'n'));
                    if (opt == 's')
                        success = true;
                    else
                        success = false;
                } else
                    success = true;
            } while (!success);
        } else {
            fileName = "log-debug.txt";
            GenericIO.writelnString("(DEBUG MODE)\n");
        }

        repository = new GeneralRepository(fileName, N_CUSTOMERS, N_MECHANICS, N_PARTS, N_REPLACEMENT_CARS);

        park = new Park(N_CUSTOMERS, N_REPLACEMENT_CARS, N_PARTS, repository);
        outsideWorld = new OutsideWorld(N_CUSTOMERS, repository);
        lounge = new Lounge(N_CUSTOMERS, N_REPLACEMENT_CARS, repository);
        repairArea = new RepairArea(N_CUSTOMERS, N_PARTS, repository);
        supplierSite = new SupplierSite(N_PARTS, N_MAX_PARTS, repository);

        manager = new Manager(N_PARTS, repository, lounge, repairArea, outsideWorld, supplierSite);
        for (int i = 0; i < N_CUSTOMERS; i++)
            customers[i] = new Customer(i, repository, lounge, park, outsideWorld);
        for (int i = 0; i < N_MECHANICS; i++)
            mechanics[i] = new Mechanic(i, repository, repairArea, park, lounge);


        /* arranque da simulação */

        manager.start();
        for (int i = 0; i < N_MECHANICS; i++)
            mechanics[i].start();
        for (int i = 0; i < N_CUSTOMERS; i++)
            customers[i].start();


        /* aguardar o fim da simulação */

        for (int i = 0; i < N_CUSTOMERS; i++) {
            try {
                customers[i].join();
            } catch (InterruptedException e) { }
            GenericIO.writelnString("O cliente " + i + " terminou.");
        }
        GenericIO.writelnString();

        for (int i = 0; i < N_MECHANICS; i++) {
            while (mechanics[i].isAlive()) {
                mechanics[i].interrupt();
                Thread.yield();
            }
            try {
                mechanics[i].join();
            } catch (InterruptedException e) { }
            GenericIO.writelnString("O mecânico " + i + " terminou.");
        }
        GenericIO.writelnString();

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

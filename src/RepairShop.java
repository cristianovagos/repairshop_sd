import entities.Customer;
import entities.Manager;
import entities.Mechanic;
import genclass.FileOp;
import genclass.GenericIO;
import regions.*;

/**
 * Classe RepairShop (Oficina)
 *
 * Este tipo de dados simula o problema descrito no âmbito deste projeto,
 * que é as actividades de uma Oficina de Reparação de Automóveis.
 * Aqui foi implementada uma solução concorrente baseada em monitores como
 * elementos de sincronização entre as entidades ativas (Cliente {@link Customer},
 * Mecânico {@link Mechanic} e Gerente {@link Manager}) e as entidades passivas
 * (Mundo Exterior {@link OutsideWorld},
 *
 */
public class RepairShop {

    private static final boolean DEBUG_MODE = true;
    private static final int N_CUSTOMERS = 30;
    private static final int N_MECHANICS = 2;
    private static final int N_PARTS = 3;
    private static final int N_REPLACEMENT_CARS = 3;
    private static final int N_MAX_PARTS = 20;

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
            try {
                mechanics[i].join();
            } catch (InterruptedException e) { }
            GenericIO.writelnString("O mecânico " + i + " terminou.");
        }
        GenericIO.writelnString();

        while (manager.isAlive()) {
            manager.interrupt();
        }
        try {
            manager.join();
        } catch (InterruptedException e) { }
        GenericIO.writelnString("O gerente terminou.");
        GenericIO.writelnString();
    }
}

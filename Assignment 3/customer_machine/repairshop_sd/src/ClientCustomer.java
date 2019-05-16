import entities.Customer;
import genclass.GenericIO;
import interfaces.IGeneralRepository;
import interfaces.ILounge;
import interfaces.IOutsideWorld;
import interfaces.IPark;
import utils.Constants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        GenericIO.writelnString("Security Manager was installed!");

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(Constants.RMI_REGISTRY_HOSTNAME, Constants.RMI_REGISTRY_PORT);
        }
        catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace ();
            System.exit (1);
        }

        IGeneralRepository repositoryInterface = null;
        try {
            repositoryInterface = (IGeneralRepository) registry.lookup(Constants.RMI_REGISTRY_REPOSITORY_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("GeneralRepository lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("GeneralRepository not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        ILounge loungeInterface = null;
        try {
            loungeInterface = (ILounge) registry.lookup(Constants.RMI_REGISTRY_LOUNGE_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("Lounge lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Lounge not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        IPark parkInterface = null;
        try {
            parkInterface = (IPark) registry.lookup(Constants.RMI_REGISTRY_PARK_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("Park lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Park not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        IOutsideWorld outsideWorldInterface = null;
        try {
            outsideWorldInterface = (IOutsideWorld) registry.lookup(Constants.RMI_REGISTRY_OUTSIDE_WORLD_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("OutsideWorld lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("OutsideWorld not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        Customer[] customers = new Customer[Constants.NUM_CUSTOMERS];

        /*Criar threads dos clientes */
        for (int i = 0; i < Constants.NUM_CUSTOMERS; i++) {
            try {
                customers[i] = new Customer(
                        i,
                        repositoryInterface,
                        loungeInterface,
                        parkInterface,
                        outsideWorldInterface
                );
            } catch (RemoteException e) {
                GenericIO.writelnString("Remote exception thrown at customer #" + i + ": " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
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

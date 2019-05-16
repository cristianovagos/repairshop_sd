import entities.Mechanic;
import genclass.GenericIO;
import interfaces.IGeneralRepository;
import interfaces.ILounge;
import interfaces.IPark;
import interfaces.IRepairArea;
import utils.Constants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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

        IRepairArea repairAreaInterface = null;
        try {
            repairAreaInterface = (IRepairArea) registry.lookup(Constants.RMI_REGISTRY_REPAIR_AREA_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("OutsideWorld lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("OutsideWorld not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        Mechanic[] mechanics = new Mechanic[Constants.NUM_MECHANICS];

        /*Criar threads dos mecanicos*/
        for (int i = 0; i < Constants.NUM_MECHANICS; i++) {
            try {
                mechanics[i] = new Mechanic(
                        i,
                        repositoryInterface,
                        repairAreaInterface,
                        parkInterface,
                        loungeInterface
                );
            } catch (RemoteException e) {
                GenericIO.writelnString("Remote exception thrown at mechanic #" + i + ": " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }

        //Iniciar as threads
        for (int i = 0; i < Constants.NUM_MECHANICS; i++)
            mechanics[i].start();

        //Aguarda pelo fim da simulação
        for (int i = 0; i < Constants.NUM_MECHANICS; i++) {
            while (mechanics[i].isAlive ()) {
                // todo - o prof vai explicar na proxima aula
//                mechanics[i].sendInterrupt();       // envia pedido de interrupção para a RepairArea, onde o Mecânico
                Thread.yield ();                    // aguarda mais trabalho para fazer. Só termina quando o Manager dá ordem
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

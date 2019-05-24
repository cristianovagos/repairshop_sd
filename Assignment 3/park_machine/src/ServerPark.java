import genclass.GenericIO;
import interfaces.IGeneralRepository;
import interfaces.Register;
import service.Park;
import interfaces.IPark;
import utils.Constants;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Servidor do Park <br>
 *
 * Classe responsável pela criação do Servidor do Park, uma das regiões partilhadas
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ServerPark {

    /**
     * Programa principal.
     */
    public static void main(String[] args) {
        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        GenericIO.writelnString("Security Manager was installed!");

        IGeneralRepository repositoryInterface = null;
        try {
            Registry registry = LocateRegistry.getRegistry(Constants.RMI_REGISTRY_HOSTNAME, Constants.RMI_REGISTRY_PORT);
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

        Park park = new Park(Constants.NUM_CUSTOMERS, Constants.NUM_REPLACEMENT_CARS, Constants.NUM_PARTS, repositoryInterface);
        IPark parkInterface = null;

        try {
            parkInterface = (IPark) UnicastRemoteObject
                    .exportObject(park, Constants.PARK_SERVER_PORT_NUMBER);
        } catch (RemoteException e) {
            GenericIO.writelnString("Park stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "Park";
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(Constants.RMI_REGISTRY_HOSTNAME, Constants.RMI_REGISTRY_PORT);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString ("RMI registry was created!");

        try {
            reg = (Register) registry.lookup (nameEntryBase);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, parkInterface);
        } catch (RemoteException e) {
            GenericIO.writelnString("Park registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString("Park already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Park object was registered!");
    }
}

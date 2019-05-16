import genclass.GenericIO;
import service.GeneralRepository;
import service.Register;
import service.IGeneralRepository;
import utils.Constants;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Servidor do GeneralRepository<br>
 *
 * Classe responsável pela criação do Servidor do GeneralRepository, uma das regiões partilhadas
 * do problema Repair Shop Activities. Comunicação baseada em Java RMI.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ServerGeneralRepository {

    /**
     * Programa principal.
     */
    public static void main(String[] args) {

        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        GenericIO.writelnString("Security Manager was installed!");

        GeneralRepository repository = new GeneralRepository(Constants.FILE_NAME, Constants.NUM_CUSTOMERS,
                Constants.NUM_MECHANICS, Constants.NUM_PARTS, Constants.NUM_REPLACEMENT_CARS);
        IGeneralRepository generalRepositoryInter = null;

        try {
            generalRepositoryInter = (IGeneralRepository) UnicastRemoteObject
                    .exportObject((Remote) repository, Constants.GENERAL_REPOSITORY_SERVER_PORT_NUMBER);
        } catch (RemoteException e) {
            GenericIO.writelnString("GeneralRepository stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "GeneralRepository";
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
            reg.bind(nameEntryObject, generalRepositoryInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("GeneralRepository registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString("GeneralRepository already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("GeneralRepository object was registered!");
    }
}

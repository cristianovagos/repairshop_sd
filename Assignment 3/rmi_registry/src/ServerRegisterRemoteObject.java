import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.AccessException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.*;
import genclass.GenericIO;
import interfaces.Register;
import utils.Constants;

/**
 *  This data type instantiates and registers a remote object that enables the registration of other remote objects
 *  located in the same or other processing nodes in the local registry service.
 *  Communication is based in Java RMI.
 */

public class ServerRegisterRemoteObject
{
  /**
   *  Main task.
   */

   public static void main(String[] args)
   {
    /* create and install the security manager */

     if (System.getSecurityManager () == null)
        System.setSecurityManager (new SecurityManager ());
     GenericIO.writelnString ("Security manager was installed!");

    /* instantiate a registration remote object and generate a stub for it */

     RegisterRemoteObject regEngine = new RegisterRemoteObject (Constants.RMI_REGISTRY_HOSTNAME, Constants.RMI_REGISTRY_PORT);
     Register regEngineStub = null;
     int listeningPort = 22109;                            /* it should be set accordingly in each case */

     try
     { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, listeningPort);
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("RegisterRemoteObject stub generation exception: " + e.getMessage ());
       System.exit (1);
     }
     GenericIO.writelnString ("Stub was generated!");

    /* register it with the local registry service */

     String nameEntry = "RegisterHandler";
     Registry registry = null;

     try
     { registry = LocateRegistry.getRegistry (Constants.RMI_REGISTRY_HOSTNAME, Constants.RMI_REGISTRY_PORT);
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
       System.exit (1);
     }
     GenericIO.writelnString ("RMI registry was created!");

     try
     { registry.rebind (nameEntry, regEngineStub);
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
       System.exit (1);
     }
     GenericIO.writelnString ("RegisterRemoteObject object was registered!");
   }
}

import entities.Manager;
import genclass.GenericIO;
import interfaces.*;
import utils.Constants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Cliente do Manager<br>
 *
 * Classe responsável pela criação do Cliente do {@link Manager}, uma das entidades
 * do problema Repair Shop Activities.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ClientManager {

    /**
     * Operação main<br>
     *
     * Aqui será inicializada a simulação do problema.
     *
     * @param args argumentos da função (não usados)
     */
    public static void main(String[] args) {

        /* get location of the RMI registry service */
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

        ISupplierSite supplierSiteInterface = null;
        try {
            supplierSiteInterface = (ISupplierSite) registry.lookup(Constants.RMI_REGISTRY_SUPPLIER_SITE_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("SupplierSite lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("SupplierSite not bound to registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        IRepairArea repairAreaInterface = null;
        try {
            repairAreaInterface = (IRepairArea) registry.lookup(Constants.RMI_REGISTRY_REPAIR_AREA_NAME);
        } catch (RemoteException e) {
            GenericIO.writelnString("RepairArea lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("RepairArea not bound to registry: " + e.getMessage());
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

        Manager manager = null;

        /*Criar thread do manager*/
        try {
            manager = new Manager(
                    Constants.NUM_PARTS,
                    repositoryInterface,
                    loungeInterface,
                    repairAreaInterface,
                    outsideWorldInterface,
                    supplierSiteInterface
            );
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception thrown at Manager: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        //Iniciar a thread
        manager.start();

        //Aguarda pelo fim da simulação
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

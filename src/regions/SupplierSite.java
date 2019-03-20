package regions;

import entities.Manager;
import entities.ManagerState;

public class SupplierSite {

    /**
     * Type of parts available in the SupplierSite.
     * The same number of parts described in the problem.
     */
    private int typeOfPartsAvailable;

    /**
     * Maximum number of parts to be filled in the shopping cart
     */
    private int numMaxOfParts;

    /**
     * Number of Parts sold by the Supplier Site
     */
    private int numOfPartsSold;

    /**
     * Reference to the {@link GeneralRepository}
     */
    private GeneralRepository repository;

    public SupplierSite (int nParts, int nMaxParts, GeneralRepository repo) {
        this.repository = repo;
        this.typeOfPartsAvailable = nParts;
        this.numMaxOfParts = nMaxParts;
        this.numOfPartsSold = 0;
    }

    /**
     * Operação goToSupplier (chamada pelo {@link entities.Manager}
     *
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a {@link RepairArea} de peças, necessárias para a reparação das viaturas.
     */
    public synchronized int[] goToSupplier() {
        // Update Manager state
        ((Manager) Thread.currentThread()).setState(ManagerState.GETTING_NEW_PARTS);

        /* Fill up the Manager shopping cart with parts.
           In our case, we chose to always fill it up with a fixed number for each part.
         */
        int[] shoppingCart = new int[typeOfPartsAvailable];
        for (int part : shoppingCart) {
            shoppingCart[part] = numMaxOfParts;
            numOfPartsSold += numMaxOfParts;
        }

        // todo update repository

        return shoppingCart;
    }

}

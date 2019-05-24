package service;

import interfaces.IGeneralRepository;
import interfaces.ISupplierSite;
import model.ManagerState;

import java.rmi.RemoteException;

/**
 * Classe SupplierSite (Fornecedor)<br>
 *
 * Esta classe é responsável pela criação do Fornecedor, uma das entidades
 * passivas do problema.<br>
 *
 * O Fornecedor é o local onde o Gerente irá obter novas peças
 * para serem posteriormente disponibilizadas na Área de Reparação,
 * para que os Mecânicos possam continuar a reparação das viaturas.<br>
 *
 * A abordagem que decidimos adotar neste caso foi ter um máximo para cada tipo de
 * peças que serão todas compradas assim que o Gerente venha comprar caso um tipo de
 * peças se esgote na Área de Reparação. Assim, o Gerente quando vem comprar um tipo
 * de peças leva no seu cesto de compras um número "máximo" de peças de todos os tipos.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class SupplierSite implements ISupplierSite {

    /**
     * Type of parts available in the SupplierSite.
     * The same number of parts described in the problem.
     */
    private int typeOfPartsAvailable;

    /**
     * Número máximo de peças unitárias a serem
     * incluídas no carrinho de compras
     */
    private int numMaxOfParts;

    /**
     * Número de peças vendidas
     */
    private int[] numOfPartsSold;

    /**
     * Referência para o Repositório
     * @see GeneralRepository
     */
    private IGeneralRepository repository;

    /**
     * Construtor do Fornecedor (Supplier Site)
     *
     * Aqui será construído o objeto referente ao Fornecedor
     *
     * @param nParts número de tipos de peças
     * @param nMaxParts número máximo de peças unitárias a serem
     *                  incluídas no carrinho de compras
     * @param repo referência para o Repositório {@link GeneralRepository}
     */
    public SupplierSite (int nParts, int nMaxParts, IGeneralRepository repo) {
        this.repository = repo;
        this.typeOfPartsAvailable = nParts;
        this.numMaxOfParts = nMaxParts;
        this.numOfPartsSold = new int[nParts];

        for (int i = 0; i < numOfPartsSold.length; i++)
            numOfPartsSold[i] = 0;
    }

    /**
     * Operação goToSupplier (chamada pelo Manager)
     *
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a RepairArea de peças, necessárias para a reparação das viaturas.
     *
     * @return carrinho de compras do Gerente
     */
    public synchronized int[] goToSupplier() throws RemoteException {
        // Update Manager state
//        ((ClientProxy) Thread.currentThread()).setManagerState(ManagerState.GETTING_NEW_PARTS);
        repository.setManagerState(ManagerState.GETTING_NEW_PARTS, false);

        /* Fill up the Manager shopping cart with parts.
           In our case, we chose to always fill it up with a fixed number for each part.
         */
        int[] shoppingCart = new int[typeOfPartsAvailable];
        for (int i = 0; i < typeOfPartsAvailable; i++) {
            shoppingCart[i] = numMaxOfParts;
            numOfPartsSold[i] += numMaxOfParts;
        }

        // update interfaces
        repository.setSoldParts(numOfPartsSold);

        return shoppingCart;
    }
}

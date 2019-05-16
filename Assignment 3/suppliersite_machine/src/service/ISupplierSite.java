package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Este tipo de dados define o interface ao SupplierSite, uma das regiões partilhadas do problema Repair Shop Activities.
 * A comunicação é feita com Java RMI.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface ISupplierSite extends Remote {
    /**
     * Operação goToSupplier (chamada pelo Manager)
     *
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a RepairArea de peças, necessárias para a reparação das viaturas.
     *
     * @return carrinho de compras do Gerente
     */
    int[] goToSupplier() throws RemoteException;
}

package regions;

/**
 * Interface ISupplierSite (ligação ao Fornecedor)<br>
 *
 * Esta interface contém todos os métodos do serviço SupplierSite, que
 * serão invocados assim que necessário.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface ISupplierSite {

    /**
     * Operação goToSupplier (chamada pelo Manager)
     *
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a RepairArea de peças, necessárias para a reparação das viaturas.
     *
     * @return carrinho de compras do Gerente
     */
    int[] goToSupplier();
}

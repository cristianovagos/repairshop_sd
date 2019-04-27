package regions;

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

package regions;

import entities.Mechanic;
import entities.Manager;
import entities.ManagerState;

/**
 * Classe SupplierSite (Fornecedor)<br>
 *
 * Esta classe é responsável pela criação do Fornecedor, uma das entidades
 * passivas do problema.<br>
 *
 * O Fornecedor é o local onde o Gerente {@link Manager} irá obter novas peças
 * para serem posteriormente disponibilizadas na Área de Reparação {@link RepairArea},
 * para que os Mecânicos {@link Mechanic} possam continuar a reparação das viaturas.<br>
 *
 * A abordagem que decidimos adotar neste caso foi ter um máximo para cada tipo de
 * peças que serão todas compradas assim que o Gerente venha comprar caso um tipo de
 * peças se esgote na Área de Reparação. Assim, o Gerente quando vem comprar um tipo
 * de peças leva no seu cesto de compras um número "máximo" de peças de todos os tipos.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface SupplierSite {

    /**
     * Operação goToSupplier (chamada pelo {@link entities.Manager}
     *
     * O Manager irá dirigir-se ao fornecedor de peças, para comprar e posteriormente
     * reabastecer a {@link RepairArea} de peças, necessárias para a reparação das viaturas.
     *
     * @return carrinho de compras do Gerente
     */
    public int[] goToSupplier();
}

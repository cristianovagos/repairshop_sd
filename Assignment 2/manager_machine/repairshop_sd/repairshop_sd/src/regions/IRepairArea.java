package regions;

import entities.*;

/**
 * Classe RepairArea (Área de Reparação)<br>
 *
 * Esta classe é responsável pela criação da Área de Reparação, uma das
 * entidades passivas do problema.<br>
 *
 * A Área de Reparação é o local onde decorrem as reparações das viaturas,
 * por parte dos Mecânicos (Mechanic), sendo que é aqui onde eles irão
 * estar a maior parte do tempo, seja a reparar viaturas, seja à espera da
 * indicação do Gerente ({@link Manager}) de que há trabalho a fazer. Nesta área
 * existe um número de peças em stock para que os Mecânicos possam substituir
 * nas viaturas que necessitem de reparar, e caso estas porventura faltem, é
 * responsabilidade do Gerente obter mais peças e por sua vez restabelecer o
 * stock para que os Mecânicos voltem ao trabalho.<br>
 * Quando o Gerente dá o dia por terminado, todos os Mecânicos vão embora.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IRepairArea {

    /**
     * Operação markEndOfDay (chamada pelo {@link Manager})<br>
     *
     * Marca o encerramento do dia para o Mechanic.<br>
     */
    void markEndOfDay();

    /**
     * Operação storePart (chamada pelo {@link Manager})<br>
     *
     * O Manager irá guardar na Repair Area as peças que foi buscar à
     * SupplierSite, adicionar de novo os carros que tinham peças em
     * falta para reparação na lista de espera, e acordar o Mechanic.<br>
     *
     * @param newParts peças novas a serem incluídas no stock
     */
    void storePart(int[] newParts);

    /**
     * Operação registerService (chamada pelo {@link Manager})<br>
     *
     * O Manager irá registar um pedido de reparação da viatura do Customer,
     * e notificar os Mechanic de que existe um serviço disponível.<br>
     *
     * @param customerId id do cliente
     */
    void registerService(int customerId);
}

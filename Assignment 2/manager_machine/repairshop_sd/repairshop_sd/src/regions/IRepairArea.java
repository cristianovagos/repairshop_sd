package regions;

import entities.*;

/**
 * Interface IRepairArea (ligação à Área de Reparação)<br>
 *
 * Esta interface contém todos os métodos do serviço RepairArea, que
 * serão invocados assim que necessário.<br>
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

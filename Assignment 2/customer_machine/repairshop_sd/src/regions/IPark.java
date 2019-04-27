package regions;

import entities.Customer;

/**
 * Interface IPark (ligação ao Parque de Estacionamento)<br>
 *
 * Esta interface contém todos os métodos do serviço Park, que
 * serão invocados assim que necessário.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IPark {
    /**
     * Operação goToRepairShop (chamada pelo {@link Customer})<br>
     *
     * O Cliente chega à RepairShop, e estaciona a sua viatura ou
     * a viatura de substituição que lhe foi atribuída no Park.<br>
     */
    void goToRepairShop();

    /**
     * Operação findCar (chamada pelo {@link Customer})<br>
     *
     * O Cliente recolhe a viatura de substituição do Park.<br>
     *
     * @param replacementCar viatura de substituição
     */
    void findCar(int replacementCar);

    /**
     * Operação collectCar (chamada pelo {@link Customer})<br>
     *
     * O Cliente recolhe a sua viatura própria do Park.<br>
     */
    void collectCar();
}

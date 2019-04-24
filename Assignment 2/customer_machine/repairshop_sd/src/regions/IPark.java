package regions;

import entities.Customer;

public interface IPark {

    /**
     * Operação goToRepairShop (chamada pelo {@link Customer})<br>
     *
     * O Cliente chega à RepairShop, e estaciona a sua viatura ou
     * a viatura de substituição que lhe foi atribuída no Park.<br>
     */
    public void goToRepairShop();

    /**
     * Operação findCar (chamada pelo {@link Customer})<br>
     *
     * O Cliente recolhe a viatura de substituição do Park.<br>
     *
     * @param replacementCar viatura de substituição
     */
    public void findCar(int replacementCar);

    /**
     * Operação collectCar (chamada pelo {@link Customer})<br>
     *
     * O Cliente recolhe a sua viatura própria do Park.<br>
     */
    public void collectCar();

}

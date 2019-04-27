package regions;

import entities.Mechanic;

public interface IPark {
    /**
     * Operação getVehicle (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico dirige-se ao Park para obter a viatura do Customer
     * e avalia qual a peça que deve substituir para a reparar.<br>
     *
     * @return a peça que necessita de substituição
     */
    int getVehicle();

    /**
     * Operação returnVehicle (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico devolve a viatura já reparada ao Park.<br>
     */
    void returnVehicle();
}

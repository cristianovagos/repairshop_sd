package regions;

import entities.Mechanic;

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

package regions;

import entities.Mechanic;

/**
 * Interface ILounge (ligação à Recepção)<br>
 *
 * Esta interface contém todos os métodos do serviço Lounge, que
 * serão invocados assim que necessário.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface ILounge {

    /* MECHANIC */

    /**
     * Operação letManagerKnow (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico informa o Gerente (Manager) de que não existem
     * peças para que este possa reparar uma viatura.<br>
     *
     * @param partRequired peça em falta
     */
    void letManagerKnow(int partRequired);

    /**
     * Operação repairConcluded (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico terminou de reparar uma viatura, e irá informar o Gerente
     * (Manager) que esta está pronta a ser levantada pelo seu proprietário,
     * o Cliente (Customer).<br>
     */
    void repairConcluded();
}

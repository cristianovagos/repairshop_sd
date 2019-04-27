package regions;

import entities.Mechanic;

public interface ILounge {

    /* MECHANIC */

    /**
     * Operação letManagerKnow (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico informa o Gerente ({@link Manager}) de que não existem
     * peças para que este possa reparar uma viatura.<br>
     *
     * @param partRequired peça em falta
     */
    void letManagerKnow(int partRequired);

    /**
     * Operação repairConcluded (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico terminou de reparar uma viatura, e irá informar o Gerente
     * ({@link Manager}) que esta está pronta a ser levantada pelo seu proprietário,
     * o Cliente ({@link Customer}).<br>
     */
    void repairConcluded();
}

package regions;

import model.MechanicState;

public interface IGeneralRepository {
    /**
     * Operação setMechanicState<br>
     *
     * Altera o estado de um mecânico em específico<br>
     *
     * @param index o índice do mecânico em questão
     * @param newState o estado novo do mecânico
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    public void setMechanicState(int index, MechanicState newState, boolean print);
}

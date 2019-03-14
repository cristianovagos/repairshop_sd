package regions;

import entities.CarState;
import entities.MechanicState;
import utils.MemFIFO;

import java.util.List;

public class RepairArea {

    /**
     * Repositório Geral de dados
     */
    private GeneralRepository repository;


    private MemFIFO customerFirstRepairQueue;
    private List<List<Integer>> customerMissingPartQueue;

    public RepairArea(GeneralRepository repo) {
        this.repository = repo;
        this.customerFirstRepairQueue = repo.getCustomerFirstRepairQueue();
        this.customerMissingPartQueue = repo.getCustomerMissingPartQueue();
    }

    /**
     * Operação startRepairProcedure (chamada pelo {@link entities.Mechanic})
     *
     */
    public synchronized void startRepairProcedure(int mechanicId) {
        int carId = (Integer) customerFirstRepairQueue.read();
        repository.setCarsState(carId, CarState.FIXING);
        repository.setMechanicsState(mechanicId, MechanicState.FIXING_THE_CAR);
        repository.setTask(mechanicId, carId);
    }

    /**
     * Operação getRequiredPart (chamada pelo {@link entities.Mechanic})
     *
     */
    public synchronized boolean getRequiredPart(int mechanicId, int partId) {
        repository.setMechanicsState(mechanicId, MechanicState.CHECKING_STOCK);

    }

    /**
     * Operação partAvailable (chamada pelo {@link entities.Mechanic})
     *
     */
    public synchronized boolean partAvailable(int partId) {
        // random sleep on Mechanic thread??
    }

    /**
     * Operação resumeRepairProcedure (chamada pelo {@link entities.Mechanic})
     *
     */
    public synchronized void resumeRepairProcedure(int mechanicId, int carId) {

    }

    /**
     * Operação storePart (chamada pelo {@link entities.Manager})
     *
     */
    public synchronized void storePart(int partId) {

    }
}

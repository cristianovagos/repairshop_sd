package entities;

import regions.Lounge;
import regions.Park;
import regions.RepairArea;

public class Mechanic extends Thread {

    private int mechanicId;
    private MechanicState state;
    private RepairArea repairArea;
    private Park park;
    private Lounge lounge;
    private int currentCarFixingId;

    public Mechanic(RepairArea repairArea, Park park, Lounge lounge) {
        this.state = MechanicState.WAITING_FOR_WORK;
        this.repairArea = repairArea;
        this.park = park;
        this.lounge = lounge;
        this.currentCarFixingId = -1;
    }

    @Override
    public void run() {
        while (repairArea.readThePaper()){
            repairArea.startRepairProcedure();
            int partRequired = park.getVehicle();

            boolean hasPart = repairArea.getRequiredPart(partRequired);
            if(!hasPart) {
                lounge.letManagerKnow(partRequired);
                continue;
            }

            repairArea.partAvailable(partRequired);
            repairArea.resumeRepairProcedure(1);
            fixIt();
            park.returnVehicle();
//            lounge.repairConcluded();
        }
    }

    /**
     * Operação setState
     *
     * Modifica o estado interno do Mecânico
     *
     * @param state o estado interno do Mecânico
     */
    public void setState(MechanicState state) {
        this.state = state;
    }

    /**
     * Operação getCurrentCarFixingId
     *
     * Marca a viatura que o Mecânico estiver a arranjar de momento,
     * o id da viatura caso esteja a arranjar uma viatura, -1 se nenhuma.
     *
     * @param carId o id da viatura (o mesmo que o do {@link Customer})
     */
    public void setCurrentCarFixingId(int carId) {
        this.currentCarFixingId = carId;
    }

    /**
     * Operação getCurrentCarFixingId
     *
     * Obtém a viatura que o Mecânico estiver a arranjar de momento.
     * Devolve o id da viatura caso esteja a arranjar uma viatura, -1 se nenhuma.
     *
     * @return o id da viatura (o mesmo que o do {@link Customer})
     */
    public int getCurrentCarFixingId() {
        return this.currentCarFixingId;
    }

    /**
     * Operação interna fixIt
     *
     * O Mecânico irá arranjar a viatura durante um período aleatório de tempo.
     */
    private void fixIt() {
        // update the state
        setState(MechanicState.FIXING_THE_CAR);

        try {
            sleep ((long) (1 + 10 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

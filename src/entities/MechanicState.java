package entities;

public enum MechanicState {
    WAITING_FOR_WORK ("WAI"),
    FIXING_THE_CAR ("FIX"),
    ALERTING_MANAGER ("AMN"),
    CHECKING_STOCK ("CHK");

    private final String state;

    MechanicState(String state) {
        this.state = state;
    }

    public String state() { return state; }
}

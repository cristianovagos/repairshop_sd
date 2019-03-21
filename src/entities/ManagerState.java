package entities;

public enum ManagerState {
    CHECKING_WHAT_TO_DO ("TODO"),
    ATTENDING_CUSTOMER ("ATTN"),
    POSTING_JOB ("POST"),
    ALERTING_CUSTOMER ("ALRT"),
    GETTING_NEW_PARTS ("GETT"),
    REPLENISH_STOCK ("RPLN");

    private final String state;

    ManagerState(String state) {
        this.state = state;
    }

    public String state() { return state; }
}

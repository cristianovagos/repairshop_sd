package entities;

public enum CustomerState {
    NORMAL_LIFE_WITH_CAR ("NLC"),
    NORMAL_LIFE_WITHOUT_CAR ("NLW"),
    PARK ("PRK"),
    RECEPTION_REPAIR ("RPR"),
    RECEPTION_PAYING ("PAY"),
    RECEPTION_COLLECT_CAR_KEY ("CKY"),
    RECEPTION_TALK_WITH_MANAGER ("TLK"),
    WAITING_FOR_REPLACE_CAR ("WAI");

    private final String state;

    CustomerState(String state) {
        this.state = state;
    }

    public String state() { return state; }
}

package entities;

import regions.Lounge;
import regions.OutsideWorld;
import regions.Park;

public class Customer extends Thread {

    private int customerId;
    private CustomerState state;
    private boolean wantsReplacementCar;

    private Lounge lounge;
    private Park park;
    private OutsideWorld outsideWorld;

    public Customer(int id, Lounge lounge, Park park, OutsideWorld outsideWorld) {
        this.customerId = id;
        this.state = CustomerState.NORMAL_LIFE_WITH_CAR;
        this.wantsReplacementCar = Math.random() >= 0.6;
        this.lounge = lounge;
        this.park = park;
        this.outsideWorld = outsideWorld;
    }

    @Override
    public void run() {
        decideOnRepair();
        park.goToRepairShop();
        lounge.queueIn();
        lounge.talkWithManager();
        if(wantsReplacementCar) {
            key = lounge.collectKey();
            park.findCar(key);
            outsideWorld.backToWorkByCar();
            park.goToRepairShop();
        } else
            outsideWorld.backToWorkByBus();
        lounge.queueIn();
        lounge.payForTheService();
        park.collectCar();
        outsideWorld.backToWorkByCar();
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    private void decideOnRepair() {
        // update state
        setState(CustomerState.NORMAL_LIFE_WITH_CAR);

        try {
            sleep ((long) (1 + 40 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

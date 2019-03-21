package entities;

import regions.Lounge;
import regions.ManagerTask;
import regions.OutsideWorld;
import regions.RepairArea;
import regions.SupplierSite;

public class Manager extends Thread {

    private ManagerState state;
    private Lounge lounge;
    private RepairArea repairArea;
    private OutsideWorld outsideWorld;
    private SupplierSite supplierSite;
    private int[] partsBasket;

    public Manager(int nParts, Lounge lounge, RepairArea repairArea, OutsideWorld outsideWorld, SupplierSite supplierSite) {
        this.state = ManagerState.CHECKING_WHAT_TO_DO;
        this.lounge = lounge;
        this.repairArea = repairArea;
        this.outsideWorld = outsideWorld;
        this.supplierSite = supplierSite;
        this.partsBasket = new int[nParts];
    }

    @Override
    public void run() {
        while (lounge.getNextTask()) {
            ManagerTask task = lounge.appraiseSit();
            switch (task) {
                case GET_PARTS:
                    partsBasket = supplierSite.goToSupplier();
                    repairArea.storePart(partsBasket);
                    break;
                case PHONE_CUSTOMER:
                    outsideWorld.phoneCustomer(lounge.getClientNumber());
                    break;
                case TALK_CUSTOMER:
                    CustomerState customer = lounge.talkToCustomer(ManagerTask.TALK_CUSTOMER);
                    if (customer == CustomerState.RECEPTION_REPAIR) {
                        if (lounge.wantReplacementCar())
                            lounge.handCarKey(false);
                        repairArea.registerService(lounge.getCurrentCustomerID());
                    } else if (customer == CustomerState.RECEPTION_PAYING) {
                        lounge.receivePayment();
                        lounge.handCarKey(false);
                    }
                    break;
                case HAND_CAR_KEY:
                    lounge.talkToCustomer(ManagerTask.HAND_CAR_KEY);
                    lounge.handCarKey(true);
                break;
                case NONE:
                break;
            }
        }
        repairArea.markEndOfDay();
    }

    public void setState(ManagerState state) {
        this.state = state;
    }
}

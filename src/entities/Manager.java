package entities;

import regions.Lounge;
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
            String task = lounge.appraiseSit();
            switch (task) {
                case "getNewParts":
                    partsBasket = supplierSite.goToSupplier();
                    repairArea.storePart(partsBasket);
                    break;
                case "phoneCustomer":
                    outsideWorld.phoneCustomer(lounge.getClientNumber());
                    break;
                case "talkCustomer":
                    Customer customer = lounge.talkToCustomer("talkCustomer");
                    if (customer.getCustomerState()== CustomerState.RECEPTION_REPAIR) {
                        if (customer.getWantsReplacementCar())
                            lounge.handCarKey(customer,false);
                        repairArea.registerService(customer);
                    } else if (customer.getCustomerState() == CustomerState.RECEPTION_PAYING) {
                        lounge.receivePayment(customer);
                        lounge.handCarKey(customer, false);
                    }
                    break;
                case "handReplacement":
                    customer = lounge.talkToCustomer("handReplacement");
                    lounge.handCarKey(customer, true);
                break;
            }
        }
        repairArea.markEndOfDay();
    }

    public void setState(ManagerState state) {
        this.state = state;
    }
}

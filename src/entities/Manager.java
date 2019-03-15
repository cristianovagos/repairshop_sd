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

    public Manager(Lounge lounge, RepairArea repairArea, OutsideWorld outsideWorld, SupplierSite supplierSite) {
        this.state = ManagerState.CHECKING_WHAT_TO_DO;
        this.lounge = lounge;
        this.repairArea = repairArea;
        this.outsideWorld = outsideWorld;
        this.supplierSite = supplierSite;
    }

    @Override
    public void run() {
        while (lounge.getNextTask()) {
            String task = lounge.appraiseSit();
            switch (task) {
                case "getNewParts":
                    supplierSite.goToSupplier();
                    repairArea.storePart();
                    break;
                case "phoneCustomer":
                    outsideWorld.phoneCustomer();
                    break;
                case "talkCustomer":
                    customer = lounge.talkToCustomer();
                    if (customer.getState() == CustomerState.RECEPTION_REPAIR) {
                        repairArea.registerService(customer);
                    } else if (customer.getState() == CustomerState.RECEPTION_PAYING) {
                        lounge.receivePayment(customer);
                        lounge.handCarKey(customer);
                    }
            }
        }
        repairArea.markEndOfDay();
    }

    public void setState(ManagerState state) {
        this.state = state;
    }
}

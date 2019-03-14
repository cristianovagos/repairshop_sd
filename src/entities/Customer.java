package entities;

public class Customer extends Thread {

    public Customer() {}

    @Override
    public void run() {

    }

    private void decideOnRepair() {
        try {
            sleep ((long) (1 + 40 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

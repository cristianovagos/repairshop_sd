package entities;

public class Mechanic extends Thread {

    public Mechanic() {}

    @Override
    public void run() {

    }

    private void fixIt() {
        try {
            sleep ((long) (1 + 10 * Math.random()));
        }
        catch (InterruptedException e) {}
    }
}

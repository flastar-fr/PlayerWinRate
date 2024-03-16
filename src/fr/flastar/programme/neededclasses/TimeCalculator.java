package fr.flastar.programme.neededclasses;

public class TimeCalculator {
    private long startTime = System.nanoTime();
    private long endTime;

    public void start() {
        endTime = 0;
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public double getTimer() {
        if (endTime == 0) {
            stop();
        }
        long duration = endTime - startTime;
        return (double) duration / 1_000_000_000.0;
    }

    public void restart() {
        this.startTime = System.nanoTime();
        this.endTime = 0;
    }

    @Override
    public String toString() {
        return String.format("Calcul Time : %,.2f s", getTimer());
    }
}

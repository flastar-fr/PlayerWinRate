package fr.flastar.programme.neededclasses;


public class Average {

    private double addResult;

    private int totalIterations;

    public double getAddResult() {
        return this.addResult;
    }

    public void setAddResult(double addResult) {
        this.addResult = addResult;
    }

    public void addToResult(double numberToAdd) {
        this.setAddResult(this.getAddResult() + numberToAdd);
        this.totalIterations += 1;
    }

    public double calculAverage() {
        return this.getAddResult() / totalIterations;
    }

    @Override
    public String toString() {
        return "La moyenne est de : " + calculAverage();
    }
}

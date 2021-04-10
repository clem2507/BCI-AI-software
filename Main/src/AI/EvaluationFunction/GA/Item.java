package AI.EvaluationFunction.GA;

public class Item {

    private double[] genotype;
    private boolean winner;

    public Item(double[] genotype) {
        this.genotype = genotype;
    }

    public double[] getGenotype() {
        return genotype;
    }

    public void setGenotype(double[] genotype) {
        this.genotype = genotype;
    }

    public boolean getWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}


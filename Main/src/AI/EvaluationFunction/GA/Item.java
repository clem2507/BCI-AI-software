package AI.EvaluationFunction.GA;

public class Item {

    /** genotype array that describes the item */
    private double[] genotype;
    /** winner boolean variable that defines the current tournament item status */
    private boolean winner;

    /**
     * item main constructor
     * @param genotype double array
     */
    public Item(double[] genotype) {
        this.genotype = genotype;
    }

    /**
     * getter for the genotype array
     * @return the item genotype
     */
    public double[] getGenotype() {
        return genotype;
    }

    /**
     * setter for the genotype variable
     * @param genotype double array
     */
    public void setGenotype(double[] genotype) {
        this.genotype = genotype;
    }

    /**
     * getter for the winning status of the item
     * @return true if the item has won its last match simulation
     */
    public boolean getWinner() {
        return winner;
    }

    /**
     * setter for the winner item status
     * @param winner boolean variable
     */
    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}


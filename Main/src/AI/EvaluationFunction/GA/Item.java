package AI.EvaluationFunction.GA;

public class Item {

    /** genotype array that describes the item */
    private double[] genotype;
    /** winner boolean variable that defines the current tournament item status */
    private boolean winner;
    /** variable that keep track the score of an Item */
    private int score;

    /**
     * item main constructor
     * @param genotype double array weights
     */
    public Item(double[] genotype) {
        this.genotype = genotype;
    }

    /**
     * item second main constructor
     * @param genotype double array weights
     * @param score of the item so far in the iteration
     */
    public Item(double[] genotype, int score){
        this.genotype = genotype;
        this.score = score;
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

    /**
     * getter for the score variable
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * setter for the score
     * @param score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
}


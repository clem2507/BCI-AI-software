package AI.TreeStructure;

public class Node{

    /** 2d board state for the node */
    private int[][] boardState;
    /** total number of simulation for node */
    private int totalSimulation;
    /** total score for the node */
    private double totalScore;
    /** 2d board state for the node */
    private double score;

    /**
     * node constructor for MCTS
     * @param boardState that corresponds to the node
     * @param totalSimulation in node
     * @param totalScore for the node
     */
    public Node(int[][] boardState, int totalSimulation, double totalScore) {
        this.boardState = boardState;
        this.totalSimulation = totalSimulation;
        this.totalScore = totalScore;
    }

    /**
     * node constructor for ABTS
     * @param boardState that corresponds to the node
     * @param score for the node
     */
    public Node(int[][] boardState, double score) {
        this.boardState = boardState;
        this.score = score;
    }

    /**
     * getter for the board state
     * @return board state 2d array
     */
    public int[][] getBoardState() {
        return boardState;
    }

    /**
     * getter for the total simulation in node
     * @return the total simulation variable
     */
    public int getTotalSimulation() {
        return totalSimulation;
    }

    /**
     * setter for the total simulation for a node
     * @param totalSimulation integer variable
     */
    public void setTotalSimulation(int totalSimulation) {
        this.totalSimulation = totalSimulation;
    }

    /**
     * getter for the total score in node
     * @return the total score variable
     */
    public double getTotalScore() {
        return totalScore;
    }

    /**
     * getter for the score node
     * @return the score node variable
     */
    public double getScore() {
        return score;
    }

    /**
     * setter for the node score
     * @param score variable
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * setter for the total node win
     * @param totalScore variable
     */
    public void setTotalWin(double totalScore) {
        this.totalScore = totalScore;
    }
}

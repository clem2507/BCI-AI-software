package AI.TreeStructure;

import President.Game.Player;

public class Node{

    /** 2d board state for the node */
    private int[][] boardState;
    /** depth of the node in the tree */
    private int depth;
    /** total number of simulation for node */
    private int totalSimulation;
    /** total score for the node */
    private double totalScore;
    /** 2d board state for the node */
    private double score;
    /** variable that checks if there is a win or a loss in the subtree starting at this node n */
    private boolean isDoneInSubTree;
    /** Player variable hold by the Node */
    private Player player;

    /**
     * node constructor for MCTS checkers
     * @param boardState that corresponds to the node
     * @param depth is the current depth in the tree for node n
     * @param totalSimulation in node
     * @param totalScore for the node
     */
    public Node(int[][] boardState, int depth, int totalSimulation, double totalScore) {
        this.boardState = boardState;
        this.depth = depth;
        this.totalSimulation = totalSimulation;
        this.totalScore = totalScore;
    }

    /**
     * node constructor for ABTS checkers
     * @param boardState that corresponds to the node
     * @param score for the node
     */
    public Node(int[][] boardState, double score) {
        this.boardState = boardState;
        this.score = score;
    }

    /**
     * node constructor for MCTS president
     * @param player state
     * @param depth is the current depth in the tree for node n
     * @param totalSimulation in node
     * @param totalScore for the node
     */
    public Node(Player player, int depth, int totalSimulation, double totalScore) {
        this.player = player;
        this.depth = depth;
        this.totalSimulation = totalSimulation;
        this.totalScore = totalScore;
    }

    /**
     * node constructor for ABTS president
     * @param player state
     * @param score for the node
     */
    public Node(Player player, double score) {
        this.player = player;
        this.score = score;
    }

    /**
     * getter for the player state
     * @return the player state
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getter for the board state
     * @return board state 2d array
     */
    public int[][] getBoardState() {
        return boardState;
    }

    /**
     * getter for the depth variable
     * @return current node depth in tree
     */
    public int getDepth() {
        return depth;
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

    /**
     * @param isDoneInSubTree variable is true when node n has a win or a loss in its subtree range
     */
    public void setIsDoneInSubTree(boolean isDoneInSubTree) {
        this.isDoneInSubTree = isDoneInSubTree;
    }

    /**
     * getter
     * @return true if game can be finished in node n subtree
     */
    public boolean isDoneInSubTree() {
        return isDoneInSubTree;
    }
}

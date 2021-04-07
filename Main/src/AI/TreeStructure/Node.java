package AI.TreeStructure;

public class Node{

    private int[][] boardState;
    private int totalSimulation;
    private double totalScore;
    private double score;

    private Node root = null;

    public Node(int[][] boardState, int totalSimulation, double totalScore) {
        this.boardState = boardState;
        this.totalSimulation = totalSimulation;
        this.totalScore = totalScore;
    }

    public Node(int[][] boardState, double score) {
        this.boardState = boardState;
        this.score = score;
    }

    public int[][] getBoardState() {
        return boardState;
    }

    public Node getRoot() {
        return root;
    }

    public int getTotalSimulation() {
        return totalSimulation;
    }

    public void setTotalSimulation(int totalSimulation) {
        this.totalSimulation = totalSimulation;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTotalWin(double totalScore) {
        this.totalScore = totalScore;
    }
}

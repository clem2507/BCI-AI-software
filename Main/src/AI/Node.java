package AI;

public class Node{

    private int[][] boardState;
    private int totalSimulation;
    private double totalScore;

    private Node root = null;

    public Node(int[][] boardState, int totalSimulation, double totalScore) {
        this.boardState = boardState;
        this.totalSimulation = totalSimulation;
        this.totalScore = totalScore;
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

    public void setTotalWin(double totalScore) {
        this.totalScore = totalScore;
    }
}

package AI.MonteCarloTreeSearch;

import AI.GameSelector;
import AI.TreeStructure.Edge;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.Game.Abalone;
import Abalone.Game.GetPossibleMoves;
import Checkers.Game.Checkers;
import AI.PossibleMoves.CheckersPossibleMoves;

import java.util.ArrayList;

public class MCTS {

    private final GameSelector game;

    private final int currentPlayer;
    private int iterations;
    private int sampleSize;

    private double stopCondition;

    private boolean isCheckers = false;
    private boolean isAbalone = false;

    private Node root;

    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<int[][]> fourBestNodes = new ArrayList<>();

    private int[][] bestMove;

    public MCTS(GameSelector game) {

        this.game = game;

        if (game.getClass().isInstance(new Checkers(null))) {
            this.root = new Node(this.game.getCheckersBoard().getGameBoard(), 0, 0);
            isCheckers = true;
        }
        else if (game.getClass().isInstance(new Abalone(null))) {
            this.root = new Node(this.game.getAbaloneBoard().getGameBoard(), 0, 0);
            isAbalone = true;
        }

        this.currentPlayer = this.game.getCurrentPlayer();
        this.nodes.add(root);
        setBestConfiguration();
    }

    // TODO: find a way to balance the MCTS configuration
    public void setBestConfiguration() {

        sampleSize = 5;
        stopCondition = 2000;
    }

    public void start() {

        long b_time = System.currentTimeMillis();
        while ((System.currentTimeMillis() - b_time) < stopCondition) {
            Selection();
//            for (Node n : nodes) {
//                System.out.print(n.getTotalSimulation() + ", ");
//            }
//            System.out.println();
            iterations++;
        }
        ArrayList<Node> rootChildren = getChildren(root);
        int i = 0;
        while (i < 4) {
            double maxSimulation = 0;
            Node bestChild = null;
            for (Node child : rootChildren) {
                if (child.getTotalSimulation() > maxSimulation) {
                    maxSimulation = child.getTotalSimulation();
                    bestMove = child.getBoardState();
                    bestChild = child;
                }
            }
            fourBestNodes.add(bestMove);
            rootChildren.remove(bestChild);
            i++;
        }
        System.out.println("Simulations = " + nodes.get(0).getTotalSimulation());
        System.out.println();
    }

    public double uctValue(Node n) {

        // c = exploration parameter
        double c = 2;
        int nodeVisits = n.getTotalSimulation();
        int parentVisits = getParent(n).getTotalSimulation();
        double nodeWin = n.getTotalScore();
        double meanScore;
        if (nodeVisits == 0) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            meanScore = nodeWin / nodeVisits;
        }
        return meanScore + (c * Math.sqrt(Math.log(parentVisits)/nodeVisits));
    }

    public Node findBestNodeWithUCT(Node n) {

        Node bestNode = new Node(null, 0, 0);
        double max = Double.NEGATIVE_INFINITY;
        for (Node node : getChildren(n)) {
            if (uctValue(node) > max) {
                max = uctValue(node);
                bestNode = node;
            }
        }
        return bestNode;
    }

    public void Selection() {
        Node n = root;
        int actualPlayer = currentPlayer;
        if (iterations > 0) {
            while (getChildren(n).size() > 0) {
                n = findBestNodeWithUCT(n);
                actualPlayer = Util.changeCurrentPlayer(actualPlayer);
            }
        }
        Expansion(n, actualPlayer);
    }

    public void Expansion(Node n, int currentPlayer) {

        ArrayList<int[][]> children = new ArrayList<>();

        if (isCheckers) {
            if (!game.isDone(n.getBoardState())) {
                CheckersPossibleMoves possibleMoves = new CheckersPossibleMoves(n.getBoardState(), currentPlayer);
                children = possibleMoves.getPossibleMoves();
            }
        }
        else if (isAbalone) {
            if (!game.isDone(n.getBoardState())) {
                GetPossibleMoves possibleMoves = new GetPossibleMoves();
                children = possibleMoves.getPossibleMoves(n.getBoardState(), currentPlayer);
            }
        }

        for (int[][] child : children) {
            Node childNode = new Node(child, 0, 0);
            nodes.add(childNode);
            Edge edge = new Edge(n, childNode);
            edges.add(edge);
        }

        if (children.size() > 0) {
            int max = children.size();
            int randomIndex = (int) (Math.random() * ((max)));
            Simulation(getChildren(n).get(randomIndex), currentPlayer);
        }
        else {
            Simulation(n, currentPlayer);
        }
    }

    public void Simulation(Node n, int currentPlayer) {

        double simulationScore = 0;
        for (int i = 0; i < sampleSize; i++) {
            int actualPlayer = currentPlayer;
            int countMoves = 0;
            int[][] actualBoard = n.getBoardState();
            while (!game.isDone(actualBoard)) {

                ArrayList<int[][]> children = new ArrayList<>();

                if (isCheckers) {
                    CheckersPossibleMoves possibleMoves = new CheckersPossibleMoves(actualBoard, actualPlayer);
                    children = possibleMoves.getPossibleMoves();
                }
                else if (isAbalone) {
                    GetPossibleMoves possibleMoves = new GetPossibleMoves();
                    children = possibleMoves.getPossibleMoves(actualBoard, actualPlayer);
                }

                int max = children.size();
                int randomIndex = (int) (Math.random() * (max));

                if (children.size() > 0) {
                    actualBoard = children.get(randomIndex);
                }
                actualPlayer = Util.changeCurrentPlayer(actualPlayer);
                countMoves++;
            }
            if (game.isVictorious(actualBoard, this.currentPlayer)) {
                simulationScore++;
            }
            else {
                simulationScore--;
            }
        }
        n.setTotalSimulation(n.getTotalSimulation()+1);
        n.setTotalWin(n.getTotalScore() + simulationScore);
        backPropagation(n, simulationScore);
    }

    public void backPropagation(Node n, double simulationScore) {

        //int back = 0;
        while (getParent(n) != null) {
            n = getParent(n);
            n.setTotalSimulation(n.getTotalSimulation() + 1);
            n.setTotalWin(n.getTotalScore() + simulationScore);
            //back++;
        }
        //System.out.println("back = " + back);

        ArrayList<Node> rootChildren = getChildren(root);
        double sumScore = 0;
        int sumSimulation = 0;
        for (Node child : rootChildren) {
            sumScore += child.getTotalScore();
            sumSimulation += child.getTotalSimulation();
        }
        root.setTotalWin(sumScore);
        root.setTotalSimulation(sumSimulation);
    }

//    public double weightingFunction(double rootScore, double currentScore) {
//
//        double score;
//        if (currentScore > rootScore) {
//            score = Math.sqrt(Math.abs(currentScore-rootScore))/5;
//        } else {
//            score = -(Math.sqrt(Math.abs(currentScore-rootScore))/5);
//        }
//        return score;
//    }

    public ArrayList<Node> getChildren(Node n) {

        ArrayList<Node> children = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource() == n) {
                children.add(e.getDestination());
            }
        }
        return children;
    }

    public Node getParent(Node n) {

        Node parent = null;
        for (Edge e : edges) {
            if (e.getDestination() == n) {
                parent = e.getSource();
            }
        }
        return parent;
    }

    public int[][] getBestMove() {
        return bestMove;
    }

    public ArrayList<int[][]> getFourBestNodes() {
        return fourBestNodes;
    }
}

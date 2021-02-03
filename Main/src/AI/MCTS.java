package AI;

import GUI.GameUI;
import Game.Board;
import Game.Checkers;
import Game.PossibleMoves;
import Output.Test;

import java.security.SecureRandom;
import java.util.ArrayList;

public class MCTS {

    private int[][] bestMove;
    private int currentPlayer;

    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<int[][]> fourBestNodes = new ArrayList<>();

    private Node root;
    private double rootScore;

    private int count = 0;

    private double timer;
    private int sampleSize;
    //private int numOfPlays;

    // Current MCTS constructor
    public MCTS(int[][] rootState, int currentPlayer, float timer, int sampleSize) {

        this.currentPlayer = currentPlayer;
        this.root = new Node(rootState, 0, 0);
        this.nodes.add(root);
        this.timer = timer;
        this.sampleSize = sampleSize;
    }

    public void start() {

        long b_time = System.currentTimeMillis();
        double stopCondition = timer;
        while ((System.currentTimeMillis() - b_time) < stopCondition) {
            Selection();
//            for (Node n : nodes) {
//                System.out.print(n.getTotalSimulation() + ", ");
//            }
//            System.out.println();
            count++;
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
            //if (isValidated(bestMove)) {
                fourBestNodes.add(bestMove);
                i++;
            //}
            rootChildren.remove(bestChild);
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
        if (count > 0) {
            while (getChildren(n).size() > 0) {
                n = findBestNodeWithUCT(n);
                if (actualPlayer == 1) {
                    actualPlayer = 2;
                } else {
                    actualPlayer = 1;
                }
            }
        }
        Expansion(n, actualPlayer);
    }

    public void Expansion(Node n, int currentPlayer) {

        PossibleMoves possibleMoves = new PossibleMoves(n.getBoardState(), currentPlayer);
        ArrayList<int[][]> children = possibleMoves.getPossibleMoves();
        for (int[][] child : children) {
            Node childNode = new Node(child, 0, 0);
            nodes.add(childNode);
            Edge edge = new Edge(n, childNode);
            edges.add(edge);
        }
        if (this.currentPlayer == currentPlayer) {
            if (children.size() > 0) {
                int max = children.size();
                int randomIndex = (int) (Math.random() * ((max)));
                Simulation(getChildren(n).get(randomIndex), currentPlayer);
            }
        }
    }

    public void Simulation(Node n, int currentPlayer) {

        double simulationScore = 0;
        int numberOfSample = sampleSize;
        for (int i = 0; i < numberOfSample; i++) {
            int actualPlayer = currentPlayer;
            int[][] actualBoard = n.getBoardState();
            int countMoves = 0;
            while (!Checkers.isVictorious(actualBoard)) {
                PossibleMoves possibleMoves = new PossibleMoves(actualBoard, actualPlayer);
                ArrayList<int[][]> children = possibleMoves.getPossibleMoves();
                int max = children.size();
                int randomIndex = (int) (Math.random() * (max));

                if (children.size() > 0) {
                    actualBoard = children.get(randomIndex);
                }

                if (actualPlayer == 1) {
                    actualPlayer = 2;
                }
                else {
                    actualPlayer = 1;
                }
                countMoves++;
            }
            //System.out.println("countMoves = " + countMoves);
            if (actualPlayer == currentPlayer) {
                // TODO: find a better weighting function for the scores
                if (countMoves > 0) {
                    simulationScore-=((double) 1/countMoves);
                }
                else {
                    simulationScore--;
                }
            }
            else {
                // TODO: find a better weighting function for the scores
                if (countMoves > 0) {
                    simulationScore+=((double) 1/countMoves);
                }
                else {
                    simulationScore++;
                }
            }
        }
        //System.out.println();
        n.setTotalSimulation(n.getTotalSimulation() + 1);
        n.setTotalWin(n.getTotalScore() + simulationScore);

        backPropagation(n, simulationScore);
    }

    public void backPropagation(Node n, double simulationScore) {

        int back = 0;
        while (getParent(n) != null) {
            n = getParent(n);
            n.setTotalSimulation(n.getTotalSimulation() + 1);
            n.setTotalWin(n.getTotalScore() + simulationScore);
            back++;
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

    public boolean isValidated(int[][] state) {

        for (int[][] board : fourBestNodes) {
            int count = 0;
            for (int i = 1; i < state.length-1; i++) {
                for (int j = 1; j < state.length-1; j++) {
                    if (board[i][j] == currentPlayer && state[i][j] == currentPlayer) {
                        count++;
                    }
                }
            }
            if (count == 4) {
                return false;
            }
        }
        return true;
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

//    public int computeSampleSize(double timer) {
//
//        timer = timer/1000;
//        return (int) Math.ceil(timer);
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


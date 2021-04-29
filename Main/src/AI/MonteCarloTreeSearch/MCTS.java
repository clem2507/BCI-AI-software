package AI.MonteCarloTreeSearch;

import AI.GameSelector;
import AI.PossibleMoves.PossibleMoves;
import AI.TreeStructure.Edge;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.GUI.Hexagon;
import Abalone.Game.Abalone;
import AI.PossibleMoves.AbaloneGetPossibleMoves;
import Checkers.GUI.GameUI;
import Checkers.Game.Checkers;
import AI.PossibleMoves.CheckersPossibleMoves;

import java.util.ArrayList;

public class MCTS {

    /** specific game to run the MCTS on */
    private final GameSelector game;
    /** current player in the game */
    private final int currentPlayer;
    /** integer variable that stands for the current number of iterations */
    private int iterations;
    /** instance that consists of the total number of random simulations to make at a certain leaf in the tree */
    private int sampleSize;
    /** stop condition to let the algorithm knows when it is done with computation */
    private double stopCondition;

    private boolean isCheckers = false;
    private boolean isAbalone = false;

    /** root node of the tree */
    private Node root;
    /** list of all edges in the game tree */
    private ArrayList<Edge> edges = new ArrayList<>();
    /** list of all nodes in the game tree */
    private ArrayList<Node> nodes = new ArrayList<>();
    /** list that contains the four best nodes after the algorithm is done running */
    private ArrayList<Node> fourBestNodes = new ArrayList<>();


    private int count = 0;

    /**
     * main constructor for the MCTS algorithm
     * @param game current game to play with
     */
    public MCTS(GameSelector game) {

        this.game = game;

        if (game.getClass().isInstance(new Checkers(null))) {
            this.root = new Node(this.game.getCheckersBoard().getGameBoard(), 1, 0, 0);
            isCheckers = true;
        }
        else if (game.getClass().isInstance(new Abalone(null))) {
            this.root = new Node(this.game.getAbaloneBoard().getGameBoard(), 1, 0, 0);
            isAbalone = true;
        }
        this.currentPlayer = this.game.getCurrentPlayer();
        this.nodes.add(root);
        setBestConfiguration();
    }

    // TODO: find a way to balance the MCTS configuration
    public void setBestConfiguration() {

        sampleSize = 10;
        stopCondition = 5000;
    }

    /**
     * start method that launches the algorithm
     */
    public void start() {

//        long b_time = System.currentTimeMillis();
//        while ((System.currentTimeMillis() - b_time) < stopCondition) {
        while (nodes.get(0).getTotalSimulation() < stopCondition) {
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
                    bestChild = child;
                }
            }
            fourBestNodes.add(bestChild);
            rootChildren.remove(bestChild);
            i++;
        }
        System.out.println("Simulations = " + nodes.get(0).getTotalSimulation());
        System.out.println("count = " + count);
        System.out.println();
    }

    /**
     * method used to balance the algorithm selection between exploration and exploitation
     * @param n current node on which we need to compute the uct score
     * @return the corresponding score
     */
    // TODO - change the uct scoring value!!
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

    /**
     * find the best node to select based on the uct scores
     * @param n current node
     * @return the best node to select next
     */
    public Node findBestNodeWithUCT(Node n) {

        Node bestNode = new Node(null, 0, 0, 0);
        double max = Double.NEGATIVE_INFINITY;
        for (Node node : getChildren(n)) {
            if (uctValue(node) > max) {
                max = uctValue(node);
                bestNode = node;
            }
        }
        return bestNode;
    }

    /**
     * first of the main MCTS method
     * aims to select the best node in the current expand tree until a leaf in reached
     * when this is the case, the tree is expanded on that particular node with its children
     */
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

    /**
     * second main MCTS method
     * it consists of expanding the leaf node to extend its robustness
     * @param n current node to expand
     * @param currentPlayer to play
     */
    public void Expansion(Node n, int currentPlayer) {

        ArrayList<int[][]> children = new ArrayList<>();
        PossibleMoves possibleMoves;
        if (isCheckers) {
            if (!game.isDone(n.getBoardState())) {
                possibleMoves = new CheckersPossibleMoves(n.getBoardState(), currentPlayer);
                children = possibleMoves.getPossibleMoves();
            }
        } else if (isAbalone) {
            if (!game.isDone(n.getBoardState())) {
                possibleMoves = new AbaloneGetPossibleMoves(n.getBoardState(), currentPlayer);
                children = possibleMoves.getPossibleMoves();
            }
        }

        for (int[][] child : children) {
            Node childNode = new Node(child, n.getDepth()+1, 0, 0);
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
            count++;
            Simulation(n, currentPlayer);
        }
    }

    /**
     * third main MCTS method
     * when the previously selected node has been expanded, we run a certain amount of random simulations on a randomly selected child
     * @param n randomly selected node from the expansion
     * @param currentPlayer to play
     */
    public void Simulation(Node n, int currentPlayer) {

        double simulationScore = 0;
        for (int i = 0; i < sampleSize; i++) {
            int actualPlayer = currentPlayer;
            int countMoves = 0;
            int[][] actualBoard = n.getBoardState();
            while (!game.isDone(actualBoard)) {

                ArrayList<int[][]> children = new ArrayList<>();
                PossibleMoves possibleMoves;
                int max;
                int randomIndex = -1;
                if (isCheckers) {
                    possibleMoves = new CheckersPossibleMoves(actualBoard, actualPlayer);
                    children = possibleMoves.getPossibleMoves();
                    max = children.size();
                    randomIndex = (int) (Math.random() * (max));
                }
                else if (isAbalone) {
                    possibleMoves = new AbaloneGetPossibleMoves(actualBoard, actualPlayer);
                    children = possibleMoves.getPossibleMoves();
                    max = children.size();
                    double rand = Math.random();
                    if (rand < 0.7) {
                        randomIndex = (int) (Math.random() * Math.floor((double) max/3));
                    }
                    else {
                        randomIndex = (int) ((Math.random() * max/3) + Math.floor((double) 2*max/3));
                    }
                }

                if (children.size() > 0) {
                    actualBoard = children.get(randomIndex);
                }
                actualPlayer = Util.changeCurrentPlayer(actualPlayer);
                countMoves++;
            }
//            EvaluationFunction eval;
            if (game.isVictorious(actualBoard, this.currentPlayer)) {
//                eval = new CheckersEvalFunction(actualBoard, this.currentPlayer);
//                simulationScore+=eval.evaluate();
                simulationScore++;
            }
            else {
//                eval = new CheckersEvalFunction(actualBoard, Util.changeCurrentPlayer(this.currentPlayer));
//                simulationScore-=eval.evaluate();
                simulationScore--;
            }
        }
//        n.setTotalSimulation(n.getTotalSimulation() + 1);
        n.setTotalSimulation(n.getTotalSimulation() + sampleSize);
//        n.setTotalWin(n.getTotalScore() + simulationScore);
        n.setTotalWin(n.getTotalScore() + (simulationScore/n.getDepth()));
//        backPropagation(n, simulationScore);
        backPropagation(n, (simulationScore/n.getDepth()));
    }

    /**
     * final main MCTS method
     * when we are done with the simulations, we need to backtrack the results until the root node is reached and update the corresponding parents
     * @param n leaf node on which we need to backtrack its score
     * @param simulationScore to backtrack
     */
    public void backPropagation(Node n, double simulationScore) {

        int back = 0;
        boolean check = false;
        if (game.isDone(n.getBoardState())) {
            check = true;
            n.setIsDoneInSubTree(true);
        }
        while (getParent(n) != null) {
            if (check) {
                getParent(n).setIsDoneInSubTree(true);
            }
            n = getParent(n);
            n.setTotalSimulation(n.getTotalSimulation() + sampleSize);
            n.setTotalWin(n.getTotalScore() + simulationScore);
            back++;
        }
//        System.out.println("back = " + back);

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

    /**
     * @param n given a certain node
     * @return its corresponding list of children
     */
    public ArrayList<Node> getChildren(Node n) {

        ArrayList<Node> children = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource() == n) {
                children.add(e.getDestination());
            }
        }
        return children;
    }

    /**
     * @param n given a certain node
     * @return its closest parent
     */
    public Node getParent(Node n) {

        Node parent = null;
        for (Edge e : edges) {
            if (e.getDestination() == n) {
                parent = e.getSource();
            }
        }
        return parent;
    }

    /**
     * @return the four best moves selected by the algorithm
     */
    public ArrayList<Node> getFourBestNodes() {
        return fourBestNodes;
    }
}

package AI.MonteCarloTreeSearch;

import AI.GameSelector;
import AI.PossibleMoves.PossibleMoves;
import AI.PossibleMoves.PresidentPossibleMoves;
import AI.TreeStructure.Edge;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.GUI.Hexagon;
import Abalone.Game.Abalone;
import AI.PossibleMoves.AbaloneGetPossibleMoves;
import Checkers.GUI.GameUI;
import Checkers.Game.Checkers;
import AI.PossibleMoves.CheckersPossibleMoves;
import President.Game.Card;
import President.Game.Player;
import President.Game.Tuple;

import java.util.ArrayList;

public class MCTS {

    /** specific game to run the MCTS on */
    private final GameSelector game;
    /** current player in the game */
    private int currentPlayer;
    /** integer variable that stands for the current number of iterations */
    private int iterations;
    /** instance that consists of the total number of random simulations to make at a certain leaf in the tree */
    private int sampleSize;
    /** stop condition to let the algorithm knows when it is done with computation */
    private double stopCondition;

    private boolean isCheckers = false;
    private boolean isAbalone = false;
    private boolean isPresident = false;

    /** root node of the tree */
    private Node root;
    /** list of all edges in the game tree */
    private ArrayList<Edge> edges = new ArrayList<>();
    /** list of all nodes in the game tree */
    private ArrayList<Node> nodes = new ArrayList<>();
    /** list that contains the four best nodes after the algorithm is done running */
    private ArrayList<Node> fourBestNodes = new ArrayList<>();

    private int count = 0;
    private double adaptiveVariable;

    /**
     * main constructor for the MCTS algorithm
     * @param game current game to play with
     */
    public MCTS(GameSelector game, double adaptiveVariable) {

        this.game = game;
        this.adaptiveVariable = adaptiveVariable;
        if (game.getClass().isInstance(new Checkers(null))) {
            this.root = new Node(this.game.getCheckersBoard().getGameBoard(), 1, 0, 0);
            isCheckers = true;
            this.currentPlayer = this.game.getCurrentPlayer();
        }
        else if (game.getClass().isInstance(new Abalone(null))) {
            this.root = new Node(this.game.getAbaloneBoard().getGameBoard(), 1, 0, 0);
            isAbalone = true;
            this.currentPlayer = this.game.getCurrentPlayer();
        }
        else {
            if (game.getPlayer1().toPlay()) {
                this.root = new Node(game.getPlayer1(), 1, 0, 0);
                this.currentPlayer = 1;
            }
            else {
                this.root = new Node(game.getPlayer2(), 1, 0, 0);
                this.currentPlayer = 2;
            }
            isPresident = true;
        }
        this.nodes.add(root);
        setBestConfiguration();
    }

    // TODO: find a way to balance the MCTS configuration
    public void setBestConfiguration() {

        if (this.currentPlayer == 1) {
            if (isPresident) {
                sampleSize = 18;
                stopCondition = 1340;
            }
            else {
                sampleSize = 5;
                stopCondition = 2000;
            }
        }
        // else, apply adaptive AI and balance the configuration here
        else {
//            double adaptiveVariable = game.getAdaptiveVariable();
//            sampleSize = (int) Math.ceil(10*adaptiveVariable);
//            stopCondition = (int) Math.ceil(5000*adaptiveVariable);

            if (isPresident) {
                sampleSize = 18;
                stopCondition = 1340;
            }
            else {
                sampleSize = 5;
                stopCondition = 2000;
            }
        }
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
        ArrayList<Node> temp = new ArrayList<>(rootChildren);
        if (isPresident) {
            if (rootChildren.size() > 1) {
                for (Node node : rootChildren) {
                    if (node.getPlayer().getGameState().getOccurrence() == 0) {
                        temp.remove(node);
                    }
                }
                rootChildren = temp;
            }
        }
        if (this.currentPlayer == 1) {
            int possibleMoveChoice = rootChildren.size();
            int i = 0;
            while (i < 4) {
                if (i < possibleMoveChoice) {
                    double maxSimulation = -1;
                    Node bestChild = null;
                    for (Node child : rootChildren) {
                        if (child.getTotalSimulation() > maxSimulation) {
                            maxSimulation = child.getTotalSimulation();
                            bestChild = child;
                        }
                    }
                    fourBestNodes.add(bestChild);
                    rootChildren.remove(bestChild);
                } else {
                    fourBestNodes.add(fourBestNodes.get(fourBestNodes.size() - 1));
                }
                i++;
            }
        }
        else {
            Node bestChild = null;
            double maxSimulation = -1;
            for (Node child : rootChildren) {
                if (child.getTotalSimulation() > maxSimulation) {
                    maxSimulation = child.getTotalSimulation();
                    bestChild = child;
                }
            }
            fourBestNodes.add(bestChild);
        }
//        System.out.println("Simulations = " + nodes.get(0).getTotalSimulation());
//        System.out.println("Repetition count = " + count);
//        System.out.println();
    }

    /**
     * method used to balance the algorithm selection between exploration and exploitation
     * @param n current node on which we need to compute the uct score
     * @return the corresponding score
     */
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

        Node bestNode = null;
        if (isCheckers || isAbalone) {
            bestNode = new Node((int[][]) null, 0, 0, 0);
        }
        else if (isPresident) {
            bestNode = new Node((Player) null, 0, 0, 0);
        }
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
        int actualPlayer = this.currentPlayer;
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
        ArrayList<Tuple> actions = new ArrayList<>();
        boolean isDone = false;
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
        else if (isPresident) {
            if (!game.isVictorious(n.getPlayer()) || n.getPlayer().getOpponentNumberCards() > 0) {
                possibleMoves = new PresidentPossibleMoves(n.getPlayer());
                if (currentPlayer==this.currentPlayer) {
                    actions = possibleMoves.getPossibleActions();
                }
                else {
                    actions = possibleMoves.getInformationSet(possibleMoves.computeInformationSetCards());
                }
            }
            else {
                isDone = true;
            }
        }

        if (isCheckers || isAbalone) {
            for (int[][] child : children) {
                Node childNode = new Node(child, n.getDepth() + 1, 0, 0);
                nodes.add(childNode);
                Edge edge = new Edge(n, childNode);
                edges.add(edge);
            }

            if (children.size() > 0) {
                int max = children.size();
                int randomIndex = (int) (Math.random() * ((max)));
                Simulation(getChildren(n).get(randomIndex), Util.changeCurrentPlayer(currentPlayer));
            } else {
                count++;
                Simulation(n, Util.changeCurrentPlayer(currentPlayer));
            }
        }
        else if (isPresident) {
            for (Tuple action : actions) {
                Player temp = new Player(n.getPlayer());
                temp.isToPlay(n.getPlayer().toPlay());
                Node child;
                temp.takeAction(action, temp.getDeck());
                child = new Node(temp, n.getDepth()+1, 0, 0);
                nodes.add(child);
                Edge edge = new Edge(n, child);
                edges.add(edge);
            }

            // CARRY WHEN NOT POSSIBLE TO PLAY FOR THE OTHER PLAYER
            if (isDone) {
                count++;
                Simulation(n, currentPlayer);
            }
            else {
                if (actions.size() > 0) {
                    int max = actions.size();
                    int randomIndex = (int) (Math.random() * ((max)));
                    Simulation(getChildren(n).get(randomIndex), Util.changeCurrentPlayer(currentPlayer));
                } else {
                    Player temp = new Player(n.getPlayer());
                    temp.setGameState(new Tuple(0, 0));
                    temp.isToPlay(false);
                    Node child = new Node(temp, n.getDepth()+1, 0, 0);
                    nodes.add(child);
                    Edge edge = new Edge(n, child);
                    edges.add(edge);
                    Simulation(child, Util.changeCurrentPlayer(currentPlayer));
                }
            }
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
            if (isCheckers || isAbalone) {
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
                    } else if (isAbalone) {
                        possibleMoves = new AbaloneGetPossibleMoves(actualBoard, actualPlayer);
                        children = possibleMoves.getPossibleMoves();
                        max = children.size();
                        double rand = Math.random();
                        if (rand < 0.7) {
                            randomIndex = (int) (Math.random() * Math.floor((double) max / 3));
                        } else {
                            randomIndex = (int) ((Math.random() * max / 3) + Math.floor((double) 2 * max / 3));
                        }
                    }
                    if (children.size() > 0) {
                        actualBoard = children.get(randomIndex);
                    }
                    actualPlayer = Util.changeCurrentPlayer(actualPlayer);
                    countMoves++;
                }
                if (game.isVictorious(actualBoard, this.currentPlayer)) {
                    simulationScore++;
                } else {
                    simulationScore--;
                }
            }
            else {
                int countActions = 0;
                Player actualPlayer = new Player(n.getPlayer());
                while (!game.isVictorious(actualPlayer) && actualPlayer.getOpponentNumberCards() > 0) {
                    PossibleMoves possibleMoves = new PresidentPossibleMoves(actualPlayer);
                    ArrayList<Tuple> actions;
                    int maxIndex;
                    int randomIndex;
                    if (actualPlayer.toPlay()) {
                        actions = possibleMoves.getPossibleActions();
                        ArrayList<Tuple> temp = new ArrayList<>(actions);
                        if (actions.size() > 1) {
                            for (Tuple tuple : actions) {
                                if (tuple.getOccurrence() == 0) {
                                    temp.remove(tuple);
                                }
                            }
                            actions = temp;
                        }
                        if (actions.size() > 0) {
                            maxIndex = actions.size();
                            randomIndex = (int) (Math.random() * (maxIndex));
                            actualPlayer.takeAction(actions.get(randomIndex), actualPlayer.getDeck());
                        }
                        else {
                            actualPlayer.setGameState(new Tuple(0, 0));
                            actualPlayer.isToPlay(false);
                        }
                    }
                    else {
                        ArrayList<Card> ISdeck = possibleMoves.computeInformationSetCards();
                        actions = possibleMoves.getInformationSet(ISdeck);
                        ArrayList<Tuple> temp = new ArrayList<>(actions);
                        if (actions.size() > 1) {
                            for (Tuple tuple : actions) {
                                if (tuple.getOccurrence() == 0) {
                                    temp.remove(tuple);
                                }
                            }
                            actions = temp;
                        }
                        if (actions.size() > 0) {
                            maxIndex = actions.size();
                            randomIndex = (int) (Math.random() * (maxIndex));
                            actualPlayer.takeAction(actions.get(randomIndex), ISdeck);
                        }
                        else {
                            actualPlayer.setGameState(new Tuple(0, 0));
                            actualPlayer.isToPlay(true);
                        }
                    }
                }
                if (game.isVictorious(actualPlayer)) {
                    simulationScore++;
                }
                else {
                    simulationScore--;
                }
            }
        }
        n.setTotalSimulation(n.getTotalSimulation() + sampleSize);
        n.setTotalWin(n.getTotalScore() + (simulationScore/n.getDepth()));
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

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public void setStopCondition(double stopCondition) {
        this.stopCondition = stopCondition;
    }
}

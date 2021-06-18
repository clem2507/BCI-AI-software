package AI.AlphaBetaTreeSearch;

import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.President.PresidentEvalFunction;
import AI.GameSelector;
import AI.PossibleMoves.PossibleMoves;
import AI.PossibleMoves.PresidentPossibleMoves;
import AI.TreeStructure.Edge;
import AI.TreeStructure.Node;
import AI.Util;
import AI.PossibleMoves.CheckersPossibleMoves;
import Checkers.Game.Checkers;
import President.Game.Card;
import President.Game.Player;
import President.Game.Tuple;

import java.util.*;

public class ABTS {

    /** game on which we want to build a game tree on */
    private GameSelector game;
    /** chosen configuration of weights for the evaluation function */
    private double[] configuration;
    /** root node of the tree */
    private Node root;
    /** list that contains all the edges of the game tree */
    private ArrayList<Edge> edges = new ArrayList<>();
    /** list that contains all the edges of the game tree */
    private ArrayList<Node> nodes = new ArrayList<>();
    /** list that contains all the nodes of the game tree root node */
    private ArrayList<Node> rootChildrenNodes;
    /** list that contains all the four best nodes found */
    private ArrayList<Node> fourBestNodes = new ArrayList<>();
    /** current player to play */
    private int currentPlayer;
    /** boolean variable to recognize the current game */
    private boolean isCheckers, isPresident;
    /** maximum depth of tree search */
    private int depth;
    /** opponent player card deck */
    private ArrayList<Card> opponentDeck;

    /**
     * class constructor
     * @param game to create the game tree structure on
     * @param depth total deepness of the tree
     */
    public ABTS(GameSelector game, int depth) {

        this.game = game;
        this.depth = depth;
        this.currentPlayer = game.getCurrentPlayer();

        if (game.getClass().isInstance(new Checkers(null))) {
            isCheckers = true;
            root = new Node(game.getCheckersBoard().getGameBoard(), 0);
            nodes.add(root);
        }
        else {
            if (game.getPlayer1().toPlay()) {
                this.root = new Node(game.getPlayer1(), 0);
                nodes.add(root);
                this.currentPlayer = 1;
            }
            else {
                this.root = new Node(game.getPlayer2(), 0);
                nodes.add(root);
                this.currentPlayer = 2;
            }
            isPresident = true;
        }
    }

    /**
     * class constructor
     * @param game to create the game tree structure on
     * @param depth total deepness of the tree
     * @param configuration weights to run the evaluation function with
     */
    public ABTS(GameSelector game, int depth, double[] configuration) {

        this.game = game;
        this.depth = depth;
        this.configuration = configuration;
        this.currentPlayer = game.getCurrentPlayer();

        if (game.getClass().isInstance(new Checkers(null))) {
            isCheckers = true;
            root = new Node(game.getCheckersBoard().getGameBoard(), 0);
            nodes.add(root);
        }
        else {
            if (game.getPlayer1().toPlay()) {
                this.root = new Node(game.getPlayer1(), 0);
                nodes.add(root);
                this.currentPlayer = 1;
            }
            else {
                this.root = new Node(game.getPlayer2(), 0);
                nodes.add(root);
                this.currentPlayer = 2;
            }
            isPresident = true;
        }
    }

    /**
     * ABTS starting algorithm method
     */
    public void start() {

        int moveChoiceLimit = 4;
        int count = 0;
        if (this.currentPlayer == 1) {
            while (fourBestNodes.size() < 4) {
                if (count < moveChoiceLimit) {
                    rootChildrenNodes = new ArrayList<>();
                    double currScore = Double.NEGATIVE_INFINITY;
                    if (isCheckers) {
                        currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    }
                    else if (isPresident) {
                        currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    }
                    if (fourBestNodes.size() == 0 && rootChildrenNodes.size() < 4) {
                        moveChoiceLimit = rootChildrenNodes.size();
                    }
                    for (Node n : rootChildrenNodes) {
                        if (n.getScore() == currScore) {
                            fourBestNodes.add(n);
                            count++;
                            break;
                        }
                    }
                } else {
                    fourBestNodes.add(fourBestNodes.get(fourBestNodes.size() - 1));
                }
                if (moveChoiceLimit==0) {
                    Player player = new Player(null, null, new Tuple(0, 0), 0, false, 0);
                    fourBestNodes.add(new Node(player, 0));
                    fourBestNodes.add(new Node(player, 0));
                    fourBestNodes.add(new Node(player, 0));
                    fourBestNodes.add(new Node(player, 0));
                    break;
                }
            }
            Collections.shuffle(fourBestNodes);
        }
        else {
            rootChildrenNodes = new ArrayList<>();
            double currScore = Double.NEGATIVE_INFINITY;
            if (isCheckers) {
                currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
            else if (isPresident) {
                currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
            for (Node n : rootChildrenNodes) {
                if (n.getScore() == currScore) {
                    fourBestNodes.add(n);
                    break;
                }
            }
            if (fourBestNodes.size() == 0) {
                Player player = new Player(null, null, new Tuple(0, 0), 0, false, 0);
                fourBestNodes.add(new Node(player, 0));
            }
        }
    }

    /**
     * Recursive method that creates the tree and proceed the ABTS search
     * @param parent current node
     * @param depth current tree search depth variable
     * @param maximizingPlayer is true when the current node is looking to maximize the current player profit
     * @param alpha represent the minimum score that the maximizing player is assured of
     * @param beta represent the maximum score that the minimizing player is assured of
     * @return the best tree search score evaluation
     */
    public double findBestNode(Node parent, int depth, boolean maximizingPlayer, double alpha, double beta) {

        int player;
        if (maximizingPlayer) {
            player = currentPlayer;
        }
        else {
            player = Util.changeCurrentPlayer(currentPlayer);
        }

        ArrayList<int[][]> children = new ArrayList<>();
        ArrayList<Tuple> actions = new ArrayList<>();
        if (depth == 0) {
            return parent.getScore();
        }
        else {
            if (isCheckers) {
                if (game.isDone(parent.getBoardState())) {
                    if (getParent(parent) == nodes.get(0)) {
                        Node node = new Node(parent.getBoardState(), parent.getScore());
                        rootChildrenNodes.add(node);
                    }
                    return parent.getScore();
                }
                children = getPossibleMoves(parent.getBoardState(), player);
                if (parent == nodes.get(0)) {
                    for (int i = 0; i < fourBestNodes.size(); i++) {
                        for (int[][] b : children) {
                            if (Util.isEqual(b, fourBestNodes.get(i).getBoardState())) {
                                children.remove(b);
                                break;
                            }
                        }
                    }
                }
            }
            else if (isPresident) {
                if (game.isVictorious(parent.getPlayer()) || parent.getPlayer().getOpponentNumberCards() <= 0) {
                    if (getParent(parent) == nodes.get(0)) {
                        Player temp = new Player(parent.getPlayer());
                        Node child = new Node(temp, parent.getScore());
                        rootChildrenNodes.add(child);
                    }
                    return parent.getScore();
                }
                actions = getPossibleMoves(parent.getPlayer());
                if (parent == nodes.get(0)) {
                    for (int i = 0; i < fourBestNodes.size(); i++) {
                        for (Tuple tuple : actions) {
                            if (tuple.getNumber() == fourBestNodes.get(i).getPlayer().getGameState().getNumber()
                            && tuple.getOccurrence() == fourBestNodes.get(i).getPlayer().getGameState().getOccurrence()) {
                                actions.remove(tuple);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (maximizingPlayer) {
            double maxEvaluation = Double.NEGATIVE_INFINITY;
            if (isCheckers) {
                for (int[][] board : children) {
                    double score = evaluateNode(board, player, configuration);
                    Node child = new Node(board, score);
                    nodes.add(child);
                    Edge edge = new Edge(parent, child);
                    edges.add(edge);
                    double evaluation = findBestNode(child, depth - 1, false, alpha, beta);
                    maxEvaluation = Double.max(maxEvaluation, evaluation);
                    alpha = Math.max(alpha, evaluation);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            else if (isPresident) {
                for (Tuple action : actions) {
                    Player temp = new Player(parent.getPlayer());
                    temp.isToPlay(parent.getPlayer().toPlay());
                    if (temp.toPlay()) {
                        temp.takeAction(action, temp.getDeck());
                    }
                    else {
                        temp.takeAction(action, opponentDeck);
                    }
                    double score = evaluateNode(temp, configuration);
                    Node child = new Node(temp, score);
                    nodes.add(child);
                    Edge edge = new Edge(parent, child);
                    edges.add(edge);
                    double evaluation = findBestNode(child, depth - 1, false, alpha, beta);
                    maxEvaluation = Double.max(maxEvaluation, evaluation);
                    alpha = Math.max(alpha, evaluation);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEvaluation;
        }
        else {
            double minEvaluation = Double.POSITIVE_INFINITY;
            if (isCheckers) {
                for (int[][] board : children) {
                    double score = evaluateNode(board, player, configuration);
                    Node child = new Node(board, score);
                    nodes.add(child);
                    Edge edge = new Edge(parent, child);
                    edges.add(edge);
                    double evaluation = findBestNode(child, depth - 1, true, alpha, beta);
                    minEvaluation = Double.min(minEvaluation, evaluation);
                    beta = Math.min(beta, evaluation);
                    if (beta <= alpha) {
                        break;
                    }
                }
                if (depth == this.depth - 1) {
                    Node node = new Node(parent.getBoardState(), minEvaluation);
                    rootChildrenNodes.add(node);
                }
            }
            else if (isPresident) {
                for (Tuple action : actions) {
                    Player temp = new Player(parent.getPlayer());
                    temp.isToPlay(parent.getPlayer().toPlay());
                    if (temp.toPlay()) {
                        temp.takeAction(action, temp.getDeck());
                    }
                    else {
                        temp.takeAction(action, opponentDeck);
                    }
                    double score = evaluateNode(temp, configuration);
                    Node child = new Node(temp, score);
                    nodes.add(child);
                    Edge edge = new Edge(parent, child);
                    edges.add(edge);
                    double evaluation = findBestNode(child, depth - 1, true, alpha, beta);
                    minEvaluation = Double.min(minEvaluation, evaluation);
                    beta = Math.min(beta, evaluation);
                    if (beta <= alpha) {
                        break;
                    }
                }
                if (depth == this.depth - 1) {
                    Player temp = new Player(parent.getPlayer());
                    Node node = new Node(temp, minEvaluation);
                    rootChildrenNodes.add(node);
                }
            }
            return minEvaluation;
        }
    }

    /**
     * Evaluation function for a certain node board
     * @param board current state
     * @param player to play
     * @return board evaluation
     */
    public double evaluateNode(int[][] board, int player, double[] configuration) {

        EvaluationFunction eval;
        double score = 0;
        if (configuration == null) {
            if (isCheckers) {
                eval = new CheckersEvalFunction(board, player);
                score = eval.evaluate();
            }
        }
        else {
            if (isCheckers) {
                eval = new CheckersEvalFunction(board, player, configuration);
                score = eval.evaluate();
            }
        }
        return score;
    }

    /**
     * Evaluation function for a certain Player object
     * @param player current player to play
     * @return game evaluation
     */
    public double evaluateNode(Player player, double[] configuration) {

        double score;
        if (configuration == null) {
            score = new PresidentEvalFunction(player).evaluate();
        }
        else {
            score = new PresidentEvalFunction(player, configuration).evaluate();
        }
        return score;
    }

    /**
     * Method that returns the possible moves from a given board state
     * @param board current state
     * @param player to play
     * @return the list of 2D integer board
     */
    public ArrayList<int[][]> getPossibleMoves(int[][] board, int player) {

        ArrayList<int[][]> out = new ArrayList<>();
        PossibleMoves moves;
        if (isCheckers) {
            moves = new CheckersPossibleMoves(board, player);
            out = moves.getPossibleMoves();
        }
        return out;
    }

    /**
     * Method that returns the possible actions from a given card deck
     * @param player state
     * @return the whole actions tuple
     */
    public ArrayList<Tuple> getPossibleMoves(Player player) {

        ArrayList<Tuple> out;
        PossibleMoves possibleMoves = new PresidentPossibleMoves(player);
        if (player.toPlay()) {
            out = possibleMoves.getPossibleActions();
        }
        else {
            opponentDeck = possibleMoves.computeInformationSetCards();
            out = possibleMoves.getInformationSet(opponentDeck);
        }
        ArrayList<Tuple> temp = new ArrayList<>(out);
        if (out.size() > 1) {
            for (Tuple tuple : out) {
                if (tuple.getOccurrence() == 0) {
                    temp.remove(tuple);
                }
            }
            out = temp;
        }
        return out;
    }

    /**
     * method to get the children in the tree of a certain node
     * @param v current node
     * @return a list a children
     */
    public ArrayList<Node> getChildren(Node v) {

        ArrayList<Node> children = new ArrayList<>();
        for (Edge e : edges){
            if(e.getSource() == v) {
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
     * setter for the current player
     * @param currentPlayer to play
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * getter for the game tree node list
     * @return the list with the nodes
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * getter for the game tree edges list
     * @return the list with the edges
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * getter for the root children nodes list
     * @return the list that contains the children nodes from the root
     */
    public ArrayList<Node> getRootChildrenNodes() {
        return rootChildrenNodes;
    }

    /**
     * getter for the four best best nodes list
     * @return the list with the four best nodes after running the algorithm
     */
    public ArrayList<Node> getFourBestNodes() {
        return fourBestNodes;
    }
}


package AI.AlphaBetaTreeSearch;

import AI.EvaluationFunction.Abalone.AbaloneEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.GameSelector;
import AI.PossibleMoves.AbaloneGetPossibleMoves;
import AI.PossibleMoves.PossibleMoves;
import AI.TreeStructure.Edge;
import AI.TreeStructure.Node;
import AI.Util;
import AI.PossibleMoves.CheckersPossibleMoves;
import Abalone.Game.Abalone;
import Checkers.Game.Checkers;

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
    private boolean isCheckers, isAbalone;
    /** maximum depth of tree search */
    private int depth;

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
        else if (game.getClass().isInstance(new Abalone(null))) {
            isAbalone = true;
            root = new Node(game.getAbaloneBoard().getGameBoard(), 0);
            nodes.add(root);
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
        else if (game.getClass().isInstance(new Abalone(null))) {
            isAbalone = true;
            root = new Node(game.getAbaloneBoard().getGameBoard(), 0);
            nodes.add(root);
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
                if (count < moveChoiceLimit || count == 0) {
                    rootChildrenNodes = new ArrayList<>();
                    double currScore = Double.NEGATIVE_INFINITY;
                    if (isCheckers) {
                        currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    } else if (isAbalone) {
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
            }
        }
        else {
            rootChildrenNodes = new ArrayList<>();
            double currScore = Double.NEGATIVE_INFINITY;
            depth = (int) Math.min(5, Math.ceil(game.getAdaptiveVariable()*5));
            if (isCheckers) {
                currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            } else if (isAbalone) {
                currScore = findBestNode(root, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
            for (Node n : rootChildrenNodes) {
                if (n.getScore() == currScore) {
                    fourBestNodes.add(n);
                    break;
                }
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

        ArrayList<int[][]> children;
        if (depth == 0) {
            return parent.getScore();
        }
        else {
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

        if (maximizingPlayer) {
            double maxEvaluation = Double.NEGATIVE_INFINITY;
            for (int[][] board : children) {
                double score = evaluateNode(board, player);
                Node child = new Node(board, score);
                nodes.add(child);
                Edge edge = new Edge(parent, child);
                edges.add(edge);
                double evaluation = findBestNode(child, depth-1, false, alpha, beta);
                maxEvaluation = Double.max(maxEvaluation, evaluation);
                alpha = Math.max(alpha, evaluation);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEvaluation;
        }
        else {
            double minEvaluation = Double.POSITIVE_INFINITY;
            for (int[][] board : children) {
                double score = evaluateNode(board, player);
                Node child = new Node(board, score);
                nodes.add(child);
                Edge edge = new Edge(parent, child);
                edges.add(edge);
                double evaluation = findBestNode(child, depth-1, true, alpha, beta);
                minEvaluation = Double.min(minEvaluation, evaluation);
                beta = Math.min(beta, evaluation);
                if (beta <= alpha) {
                    break;
                }
            }
            if (depth == this.depth-1) {
                Node node = new Node(parent.getBoardState(), minEvaluation);
                rootChildrenNodes.add(node);
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
    public double evaluateNode(int[][] board, int player) {

        EvaluationFunction eval;
        double score = 0;
        if (isCheckers) {
            eval = new CheckersEvalFunction(board, player);
            score = eval.evaluate();
        }
        else if (isAbalone) {
            eval = new AbaloneEvalFunction(board, player);
            score = eval.evaluate();
        }
        return score;
    }

    /**
     * Method that returns the possible moves from a given state
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
        else if (isAbalone){
            moves = new AbaloneGetPossibleMoves(board, player);
            out = moves.getPossibleMoves();
        }
        return out;
    }

//    /**
//     * method used to backtrack the win or loss information in subtree util the children of the root
//     * @param n starting node
//     */
//    public void backtrackWinOrLoss(Node n) {
//
//        while (getParent(n) != null) {
//            n.setIsDoneInSubTree(true);
//            n = getParent(n);
//        }
//    }

    /**
     * method to get the children in the tree of a certain node
     * @param v current node
     * @return a list a children
     */
    public ArrayList<Node> getChildren(Node v) { // String vertex
        // Returns all neighbours of a given vertex
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


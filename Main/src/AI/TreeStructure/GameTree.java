package AI.TreeStructure;

import AI.EvaluationFunction.Abalone.AbaloneEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.GameSelector;
import AI.PossibleMoves.AbaloneGetPossibleMoves;
import AI.PossibleMoves.PossibleMoves;
import AI.Util;
import AI.PossibleMoves.CheckersPossibleMoves;
import Abalone.GUI.Hexagon;
import Abalone.Game.Abalone;
import Checkers.GUI.GameUI;
import Checkers.Game.Checkers;

import java.util.*;

public class GameTree {

    /** possible moves selector */
    private PossibleMoves possibleMoves;
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
    /** list that contains the previous generation of nodes when building the game tree */
    private ArrayList<Node> previousGeneration = new ArrayList<>();
    /** list that contains all the current generation nodes -> temporary variable */
    private ArrayList<Node> currentGeneration = new ArrayList<>();
    /** total number of generation -> corresponding to the final depth of the tree */
    private int totalNumGeneration;
    /** current player to play */
    private int currentPlayer;
    /** current generation counter variable */
    private int generationCounter = 1;
    /** boolean variable to recognize the current game */
    private boolean isCheckers, isAbalone;

    private ArrayList<Node> rootChildrenNodes = new ArrayList<>();
    private ArrayList<int[][]> fourBestNodes = new ArrayList<>();
    private int totalDepth;

    /**
     * class constructor
     * @param game to create the game tree structure on
     * @param depth total deepness of the tree
     */
    public GameTree(GameSelector game, int depth) {

        this.game = game;
        this.totalNumGeneration = Math.min(depth, (15-GameUI.turnCounter));
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
    public GameTree(GameSelector game, int depth, double[] configuration) {

        this.game = game;
        this.totalNumGeneration = depth;
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

    public void createTreeDFS() {

        if (isCheckers) {
//            totalDepth = (15 - GameUI.turnCounter);
            totalDepth = Math.min(7, (15-GameUI.turnCounter));
            createTreeDFS(root, totalDepth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        else if (isAbalone){
//            totalDepth = (15 - Hexagon.turnCounter);
            totalDepth = Math.min(3, (15-Hexagon.turnCounter));
            createTreeDFS(root, totalDepth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        fourBestNodes = computeFourBestMoves(rootChildrenNodes);
    }

    public ArrayList<int[][]> computeFourBestMoves(ArrayList<Node> rootChildrenNodes) {

        int count = 0;
        for (Node n : rootChildrenNodes) {
            if (n.isDoneInSubTree()) {
                count++;
            }
        }
        while (fourBestNodes.size() < 4) {
            double bestScore = Double.NEGATIVE_INFINITY;
            int[][] bestMove = null;
            Node bestNode = null;
            for (Node n : rootChildrenNodes) {
                if (n.getScore() > bestScore) {
                    if (n.isDoneInSubTree() && count > 0) {
                        bestScore = n.getScore();
                        bestMove = n.getBoardState();
                        bestNode = n;
                    }
                    if (count == 0){
                        bestScore = n.getScore();
                        bestMove = n.getBoardState();
                        bestNode = n;
                    }
                }
            }
            if (bestNode == null && fourBestNodes.size() > 0) {
                bestMove = fourBestNodes.get(fourBestNodes.size()-1);
            }
            fourBestNodes.add(bestMove);
            rootChildrenNodes.remove(bestNode);
        }
        return fourBestNodes;
    }

    public double createTreeDFS(Node parent, int depth, boolean maximizingPlayer, double alpha, double beta) {

        int player;
        if (maximizingPlayer) {
            player = currentPlayer;
        }
        else {
            player = Util.changeCurrentPlayer(currentPlayer);
        }

        ArrayList<int[][]> children;

        if (depth == 0) {
            if (game.isDone(parent.getBoardState())) {
                backtrackWinOrLoss(parent);
            }
            if (getParent(parent) == nodes.get(0)) {
                Node node = new Node(parent.getBoardState(), parent.getScore());
                node.setIsDoneInSubTree(parent.isDoneInSubTree());
                rootChildrenNodes.add(node);
            }
            return parent.getScore();
        }
        else {
            if (game.isDone(parent.getBoardState())) {
                backtrackWinOrLoss(parent);
                return parent.getScore();
            }
            children = getPossibleMoves(parent.getBoardState(), player);
            if (children.size() == 0) {
                if (depth == totalDepth-1) {
                    Node node = new Node(parent.getBoardState(), parent.getScore());
                    node.setIsDoneInSubTree(parent.isDoneInSubTree());
                    rootChildrenNodes.add(node);
                }
                return parent.getScore();
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
                double evaluation = createTreeDFS(child, depth-1, false, alpha, beta);
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
                double evaluation = createTreeDFS(child, depth-1, true, alpha, beta);
                minEvaluation = Double.min(minEvaluation, evaluation);
                beta = Math.min(beta, evaluation);
                if (beta <= alpha) {
                    break;
                }
            }
            if (depth == totalDepth-1) {
                Node node = new Node(parent.getBoardState(), minEvaluation);
                node.setIsDoneInSubTree(parent.isDoneInSubTree());
                rootChildrenNodes.add(node);
            }
            return minEvaluation;
        }
    }

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

    /**
     * method used to backtrack the win or loss information in subtree util the children of the root
     * @param n starting node
     */
    public void backtrackWinOrLoss(Node n) {

        while (getParent(n) != null) {
            n.setIsDoneInSubTree(true);
            n = getParent(n);
        }
    }

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
     * getter for the total number of generation
     * @return the total number of generation
     */
    public int getTotalNumGeneration() {
        return totalNumGeneration;
    }

    public ArrayList<Node> getRootChildrenNodes() {
        return rootChildrenNodes;
    }

    public ArrayList<int[][]> getFourBestNodes() {
        return fourBestNodes;
    }
}


package AI.TreeStructure;

import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.GameSelector;
import AI.PossibleMoves.PossibleMoves;
import AI.Util;
import AI.PossibleMoves.CheckersPossibleMoves;
import Abalone.Game.Abalone;
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

    /**
     * class constructor
     * @param game to create the game tree structure on
     * @param depth total deepness of the tree
     */
    public GameTree(GameSelector game, int depth) {

        this.game = game;
        this.totalNumGeneration = depth;
        this.currentPlayer = game.getCurrentPlayer();
    }

    /**
     * class constructor
     * @param game to create the game tree structure on
     * @param depth total deepness of the tree
     * @param configuration weights to run the evaluation function with
     */
    public GameTree(GameSelector game, int depth, double[] configuration) {

        this.game = game;
        this.configuration = configuration;
        this.totalNumGeneration = depth;
        this.currentPlayer = game.getCurrentPlayer();
    }

    /**
     * main function of game tree creation
     * BFS way of constructing the game tree
     */
    public void createTree() {

        if (game.getClass().isInstance(new Checkers(null))) {
            root = new Node(game.getCheckersBoard().getGameBoard(), 0);
            nodes.add(root);
        }
        else if (game.getClass().isInstance(new Abalone(null))) {
            root = new Node(game.getAbaloneBoard().getGameBoard(), 0);
            nodes.add(root);
        }

        createChildren(root, root.getBoardState(), currentPlayer);

        while(generationCounter < totalNumGeneration){
            currentPlayer = Util.changeCurrentPlayer(currentPlayer);
            generationCounter++;
            for(Node n : previousGeneration) {
                if (!game.isDone(n.getBoardState())) {
                    createChildren(n, n.getBoardState(), currentPlayer);
                }
            }
            previousGeneration.clear();
            previousGeneration.addAll(currentGeneration);
            currentGeneration.clear();
        }
        System.out.println("Nodes in game tree = " + nodes.size());
    }

    /**
     * method that add the new children in the game tree
     * @param parent to whom children are created
     * @param currentBoardState of the current parent node
     * @param currentPlayer to play
     */
    public void createChildren(Node parent, int[][] currentBoardState, int currentPlayer){

        possibleMoves = new CheckersPossibleMoves(currentBoardState, currentPlayer);
        ArrayList<int[][]> childrenStates = possibleMoves.getPossibleMoves();

        for(int[][] child : childrenStates){
            EvaluationFunction eval;
            if (configuration != null) {
                eval = new CheckersEvalFunction(child, currentPlayer, configuration);
            }
            else {
                eval = new CheckersEvalFunction(child, currentPlayer);
            }
            double score = eval.evaluate();

            if (generationCounter == 1) {
                Node node = new Node(child, score);
                nodes.add(node);

                Edge edge = new Edge(parent, node);
                edges.add(edge);

                previousGeneration.add(node);
            }
            else {
                Node node = new Node(child, score);
                nodes.add(node);

                Edge edge = new Edge(parent, node);
                edges.add(edge);

                currentGeneration.add(node);
            }
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
     * @return the total number of generationvc
     */
    public int getTotalNumGeneration() {
        return totalNumGeneration;
    }
}


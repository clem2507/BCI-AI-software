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

    private PossibleMoves possibleMoves;
    private GameSelector game;
    private double[] configuration;

    private Node root;

    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Node> previousGeneration = new ArrayList<>();
    private ArrayList<Node> currentGeneration = new ArrayList<>();

    private int totalNumGeneration;
    private int currentPlayer;
    private int generationCounter = 1;

    public GameTree(GameSelector game, int depth) {

        this.game = game;
        this.totalNumGeneration = depth;
        this.currentPlayer = game.getCurrentPlayer();
    }

    public GameTree(GameSelector game, int depth, double[] configuration) {

        this.game = game;
        this.configuration = configuration;
        this.totalNumGeneration = depth;
        this.currentPlayer = game.getCurrentPlayer();
    }

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
//        System.out.println("Nodes in game tree = " + nodes.size());
    }

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

    public boolean adjacent(Node x, Node y)	{
        // Returns true when there’s an edge from x to y
        for(Edge e : edges){
            if (e.getSource() == x){
                if(e.getDestination() == y){
                    return true;
                }
            }else if (e.getSource() == y){
                if(e.getDestination() == x) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Node> getNeighbours(Node v) { // String vertex
        // Returns all neighbours of a given vertex
        List<Node> neighbours = new ArrayList<>();
        for (Edge e : edges){
            if(e.getSource() == v){
                neighbours.add(e.getDestination());
            }else if(e.getDestination() == v){
                neighbours.add(e.getSource());
            }
        }
        return neighbours;
    }

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

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public int getTotalNumGeneration() {
        return totalNumGeneration;
    }
}


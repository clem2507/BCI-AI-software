package AI.AlphaBetaTreeSearch;

import AI.TreeStructure.GameTree;
import AI.TreeStructure.Node;

import java.util.ArrayList;

public class ABTS {

    /** game tree on which run the algorithm */
    private GameTree tree;
    /** game tree total depth */
    private int totalDepth;
    /** list that contains the root children board and score */
    private ArrayList<Node> rootChildren;
    private ArrayList<Node> rootChildrenNodes = new ArrayList<>();
    /** the list of the four best node moves for a given position */
    private ArrayList<int[][]> fourBestNodes = new ArrayList<>();
    /** simple index counter variable */
    private int index = 0;
    /** integer variable that stands for the number visited nodes by the ABTS */
    private int investigatedNodes = 0;

    /**
     * main class constructor
     * @param gameTree stands for the structure on which the algorithm runs on
     */
    public ABTS(GameTree gameTree) {

        this.tree = gameTree;
        this.totalDepth = gameTree.getTotalNumGeneration();
    }

    /**
     * start method that launches the algorithm
     */
    public void start() {

        rootChildren = tree.getChildren(tree.getNodes().get(0));
        double bestOutcome = ab_minimax(tree.getNodes().get(0), totalDepth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        while (fourBestNodes.size() < 4) {
            double bestScore = Double.NEGATIVE_INFINITY;
            int[][] bestMove = null;
            Node bestNode = null;
            for (Node n : rootChildrenNodes) {
                if (n.getScore() > bestScore) {
                    bestScore = n.getScore();
                    bestMove = n.getBoardState();
                    bestNode = n;
                }
            }
//            System.out.println("bestScore = " + bestScore);
            if (bestNode == null && fourBestNodes.size() > 0) {
                bestMove = fourBestNodes.get(fourBestNodes.size()-1);
            }
            fourBestNodes.add(bestMove);
            rootChildrenNodes.remove(bestNode);
        }
//        System.out.println("investigatedNodes = " + investigatedNodes);
//        System.out.println();
    }

    /**
     * Alpha-Beta Tree Search (minimax tree traversal) algorithm recursive method
     * @param position current node position of the tree search
     * @param depth current depth at which the current node is located in the tree
     * @param maximizingPlayer is true if the process is located on the maximizing layer
     * @param alpha current best value of alpha
     * @param beta current best value of beta
     * @return the best score suggest by the algorithm as a next move
     */
    public double ab_minimax(Node position, int depth, boolean maximizingPlayer, double alpha, double beta) {

        ArrayList<Node> children;

        if (depth == 0) {
            return position.getScore();
        }
        else {
            children = tree.getChildren(position);
            if (children.size() == 0) {
                if (depth == totalDepth-1) {
                    Node node = new Node(rootChildren.get(index).getBoardState(), position.getScore());
                    rootChildrenNodes.add(node);
                    index++;
                }
                return position.getScore();
            }
        }

        if (maximizingPlayer) {
            double maxEvaluation = Double.NEGATIVE_INFINITY;
            for (Node child : children) {
                double evaluation = ab_minimax(child, depth-1, false, alpha, beta);
                maxEvaluation = Double.max(maxEvaluation, evaluation);
                alpha = Math.max(alpha, evaluation);
                if (beta <= alpha) {
                    break;
                }
                investigatedNodes++;
            }
            return maxEvaluation;
        }
        else {
            double minEvaluation = Double.POSITIVE_INFINITY;
            for (Node child : children) {
                double evaluation = ab_minimax(child, depth-1, true, alpha, beta);
                minEvaluation = Double.min(minEvaluation, evaluation);
                beta = Math.min(beta, evaluation);
                if (beta <= alpha) {
                    break;
                }
                investigatedNodes++;
            }
            if (depth == totalDepth-1) {
                Node node = new Node(rootChildren.get(index).getBoardState(), minEvaluation);
                rootChildrenNodes.add(node);
                index++;
            }
            return minEvaluation;
        }
    }

    /**
     * getter for the four best nodes list
     * @return fourBestNodes list
     */
    public ArrayList<int[][]> getFourBestNodes() {
        return fourBestNodes;
    }

    /**
     * getter for the total number of visited nodes
     * @return investigatedNodes integer variable
     */
    public int getInvestigatedNodes() {
        return investigatedNodes;
    }
}

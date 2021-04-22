package AI.AlphaBetaTreeSearch;

import AI.TreeStructure.GameTree;
import AI.TreeStructure.Node;

import java.util.ArrayList;

public class ABTS {

    /** game tree on which run the algorithm */
    private GameTree tree;
    /** game tree total depth */
    private int totalDepth;
    /** list that contains the root children board state and score */
    private ArrayList<Node> rootChildrenNodes = new ArrayList<>();
    /** the list of the four best node moves for a given position */
    private ArrayList<int[][]> fourBestNodes = new ArrayList<>();
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

        double bestOutcome = ab_minimax(tree.getNodes().get(0), totalDepth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        fourBestNodes = tree.getFourBestNodes();
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
            if (tree.getParent(position) == tree.getNodes().get(0)) {
                Node node = new Node(position.getBoardState(), position.getScore());
                node.setIsDoneInSubTree(position.isDoneInSubTree());
                rootChildrenNodes.add(node);
            }
            return position.getScore();
        }
        else {
            children = tree.getChildren(position);
            if (children.size() == 0) {
                if (depth == totalDepth-1) {
                    Node node = new Node(position.getBoardState(), position.getScore());
                    node.setIsDoneInSubTree(position.isDoneInSubTree());
                    rootChildrenNodes.add(node);
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
                Node node = new Node(position.getBoardState(), minEvaluation);
                node.setIsDoneInSubTree(position.isDoneInSubTree());
                rootChildrenNodes.add(node);
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

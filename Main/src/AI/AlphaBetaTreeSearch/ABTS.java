package AI.AlphaBetaTreeSearch;

import AI.TreeStructure.GameTree;
import AI.TreeStructure.Node;

import java.util.ArrayList;
import java.util.Arrays;

public class ABTS {

    private GameTree tree;
    private int totalDepth;
    private ArrayList<Node> rootChildren;
    private ArrayList<Node> rootChildrenNodesScore = new ArrayList<>();
    private ArrayList<int[][]> fourBestNodes = new ArrayList<>();
    private int index = 0;
    private int investigatedNodes = 0;

    public ABTS(GameTree gameTree) {

        this.tree = gameTree;
        this.totalDepth = gameTree.getTotalNumGeneration();
    }

    public void start() {

        rootChildren = tree.getChildren(tree.getNodes().get(0));
        double bestOutcome = ab_minimax(tree.getNodes().get(0), totalDepth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        while (fourBestNodes.size() < 4) {
            double bestScore = Double.NEGATIVE_INFINITY;
            int[][] bestMove = null;
            Node bestNode = null;
            for (Node n : rootChildrenNodesScore) {
                if (n.getScore() > bestScore) {
                    bestScore = n.getScore();
                    bestMove = n.getBoardState();
                    bestNode = n;
                }
            }
            if (bestNode == null) {
                bestMove = fourBestNodes.get(fourBestNodes.size()-1);
            }
            fourBestNodes.add(bestMove);
            rootChildrenNodesScore.remove(bestNode);
        }

    }

    public double ab_minimax(Node position, int depth, boolean maximizingPlayer, double alpha, double beta) {

        ArrayList<Node> children;

        if (depth == 0) {
            return position.getScore();
        }
        else {
            children = tree.getChildren(position);
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
                rootChildrenNodesScore.add(node);
                index++;
            }
            return minEvaluation;
        }
    }

    public ArrayList<int[][]> getFourBestNodes() {
        return fourBestNodes;
    }

    public int getInvestigatedNodes() {
        return investigatedNodes;
    }
}

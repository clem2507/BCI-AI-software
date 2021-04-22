package AI.EvaluationFunction.Abalone;

import AI.EvaluationFunction.EvaluationFunction;
import AI.PossibleMoves.AbaloneGetPossibleMoves;
import AI.PossibleMoves.PossibleMoves;
import AI.Util;

import java.util.ArrayList;

public class AbaloneEvalFunction extends EvaluationFunction {

    private ArrayList<int[][]> children;
    private int[][] board;
    private int currentPlayer;
    private double w1;
    private double w2;
    private double w3;
    private double w4;
    private double w5;

    public AbaloneEvalFunction(int[][] board, int currentPlayer, double[] configuration) {

        this.board = board;
        this.currentPlayer = currentPlayer;
        setWeights(configuration);
    }

    public AbaloneEvalFunction(int[][] board, int currentPlayer) {

        this.board = board;
        this.currentPlayer = currentPlayer;
        double[] bestConfig = new double[]{1, 0.2, 0.5, 0.1, 0.1};
        setWeights(bestConfig);
    }

    @Override
    public double evaluate() {

        int h1 = Util.countMarbles(board, currentPlayer) - Util.countMarbles(board, Util.changeCurrentPlayer(currentPlayer));
        int h2 = attackingPosition(board, currentPlayer) - attackingPosition(board, Util.changeCurrentPlayer(currentPlayer));
        int h3 = pushingOutPosition(board, currentPlayer) - pushingOutPosition(board, Util.changeCurrentPlayer(currentPlayer));
        int h4 = breakStrongGroupPattern(board, currentPlayer) - breakStrongGroupPattern(board, Util.changeCurrentPlayer(currentPlayer));
        int h5 = strengthenGroupPattern(board, currentPlayer) - strengthenGroupPattern(board, Util.changeCurrentPlayer(currentPlayer));

        return (w1*h1) + (w2*h2) + (w3*h3) + (w4*h4) + (w5*h5);
    }

    private int attackingPosition(int[][] board, int currentPlayer) {

        PossibleMoves possibleMoves = new AbaloneGetPossibleMoves(board, currentPlayer);
        children = possibleMoves.getPossibleMoves();
        int count = 0;
        int opponentPlayer = Util.changeCurrentPlayer(currentPlayer);
        for (int[][] child : children) {
            if (!checkOpponentMarble(board, child, opponentPlayer)) {
                count++;
            }
        }
        return count;
    }

    public boolean checkOpponentMarble(int[][] board, int[][] currentBoard, int opponentPlayer) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == opponentPlayer && currentBoard[i][j] != opponentPlayer) {
                    return false;
                }
            }
        }
        return true;
    }

    private int pushingOutPosition(int[][] board, int currentPlayer) {

        PossibleMoves possibleMoves = new AbaloneGetPossibleMoves(board, currentPlayer);
        children = possibleMoves.getPossibleMoves();
        int count = 0;
        int opponentPlayer = Util.changeCurrentPlayer(currentPlayer);
        for(int[][] child : children){
            if(Util.countMarbles(board, opponentPlayer) > Util.countMarbles(child, opponentPlayer)){
                count++;
            }
        }
        return count;
    }

    private int breakStrongGroupPattern(int[][] board, int currentPlayer) {

        int opponentPlayer = Util.changeCurrentPlayer(currentPlayer);
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 1; j < board[0].length-1; j++) {
                if (board[i][j] == currentPlayer) {
                    if (i == 0 || i == 8) {
                        if ((board[i][j-1] == opponentPlayer) && (board[i][j+1] == opponentPlayer)) {
                            count++;
                        }
                    }
                    if (i != 4) {
                        if (i >= 1 && i <= 7) {
                            if (i <= 3) {
                                if ((board[i - 1][j - 1] == opponentPlayer) && (board[i + 1][j + 1]) == opponentPlayer) {
                                    count++;
                                }
                            }
                            if (i >= 5) {
                                if ((board[i - 1][j + 1] == opponentPlayer) && (board[i + 1][j - 1]) == opponentPlayer) {
                                    count++;
                                }
                            }
                            if ((board[i - 1][j] == opponentPlayer) && (board[i + 1][j]) == opponentPlayer) {
                                count++;
                            }
                            if ((board[i][j - 1] == opponentPlayer) && (board[i][j + 1]) == opponentPlayer) {
                                count++;
                            }
                        }
                    }
                    else {
                        if ((board[i-1][j] == opponentPlayer) && (board[i+1][j-1]) == opponentPlayer) {
                            count++;
                        }
                        if ((board[i][j-1] == opponentPlayer) && (board[i][j+1]) == opponentPlayer) {
                            count++;
                        }
                        if ((board[i-1][j-1] == opponentPlayer) && (board[i+1][j]) == opponentPlayer) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public int strengthenGroupPattern(int[][] board, int currentPlayer) {

        int opponentPlayer = Util.changeCurrentPlayer(currentPlayer);
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 1; j < board[0].length-1; j++) {
                if (board[i][j] == currentPlayer) {
                    if (i == 0 || i == 8) {
                        if (((board[i][j-1] == opponentPlayer) && (board[i][j+1] == currentPlayer))
                                || ((board[i][j-1] == currentPlayer) && (board[i][j+1] == opponentPlayer))) {
                            count++;
                        }
                    }
                    if (i != 4) {
                        if (i >= 1 && i <= 7) {
                            if (i <= 3) {
                                if (((board[i-1][j-1] == opponentPlayer) && (board[i+1][j+1]) == currentPlayer)
                                        || ((board[i-1][j-1] == opponentPlayer) && (board[i+1][j+1]) == opponentPlayer)) {
                                    count++;
                                }
                            }
                            if (i >= 5) {
                                if (((board[i-1][j+1] == currentPlayer) && (board[i+1][j-1]) == opponentPlayer)
                                        || ((board[i-1][j+1] == opponentPlayer) && (board[i+1][j-1]) == currentPlayer)) {
                                    count++;
                                }
                            }
                            if (((board[i-1][j] == currentPlayer) && (board[i+1][j]) == opponentPlayer)
                                    || ((board[i-1][j] == opponentPlayer) && (board[i+1][j]) == currentPlayer)) {
                                count++;
                            }
                            if (((board[i][j-1] == currentPlayer) && (board[i][j+1]) == opponentPlayer)
                                    || ((board[i][j-1] == opponentPlayer) && (board[i][j+1]) == currentPlayer)) {
                                count++;
                            }
                        }
                    }
                    else {
                        if (((board[i-1][j] == currentPlayer) && (board[i+1][j-1]) == opponentPlayer)
                                || ((board[i-1][j] == opponentPlayer) && (board[i+1][j-1]) == currentPlayer)) {
                            count++;
                        }
                        if (((board[i][j-1] == currentPlayer) && (board[i][j+1]) == opponentPlayer)
                                || ((board[i][j-1] == opponentPlayer) && (board[i][j+1]) == currentPlayer)) {
                            count++;
                        }
                        if (((board[i-1][j-1] == currentPlayer) && (board[i+1][j]) == opponentPlayer)
                                || ((board[i-1][j-1] == opponentPlayer) && (board[i+1][j]) == currentPlayer)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private void setWeights(double[] configuration) {

        w1 = configuration[0];
        w2 = configuration[1];
        w3 = configuration[2];
        w4 = configuration[3];
        w5 = configuration[4];
    }
}

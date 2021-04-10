package AI.EvaluationFunction.Checkers;

import AI.EvaluationFunction.EvaluationFunction;
import AI.Util;
import Abalone.Game.MoveDirection;

public class CheckersEvalFunction extends EvaluationFunction {

    private int[][] rootBoard;
    private int currentPlayer;
    private double[] configuration;

    private double w1;
    private double w2;
    private double w3;
    private double w4;
    private double w5;
    private double w6;

    public CheckersEvalFunction(int[][] rootBoard, int currentPlayer, double[] configuration) {

        this.rootBoard = rootBoard;
        this.currentPlayer = currentPlayer;
        this.configuration = configuration;
        setWeights(configuration);
    }

    public CheckersEvalFunction(int[][] rootBoard, int currentPlayer) {

        this.rootBoard = rootBoard;
        this.currentPlayer = currentPlayer;

        double[] bestConfig = new double[]{109, 1723, -1927, 98, 442, 168};
        setWeights(bestConfig);
    }

    @Override
    public double evaluate() {

        int h1 = attackingPosition(currentPlayer) - attackingPosition(Util.changeCurrentPlayer(currentPlayer));
        int h2 = victoriousPosition(currentPlayer);
        int h3 = victoriousPosition(Util.changeCurrentPlayer(currentPlayer));
        int h4 = distanceToTheOtherSide(Util.changeCurrentPlayer(currentPlayer)) - distanceToTheOtherSide(currentPlayer);
        int h5 = Util.countMarbles(rootBoard, currentPlayer) - Util.countMarbles(rootBoard, Util.changeCurrentPlayer(currentPlayer));
        int h6 = aba_PatternCount(currentPlayer) - aba_PatternCount(Util.changeCurrentPlayer(currentPlayer));

        return (w1*h1) + (w2*h2) + (w3*h3) + (w4*h4) + (w5*h5) + (w6*h6);
    }

    public void setWeights(double[] configuration) {

        w1 = configuration[0];
        w2 = configuration[1];
        w3 = configuration[2];
        w4 = configuration[3];
        w5 = configuration[4];
        w6 = configuration[5];
    }

    public int attackingPosition(int player) {

        int count = 0;
        for (int i = 1; i < rootBoard.length-1; i++) {
            for (int j = 1; j < rootBoard.length-1; j++) {
                if (rootBoard[i][j] == player) {
                    count+=checkFourDirections(i, j, player);
                }
            }
        }
        return count;
    }

    public int checkFourDirections(int i, int j, int player) {

        int count = 0;
        MoveDirection[] directions = {MoveDirection.TOP_LEFT, MoveDirection.TOP_RIGHT, MoveDirection.BOTTOM_RIGHT, MoveDirection.BOTTOM_LEFT};
        for (MoveDirection dir : directions) {
            switch (dir) {
                case TOP_LEFT:
                    if (player == 1) {
                        if (rootBoard[i-1][j-1] == Util.changeCurrentPlayer(player)) {
                            if (rootBoard[i-2][j-2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
                case TOP_RIGHT:
                    if (player == 1) {
                        if (rootBoard[i-1][j+1] == Util.changeCurrentPlayer(player)) {
                            if (rootBoard[i-2][j+2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
                case BOTTOM_RIGHT:
                    if (player == 2) {
                        if (rootBoard[i+1][j+1] == Util.changeCurrentPlayer(player)) {
                            if (rootBoard[i+2][j+2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
                case BOTTOM_LEFT:
                    if (player == 2) {
                        if (rootBoard[i+1][j-1] == Util.changeCurrentPlayer(player)) {
                            if (rootBoard[i+2][j-2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
            }
        }
        return count;
    }

    public int victoriousPosition(int player) {

        return isVictorious(rootBoard, player);
    }

    public int isVictorious(int[][] board, int player) {

        if (player==1) {
            for (int i = 1; i < board.length-1; i++) {
                if (board[1][i] == 1) {
                    return 1;
                }
            }
        }
        else {
            for (int i = 1; i < board.length-1; i++) {
                if (board[board.length-2][i] == 2) {
                    return 1;
                }
            }
        }
        if (Util.countMarbles(board, Util.changeCurrentPlayer(player)) == 0) {
            return 1;
        }
        return 0;
    }

    public int distanceToTheOtherSide(int player) {

        switch (player) {
            case 1:
                for (int i = 1; i < rootBoard.length-1; i++) {
                    for (int j = 1; j < rootBoard.length-1; j++) {
                        if (rootBoard[i][j] == player) {
                            return i-1;
                        }
                    }
                }
                break;
            case 2:
                for (int i = rootBoard.length-2; i > 0; i--) {
                    for (int j = rootBoard.length-2; j > 0 ; j--) {
                        if (rootBoard[i][j] == player) {
                            return rootBoard.length-2-i;
                        }
                    }
                }
                break;
        }
        return 0;
    }

    private int aba_PatternCount(int currentPlayer) {

        int count = 0;
        for (int i = 1; i < rootBoard.length-1; i++) {
            for (int j = 1; j < rootBoard.length-1; j++) {
                if (rootBoard[i][j] == currentPlayer) {
                    count += checkPatternInSquare(i, j, currentPlayer);
                }
            }
        }
        return count;
    }

    public int checkPatternInSquare(int i, int j, int player) {

        int count = 0;
        if (rootBoard[i+1][j-1] == Util.changeCurrentPlayer(player) && rootBoard[i-1][j+1] == Util.changeCurrentPlayer(player)) {
            count++;
        }
        if (rootBoard[i-1][j-1] == Util.changeCurrentPlayer(player) && rootBoard[i+1][j+1] == Util.changeCurrentPlayer(player)) {
            count++;
        }
        return count;
    }
}

package AI.EvaluationFunction.Checkers;

import AI.EvaluationFunction.EvaluationFunction;
import AI.Util;
import Abalone.Game.MoveDirection;

public class CheckersEvalFunction extends EvaluationFunction {

    /** current root game board */
    private int[][] rootBoard;
    /** current player to play */
    private int currentPlayer;
    /** different weights for the evaluation function */
    private double w1;
    private double w2;
    private double w3;
    private double w4;
    private double w5;
    private double w6;

    /**
     * class first constructor
     * @param rootBoard current root game board
     * @param currentPlayer to play
     * @param configuration weights for the evaluation function
     */
    public CheckersEvalFunction(int[][] rootBoard, int currentPlayer, double[] configuration) {

        this.rootBoard = rootBoard;
        this.currentPlayer = currentPlayer;
        setWeights(configuration);
    }

    /**
     * class second constructor
     * @param rootBoard current root game board
     * @param currentPlayer to play
     */
    public CheckersEvalFunction(int[][] rootBoard, int currentPlayer) {

        this.rootBoard = rootBoard;
        this.currentPlayer = currentPlayer;

        double[] bestConfigNotComplete = new double[]{0.3676439321801084, 0.34927666124021406, -0.5096797020134634, 0.9393348144299724, 0.39430677233420064, 0.5610948160176769};
        setWeights(bestConfigNotComplete);

//        double[] bestConfigComplete = new double[]{0.43513680781989394, 0.16110031633119282, -0.29187530553211827, 0.9050849723766596, 0.10650690656051254, 0.6613394768774338};
//        setWeights(bestConfigComplete);
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

    /**
     * setter to tune the evaluation functions weights
     * @param configuration corresponding values for weights
     */
    public void setWeights(double[] configuration) {

        w1 = configuration[0];
        w2 = configuration[1];
        w3 = configuration[2];
        w4 = configuration[3];
        w5 = configuration[4];
        w6 = configuration[5];
    }

    /**
     * method that computes the total number of attacking positions for a given player
     * @param player current player
     * @return the total count of such that position
     */
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

    /**
     * checks the the # of attacking positions for a given marbles in its square
     * @param i position on board
     * @param j position on board
     * @param player current player
     * @return the counter
     */
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

    /**
     * @param player current player
     * @return true of the board is victorious for that particular player
     */
    public int victoriousPosition(int player) {

        return isVictorious(rootBoard, player);
    }

    /**
     * @param player current player
     * @return true of the board is victorious for that particular player
     */
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

    /**
     * method that computes the distance between the closer marble to the other side to of the current player
     * @param player current player
     * @return the distance to the other side (considering that it has to make moves to reach the other side)
     */
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

    /**
     * method that computes the total amount of pattern where one current player marbles is situated between of the other player
     * @param currentPlayer to play
     * @return the counter
     */
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

    /**
     * checks for the previous method pattern in marble square
     * @param i position on board
     * @param j position on board
     * @param player to play
     * @return the counter
     */
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

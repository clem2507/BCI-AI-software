package AI.EvaluationFunction.Checkers;

import AI.EvaluationFunction.EvaluationFunction;
import AI.PossibleMoves.CheckersPossibleMoves;
import AI.PossibleMoves.PossibleMoves;
import AI.Util;
import Checkers.Game.MoveDirection;

import java.util.ArrayList;

public class CheckersEvalFunction extends EvaluationFunction {

    /** current game board */
    private int[][] board;
    /** current player to play */
    private int currentPlayer;
    /** different weights for the evaluation function */
    private double w1;
    private double w2;
    private double w3;
    private double w4;
    private double w5;
    private double w6;
    private double w7;
    private double w8;

    /**
     * class first constructor
     * @param board current game board
     * @param currentPlayer to play
     * @param configuration weights for the evaluation function
     */
    public CheckersEvalFunction(int[][] board, int currentPlayer, double[] configuration) {

        this.board = board;
        this.currentPlayer = currentPlayer;
        setWeights(configuration);
    }

    /**
     * class second constructor
     * @param board current game board
     * @param currentPlayer to play
     */
    public CheckersEvalFunction(int[][] board, int currentPlayer) {

        this.board = board;
        this.currentPlayer = currentPlayer;
        double[] bestConfig = new double[]{0.6471705360866706, 0.3, 0.2, 0.8227407520078385, 0.44559445326033087, 0.7313818909336706, 0.5238725061974904, 0.6250817564796307};
        setWeights(bestConfig);
    }

    /**
     * @return the evaluation score from the state
     */
    @Override
    public double evaluate() {

        int h1 = attackingPosition(currentPlayer) - attackingPosition(Util.changeCurrentPlayer(currentPlayer));
        int h2 = possibleVictoriousPositionCount(currentPlayer);
        int h3 = possibleDefeatPositionCount(currentPlayer);
        int h4 = victoriousPosition(currentPlayer);
        int h5 = victoriousPosition(Util.changeCurrentPlayer(currentPlayer));
        int h6 = distanceToTheOtherSide(Util.changeCurrentPlayer(currentPlayer)) - distanceToTheOtherSide(currentPlayer);
        int h7 = Util.countMarbles(board, currentPlayer) - Util.countMarbles(board, Util.changeCurrentPlayer(currentPlayer));
        int h8 = aba_PatternCount(currentPlayer) - aba_PatternCount(Util.changeCurrentPlayer(currentPlayer));

        return (w1*h1) + (w2*h2) - (w3*h3) + (w4*h4) - (w5*h5) + (w6*h6) + (w7*h7) + (w8*h8);
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
        w7 = configuration[6];
        w8 = configuration[7];
    }

    /**
     * method that computes the total number of attacking positions for a given player
     * @param player current player
     * @return the total count of such that position
     */
    public int attackingPosition(int player) {

        int count = 0;
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                if (board[i][j] == player) {
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
                        if (board[i-1][j-1] == Util.changeCurrentPlayer(player)) {
                            if (board[i-2][j-2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
                case TOP_RIGHT:
                    if (player == 1) {
                        if (board[i-1][j+1] == Util.changeCurrentPlayer(player)) {
                            if (board[i-2][j+2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
                case BOTTOM_RIGHT:
                    if (player == 2) {
                        if (board[i+1][j+1] == Util.changeCurrentPlayer(player)) {
                            if (board[i+2][j+2] == 0) {
                                count++;
                            }
                        }
                    }
                    break;
                case BOTTOM_LEFT:
                    if (player == 2) {
                        if (board[i+1][j-1] == Util.changeCurrentPlayer(player)) {
                            if (board[i+2][j-2] == 0) {
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
     * counter or possible next state winning position
     * @param currentPlayer to play
     * @return the actual count
     */
    private int possibleVictoriousPositionCount(int currentPlayer) {

        PossibleMoves possibleMoves = new CheckersPossibleMoves(board, currentPlayer);
        ArrayList<int[][]> children = possibleMoves.getPossibleMoves();
        int count = 0;
        for (int[][] child : children) {
            if (isVictorious(child, currentPlayer) == 1) {
                count++;
            }
        }
        return count;
    }

    /**
     * counter or possible next state lost position
     * @param currentPlayer to play
     * @return the actual count
     */
    private int possibleDefeatPositionCount(int currentPlayer) {

        PossibleMoves possibleMoves = new CheckersPossibleMoves(board, Util.changeCurrentPlayer(currentPlayer));
        ArrayList<int[][]> children = possibleMoves.getPossibleMoves();
        int count = 0;
        for (int[][] child : children) {
            if (isVictorious(child, Util.changeCurrentPlayer(currentPlayer)) == 1) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param player current player
     * @return true of the board is victorious for that particular player
     */
    public int victoriousPosition(int player) {

        return isVictorious(board, player);
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
        PossibleMoves possibleMoves = new CheckersPossibleMoves(board, Util.changeCurrentPlayer(player));
        if (possibleMoves.getPossibleMoves().size() == 0) {
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
                for (int i = 1; i < board.length-1; i++) {
                    for (int j = 1; j < board.length-1; j++) {
                        if (board[i][j] == player) {
                            return i-1;
                        }
                    }
                }
                break;
            case 2:
                for (int i = board.length-2; i > 0; i--) {
                    for (int j = board.length-2; j > 0 ; j--) {
                        if (board[i][j] == player) {
                            return board.length-2-i;
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
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                if (board[i][j] == currentPlayer) {
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
        if (board[i+1][j-1] == Util.changeCurrentPlayer(player) && board[i-1][j+1] == Util.changeCurrentPlayer(player)) {
            count++;
        }
        if (board[i-1][j-1] == Util.changeCurrentPlayer(player) && board[i+1][j+1] == Util.changeCurrentPlayer(player)) {
            count++;
        }
        return count;
    }
}

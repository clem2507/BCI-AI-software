package AI.PossibleMoves;

import AI.Util;
import Checkers.Game.Checkers;
import Checkers.Game.Move;
import Checkers.Game.MoveDirection;

import java.util.ArrayList;

public class CheckersPossibleMoves extends PossibleMoves {

    private int[][] currentBoard;
    private int currentPlayer;
    private MoveDirection[] directionArray = {MoveDirection.TOP_LEFT, MoveDirection.BOTTOM_LEFT, MoveDirection.TOP_RIGHT, MoveDirection.BOTTOM_RIGHT};

    public CheckersPossibleMoves(int[][] currentBoard, int currentPlayer) {

        this.currentBoard = currentBoard;
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<int[][]> getPossibleMoves() {
        ArrayList<int[][]> movesArray = new ArrayList<>();
        if (!Util.isDone(currentBoard)) {
            for (int i = 1; i < currentBoard.length - 1; i++) {
                for (int j = 1; j < currentBoard.length - 1; j++) {
                    if (currentBoard[i][j] == currentPlayer) {
                        for (MoveDirection dir : directionArray) {
                            Move move = new Move(i, j, dir, currentBoard, currentPlayer);
                            if (move.checkMove()) {
                                movesArray.add(move.getNewState());
                            }
                        }
                    }
                }
            }
        }
        return movesArray;
    }

    public ArrayList<int[][]> getPossibleMovesFromMarble(int i, int j) {
        ArrayList<int[][]> movesArray = new ArrayList<>();
        for (MoveDirection dir : directionArray) {
            Move move = new Move(i, j, dir, currentBoard, Checkers.currentPlayer);
            if (move.checkMove()) {
                movesArray.add(move.getNewState());
            }
        }
        return movesArray;
    }
}

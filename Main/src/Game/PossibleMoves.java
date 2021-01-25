package Game;

import Output.Test;

import java.util.ArrayList;

public class PossibleMoves {

    private int[][] currentBoard;
    private int currentPlayer;
    private MoveDirection[] directionArray = {MoveDirection.TOP_LEFT, MoveDirection.LEFT, MoveDirection.BOTTOM_LEFT, MoveDirection.TOP_RIGHT, MoveDirection.RIGHT, MoveDirection.BOTTOM_RIGHT, MoveDirection.FORWARD, MoveDirection.BACKWARD};

    public PossibleMoves(int[][] currentBoard, int currentPlayer) {

        this.currentBoard = currentBoard;
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<int[][]> getPossibleMoves() {

        ArrayList<int[][]> movesArray = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
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
        return movesArray;
    }
}

package Game;

import java.util.ArrayList;

public class PossibleMoves {

    private Board currentBoard;
    private int currentPlayer;
    private MoveDirection[] directionArray = {MoveDirection.TOP_LEFT, MoveDirection.LEFT, MoveDirection.BOTTOM_LEFT, MoveDirection.TOP_RIGHT, MoveDirection.RIGHT, MoveDirection.BOTTOM_RIGHT, MoveDirection.FORWARD, MoveDirection.BACKWARD};

    public PossibleMoves(Board currentBoard, int currentPlayer) {

        this.currentBoard = currentBoard;
        this.currentPlayer = currentPlayer;
    }

    // TODO: Fix this method
    public ArrayList<Board> getPossibleMoves() {

        ArrayList<Board> movesArray = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (MoveDirection dir : directionArray) {
                    Move move = new Move(i, j, dir, currentBoard, currentPlayer);
                    if (move.checkMove()) {
                        Board board = new Board(move.getNewState());
                        movesArray.add(board);
                    }
                }
            }
        }
        return movesArray;
    }
}

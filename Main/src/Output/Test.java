package Output;

import Game.Board;
import Game.Move;
import Game.MoveDirection;
import Game.PossibleMoves;

import java.util.ArrayList;

public class Test {

    private static int[][] gameBoard = new int[][]{

            {2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1}

    };

    public static void printBoard(int[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[]args) {

        int currentPlayer = 1;
        Board board = new Board(gameBoard);
        System.out.println();
        printBoard(board.getGameBoard());
        System.out.println();
        Move move = new Move(4, 0, MoveDirection.FORWARD, board, currentPlayer);
        if (move.checkMove()) {
            move.makeMove();
        }
        printBoard(board.getGameBoard());
        System.out.println();
        PossibleMoves possibleMoves = new PossibleMoves(board, currentPlayer);
        ArrayList<Board> possibleBoard = possibleMoves.getPossibleMoves();
        for (Board b : possibleBoard) {
            printBoard(b.getGameBoard());
            System.out.println();
        }
    }
}

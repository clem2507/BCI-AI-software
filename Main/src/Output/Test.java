package Output;

import Game.Board;
import Game.Move;
import Game.MoveDirection;
import Game.PossibleMoves;

import java.util.ArrayList;

public class Test {

    public static Board board;

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
        board = new Board();
        System.out.println();
        printBoard(board.getGameBoard());
        System.out.println();
        Move move = new Move(4, 0, MoveDirection.FORWARD, board.getGameBoard(), currentPlayer);
        if (move.checkMove()) {
            move.makeMove();
        }
        printBoard(board.getGameBoard());
        System.out.println();
        System.out.println("-----------------");
        System.out.println();
        PossibleMoves possibleMoves = new PossibleMoves(board.getGameBoard(), currentPlayer);
        ArrayList<int[][]> possibleBoard = possibleMoves.getPossibleMoves();
        for (int[][] b : possibleBoard) {
            printBoard(b);
            System.out.println();
        }
    }
}

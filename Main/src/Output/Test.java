package Output;

import Game.Board;

public class Test {

    public static Board board;

    private static int[][] gameBoard = new int[][]{

            {3, 3, 3, 3, 3, 3, 3},
            {3, 2, 0, 2, 0, 2, 3},
            {3, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 3},
            {3, 1, 0, 1, 0, 1, 3},
            {3, 3, 3, 3, 3, 3, 3}

    };

    public static void printBoard(boolean[][] board) {

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
//        System.out.println();
//        printBoard(board.getGameBoard());
//        System.out.println();
//        Move move = new Move(5, 1, MoveDirection.FORWARD, board.getGameBoard(), currentPlayer);
//        if (move.checkMove()) {
//            board.setBoard(move.getNewState());
//        }
//        printBoard(board.getGameBoard());
//        System.out.println();
//        System.out.println("-----------------");
//        System.out.println();
//        PossibleMoves possibleMoves = new PossibleMoves(board.getGameBoard(), currentPlayer);
//        ArrayList<int[][]> possibleBoard = possibleMoves.getPossibleMovesFromMarble(5, 3);
//        for (int[][] b : possibleBoard) {
//            printBoard(b);
//            System.out.println();
//        }
    }
}

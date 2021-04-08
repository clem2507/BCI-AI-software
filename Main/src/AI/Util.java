package AI;

public class Util {

    public static int changeCurrentPlayer(int currentPlayer) {

        if (currentPlayer == 1) {
            return 2;
        }
        else {
            return 1;
        }
    }

    public static int countMarbles(int[][] board, int player) {

        int count = 0;
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                if (board[i][j]==player) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void printBoard(int[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

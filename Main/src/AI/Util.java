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

    public static boolean isDone(int[][] board) {

        for (int i = 1; i < board.length-1; i++) {
            if (board[1][i] == 1) {
                return true;
            }
        }
        for (int i = 1; i < board.length-1; i++) {
            if (board[board.length-2][i] == 2) {
                return true;
            }
        }
        if (Util.countMarbles(board, 1) == 0 || Util.countMarbles(board, 2) == 0) {
            return true;
        }
        return false;
    }
}
package Game;

public class Checkers {

    private Board board;
    public static int currentPlayer = 1;

    public Checkers(Board board) {

        this.board = board;
    }

    public void runGame() {

        while(!isVictorious(board.getGameBoard())) {

        }
    }

    public boolean isVictorious(int[][] board) {

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
        return false;
    }
}

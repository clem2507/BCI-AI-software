package Game;

import AI.MCTS;

public class Checkers {

    private Board board;
    private int timer = 2000;
    private int sampleSize = 10;
    public static int currentPlayer = 1;

    public Checkers(Board board) {

        this.board = board;
    }

    public void runGame() {

        while(!isVictorious(board.getGameBoard())) {

//            MCTS mcts = new MCTS(board.getGameBoard(), currentPlayer, timer, sampleSize);
//            mcts.start();
//            board.setBoard(mcts.getBestMove());
//            board.drawAllMarbles();
//
//            if (currentPlayer == 1) {
//                currentPlayer = 2;
//            }
//            else {
//                currentPlayer = 1;
//            }
        }
    }

    public static boolean isVictorious(int[][] board) {

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

package Game;

import AI.MCTS;
import GUI.GameUI;

import java.util.ArrayList;

public class Checkers {

    private Board board;
    private int timer = 2000;
    private int sampleSize = 10;
    public static int currentPlayer = 1;

    private boolean flag = true;
    private ArrayList<int[][]> fourBestMoves;

    public Checkers(Board board) {

        this.board = board;
    }

    public void runGame() {

    }

    public void runMCTS() {

        MCTS mcts = new MCTS(board.getGameBoard(), currentPlayer, timer, sampleSize);
        mcts.start();
        //board.setBoard(mcts.getBestMove());
        fourBestMoves = mcts.getFourBestNodes();
        board.drawAllMarbles();
        GameUI.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
        //board.drawPossibleMovesFromMCTS(fourBestMoves, false);
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

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public ArrayList<int[][]> getFourBestMoves() {
        return fourBestMoves;
    }

    public void setFourBestMoves(ArrayList<int[][]> fourBestMoves) {
        this.fourBestMoves = fourBestMoves;
    }
}

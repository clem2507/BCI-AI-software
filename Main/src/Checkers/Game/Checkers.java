package Checkers.Game;

import AI.GameSelector;
import AI.MCTS;
import Abalone.Game.BoardUI;
import Checkers.GUI.GameUI;

import java.util.ArrayList;

public class Checkers extends GameSelector {

    private Board board;
    public static int currentPlayer = 1;
    private boolean flag = true;
    private ArrayList<int[][]> fourBestMoves;

    public Checkers(Board board) {

        this.board = board;
    }

    public void runMCTS() {

        MCTS mcts = new MCTS(this);
        mcts.start();
        fourBestMoves = mcts.getFourBestNodes();
        board.drawAllMarbles();
        GameUI.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
    }

    @Override
    public boolean isDone(int[][] board) {

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
        if (countMarbles(board, 1) == 0 || countMarbles(board, 2) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isVictorious(int[][] board, int player) {

        if (player==1) {
            for (int i = 1; i < board.length-1; i++) {
                if (board[1][i] == 1) {
                    return true;
                }
            }
        }
        else {
            for (int i = 1; i < board.length-1; i++) {
                if (board[board.length-2][i] == 2) {
                    return true;
                }
            }
        }
        if (countMarbles(board, player) == 0) {
            return false;
        }
        return false;
    }

    public int countMarbles(int[][] board, int player) {

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

    @Override
    public Board getCheckersBoard() {
        return board;
    }

    @Override
    public BoardUI getAbaloneBoard() {
        return null;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }
}

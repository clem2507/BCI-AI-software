package Checkers.Game;

import AI.GameSelector;
import AI.MonteCarloTreeSearch.MCTS;
import AI.AlphaBetaTreeSearch.ABTS;
import AI.PossibleMoves.CheckersPossibleMoves;
import AI.PossibleMoves.PossibleMoves;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.Game.BoardUI;
import Checkers.GUI.GameUI;

import java.util.ArrayList;

public class Checkers extends GameSelector {

    private Board board;
    public static int currentPlayer = 1;
    private ArrayList<Node> fourBestMoves;

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

    public void runABTS() {

        ABTS ABTS = new ABTS(this, 5);
        ABTS.start();
        fourBestMoves = ABTS.getFourBestNodes();
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
        if (Util.countMarbles(board, 1) == 0 || Util.countMarbles(board, 2) == 0) {
            return true;
        }
        PossibleMoves possibleMoves1 = new CheckersPossibleMoves(board, 1);
        PossibleMoves possibleMoves2 = new CheckersPossibleMoves(board, 2);
        if (possibleMoves1.getPossibleMoves().size() == 0 || possibleMoves2.getPossibleMoves().size() == 0) {
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
        if (Util.countMarbles(board, Util.changeCurrentPlayer(player)) == 0) {
            return true;
        }
        PossibleMoves possibleMoves = new CheckersPossibleMoves(board, Util.changeCurrentPlayer(player));
        if (possibleMoves.getPossibleMoves().size() == 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Node> getFourBestMoves() {
        return fourBestMoves;
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

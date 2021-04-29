package Abalone.Game;

import AI.GameSelector;
import AI.MonteCarloTreeSearch.MCTS;
import AI.AlphaBetaTreeSearch.ABTS;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.GUI.Hexagon;
import Checkers.Game.Board;

import java.util.ArrayList;

public class Abalone extends GameSelector {

    private BoardUI board;
    private ArrayList<Node> fourBestMoves;
    public static int currentPlayer = 1;
    private Player[] player = new Player[2];

    public Abalone(BoardUI board, Player p1, Player p2) {

        this.board = board;
        this.player[0] = p1;
        this.player[1] = p2;
        this.player[0].setBoard(board);
        this.player[1].setBoard(board);
        this.player[0].setTurn(1);
        this.player[1].setTurn(2);
    }

    public Abalone(BoardUI board) {

        this.board = board;
    }

    public void runMCTS() {

        MCTS mcts = new MCTS(this);
        mcts.start();
        fourBestMoves = mcts.getFourBestNodes();
        board.drawAllCells();
        Hexagon.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
    }

    public void runABTS() {

        ABTS ABTS = new ABTS(this, 3);
        ABTS.start();
        fourBestMoves = ABTS.getFourBestNodes();
        board.drawAllCells();
        Hexagon.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Board getCheckersBoard() {
        return null;
    }

    @Override
    public BoardUI getAbaloneBoard() {
        return board;
    }

    @Override
    public boolean isDone(int[][] actualBoard) {
        return Util.countMarbles(actualBoard, 1) != Util.countMarbles(actualBoard, 2);
    }

    @Override
    public boolean isVictorious(int[][] actualBoard, int player) {
        return Util.countMarbles(actualBoard, player) > Util.countMarbles(actualBoard, Util.changeCurrentPlayer(player));
    }

    public ArrayList<Node> getFourBestMoves() {
        return fourBestMoves;
    }
}

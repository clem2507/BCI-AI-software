package Abalone.Game;

import AI.AlphaBetaTreeSearch.ABTS;
import AI.GameSelector;
import AI.MonteCarloTreeSearch.MCTS;
import AI.TreeStructure.GameTree;
import AI.Util;
import Abalone.GUI.Hexagon;
import Checkers.Game.Board;

import java.util.ArrayList;

public class Abalone extends GameSelector {

    private BoardUI board;
    private ArrayList<int[][]> fourBestMoves;
    public static int currentPlayer = 1;
    private int indexMove = 0;
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

        GameTree gameTree = new GameTree(this, 3);
//        gameTree.createTreeBFS();
//        ABTS abts = new ABTS(gameTree);
//        abts.start();
//        fourBestMoves = abts.getFourBestNodes();
        gameTree.createTreeDFS();
        fourBestMoves = gameTree.getFourBestNodes();
        board.drawAllCells();
        Hexagon.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
    }

    public void makeMove() {

//            int p = 2;
        try {
            System.out.println("1");
            Move mv = player[indexMove & 1].collectMove();
            System.out.println("2");
            mv.board = board.getBoard();
            System.out.println("3");
            Rules checkRules = new Rules(mv);
            System.out.println("4");
            checkRules.move();
            System.out.println("5");

//                if (checkRules.checkMove(mv.pushing, mv.dir, mv.board, mv.turn)) {
//                    checkRules.move();
//                } else {
//                    p = 1;
//                    numberOfTurn--;
//                }

        } catch (InterruptedException e) {
            System.out.println("concurrency problem, aborting...");
            System.exit(0);
        }
        indexMove++;
        currentPlayer = Util.changeCurrentPlayer(currentPlayer);
//        whosePlaying.setText(getCurrentPlayerColor(Abalone.currentPlayer) + " to play");
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

    public ArrayList<int[][]> getFourBestMoves() {
        return fourBestMoves;
    }
}

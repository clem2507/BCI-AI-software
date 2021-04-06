package Abalone.Game;

import AI.GameSelector;
import Checkers.Game.Board;

public class Abalone extends GameSelector {

    private BoardUI board;

    public Abalone(BoardUI board) {

        this.board = board;
    }

    @Override
    public int getCurrentPlayer() {
        return 0;
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
        int countP1 = 0;
        int countP2 = 0;
        for (int i = 0; i < actualBoard.length; i++) {
            for (int j = 0; j < actualBoard[0].length; j++) {
                if (actualBoard[i][j] == 1) {
                    countP1++;
                }
                if (actualBoard[i][j] == 2) {
                    countP2++;
                }
            }
        }
        if (countP1 <= 8 || countP2 <= 8) {
            return true;
        }
        else {
            return false;
        }
    }

    // TODO - Needs to be implemented
    @Override
    public boolean isVictorious(int[][] actualBoard, int player) {
        return false;
    }
}

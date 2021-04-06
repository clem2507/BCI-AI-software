package AI;

import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;

public abstract class GameSelector {

    public abstract int getCurrentPlayer();
    public abstract BoardUI getAbaloneBoard();
    public abstract Board getCheckersBoard();
    public abstract boolean isDone(int[][] actualBoard);
    public abstract boolean isVictorious(int[][] actualBoard, int player);
}

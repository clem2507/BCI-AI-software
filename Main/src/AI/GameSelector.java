package AI;

import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import President.Game.Player;
import President.Game.Tuple;

public abstract class GameSelector {

    public abstract int getCurrentPlayer();
    public abstract Tuple getCurrentGameState();
    public abstract BoardUI getAbaloneBoard();
    public abstract Board getCheckersBoard();
    public abstract boolean isDone(int[][] actualBoard);
    public abstract boolean isVictorious(int[][] actualBoard, int player);
    public abstract boolean isDone(Player player1, Player player2);
    public abstract boolean isVictorious(Player player);
    public abstract void setAdaptiveVariable(int[][] previousBoard, int[][] currentBoard);
    public abstract double getAdaptiveVariable();
}

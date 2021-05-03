package AI;

import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import President.Game.Card;
import President.Game.Player;
import President.Game.Tuple;

import java.util.ArrayList;

public abstract class GameSelector {

    public abstract int getCurrentPlayer();
    public abstract Tuple getCurrentGameState();
    public abstract BoardUI getAbaloneBoard();
    public abstract Board getCheckersBoard();
    public abstract boolean isDone(int[][] actualBoard);
    public abstract boolean isVictorious(int[][] actualBoard, int player);
    public abstract boolean isDone(Player player1, Player player2);
    public abstract boolean isVictorious(Player player);
    public abstract Player getPlayer1();
    public abstract Player getPlayer2();
    public abstract ArrayList<Card> getGameDeck();
    public abstract void setAdaptiveVariable(int[][] previousBoard, int[][] currentBoard);
    public abstract double getAdaptiveVariable();
}

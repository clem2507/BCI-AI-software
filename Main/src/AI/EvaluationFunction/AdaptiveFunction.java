package AI.EvaluationFunction;

import AI.EvaluationFunction.President.PresidentEvalFunction;
import AI.GameSelector;
import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;

import java.util.ArrayList;

public class AdaptiveFunction extends GameSelector {

    private GameSelector game;

    private double adaptiveVariablePlayer1;
    private double adaptiveVariablePlayer2;

    public AdaptiveFunction(GameSelector game) {

        this.game = game;
        updateAdaptiveVariable();
    }

    public void updateAdaptiveVariable() {

        EvaluationFunction evaluationFunction;
        if (game.getClass().isInstance(new President(0))) {
            evaluationFunction = new PresidentEvalFunction(game.getPlayer1());
            adaptiveVariablePlayer1 = evaluationFunction.evaluate();
            evaluationFunction = new PresidentEvalFunction(game.getPlayer2());
            adaptiveVariablePlayer2 = evaluationFunction.evaluate();
        }
    }

    public double getGlobalAdaptiveFunction() {

        return adaptiveVariablePlayer1 - adaptiveVariablePlayer2;
    }

    @Override
    public int getCurrentPlayer() {
        return 0;
    }

    @Override
    public Tuple getCurrentGameState() {
        return null;
    }

    @Override
    public BoardUI getAbaloneBoard() {
        return null;
    }

    @Override
    public Board getCheckersBoard() {
        return null;
    }

    @Override
    public boolean isDone(int[][] actualBoard) {
        return false;
    }

    @Override
    public boolean isVictorious(int[][] actualBoard, int player) {
        return false;
    }

    @Override
    public boolean isDone(Player player1, Player player2) {
        return false;
    }

    @Override
    public boolean isVictorious(Player player) {
        return false;
    }

    @Override
    public Player getPlayer1() {
        return null;
    }

    @Override
    public Player getPlayer2() {
        return null;
    }

    @Override
    public ArrayList<Card> getGameDeck() {
        return null;
    }

    @Override
    public void setAdaptiveVariable(int[][] previousBoard, int[][] currentBoard) {

    }

    @Override
    public double getAdaptiveVariable(int player) {

        if (player == 1) {
            return adaptiveVariablePlayer1;
        }
        else {
            return adaptiveVariablePlayer2;
        }
    }
}

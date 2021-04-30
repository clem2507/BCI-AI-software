package President.Game;

import AI.GameSelector;
import Abalone.Game.BoardUI;
import Checkers.Game.Board;

import java.util.ArrayList;
import java.util.Random;

public class President extends GameSelector {

    private ArrayList<Card> deck;

    private Player player1;
    private Player player2;

    private Tuple gameState;

    public President() {

        this.deck = new ArrayList<>();
        this.initializeDeck();
        this.player1 = new Player(setInitialPlayerDeck(deck));
        this.player1.isToPlay(true);
        this.player2 = new Player(setInitialPlayerDeck(deck));
        this.player2.isToPlay(false);
        this.gameState = new Tuple(0, 0);
    }

    public void initializeDeck() {

        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j <= 4; j++) {
                Card card = new Card(i);
                deck.add(card);
            }
        }
    }

    public ArrayList<Card> setInitialPlayerDeck(ArrayList<Card> deck) {

        ArrayList<Card> newDeck = new ArrayList<>();
        while (newDeck.size() < 26) {
            Random random = new Random();
            int index = random.nextInt(deck.size());
            newDeck.add(deck.get(index));
            deck.remove(index);
        }
        return newDeck;
    }

    @Override
    public boolean isDone(Player player1, Player player2) {
        return player1.getDeck().size()==0 || player2.getDeck().size()==0;
    }

    @Override
    public boolean isVictorious(Player player) {
        return player.getDeck().size()==0;
    }

    @Override
    public Tuple getCurrentGameState() {
        return gameState;
    }

    @Override
    public void setAdaptiveVariable(int[][] previousBoard, int[][] currentBoard) {

    }

    @Override
    public double getAdaptiveVariable() {
        return 0;
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
    public int getCurrentPlayer() {
        return 0;
    }

    @Override
    public BoardUI getAbaloneBoard() {
        return null;
    }

    @Override
    public Board getCheckersBoard() {
        return null;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setGameState(Tuple gameState) {
        this.gameState = gameState;
    }
}

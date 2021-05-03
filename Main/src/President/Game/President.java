package President.Game;

import AI.GameSelector;
import AI.MonteCarloTreeSearch.MCTS;
import AI.TreeStructure.Node;
import Abalone.Game.BoardUI;
import Checkers.GUI.GameUI;
import Checkers.Game.Board;
import President.GUI.UserInterface;

import java.util.ArrayList;
import java.util.Random;

public class President extends GameSelector {

    private ArrayList<Card> gameDeck;
    private ArrayList<Card> deck;
    private ArrayList<Node> fourBestActions;


    private Player player1;
    private Player player2;

    private Tuple gameState;

    private int numberOfCards;

    public President(int deckSize) {

        this.numberOfCards = deckSize;
        this.gameDeck = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.deck = initializeDeck();
        this.gameState = new Tuple(0, 0);
        this.player1 = new Player(setInitialPlayerDeck(deck), gameDeck, gameState, numberOfCards, true, 1);
        this.player2 = new Player(setInitialPlayerDeck(deck), gameDeck, gameState, numberOfCards, false, 2);
        this.deck.clear();
    }

    public void runMCTS() {

        MCTS mcts = new MCTS(this);
        mcts.start();
        fourBestActions = mcts.getFourBestNodes();
        UserInterface.readyText.setText("Ready!\n\nChoose between action\n1, 2, 3 or 4\n\nPress SPACE to update game");
    }

    public void runABTS() {

    }

    public static ArrayList<Card> initializeDeck() {

        ArrayList<Card> out = new ArrayList<>();
        for (int i = 2; i <= 14; i++) {
            for (int j = 1; j <= 4; j++) {
                Card card = new Card(i, j);
                out.add(card);
            }
        }
        return out;
    }

    public ArrayList<Card> setInitialPlayerDeck(ArrayList<Card> deck) {

        ArrayList<Card> newDeck = new ArrayList<>();
        while (newDeck.size() < numberOfCards) {
            Random random = new Random();
            int index = random.nextInt(deck.size());
            newDeck.add(deck.get(index));
            deck.remove(index);
        }
        return newDeck;
    }

    @Override
    public boolean isDone(Player player1, Player player2) {
        return player1.getDeck().size()<=0 || player2.getDeck().size()<=0;
    }

    @Override
    public boolean isVictorious(Player player) {
        return player.getDeck().size()<=0;
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

    @Override
    public Player getPlayer1() {
        return player1;
    }

    @Override
    public Player getPlayer2() {
        return player2;
    }

    @Override
    public ArrayList<Card> getGameDeck() {
        return deck;
    }

    public void setGameState(Tuple gameState) {
        this.gameState = gameState;
    }

    public ArrayList<Node> getFourBestActions() {
        return fourBestActions;
    }
}

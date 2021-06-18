package President.Game;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> deck;
    private ArrayList<Card> gameDeck;
    private Tuple gameState;
    private int opponentNumberCards;
    private int id;
    private boolean toPlay;

    public Player(ArrayList<Card> deck, ArrayList<Card> gameDeck, Tuple gameState, int opponentNumberCards, boolean toPlay, int id) {

        this.deck = deck;
        this.gameDeck = gameDeck;
        this.gameState = gameState;
        this.opponentNumberCards = opponentNumberCards;
        this.toPlay = toPlay;
        this.id = id;
    }

    public Player(Player copy) {

        this.deck = copy.getDeck();
        this.gameDeck = copy.getGameDeck();
        this.gameState = copy.getGameState();
        this.opponentNumberCards = copy.getOpponentNumberCards();
        this.toPlay = copy.toPlay;
        this.id = copy.id;
    }

    public void takeAction(Tuple action, ArrayList<Card> deck) {

        int count = 0;
        boolean currToPlay = false;
        ArrayList<Card> temp1 = new ArrayList<>(deck);
        ArrayList<Card> temp2 = new ArrayList<>(this.gameDeck);
        this.gameState = action;
        for (Card card : deck) {
            if (count < action.getOccurrence()) {
                if (toPlay) {
                    if (card.getNumber() == action.getNumber()) {
                        if (card.getNumber() == 2) {
                            currToPlay = true;
                            temp1.remove(card);
                            this.deck = temp1;
                            temp2.add(card);
                            this.gameState = new Tuple(2, 1);
                            break;
                        } else {
                            currToPlay = false;
                            temp1.remove(card);
                            temp2.add(card);
                            count++;
                        }
                        this.deck = temp1;
                    }
                }
                else {
                    if (card.getNumber() == action.getNumber()) {
                        if (action.getNumber() == 2) {
                            currToPlay = false;
                            this.opponentNumberCards--;
                            temp2.add(card);
                            this.gameState = new Tuple(2, 1);
                            break;
                        } else {
                            currToPlay = true;
                            this.opponentNumberCards--;
                            temp2.add(card);
                            count++;
                        }
                    }
                }
            }
        }
        this.isToPlay(currToPlay);
        this.gameDeck = temp2;
    }

    public ArrayList<Tuple> getSortedDeck(ArrayList<Card> deck) {

        ArrayList<Tuple> out = new ArrayList<>();
        for (int i = 2; i <= 14; i++) {
            int count = 0;
            for (Card card : deck) {
                if (card.getNumber() == i) {
                    count++;
                }
            }
            if (count != 0) {
                out.add(new Tuple(i, count));
            }
        }
        return out;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getGameDeck() {
        return gameDeck;
    }

    public void setGameDeck(ArrayList<Card> gameDeck) {
        this.gameDeck = gameDeck;
    }

    public Tuple getGameState() {
        return gameState;
    }

    public void setGameState(Tuple gameState) {
        this.gameState = gameState;
    }

    public int getOpponentNumberCards() {
        return opponentNumberCards;
    }

    public void setOpponentNumberCards(int opponentNumberCards) {
        this.opponentNumberCards = opponentNumberCards;
    }

    public boolean toPlay() {
        return toPlay;
    }

    public void isToPlay(boolean toPlay) {
        this.toPlay = toPlay;
    }

    public int getId() {
        return id;
    }
}

package President.Game;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> deck;
    private boolean toPlay;
    private int number;

    public Player(ArrayList<Card> deck, int number) {

        this.deck = deck;
        this.number = number;
    }

    public void takeAction(int number, int occurrence) {

        int count = 0;
        ArrayList<Card> temp = new ArrayList<>(deck);
        for (Card card : deck) {
            if (count < occurrence) {
                if (card.getNumber() == number) {
                    temp.remove(card);
                    count++;
                }
            }
        }
        this.deck = temp;
    }

    public ArrayList<Tuple> getSortedDeck() {

        ArrayList<Tuple> out = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            int count = 0;
            for (Card card : deck) {
                if (card.getNumber() == i) {
                    count++;
                }
            }
            out.add(new Tuple(i, count));
        }
        return out;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public boolean toPlay() {
        return toPlay;
    }

    public void isToPlay(boolean toPlay) {
        this.toPlay = toPlay;
    }

    public int getNumber() {
        return number;
    }
}

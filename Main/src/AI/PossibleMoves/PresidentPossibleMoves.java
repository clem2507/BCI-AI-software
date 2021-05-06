package AI.PossibleMoves;

import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;

import java.util.ArrayList;
import java.util.Random;

public class PresidentPossibleMoves extends PossibleMoves {

    private Player player;
    private Tuple gameState;
    private ArrayList<Card> gameDeck;
    private ArrayList<Card> informationSetCards;

    public PresidentPossibleMoves(Player player) {

        this.player = player;
        this.gameState = player.getGameState();
        this.gameDeck = player.getGameDeck();
    }

    @Override
    public ArrayList<Tuple> getPossibleActions() {

        return computeActions(player.getDeck());
    }

    @Override
    public ArrayList<Tuple> getInformationSet(ArrayList<Card> deck) {

        return computeActions(deck);
    }

    public ArrayList<Tuple> computeActions(ArrayList<Card> deck) {

        if (gameState.getNumber() == 2 && gameState.getOccurrence() == 1) {
            return player.getSortedDeck(deck);
        }
        ArrayList<Tuple> out = new ArrayList<>();
        for (Tuple tuple : player.getSortedDeck(deck)) {
            if (tuple.getNumber() == 2 && tuple.getOccurrence() > 0) {
                out.add(new Tuple(2, 1));
                continue;
            }
            if (tuple.getNumber() > gameState.getNumber()) {
                int possActionsOnTuple = (tuple.getOccurrence() - gameState.getOccurrence()) + 1;
                if (possActionsOnTuple > 0) {
                    int count = 0;
                    while (count < possActionsOnTuple) {
                        out.add(new Tuple(tuple.getNumber(), gameState.getOccurrence() + count));
                        count++;
                    }
                }
            }
        }
        return out;
    }

    public ArrayList<Card> computeInformationSetCards() {

        ArrayList<Card> informationDeck = new ArrayList<>();
        ArrayList<Card> fullDeck = President.initializeDeck();
        int count = 0;
        for (Card card : fullDeck) {
            boolean check = false;
            for (Card playerCard : player.getDeck()) {
                if ((card.getNumber() == playerCard.getNumber()) && (card.getSymbol() == playerCard.getSymbol())) {
                    check = true;
                    count++;
                    break;
                }
            }
            if (!check) {
                for (Card deckCard : gameDeck) {
                    if ((card.getNumber() == deckCard.getNumber()) && (card.getSymbol() == deckCard.getSymbol())) {
                        check = true;
                        count++;
                        break;
                    }
                }
                if (!check) {
                    informationDeck.add(card);
                }
            }
        }
        informationSetCards = new ArrayList<>();
        while (informationSetCards.size() < player.getOpponentNumberCards()) {
            Random random = new Random();
            int randomIndex = random.nextInt(informationDeck.size());
            informationSetCards.add(informationDeck.get(randomIndex));
            informationDeck.remove(randomIndex);
        }
        return informationSetCards;
    }

    @Override
    public ArrayList<int[][]> getPossibleMoves() {
        return null;
    }
}

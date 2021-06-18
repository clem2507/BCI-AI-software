package AI.EvaluationFunction.President;

import AI.EvaluationFunction.EvaluationFunction;
import President.Game.Card;
import President.Game.Player;
import President.Game.Tuple;

import java.util.ArrayList;

public class PresidentEvalFunction extends EvaluationFunction {

    /** Player to evaluate on the current game state */
    private Player player;
    /** Player deck of cards */
    private ArrayList<Card> playerDeck;
    /** different weights for the evaluation function */
    private double w1;
    private double w2;
    private double w3;

    /**
     * class constructor for the president evaluation function
     * @param player to evaluate
     */
    public PresidentEvalFunction(Player player) {

        this.player = player;
        this.playerDeck = player.getDeck();
        double[] bestConfigNotComplete = new double[]{0.8777166075606919, 0.706566807355142, 0.04648898522118283};
        setWeights(bestConfigNotComplete);
    }

    /**
     * class constructor for the president evaluation function
     * @param player to evaluate
     * @param configuration of weights
     */
    public PresidentEvalFunction(Player player, double[] configuration) {

        this.player = player;
        this.playerDeck = player.getDeck();
        setWeights(configuration);
    }

    /**
     * @return the evaluation score from the state
     */
    @Override
    public double evaluate() {

        int h1 = player.getOpponentNumberCards() - player.getDeck().size();
        int h2 = count2Cards();
        int h3 = computeCardValues();

        return (w1*h1) + (w2*h2) + (w3*h3);
    }

    /**
     * setter to tune the evaluation functions weights
     * @param config corresponding values for weights
     */
    public void setWeights(double[] config) {

        w1 = config[0];
        w2 = config[1];
        w3 = config[2];
    }

    /**
     * method that count the number of value 2 card occurrences
     * @return the actual count
     */
    public int count2Cards() {

        int count = 0;
        for (Card card : playerDeck) {
            if (card.getNumber() == 2) {
                count++;
            }
        }
        return count;
    }

    /**
     * method used to compute a deck of cards values
     * @return the card deck values
     */
    public int computeCardValues() {

        int count = 0;
        for (Tuple tuple : player.getSortedDeck(playerDeck)) {
            if (tuple.getNumber() != 2) {
                count += tuple.getNumber() * tuple.getOccurrence();
            }
        }
        return count;
    }
}

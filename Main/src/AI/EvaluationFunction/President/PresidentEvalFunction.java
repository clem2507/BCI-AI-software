package AI.EvaluationFunction.President;

import AI.EvaluationFunction.EvaluationFunction;
import AI.PossibleMoves.PossibleMoves;
import AI.PossibleMoves.PresidentPossibleMoves;
import President.Game.Card;
import President.Game.Player;
import President.Game.Tuple;

import java.util.ArrayList;

public class PresidentEvalFunction extends EvaluationFunction {

    private Player player;
    private ArrayList<Card> playerDeck;

    private PossibleMoves possibleMoves;
    private ArrayList<Tuple> actions;

    private double w1;
    private double w2;
    private double w3;

    private double[] configuration;

    public PresidentEvalFunction(Player player) {

        this.player = player;
        this.possibleMoves = new PresidentPossibleMoves(player);
        if (player.toPlay()) {
            this.playerDeck = player.getDeck();
            this.actions = possibleMoves.getPossibleActions();
        }
        else {
            this.playerDeck = possibleMoves.computeInformationSetCards();
            this.actions = possibleMoves.getInformationSet(playerDeck);
        }
//        double[] bestConfigNotComplete = new double[]{0.7747813139987886, 0.6160252690314191, 0.6488176227531302};
        double[] bestConfigNotComplete = new double[]{0.8777166075606919, 0.706566807355142, 0.04648898522118283};
        setWeights(bestConfigNotComplete);
    }

    public PresidentEvalFunction(Player player, double[] configuration) {

        this.player = player;
        this.possibleMoves = new PresidentPossibleMoves(player);
        this.configuration = configuration;
        if (player.toPlay()) {
            this.playerDeck = player.getDeck();
            this.actions = possibleMoves.getPossibleActions();
        }
        else {
            this.playerDeck = possibleMoves.computeInformationSetCards();
            this.actions = possibleMoves.getInformationSet(playerDeck);
        }
        setWeights(configuration);
    }

    @Override
    public double evaluate() {

        int h1;
        if (player.toPlay()) {
            h1 = player.getOpponentNumberCards() - player.getDeck().size();
        }
        else {
            h1 = player.getDeck().size() - player.getOpponentNumberCards();
        }
        int h2 = count2Cards();
        int h3 = computeCardValues();

//        System.out.println();
//        System.out.println("h1 = " + (w1*h1));
//        System.out.println("h2 = " + (w2*h2));
//        System.out.println("h3 = " + (w3*h3));

        return (w1*h1) + (w2*h2) + (w3*h3);
    }

    public void setWeights(double[] config) {

        w1 = config[0];
        w2 = config[1];
        w3 = config[2];
    }

    public int count2Cards() {

        int count = 0;
        for (Card card : playerDeck) {
            if (card.getNumber() == 2) {
                count++;
            }
        }
        return count;
    }

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

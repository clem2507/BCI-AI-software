package AI.EvaluationFunction.GA;

import Checkers.Game.Checkers;
import President.Game.President;

/**
 * Testing class for the GA
 */
public class TournamentPlay {

    public static void main(String[]args) {

        TournamentGeneticAlgorithm checkers_ga = new TournamentGeneticAlgorithm(new Checkers(null), 8, 10, 0, (int) Math.pow(2, 8), 0.01, 0.6);
        checkers_ga.start();
        TournamentGeneticAlgorithm president_ga = new TournamentGeneticAlgorithm(new President(14), 3, 10, 0, (int) Math.pow(2, 8), 0.01, 0.6);
        president_ga.start();
    }
}

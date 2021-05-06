package AI.EvaluationFunction.GA;

import Abalone.Game.Abalone;
import Checkers.Game.Checkers;
import President.Game.President;

public class TournamentPlay {

    public static void main(String[]args) {

//        TournamentGeneticAlgorithm ga = new TournamentGeneticAlgorithm(new Checkers(null), 8, 10, 0, (int) Math.pow(2, 7), 0.01, 0.6);
//        TournamentGeneticAlgorithm ga = new TournamentGeneticAlgorithm(new Abalone(null), 4, 5, 0, (int) Math.pow(2, 4), 0.01, 0.7);
//        TournamentGeneticAlgorithm ga = new TournamentGeneticAlgorithm(new President(14), 3, 10, 0, (int) Math.pow(2, 8), 0.01, 0.6);
//        TournamentGeneticAlgorithm gaWithFile = new TournamentGeneticAlgorithm("res/OutputResults/Checkers/6MarblesNotComplete/MatingPoolGeneration15.csv", new Checkers(null), 5, 16, (int) Math.pow(2, 7), 0.01, 0.7);
        TournamentGeneticAlgorithm ga = new TournamentGeneticAlgorithm(new President(0), 2, 50);
//        ga.start();
        ga.startLeague();
    }
}

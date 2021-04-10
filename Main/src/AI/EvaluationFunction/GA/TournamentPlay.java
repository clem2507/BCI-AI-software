package AI.EvaluationFunction.GA;

import Checkers.Game.Checkers;

public class TournamentPlay {

    public static void main(String[]args) {

        TournamentGeneticAlgorithm ga = new TournamentGeneticAlgorithm(new Checkers(null), 6, 10, 0, (int) Math.pow(2, 5), 0.01, 0.7);
        TournamentGeneticAlgorithm gaWithFile = new TournamentGeneticAlgorithm("res/OutputResults/Checkers/6MarblesNotComplete/MatingPoolGeneration15.csv", new Checkers(null), 5, 16, (int) Math.pow(2, 7), 0.01, 0.7);
        ga.start();

    }
}

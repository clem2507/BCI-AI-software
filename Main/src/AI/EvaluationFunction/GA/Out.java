package AI.EvaluationFunction.GA;

import Checkers.Game.Checkers;

import java.util.Arrays;
import java.util.Random;

public class Out {

    public static void main(String[]args) {

        TournamentGeneticAlgorithm ga = new TournamentGeneticAlgorithm(new Checkers(null), 6, 15, (int) Math.pow(2, 8), 0.05, 0.8);
        ga.start();

    }
}

package AI.EvaluationFunction.GA;

import AI.AlphaBetaTreeSearch.ABTS;
import AI.GameSelector;
import AI.TreeStructure.GameTree;
import AI.Util;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import Output.OutputCSV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class TournamentGeneticAlgorithm {

    private GameSelector game;
    private Random random;
    private ArrayList<Item> population;
    private ArrayList<Item> matingPool;
    private ArrayList<Item[]> tournament;
    private int genotypeSize;
    private int populationSize;
    private int generationSize;
    private int numberOfTournaments;
    private int numberOfParticipantsPerTournament;
    private int totalGenerationNumber;
    private double mutationRate;
    private double crossoverRate;

    public TournamentGeneticAlgorithm(GameSelector game, int genotypeSize, int generationSize, int numberOfTournaments, double mutationRate, double crossoverRate) {

        this.game = game;
        this.random = new Random();
        this.population = new ArrayList<>();
        this.genotypeSize = genotypeSize;
        this.generationSize = generationSize;
        this.numberOfTournaments = numberOfTournaments;
        this.numberOfParticipantsPerTournament = (int) Math.pow(2, 4);
        this.populationSize = (numberOfParticipantsPerTournament*numberOfTournaments);
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
    }

    public void start() {

        System.out.println();
        System.out.println("----- SETUP -----");
        System.out.println();
        System.out.println("Mutation rate = " + mutationRate);
        System.out.println("Crossover rate = " + crossoverRate);
        System.out.println();
        System.out.println("Population Size = " + populationSize);
        System.out.println("Total # of generations = " + generationSize);
        System.out.println("Total # of tournaments = " + numberOfTournaments);
        System.out.println("Total # of participants per tournament = " + numberOfParticipantsPerTournament);
        System.out.println();
        System.out.println("----- START -----");
        initializePopulation();
        while (totalGenerationNumber < generationSize) {
            System.out.println();
            System.out.println("--> Generation = " + (totalGenerationNumber+1));
            System.out.println();
            tournamentSelection();
            play();
            System.out.println();
            System.out.println("--> mating pool = ");
            System.out.println();
            getMatingPoolGenotypes();
            reproduction();
            totalGenerationNumber++;
        }
        System.out.println("----- END -----");
        System.out.println();
        System.out.println("--> Best two genotypes : ");
        System.out.println();
        System.out.println("----> " + Arrays.toString(getBestGenotype()));
    }

    public void initializePopulation() {

        for (int i = 0; i < populationSize; i++) {
            Item item = new Item(createRandomGenotype());
            population.add(item);
        }
    }

    public double[] createRandomGenotype() {

        double[] genotype = new double[genotypeSize];
        for (int i = 0; i < genotypeSize; i++) {
            if (i==2) {
                genotype[i] = -(random.nextDouble());
            }
            else {
                genotype[i] = random.nextDouble();
            }
        }
        return genotype;
    }
    
    public void tournamentSelection() {
        
        int count = 0;
        tournament = new ArrayList<>();
        for (int i = 0; i < numberOfTournaments; i++) {
            Item[] league = new Item[numberOfParticipantsPerTournament];
            int index = 0;
            for (int j = 0; j < numberOfParticipantsPerTournament; j++) {
                int rand = random.nextInt(populationSize-count);
                league[index] = population.get(rand);
                population.remove(rand);
                count++;
                index++;
            }
            tournament.add(league);
        }
    }
    
    public void play() {

        matingPool = new ArrayList<>();
        for (int i = 0; i < tournament.size(); i++) {
            System.out.println("tournament = " + (i+1));
            ArrayList<Item> temp = new ArrayList<>();
            Collections.addAll(temp, tournament.get(i));
            matingPool.add(playTournament(temp)[0]);
            matingPool.add(playTournament(temp)[1]);
        }
    }

    public Item[] playTournament(ArrayList<Item> list) {

        int count = 0;
        int numberOfParticipant = list.size();
        while (list.size() > 2) {
            for (int j = 0; j < numberOfParticipant/Math.pow(2,count); j+=2) {
                simulateGames(list.get(j), list.get(j+1));
            }
            list.removeIf(item -> !item.getWinner());
            count++;
        }
        return new Item[]{list.get(0), list.get(1)};
    }

    public void simulateGames(Item participant1, Item participant2) {

        Board board = null;
        if (game.getClass().isInstance(new Checkers(null))) {
            board = new Board(6, false);
            game = new Checkers(board);
        }
        int currentPlayer = random.nextInt(2)+1;
        while (!game.isDone(board.getGameBoard())) {
            GameTree gameTree;
            if (currentPlayer == 1) {
                gameTree = new GameTree(game, 5, participant1.getGenotype());
            }
            else {
                gameTree = new GameTree(game, 5, participant2.getGenotype());
            }
            gameTree.setCurrentPlayer(currentPlayer);
            gameTree.createTree();
            ABTS abts = new ABTS(gameTree);
            abts.start();
            double moveChoice = random.nextDouble();
            if (moveChoice < 0.55) {
                if (abts.getFourBestNodes().get(0) != null) {
                    board.setBoard(abts.getFourBestNodes().get(0));
                }
            }
            else if (moveChoice < 0.75) {
                if (abts.getFourBestNodes().get(1) != null) {
                    board.setBoard(abts.getFourBestNodes().get(1));
                }
            }
            else if (moveChoice < 0.9) {
                if (abts.getFourBestNodes().get(2) != null) {
                    board.setBoard(abts.getFourBestNodes().get(2));
                }
            }
            else {
                if (abts.getFourBestNodes().get(3) != null) {
                    board.setBoard(abts.getFourBestNodes().get(3));
                }
            }
            currentPlayer = Util.changeCurrentPlayer(currentPlayer);
        }
        if (game.isVictorious(board.getGameBoard(), 1)) {
            participant1.setWinner(true);
            participant2.setWinner(false);
        }
        else {
            participant1.setWinner(false);
            participant2.setWinner(true);
        }
    }

    public void reproduction() {

        population = new ArrayList<>();
        while (population.size() < populationSize) {
            int r1 = random.nextInt(matingPool.size());
            int r2 = random.nextInt(matingPool.size());
            while (r1 == r2) {
                r2 = random.nextInt(matingPool.size());
            }
            Item[] children = crossover(matingPool.get(r1), matingPool.get(r2));
            children = mutation(children);
            population.add(children[0]);
            population.add(children[1]);
        }
    }

    public Item[] crossover(Item parent1, Item parent2) {

        Item child1 = new Item(parent1.getGenotype());
        Item child2 = new Item(parent2.getGenotype());
        Item[] children = new Item[]{child1, child2};
        Item[] parents = new Item[]{parent1, parent2};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < genotypeSize; j++) {
                if (random.nextDouble() < crossoverRate) {
                    if (i == 0) {
                        children[i].getGenotype()[j] = parents[i+1].getGenotype()[j];
                    } else {
                        children[i].getGenotype()[j] = parents[i-1].getGenotype()[j];
                    }
                }
            }
        }
        return children;
    }

    public Item[] mutation(Item[] children) {

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < genotypeSize; j++) {
                if (random.nextDouble() < mutationRate) {
                    if (j==2) {
                        children[i].getGenotype()[j] = -(random.nextDouble());
                    }
                    else {
                        children[i].getGenotype()[j] = random.nextDouble();
                    }
                }
            }
        }
        return children;
    }

    public double[] getBestGenotype() {

        tournamentSelection();
        play();
        playTournament(matingPool);
        OutputCSV out = new OutputCSV("BestTwoGenotypes.csv", "BestTwoGenotypes");
        String[][] data = new String[2][genotypeSize];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < genotypeSize; j++) {
                data[i][j] = String.valueOf(matingPool.get(i).getGenotype()[j]);
            }
        }
        out.writeResume(data);
        return matingPool.get(0).getGenotype();
    }

    public void getMatingPoolGenotypes() {

        OutputCSV out = new OutputCSV("MatingPoolGeneration"+ (totalGenerationNumber + 1) +".csv", "Generation " + (totalGenerationNumber + 1));
        String[][] data = new String[matingPool.size()][genotypeSize];
        int row = 0;
        for (Item item : matingPool) {
            System.out.println(Arrays.toString(item.getGenotype()));
            for (int i = 0; i < genotypeSize; i++) {
                data[row][i] = String.valueOf(item.getGenotype()[i]);
            }
            row++;
        }
        out.writeResume(data);
    }
}

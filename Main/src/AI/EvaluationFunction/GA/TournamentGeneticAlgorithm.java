package AI.EvaluationFunction.GA;

import AI.AlphaBetaTreeSearch.ABTS;
import AI.GameSelector;
import AI.TreeStructure.GameTree;
import AI.Util;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import Output.OutputCSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TournamentGeneticAlgorithm {

    /** game to run the algorithm on */
    private GameSelector game;
    private Random random;
    /** main population list of items */
    private ArrayList<Item> population;
    /** pre-processed population list of the fittest generation items  */
    private ArrayList<Item> matingPool;
    /** tournament list where each item[] contains the array of participant of a single tournament */
    private ArrayList<Item[]> tournament;
    /** 2d array used to store the population genotypes when given from a csv file */
    private double[][] dataPopulation;
    /** corresponds to the total number of elements in each genotype */
    private int genotypeSize;
    /** population size stands for the total amount of items before splitting them into different tournaments */
    private int populationSize;
    /** algorithm stop condition, when a certain number of generations is reached, we can output the best result */
    private int generationSize;
    /** number of tournaments per generation */
    private int numberOfTournaments;
    /** number of participants per tournament (always 2^n) */
    private int numberOfParticipantsPerTournament;
    /** current number of generation in the process */
    private int totalGenerationNumber;
    /** corresponds to the rate at which a child undergoes a mutation in his genotype */
    private double mutationRate;
    /** same as for the mutation but for the crossover between the two parents */
    private double crossoverRate;
    /** boolean variable that tells the simulation method if the board is complete or simplified */
    private boolean completeBoard = true;

    /**
     * Class constructor to start the GA with a random population of items
     */
    public TournamentGeneticAlgorithm(GameSelector game, int genotypeSize, int generationSize, int startingGeneration, int numberOfTournaments, double mutationRate, double crossoverRate) {

        this.game = game;
        this.random = new Random();
        this.population = new ArrayList<>();
        this.genotypeSize = genotypeSize;
        this.generationSize = generationSize+startingGeneration;
        this.totalGenerationNumber = startingGeneration;
        this.numberOfTournaments = numberOfTournaments;
        this.numberOfParticipantsPerTournament = (int) Math.pow(2, 4);
        this.populationSize = (numberOfParticipantsPerTournament*numberOfTournaments);
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        initializePopulation();
    }

    /**
     * Class constructor to start the GA with a predefined population from a csv file
     */
    public TournamentGeneticAlgorithm(String fileName, GameSelector game, int generationSize, int startingGeneration, int numberOfTournaments, double mutationRate, double crossoverRate) {

        this.dataPopulation = fileParser("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/"+fileName);
        this.game = game;
        this.random = new Random();
        this.population = new ArrayList<>();
        this.genotypeSize = dataPopulation[0].length;
        this.generationSize = generationSize+startingGeneration;
        this.totalGenerationNumber = startingGeneration;
        this.numberOfTournaments = numberOfTournaments;
        this.numberOfParticipantsPerTournament = (int) Math.pow(2, 4);
        this.populationSize = (numberOfParticipantsPerTournament*numberOfTournaments);
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        initializePopulation(dataPopulation);
    }

    /**
     * method that start the tournament GA process and contains all the printing statements
     */
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
        while (totalGenerationNumber < generationSize) {
            System.out.println();
            System.out.println("--> Generation = " + (totalGenerationNumber));
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

    /**
     * given the population size, it initializes a random first generation of items
     */
    public void initializePopulation() {

        for (int i = 0; i < populationSize; i++) {
            Item item = new Item(createRandomGenotype());
            population.add(item);
        }
    }

    /**
     * given the genotype size
     * @return an array (-> genotype) of appropriate size of random elements
     */
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

    /**
     * method that initializes the first population
     * @param data from which the population is created
     */
    public void initializePopulation(double[][] data) {

        matingPool = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            double[] temp = new double[data[0].length];
            for (int j = 0; j < data[0].length; j++) {
                temp[j] = data[i][j];
            }
            Item item = new Item(temp);
            matingPool.add(item);
        }
        reproduction();
    }

    /**
     * creation of multiple tournaments with a random distribution of participant in each of them
     */
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

    /**
     * given the list of different tournaments, each of them is played one after the other
     */
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

    /**
     * @param list of participants for a specific tournament
     * @return the two tournament finalist that will enter the mating pool
     */
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

    /**
     * method that set the winner variable of the best of the two participants to true
     * @param participant1 first item to compete
     * @param participant2 second item to compete
     */
    public void simulateGames(Item participant1, Item participant2) {

        Board board = null;
        if (game.getClass().isInstance(new Checkers(null))) {
            board = new Board(6, completeBoard);
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

    /**
     * this will create a new population of the same size as the previous one with the individuals selected in the mating pool
     */
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

    /**
     * @param parent1 first breeder
     * @param parent2 second breeder
     * @return an item array with the two children created by crossover from the selected parents
     */
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

    /**
     * @param children item array with the two children
     * @return same item array after having potentially received a mutation
     */
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

    /**
     * when the total # of generations is reached, we want to find the best item from the mating pool
     * @return the two best items from the mating pool
     */
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

    /**
     * method used to save the mating pool each generation in a csv file to observe the evolution of the genotypes
     */
    public void getMatingPoolGenotypes() {

        OutputCSV out = new OutputCSV("CompleteMatingPoolGeneration"+ (totalGenerationNumber) +".csv");
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

    /**
     * @param path csv file path with the data
     * @return the file parsed into a 2d double array with the corresponding data
     */
    public double[][] fileParser(String path) {

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] d = new double[records.size()][records.get(0).size()];
        for (int i = 0; i < records.size(); i++) {
            for (int j = 0; j < records.get(0).size(); j++) {
                d[i][j] = Double.parseDouble(records.get(i).get(j));
            }
        }
        return d;
    }
}

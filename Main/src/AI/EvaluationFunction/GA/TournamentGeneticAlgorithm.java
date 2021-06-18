package AI.EvaluationFunction.GA;

import AI.GameSelector;
import AI.AlphaBetaTreeSearch.ABTS;
import AI.MonteCarloTreeSearch.MCTS;
import AI.PossibleMoves.PossibleMoves;
import AI.PossibleMoves.PresidentPossibleMoves;
import AI.Util;
import Abalone.Game.Abalone;
import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import Output.OutputCSV;
import President.Game.Card;
import President.Game.President;
import President.Game.Tuple;

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
    private boolean completeBoard = false;

    private int deckSize;
    private int numberOfParticipantsInLeague;

    /**
     * Class constructor to start the GA with a random population of items
     */
    public TournamentGeneticAlgorithm(GameSelector game, int genotypeSize, int generationSize, int startingGeneration, int numberOfTournaments, double mutationRate, double crossoverRate) {

        this.game = game;
        this.deckSize = game.getPlayer1().getDeck().size();
        this.random = new Random();
        this.population = new ArrayList<>();
        this.genotypeSize = genotypeSize;
        this.generationSize = generationSize+startingGeneration;
        this.totalGenerationNumber = startingGeneration;
        this.numberOfTournaments = numberOfTournaments;
        this.numberOfParticipantsPerTournament = (int) Math.pow(2, 5);
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
        this.deckSize = game.getPlayer1().getDeck().size();
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

    public TournamentGeneticAlgorithm(GameSelector game, int genotypeSize, int numberOfParticipantsInLeague) {

        this.game = game;
        this.deckSize = game.getPlayer1().getDeck().size();
        this.random = new Random();
        this.population = new ArrayList<>();
        this.genotypeSize = genotypeSize;
        this.numberOfParticipantsInLeague = numberOfParticipantsInLeague;
        this.populationSize = numberOfParticipantsInLeague;
        initializePopulation();
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
    
    public void startLeague() {

        System.out.println();
        System.out.println("----- SETUP -----");
        System.out.println();
        System.out.println("Population Size = " + populationSize);
        System.out.println("Total # of participants in league = " + numberOfParticipantsInLeague);
        System.out.println();
        System.out.println("----- START -----");
        simulateLeague();
        System.out.println("----- END -----");
        System.out.println();
        
    }
    
    public void simulateLeague() {

        int counter = 0;
        for (int i = 0; i < populationSize; i++) {
            int count = 0;
            for (int j = populationSize-1; j >= 0; j--) {
                if (i != j) {
                    President president = new President(14);
                    while (!president.isDone(president.getPlayer1(), president.getPlayer2())) {
                        MCTS mcts;
                        if (president.getPlayer1().toPlay()) {
                            mcts = new MCTS(president, new double[]{1, 2500});
                            mcts.setSampleSize((int) population.get(i).getGenotype()[0]);
                            mcts.setSampleSize((int) population.get(i).getGenotype()[1]);
                            mcts.start();
                            president.getPlayer1().takeAction(mcts.getFourBestNodes().get(0).getPlayer().getGameState(), president.getPlayer1().getDeck());
                            president.getPlayer2().setOpponentNumberCards(president.getPlayer1().getDeck().size());
                            president.getPlayer2().setGameState(mcts.getFourBestNodes().get(0).getPlayer().getGameState());
                            president.getPlayer2().isToPlay(!president.getPlayer1().toPlay());
                            president.getPlayer2().setGameDeck(president.getPlayer1().getGameDeck());
                        }
                        else {
                            mcts = new MCTS(president, new double[]{1, 2500});
                            mcts.setSampleSize((int) population.get(j).getGenotype()[0]);
                            mcts.setSampleSize((int) population.get(j).getGenotype()[1]);
                            mcts.start();
                            president.getPlayer2().takeAction(mcts.getFourBestNodes().get(0).getPlayer().getGameState(), president.getPlayer2().getDeck());
                            president.getPlayer1().setOpponentNumberCards(president.getPlayer2().getDeck().size());
                            president.getPlayer1().setGameState(mcts.getFourBestNodes().get(0).getPlayer().getGameState());
                            president.getPlayer1().isToPlay(!president.getPlayer2().toPlay());
                            president.getPlayer1().setGameDeck(president.getPlayer2().getGameDeck());
                        }
                    }
                    if (president.isVictorious(president.getPlayer1())) {
                        population.get(i).setScore(population.get(i).getScore() + 1);
                    }
                    else {
                        population.get(j).setScore(population.get(j).getScore() + 1);
                    }
                    count++;
                    System.out.println("match = " + count);
                }
            }
            counter++;
            System.out.println();
            System.out.println("player = " + counter);
            System.out.println();
        }
        ArrayList<Item> temp = new ArrayList<>();
        while (temp.size() < populationSize) {
            int max = -1;
            Item tempItem = null;
            for (Item item : population) {
                if (item.getScore() > max) {
                    max = item.getScore();
                    tempItem = item;
                }
            }
            if (tempItem != null) {
                temp.add(tempItem);
                population.remove(tempItem);
            }
        }
        population = temp;
        for (Item item : population) {
            System.out.println("sample size = " + item.getGenotype()[0] + ", stop condition = " + item.getGenotype()[1]);
            System.out.println("--> " + item.getScore());
            System.out.println();
        }
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
//        for (int i = 0; i < genotypeSize; i++) {
//            genotype[i] = random.nextDouble();
//        }
        Random random = new Random();
        genotype[0] = Math.ceil(random.nextDouble()*100);
        genotype[1] = Math.ceil(random.nextDouble()*20000)+100;
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
        BoardUI boardUI = null;
        if (game.getClass().isInstance(new Checkers(null))) {
            board = new Board(6, completeBoard);
            game = new Checkers(board);
        }
        else if (game.getClass().isInstance(new Abalone(null))) {
            boardUI = new BoardUI();
            game = new Abalone(boardUI);
        }
        else if (game.getClass().isInstance(new President(0))) {
            game = new President(deckSize);
        }
        int currentPlayer = random.nextInt(2)+1;
        //while (!game.isDone(boardUI.getGameBoard())) {
        if (!game.getClass().isInstance(new President(0))) {
            while (!game.isDone(board.getGameBoard())) {
                ABTS ABTS;
                if (currentPlayer == 1) {
                    ABTS = new ABTS(game, 5, participant1.getGenotype());
                } else {
                    ABTS = new ABTS(game, 5, participant2.getGenotype());
                }
                ABTS.setCurrentPlayer(currentPlayer);
                ABTS.start();
                double moveChoice = random.nextDouble();
                if (moveChoice < 0.55) {
                    if (ABTS.getFourBestNodes().get(0) != null) {
                        //boardUI.setBoard(ABTS.getFourBestNodes().get(0).getBoardState());
                        board.setBoard(ABTS.getFourBestNodes().get(0).getBoardState());
                    }
                } else if (moveChoice < 0.75) {
                    if (ABTS.getFourBestNodes().get(1) != null) {
                        //boardUI.setBoard(ABTS.getFourBestNodes().get(1).getBoardState());
                        board.setBoard(ABTS.getFourBestNodes().get(1).getBoardState());
                    }
                } else if (moveChoice < 0.9) {
                    if (ABTS.getFourBestNodes().get(2) != null) {
                        //boardUI.setBoard(ABTS.getFourBestNodes().get(2).getBoardState());
                        board.setBoard(ABTS.getFourBestNodes().get(2).getBoardState());
                    }
                } else {
                    if (ABTS.getFourBestNodes().get(3) != null) {
                        //boardUI.setBoard(ABTS.getFourBestNodes().get(3).getBoardState());
                        board.setBoard(ABTS.getFourBestNodes().get(3).getBoardState());
                    }
                }
                currentPlayer = Util.changeCurrentPlayer(currentPlayer);
            }
            //if (game.isVictorious(boardUI.getGameBoard(), 1)) {
            if (game.isVictorious(board.getGameBoard(), 1)) {
                participant1.setWinner(true);
                participant2.setWinner(false);
            } else {
                participant1.setWinner(false);
                participant2.setWinner(true);
            }
        }
        else {
            while (!game.isDone(game.getPlayer1(), game.getPlayer2())) {
                ABTS abts;
                if (game.getPlayer1().toPlay()) {
                    abts = new ABTS(game, 5, participant1.getGenotype());
                }
                else {
                    abts = new ABTS(game, 5, participant2.getGenotype());
                }
                abts.start();
                boolean checkPlayer1ToPlay = game.getPlayer1().toPlay();
                boolean checkPlayer2ToPlay = game.getPlayer2().toPlay();
                if (game.getPlayer1().toPlay()) {
                    game.getPlayer1().takeAction(abts.getFourBestNodes().get(0).getPlayer().getGameState(), game.getPlayer1().getDeck());
                    game.getPlayer2().setOpponentNumberCards(game.getPlayer1().getDeck().size());
                    game.getPlayer2().setGameState(abts.getFourBestNodes().get(0).getPlayer().getGameState());
                    game.getPlayer2().setGameDeck(game.getPlayer1().getGameDeck());
                }
                else {
                    game.getPlayer2().takeAction(abts.getFourBestNodes().get(0).getPlayer().getGameState(), game.getPlayer2().getDeck());
                    game.getPlayer1().setOpponentNumberCards(game.getPlayer2().getDeck().size());
                    game.getPlayer1().setGameState(abts.getFourBestNodes().get(0).getPlayer().getGameState());
                    game.getPlayer1().setGameDeck(game.getPlayer2().getGameDeck());
                }
                if (checkPlayer1ToPlay && game.getPlayer1().toPlay()) {
                    game.getPlayer1().isToPlay(true);
                    game.getPlayer2().isToPlay(false);
                }
                else if (checkPlayer1ToPlay && !game.getPlayer1().toPlay()){
                    game.getPlayer1().isToPlay(false);
                    game.getPlayer2().isToPlay(true);
                }
                else if (checkPlayer2ToPlay && game.getPlayer2().toPlay()) {
                    game.getPlayer1().isToPlay(false);
                    game.getPlayer2().isToPlay(true);
                }
                else if (checkPlayer2ToPlay && !game.getPlayer2().toPlay()){
                    game.getPlayer1().isToPlay(true);
                    game.getPlayer2().isToPlay(false);
                }
            }
            if (game.isVictorious(game.getPlayer1())) {
                participant1.setWinner(true);
                participant2.setWinner(false);
            }
            else {
                participant1.setWinner(false);
                participant2.setWinner(true);
            }
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
                    children[i].getGenotype()[j] = random.nextDouble();
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
        OutputCSV out = new OutputCSV("President14BestTwoGen.csv", "BestTwoGenotypes");
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

        OutputCSV out = new OutputCSV("President14MatingPoolGen"+ (totalGenerationNumber) +".csv");
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

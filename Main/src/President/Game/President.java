package President.Game;

import AI.AlphaBetaTreeSearch.ABTS;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.President.PresidentEvalFunction;
import AI.GameSelector;
import AI.MonteCarloTreeSearch.MCTS;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.Game.BoardUI;
import Checkers.GUI.GameUI;
import Checkers.Game.Board;
import Output.HomePage;
import President.GUI.UserInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class President extends GameSelector {

    private ArrayList<Card> gameDeck;
    private ArrayList<Card> deck;
    private ArrayList<Node> fourBestActions;
    private ArrayList<Node> actions;
    private static ArrayList<Double> adaptiveVariableStack = new ArrayList<>();
    private static double adaptiveVariable;
    private static double globalAdaptiveVariable;

    private Player player1;
    private Player player2;

    private Tuple gameState;

    private int numberOfCards;

    public President(int deckSize) {

        this.numberOfCards = deckSize;
        this.gameDeck = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.deck = initializeDeck();
        this.gameState = new Tuple(0, 0);
        this.player1 = new Player(setInitialPlayerDeck(deck), gameDeck, gameState, numberOfCards, true, 1);
        this.player2 = new Player(setInitialPlayerDeck(deck), gameDeck, gameState, numberOfCards, false, 2);
        this.deck.clear();
    }

    public President(President president) {

        this.gameDeck = president.getGameDeck();
        this.gameState = president.getCurrentGameState();
        this.player1 = president.getPlayer1();
        this.player2 = president.getPlayer2();
    }

    public void runMCTS(double[] configuration) {

        MCTS mcts = new MCTS(this, configuration);
        mcts.start();
        fourBestActions = mcts.getFourBestNodes();
        actions = mcts.getActionsOrdered();
        UserInterface.readyText.setText("Ready!\n\nChoose between action\n1, 2, 3 or 4\n\nPress SPACE to update game");
    }

    public void runABTS() {

        ABTS abts = new ABTS(this, 5);
        abts.start();
        fourBestActions = abts.getFourBestNodes();
        UserInterface.readyText.setText("Ready!\n\nChoose between action\n1, 2, 3 or 4\n\nPress SPACE to update game");
    }

    public static ArrayList<Card> initializeDeck() {

        ArrayList<Card> out = new ArrayList<>();
        for (int i = 2; i <= 14; i++) {
            for (int j = 1; j <= 4; j++) {
                Card card = new Card(i, j);
                out.add(card);
            }
        }
        return out;
    }

    public ArrayList<Card> setInitialPlayerDeck(ArrayList<Card> deck) {

        ArrayList<Card> newDeck = new ArrayList<>();
        while (newDeck.size() < numberOfCards) {
            Random random = new Random();
            int index = random.nextInt(deck.size());
            newDeck.add(deck.get(index));
            deck.remove(index);
        }
        return newDeck;
    }

    @Override
    public void updateAdaptiveVariable(Player previousPlayer1State, Player previousPlayer2State, Player currentPlayer1State, Player currentPlayer2State) {

//        System.out.println(previousGameState.getPlayer1().getGameState().getNumber() + " --> " + previousGameState.getPlayer1().getGameState().getOccurrence());
//        System.out.println(previousGameState.getPlayer2().getGameState().getNumber() + " --> " + previousGameState.getPlayer2().getGameState().getOccurrence());
//        System.out.println(currentGameState.getPlayer1().getGameState().getNumber() + " --> " + currentGameState.getPlayer1().getGameState().getOccurrence());
//        System.out.println(currentGameState.getPlayer2().getGameState().getNumber() + " --> " + currentGameState.getPlayer2().getGameState().getOccurrence());

        EvaluationFunction currPlayerEval1 = new PresidentEvalFunction(previousPlayer1State);
        EvaluationFunction currPlayerEval2 = new PresidentEvalFunction(currentPlayer1State);
        EvaluationFunction oppPlayerEval1 = new PresidentEvalFunction(previousPlayer2State);
        EvaluationFunction oppPlayerEval2 = new PresidentEvalFunction(currentPlayer2State);

//        if (HomePage.username.equals("Username") || HomePage.username.equals("")) {
//            adaptiveVariableStack.add(weightingFunction(currPlayerEval1.evaluate(), currPlayerEval2.evaluate(), oppPlayerEval1.evaluate(), oppPlayerEval2.evaluate()));
//            double sum = 0;
//            for (double x : adaptiveVariableStack) {
//                sum+=x;
//            }
//            adaptiveVariable = sum / adaptiveVariableStack.size();
//        }
//        else {
            Util.updateNameAdaptiveVar("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_level_president.txt", HomePage.username, weightingFunction(currPlayerEval1.evaluate(), currPlayerEval2.evaluate(), oppPlayerEval1.evaluate(), oppPlayerEval2.evaluate()));
            adaptiveVariable = Util.getNameAdaptiveVariable("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_level_president.txt", HomePage.username);
//        }

        globalAdaptiveVariable = currPlayerEval2.evaluate() - oppPlayerEval2.evaluate();
//        System.out.println("adaptiveVariable = " + adaptiveVariable);
//        System.out.println("globalAdaptiveVariable = " + globalAdaptiveVariable);
//        System.out.println();
    }

    @Override
    public void updateWinnerFile(String path, String username, boolean win) {

        if (!username.equals("Username") && !username.equals("")) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line = br.readLine();
                String[] tokens = line.split(", ");
                int lineNumber = 0;
                while (!tokens[0].equals(username)) {
                    line = br.readLine();
                    tokens = line.split(", ");
                    lineNumber++;
                }
                double win_number = Double.parseDouble(tokens[1]);
                double game_number = Double.parseDouble(tokens[2]);
                game_number++;
                if (win) {
                    win_number++;
                }
                double ratio = win_number/game_number;
                String out = username + ", " + win_number + ", " + game_number + ", " + ratio + ", ";

                int count = 0;
                StringBuffer buffer = new StringBuffer();
                BufferedReader new_br = new BufferedReader(new FileReader(path));
                line = new_br.readLine();
                while (line != null) {
                    if (count != lineNumber) {
                        buffer.append(line).append(System.lineSeparator());
                    }
                    else {
                        buffer.append(out).append(System.lineSeparator());
                    }
                    line = new_br.readLine();
                    count++;
                }
                PrintWriter writer = new PrintWriter(path);
                writer.print("");
                String fileContents = buffer.toString();
                writer.append(fileContents);
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO - improve the adaptive variable computation
    public double weightingFunction(double x1, double x2, double y1, double y2) {

//        return (x2 - x1) + (y1 - y2);
        return (x2 - x1);
    }

    @Override
    public boolean isDone(Player player1, Player player2) {
        return player1.getDeck().size()<=0 || player2.getDeck().size()<=0;
    }

    @Override
    public boolean isVictorious(Player player) {
        return player.getDeck().size()<=0;
    }

    @Override
    public Tuple getCurrentGameState() {
        return gameState;
    }

    @Override
    public void updateAdaptiveVariable(int[][] previousBoard, int[][] currentBoard) {

    }

    @Override
    public boolean isDone(int[][] actualBoard) {
        return false;
    }

    @Override
    public boolean isVictorious(int[][] actualBoard, int player) {
        return false;
    }

    @Override
    public int getCurrentPlayer() {
        return 0;
    }

    @Override
    public BoardUI getAbaloneBoard() {
        return null;
    }

    @Override
    public Board getCheckersBoard() {
        return null;
    }

    @Override
    public Player getPlayer1() {
        return player1;
    }

    @Override
    public Player getPlayer2() {
        return player2;
    }

    @Override
    public ArrayList<Card> getGameDeck() {
        return deck;
    }

    public void setGameState(Tuple gameState) {
        this.gameState = gameState;
    }

    public ArrayList<Node> getFourBestActions() {
        return fourBestActions;
    }

    public ArrayList<Node> getActions() {
        return actions;
    }

    public static double getAdaptiveVariable() {
        return adaptiveVariable;
    }
}

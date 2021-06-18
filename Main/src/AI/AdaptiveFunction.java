package AI;

import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.President.PresidentEvalFunction;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import HomePage.HomePage;
import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;

import java.io.*;
import java.util.ArrayList;

public class AdaptiveFunction extends GameSelector {

    /** game to adapt the AI on */
    private GameSelector game;
    /** array list that holds the current values for the regulatization terms */
    private ArrayList<double[]> regularization_terms = new ArrayList<>();
    /** boolean variable that tells the class if the actual game is checkers */
    private boolean isCheckers = false;
    /** boolean variable that tells the class if the actual game is president */
    private boolean isPresident = false;
    /** different String variable for access paths */
    private String path = "";
    private String path_avg = "";
    private String path_win = "";

    /**
     * main class constructor
     * @param game to adapt the AI on
     */
    public AdaptiveFunction(GameSelector game) {

        this.game = game;
        if (game.getClass().isInstance(new President(0))) {
            isPresident = true;
            path = "Main/res/regularization_terms_president.txt";
            path_avg = "Main/res/regularization_terms_president_avg.txt";
            path_win = "Main/res/players_win_rate_president.txt";
        }
        else if (game.getClass().isInstance(new Checkers(null))) {
            isCheckers = true;
            path = "Main/res/regularization_terms_checkers.txt";
            path_avg = "Main/res/regularization_terms_checkers_avg.txt";
            path_win = "Main/res/players_win_rate_checkers.txt";
        }
        createRegularizationVector();
    }

    /**
     * getter for the current human player adaptive variable
     * @return the values that corresponds to the level of the player
     */
    public double getAdaptiveVariable() {

        if (isCheckers) {
            return Checkers.getAdaptiveVariable();
        }
        else if (isPresident){
            return President.getAdaptiveVariable();
        }
        return 0;
    }

    /**
     * getter for the current state global variable
     * @return the value of it
     */
    public double getGlobalAdaptiveVariable() {

        if (isCheckers) {
            EvaluationFunction evalFunction1 = new CheckersEvalFunction(game.getCheckersBoard().getGameBoard(), 1);
            EvaluationFunction evalFunction2 = new CheckersEvalFunction(game.getCheckersBoard().getGameBoard(), 2);
            return evalFunction1.evaluate() - evalFunction2.evaluate();
        }
        else if (isPresident){
            EvaluationFunction evalFunction1 = new PresidentEvalFunction(game.getPlayer1());
            EvaluationFunction evalFunction2 = new PresidentEvalFunction(game.getPlayer2());
            return evalFunction1.evaluate() - evalFunction2.evaluate();
        }
        return 0;
    }

    /**
     * method that finds the index of the next action to pick
     * @param globalVar of the current state
     * @param adaptiveVar of player 1
     * @param numOfActions for that given state
     * @return the action index to choose
     */
    public int getActionIndex(double globalVar, double adaptiveVar, int numOfActions) {

        if (numOfActions == 1) {
            return 0;
        }
        double mean;
        double std = 0.3;
        double x1_bound = -0.4999;
        double x_middle = ((double) numOfActions)/2;
        double x2_bound = (numOfActions-1) + 0.4999;
        double regularization_term = Util.getArrayAverage(regularization_terms.get(numOfActions-1));
        double win_ratio = 0.5;

        try (BufferedReader br = new BufferedReader(new FileReader(path_win))) {
            String line = br.readLine();
            while (line!=null) {
                if (line.split(", ")[0].equals(HomePage.username)) {
                    if (!line.split(", ")[2].equals("0.0")) {
                        win_ratio = Double.parseDouble(line.split(", ")[3]);
                    }
                    break;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = -1;
        if (globalVar >= 0) {
            mean = (x_middle-1) - (((Math.sqrt(globalVar)/2) + (adaptiveVar*(2*win_ratio))) * regularization_term);
        }
        else {
            mean = (x_middle-1) + (((Math.sqrt(-globalVar)/2) - (adaptiveVar*(2*win_ratio))) * regularization_term);
        }

        if (mean < x1_bound) {
            return 0;
        }
        else if (mean > x2_bound) {
            return numOfActions-1;
        }
        else {
            while (index < x1_bound || index > x2_bound) {
                index = (int) Math.round(Util.getGaussian(mean, std));
            }
        }

        return index;
    }

    /**
     * method that creates the regularization vector from the text file
     */
    public void createRegularizationVector() {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while (line!=null) {
                String[] lineTemp = line.split(", ");
                double[] temp = new double[lineTemp.length];
                for (int j = 0; j < lineTemp.length; j++) {
                    if (lineTemp[j].charAt(lineTemp[j].length()-1) == ',') {
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < lineTemp[j].length()-1; i++) {
                            s.append(lineTemp[j].charAt(i));
                        }
                        lineTemp[j] = String.valueOf(s);
                    }
                    temp[j] = Double.parseDouble(lineTemp[j]);
                }
                regularization_terms.add(temp);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method for updating the vector based on the direct feedback it gets
     * @param globalVariableBefore its value before the actions is taken
     * @param globalVariableAfter its value after the action is taken
     * @param index of the action previously taken
     */
    public void updateVector(double globalVariableBefore, double globalVariableAfter, int index) {

        double penalty = 0;
        if (globalVariableBefore < 0) {
            if (globalVariableAfter <= 0) {
                if (globalVariableAfter < globalVariableBefore) {
                    penalty = (globalVariableAfter - globalVariableBefore)/10;
                }
            }
            if (globalVariableAfter > 0) {
                if (globalVariableAfter > Math.abs(globalVariableBefore)) {
                    penalty = (globalVariableAfter + globalVariableBefore)/10;
                }
            }
        }
        else {
            if (globalVariableAfter >= 0) {
                if (globalVariableAfter > globalVariableBefore) {
                    penalty =  (globalVariableAfter - globalVariableBefore)/10;
                }
            }
            if (globalVariableAfter < 0) {
                if (Math.abs(globalVariableAfter) > globalVariableBefore) {
                    penalty = (globalVariableBefore + globalVariableAfter)/10;
                }
            }
        }

        if (penalty != 0) {
            double[] temp = new double[regularization_terms.get(index).length + 1];
            for (int i = 0; i < regularization_terms.get(index).length; i++) {
                temp[i] = regularization_terms.get(index)[i];
            }
            temp[regularization_terms.get(index).length] = regularization_terms.get(index)[regularization_terms.get(index).length - 1] + penalty;
            regularization_terms.set(index, temp);
            updateFile();
        }
    }

    /**
     * method to update the file where the regularization factors are stored
     */
    public void updateFile() {

        try {
            BufferedReader file = new BufferedReader(new FileReader(path));
            StringBuffer inputBuffer = new StringBuffer();
            int count = 0;
            while (count < regularization_terms.size()) {
                StringBuilder line = new StringBuilder();
                for (double d : regularization_terms.get(count)) {
                    line.append(d).append(", ");
                }
                inputBuffer.append(line);
                if (count < regularization_terms.size()-1) {
                    inputBuffer.append('\n');
                }
                count++;
            }
            file.close();
            FileOutputStream fileOut = new FileOutputStream(path);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }

        try {
            BufferedReader file = new BufferedReader(new FileReader(path));
            StringBuffer inputBuffer = new StringBuffer();
            int count = 0;
            while (count < regularization_terms.size()) {
                String line = String.valueOf(Util.getArrayAverage(regularization_terms.get(count)));
                inputBuffer.append(line);
                if (count < regularization_terms.size()-1) {
                    inputBuffer.append('\n');
                }
                count++;
            }
            file.close();
            FileOutputStream fileOut = new FileOutputStream(path_avg);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }
    }

    @Override
    public int getCurrentPlayer() {
        return 0;
    }

    @Override
    public Tuple getCurrentGameState() {
        return null;
    }

    @Override
    public Board getCheckersBoard() {
        return null;
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
    public boolean isDone(Player player1, Player player2) {
        return false;
    }

    @Override
    public boolean isVictorious(Player player) {
        return false;
    }

    @Override
    public Player getPlayer1() {
        return null;
    }

    @Override
    public Player getPlayer2() {
        return null;
    }

    @Override
    public ArrayList<Card> getGameDeck() {
        return null;
    }

    @Override
    public void updateAdaptiveVariable(int[][] previousBoard, int[][] currentBoard) {

    }

    @Override
    public void updateWinnerFile(String path, String username, boolean win) {

    }

    @Override
    public void updateAdaptiveVariable(Player previousPlayer1State, Player previousPlayer2State, Player currentPlayer1State, Player currentPlayer2State) {

    }

}

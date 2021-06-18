package AI.EvaluationFunction.Adaptive;

import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.President.PresidentEvalFunction;
import AI.GameSelector;
import AI.Util;
import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import Output.HomePage;
import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AdaptiveFunction extends GameSelector {

    private GameSelector game;

    private double adaptiveVariablePlayer1;
    private double adaptiveVariablePlayer2;

//    private double[] regularization_terms = new double[50];
    private ArrayList<double[]> regularization_terms = new ArrayList<>();

    private boolean isCheckers = false;
    private boolean isPresident = false;

    private ArrayList<Rule> ruleBase;

    private String path = "";
    private String path_avg = "";
    private String path_win = "";

    public AdaptiveFunction(GameSelector game) {

        this.game = game;
        if (game.getClass().isInstance(new President(0))) {
            isPresident = true;
            path = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/regularization_terms_president.txt";
            path_avg = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/regularization_terms_president_avg.txt";
            path_win = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_president.txt";
        }
        else if (game.getClass().isInstance(new Checkers(null))) {
            isCheckers = true;
            path = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/regularization_terms_checkers.txt";
            path_avg = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/regularization_terms_checkers_avg.txt";
            path_win = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_checkers.txt";
        }
//        updateAdaptiveVariable();
        createRegularizationVector();
//        updateWeights(getGlobalAdaptiveFunction());
    }

//    public void updateAdaptiveVariable() {
//
//        EvaluationFunction evaluationFunction;
//        if (game.getClass().isInstance(new President(0))) {
//            evaluationFunction = new PresidentEvalFunction(game.getPlayer1());
//            adaptiveVariablePlayer1 = evaluationFunction.evaluate();
//            evaluationFunction = new PresidentEvalFunction(game.getPlayer2());
//            adaptiveVariablePlayer2 = evaluationFunction.evaluate();
//        }
//    }

    public double getAdaptiveVariable() {

        if (isCheckers) {
            return Checkers.getAdaptiveVariable();
        }
        else {
            return President.getAdaptiveVariable();
        }
    }

    public double getGlobalAdaptiveVariable() {

        if (isCheckers) {
            EvaluationFunction evalFunction1 = new CheckersEvalFunction(game.getCheckersBoard().getGameBoard(), 1);
            EvaluationFunction evalFunction2 = new CheckersEvalFunction(game.getCheckersBoard().getGameBoard(), 2);
            return evalFunction1.evaluate() - evalFunction2.evaluate();
        }
        else {
            EvaluationFunction evalFunction1 = new PresidentEvalFunction(game.getPlayer1());
            EvaluationFunction evalFunction2 = new PresidentEvalFunction(game.getPlayer2());
            return evalFunction1.evaluate() - evalFunction2.evaluate();
        }
    }

    public int getActionIndex(double globalVar, double adaptiveVar, int numOfActions) {

//        System.out.println("globalVar = " + globalVar);
//        System.out.println("adaptiveVar = " + adaptiveVar);
//        System.out.println("numOfActions = " + numOfActions);
        if (numOfActions == 1) {
//            System.out.println("index = " + 0);
//            System.out.println();
            return 0;
        }
        double mean;
        double std = 0.3;
        double x1_bound = -0.4999;
        double x_middle = ((double) numOfActions)/2;
        double x2_bound = (numOfActions-1) + 0.4999;
//        double regularization_term = regularization_terms.get(numOfActions-1)[regularization_terms.get(numOfActions-1).length];
        double regularization_term = Util.getArrayAverage(regularization_terms.get(numOfActions-1));
//        System.out.println("regularization_term = " + regularization_term);

        double win_ratio = 0.5;
        try (BufferedReader br = new BufferedReader(new FileReader(path_win))) {
            String line = br.readLine();
            while (line!=null) {
//                System.out.println(line.split(", ")[0]);
//                System.out.println("HomePage.username = " + HomePage.username);
//                if (line.split(", ")[0].equals(HomePage.username)) {
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

//        System.out.println("win_ratio = " + win_ratio);
        int index = -1;
        if (globalVar >= 0) {
//            mean = x_middle - (Math.sqrt(globalVar)/2) - (Math.sqrt(numOfActions)*adaptiveVar);
            mean = (x_middle-1) - (((Math.sqrt(globalVar)/2) + (adaptiveVar*(2*win_ratio))) * regularization_term);
//            mean = (x_middle-1) - (((Math.sqrt(globalVar)/2) + (adaptiveVar*(2*win_ratio))) * Math.sqrt(numOfActions));
        }
        else {
//            mean = x_middle + (Math.sqrt(-globalVar)/2) - (Math.sqrt(numOfActions)*adaptiveVar);
            mean = (x_middle-1) + (((Math.sqrt(-globalVar)/2) - (adaptiveVar*(2*win_ratio))) * regularization_term);
//            mean = (x_middle-1) + (((Math.sqrt(-globalVar)/2) - (adaptiveVar*(2*win_ratio))) * Math.sqrt(numOfActions));
        }
//        System.out.println("mean = " + mean);
        if (mean < x1_bound) {
//            System.out.println("index = " + 0);
//            System.out.println();
            return 0;
        }
        else if (mean > x2_bound) {
//            System.out.println("index = " + (numOfActions-1));
//            System.out.println();
            return numOfActions-1;
        }
        else {
            while (index < x1_bound || index > x2_bound) {
                index = (int) Math.round(Util.getGaussian(mean, std));
            }
        }
//        System.out.println("index = " + index);
//        System.out.println();
        return index;
    }

    public void createRegularizationVector() {

//        ArrayList<Rule> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while (line!=null) {
//                System.out.println(line);
//            for (int i = 0; i < 50; i++) {
//            Rule rule = new Rule(new double[]{1, 100*i}, i);
//            double score;
//            int count = 0;
//            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//                while (count < i) {
                String[] lineTemp = line.split(", ");
//                System.out.println(Arrays.toString(lineTemp));
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
//                System.out.println(line);
//                regularization_terms[i] = Double.parseDouble(line);
//                    count++;
//                }
//            score = Double.parseDouble(line);
//            rule.setScore(score);
//            out.add(rule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        this.ruleBase = out;
    }

//    public void updateVector(double penalty, int index) {
//
//        System.out.println("penalty = " + penalty);
//        System.out.println();
//        double[] temp = new double[regularization_terms.get(index).length+1];
//        for (int i = 0; i < regularization_terms.get(index).length; i++) {
//            temp[i] = regularization_terms.get(index)[i];
//        }
//        temp[regularization_terms.get(index).length] = regularization_terms.get(index)[regularization_terms.get(index).length-1]-penalty;
//        regularization_terms.set(index, temp);
//    }

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

//        System.out.println("penalty = " + penalty);
//        System.out.println();

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

    // TODO - Improve the computation of the temporary score
    public double computeTempScore(double score, double globalAdaptiveVariable) {

        if (globalAdaptiveVariable > 0) {
            if (score > 0) {
                return globalAdaptiveVariable * score;
            }
        }
        else {
            if (score < 0) {
                return globalAdaptiveVariable * score;
            }
        }
        return 0.0001;
    }

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

    public int getRuleIndex() {

        double sum = 0;
        double randomNumber = Math.random();
        for (int i = 0; i < ruleBase.size(); i++) {
            sum+=ruleBase.get(i).getWeight();
            if (randomNumber <= sum) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Rule> getRuleBase() {
        return ruleBase;
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
    public BoardUI getAbaloneBoard() {
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

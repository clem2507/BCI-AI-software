package AI.EvaluationFunction.Adaptive;

import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.President.PresidentEvalFunction;
import AI.GameSelector;
import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;

import java.io.*;
import java.util.ArrayList;

public class AdaptiveFunction extends GameSelector {

    private GameSelector game;

    private double adaptiveVariablePlayer1;
    private double adaptiveVariablePlayer2;

    private ArrayList<Rule> ruleBase;

    private String path = "";

    public AdaptiveFunction(GameSelector game) {

        this.game = game;
        if (game.getClass().isInstance(new President(0))) {
            path = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/ruleBaseScorePresident.txt";
        }
        else if (game.getClass().isInstance(new Checkers(null))) {
            path = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/ruleBaseScoreCheckers.txt";
        }
        updateAdaptiveVariable();
        createRuleBase();
        updateWeights(getGlobalAdaptiveFunction());
    }

    public void updateAdaptiveVariable() {

        EvaluationFunction evaluationFunction;
        if (game.getClass().isInstance(new President(0))) {
            evaluationFunction = new PresidentEvalFunction(game.getPlayer1());
            adaptiveVariablePlayer1 = evaluationFunction.evaluate();
            evaluationFunction = new PresidentEvalFunction(game.getPlayer2());
            adaptiveVariablePlayer2 = evaluationFunction.evaluate();
        }
    }

    public double getGlobalAdaptiveFunction() {

        return adaptiveVariablePlayer1 - adaptiveVariablePlayer2;
    }

    public void createRuleBase() {

        ArrayList<Rule> out = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Rule rule = new Rule(new double[]{1, 100*i}, i);
            double score;
            String line = "0";
            int count = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                while (count < i) {
                    line = br.readLine();
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            score = Double.parseDouble(line);
            rule.setScore(score);
            out.add(rule);
        }
        this.ruleBase = out;
    }

    public void updateWeights(double globalAdaptiveVariable) {

        double normalisationFactor = 0;
        for (Rule rule : ruleBase) {
            normalisationFactor += computeTempScore(rule.getScore(), globalAdaptiveVariable);
        }
        for (Rule rule : ruleBase) {
            rule.setWeight(computeTempScore(rule.getScore(), globalAdaptiveVariable)/normalisationFactor);
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
            String line;
            int count = 0;
            while (count < ruleBase.size()) {
                line = Double.toString(ruleBase.get(count).getScore());
                inputBuffer.append(line);
                if (count < ruleBase.size()-1) {
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
    public void setAdaptiveVariable(int[][] previousBoard, int[][] currentBoard) {

    }

    @Override
    public double getAdaptiveVariable(int player) {

        if (player == 1) {
            return adaptiveVariablePlayer1;
        }
        else {
            return adaptiveVariablePlayer2;
        }
    }
}

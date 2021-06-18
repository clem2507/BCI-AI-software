package Checkers.Game;

import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.GameSelector;
import AI.MonteCarloTreeSearch.MCTS;
import AI.AlphaBetaTreeSearch.ABTS;
import AI.PossibleMoves.CheckersPossibleMoves;
import AI.PossibleMoves.PossibleMoves;
import AI.TreeStructure.Node;
import AI.Util;
import Checkers.GUI.GameUI;
import HomePage.HomePage;
import President.Game.Card;
import President.Game.Player;
import President.Game.Tuple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Checkers extends GameSelector {

    private Board board;
    public static int currentPlayer = 1;
    private ArrayList<Node> fourBestMoves;
    private ArrayList<Node> actions;
    private static double adaptiveVariable;

    public Checkers(Board board) {

        this.board = board;
    }

    public void runMCTS(double[] configuration) {

        MCTS mcts = new MCTS(this, configuration);
        mcts.start();
        fourBestMoves = mcts.getFourBestNodes();
        actions = mcts.getActionsOrdered();
        board.drawAllMarbles();
        GameUI.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
    }

    public void runABTS() {

        ABTS ABTS = new ABTS(this, 5);
        ABTS.start();
        fourBestMoves = ABTS.getFourBestNodes();
        board.drawAllMarbles();
        GameUI.readyText.setText("Ready!\n\nChoose between move\n1, 2, 3 or 4\n\nPress SPACE to update board");
    }

    @Override
    public void updateAdaptiveVariable(int[][] previousBoard, int[][] currentBoard) {

        EvaluationFunction currPlayerEval1 = new CheckersEvalFunction(previousBoard, Util.changeCurrentPlayer(currentPlayer));
        EvaluationFunction currPlayerEval2 = new CheckersEvalFunction(currentBoard, Util.changeCurrentPlayer(currentPlayer));

        Util.updateNameAdaptiveVar("Main/res/players_level_checkers.txt", HomePage.username, weightingFunction(currPlayerEval1.evaluate(), currPlayerEval2.evaluate()));
        adaptiveVariable = Util.getNameAdaptiveVariable("Main/res/players_level_checkers.txt", HomePage.username);
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

    public double getGlobalAdaptiveVariable(int[][] board) {

        EvaluationFunction currPlayerEval = new CheckersEvalFunction(board, 1);
        EvaluationFunction oppPlayerEval = new CheckersEvalFunction(board, 2);

        return currPlayerEval.evaluate() - oppPlayerEval.evaluate();
    }

    public double weightingFunction(double x1, double x2) {

        return (x2 - x1);
    }

    @Override
    public void updateAdaptiveVariable(Player previousPlayer1State, Player previousPlayer2State, Player currentPlayer1State, Player currentPlayer2State) {

    }

    @Override
    public boolean isDone(int[][] board) {

        for (int i = 1; i < board.length-1; i++) {
            if (board[1][i] == 1) {
                return true;
            }
        }
        for (int i = 1; i < board.length-1; i++) {
            if (board[board.length-2][i] == 2) {
                return true;
            }
        }
        if (Util.countMarbles(board, 1) == 0 || Util.countMarbles(board, 2) == 0) {
            return true;
        }
        PossibleMoves possibleMoves1 = new CheckersPossibleMoves(board, 1);
        PossibleMoves possibleMoves2 = new CheckersPossibleMoves(board, 2);
        if (possibleMoves1.getPossibleMoves().size() == 0 || possibleMoves2.getPossibleMoves().size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isVictorious(int[][] board, int player) {

        if (player==1) {
            for (int i = 1; i < board.length-1; i++) {
                if (board[1][i] == 1) {
                    return true;
                }
            }
        }
        else {
            for (int i = 1; i < board.length-1; i++) {
                if (board[board.length-2][i] == 2) {
                    return true;
                }
            }
        }
        if (Util.countMarbles(board, Util.changeCurrentPlayer(player)) == 0) {
            return true;
        }
        PossibleMoves possibleMoves = new CheckersPossibleMoves(board, Util.changeCurrentPlayer(player));
        if (possibleMoves.getPossibleMoves().size() == 0) {
            return true;
        }
        return false;
    }

    public static double getAdaptiveVariable() {
        return adaptiveVariable;
    }

    public ArrayList<Node> getActions() {
        return actions;
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

    public ArrayList<Node> getFourBestMoves() {
        return fourBestMoves;
    }

    @Override
    public Board getCheckersBoard() {
        return board;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Tuple getCurrentGameState() {
        return null;
    }
}

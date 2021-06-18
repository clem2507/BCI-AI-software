package Output;

import AI.AlphaBetaTreeSearch.ABTS;
import AI.EvaluationFunction.Adaptive.AdaptiveFunction;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
import AI.EvaluationFunction.President.PresidentEvalFunction;
import AI.MonteCarloTreeSearch.MCTS;
import AI.PossibleMoves.PossibleMoves;
import AI.PossibleMoves.PresidentPossibleMoves;
import AI.TreeStructure.Node;
import AI.Util;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import President.Game.Card;
import President.Game.President;
import President.Game.Tuple;

import java.util.ArrayList;
import java.util.Random;

public class Test {

    public static Board board;

    private static int[][] gameBoard = new int[][]{

            {3, 3, 3, 3, 3, 3, 3, 3},
            {3, 0, 2, 0, 2, 0, 2, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 1, 0, 1, 0, 1, 0, 3},
            {3, 3, 3, 3, 3, 3, 3, 3}

    };

    private static int[][] gameBoardAbalone = new int[][]{

            {2, 2, 2, -1, -1, -1, -1},
            {2, 2, 2, 2, -1, -1, -1},
            {0, 2, 2, 2,  0, -1, -1},
            {0, 0, 0, 0,  0,  0, -1},
            {0, 0, 0, 0,  0,  0, 0},
            {0, 0, 0, 0,  0,  0, -1},
            {0, 1, 1, 1,  0, -1, -1},
            {1, 1, 1, 1, -1, -1, -1},
            {1, 1, 1, -1, -1, -1, -1}

    };

    public static void printBoard(int[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static void main(String[]args) {

//        int currentPlayer = 1;
//        board = new Board(6, false);
//        System.out.println();
//        printBoard(board.getGameBoard());
//        System.out.println();
//        Move move = new Move(6, 1, MoveDirection.TOP_RIGHT, board.getGameBoard(), currentPlayer);
//        if (move.checkMove()) {
//            board.setBoard(move.getNewState());
//        }
//        printBoard(board.getGameBoard());
//        System.out.println();
//        System.out.println("-----------------");
//        System.out.println();
//        PossibleMoves possibleMoves = new PossibleMoves(board.getGameBoard(), currentPlayer);
//        ArrayList<int[][]> possibleBoard = possibleMoves.getPossibleMoves();
//        for (int[][] b : possibleBoard) {
//            printBoard(b);
//            System.out.println();
//        }

//        MCTS mcts = new MCTS(board.getGameBoard(), currentPlayer, 2000, 10, "Checkers");
//        mcts.start();
//        System.out.println();
//        Test.printBoard(mcts.getBestMove());

//        GameTree gameTree = new GameTree(new Checkers(board), 5);
//        System.out.println("Total # of nodes in GT = " + gameTree.getNodes().size());
//        ABTS abts = new ABTS(gameTree);
//        abts.start();
//        for (int[][] arr : abts.getFourBestNodes()) {
//            System.out.println(Arrays.deepToString(arr));
//        }

//        EvaluationFunction f = new CheckersEvalFunction(gameBoard, 1);
//        System.out.println(f.evaluate());

//        PossibleMoves possibleMoves = new CheckersPossibleMoves(gameBoard, 2);
//        for (int[][] arr : possibleMoves.getPossibleMoves()) {
//            Util.printBoard(arr);
//        }

//        board.setBoard(gameBoard);
//        GameTree gameTree = new GameTree(new Checkers(board), 5);
//        gameTree.createTree();
//        for (Node n : gameTree.getNodes()) {
//            Util.printBoard(n.getBoardState());
//            System.out.println(n.getScore());
//            System.out.println();
//        }

//        GetPossibleMoves getPossibleMoves = new GetPossibleMoves();
//        ArrayList<int[][]> moves = getPossibleMoves.getPossibleMoves(gameBoardAbalone, 1);
//        System.out.println(moves.size());
//        for (int[][] board : moves) {
//            Util.printBoard(board);
//        }

//        EvaluationFunction eval = new AbaloneEvalFunction(gameBoardAbalone, 1);
//        System.out.println(eval.evaluate());
//        System.out.println();

//        GameTree gameTree = new GameTree(new Abalone(new BoardUI()), 3);
//        ABTS ABTS = new ABTS(new Checkers(new Board(6, false)), 3);
//        ABTS.start();
//        System.out.println(ABTS.getNodes().size());
//        System.out.println(ABTS.getRootChildrenNodes().size());
//        for (Node n : ABTS.getFourBestNodes()) {
//            Util.printBoard(n.getBoardState());
//        }


//        ABTS abts = new ABTS(gameTree);
//        abts.start();
//        for (int[][] arr : abts.getFourBestNodes()) {
//            Util.printBoard(arr);
//        }

//        President president = new President();
//        for (Tuple tuple : president.getPlayer1().getSortedDeck()) {
//            System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
//        }
//        System.out.println();
//
//        PossibleMoves possibleMoves = new PresidentPossibleMoves(president.getPlayer1(), new Tuple(4, 2));
//        System.out.println("Current state: 4 -> 2");
//        System.out.println();
//        for (Tuple tuple : possibleMoves.getPossibleActions()) {
//            System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
//        }
//        System.out.println();

//        for (Card card : president.getPlayer1().getDeck()) {
//            System.out.println(card.getNumber() + " -> " + card.getSymbol());
//        }
//        System.out.println();
//        ArrayList<Card> out = new ArrayList<>();
//        out.add(new Card(2,4));
//        out.add(new Card(6,4));
//        out.add(new Card(8,4));
//        out.add(new Card(11,4));
//        PossibleMoves possibleMoves = new PresidentPossibleMoves(president.getPlayer1(), new Tuple(0,0), out);
//        for (Tuple tuple : possibleMoves.getInformationSet()) {
//            System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
//        }
//        System.out.println();

//        President president = new President(20);
//        PossibleMoves possibleMoves = new PresidentPossibleMoves(president.getPlayer1());
//        for (Card card : president.getPlayer1().getDeck()) {
//            System.out.println(card.getNumber());
//        }
//        System.out.println();
//        for (Tuple tuple : possibleMoves.getPossibleActions()){
//            System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
//        }
//        System.out.println();


//        president.getPlayer1().isToPlay(false);
//        president.getPlayer2().isToPlay(true);
//        MCTS mcts = new MCTS(president);
//        mcts.start();
//        for (Node node : mcts.getFourBestNodes()) {
//            System.out.println(node.getPlayer().getGameState().getNumber() + " -> " + node.getPlayer().getGameState().getOccurrence());
//        }


//        for (Tuple tuple : president.getPlayer1().getSortedDeck(president.getPlayer1().getDeck())) {
//            System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
//        }
//        System.out.println();
//
//        EvaluationFunction evaluationFunction = new PresidentEvalFunction(president.getPlayer1());
//        System.out.println("score = " + evaluationFunction.evaluate());
//
//        PossibleMoves possibleMoves = new PresidentPossibleMoves(president.getPlayer1());
//        ArrayList<Card> deckIS = possibleMoves.computeInformationSetCards();
//        for (Card card : deckIS) {
//            System.out.println(card.getNumber() + " -> " + card.getSymbol());
//        }
//        System.out.println();
//
//        for (Tuple card : possibleMoves.getInformationSet(deckIS)) {
//            System.out.println(card.getNumber() + " -> " + card.getOccurrence());
//        }
//        System.out.println();

//        ABTS abts = new ABTS(president, 3);
//        abts.start();
//        for (Node node : abts.getFourBestNodes()) {
//            System.out.println(node.getPlayer().getGameState().getNumber() + " -> " + node.getPlayer().getGameState().getOccurrence());
//        }
//        System.out.println();

//        AdaptiveFunction adaptiveFunction = new AdaptiveFunction(new Checkers(null));

//        int count = 100;
//        OutputCSV outputCSV = new OutputCSV("output.csv");
//        String[][] data = new String[count][1];
//        for (int i = 0; i < count; i++) {
//            data[i][0] = String.valueOf(Util.getGaussian(0.6, 5));
//        }
//        outputCSV.writeResume(data);

//        ---------------------------------------------------------------------------------------------------------

        Random random = new Random();
        int[] config = new int[]{2, 3, 4, 5, 6};
        int index = 0;
        int counter = 0;
        double counterWinP1 = 0;
        double counterWinP2 = 0;
        for (int i = 0; i < 100; i++) {

            int currPlayer = 1;
//            Board board = new Board(6, false);
            Board board = new Board(8, true);
            Checkers checkers = new Checkers(board);
            Checkers.currentPlayer = currPlayer;
            AdaptiveFunction adaptiveFunction = new AdaptiveFunction(checkers);

            while (!checkers.isDone(board.getGameBoard())) {

//                MCTS mcts = null;
//                ABTS abts = null;
//                if (currPlayer == 1) {
//                    mcts = new MCTS(checkers, new double[]{1, 2500});
//                    mcts.start();
//                } else {
//                    abts = new ABTS(checkers, 5);
//                    abts.setCurrentPlayer(currPlayer);
//                    abts.start();
//                }

                int[][] previousBoard = board.getGameBoard();
                double globalVariableBefore = checkers.getGlobalAdaptiveVariable(previousBoard);

                MCTS mcts = new MCTS(checkers, new double[]{1, 1000});
                mcts.start();

                boolean check = false;
                while (!check) {
                    double moveChoice = random.nextDouble();
                    if (currPlayer == 1) {
                        if (moveChoice < 0.65) {
                            if (mcts.getFourBestNodes().get(0) != null) {
                                board.setBoard(mcts.getFourBestNodes().get(0).getBoardState());
                                check = true;
                            }
                        } else if (moveChoice < 0.8) {
                            if (mcts.getFourBestNodes().get(1) != null) {
                                board.setBoard(mcts.getFourBestNodes().get(1).getBoardState());
                                check = true;
                            }
                        } else if (moveChoice < 0.9) {
                            if (mcts.getFourBestNodes().get(2) != null) {
                                board.setBoard(mcts.getFourBestNodes().get(2).getBoardState());
                                check = true;
                            }
                        } else {
                            if (mcts.getFourBestNodes().get(3) != null) {
                                board.setBoard(mcts.getFourBestNodes().get(3).getBoardState());
                                check = true;
                            }
                        }
                        checkers.updateAdaptiveVariable(previousBoard, board.getGameBoard());
                    }
//                    else {
//                        if (moveChoice < 0.65) {
//                            if (abts.getFourBestNodes().get(0) != null) {
//                                board.setBoard(abts.getFourBestNodes().get(0).getBoardState());
//                                check = true;
//                            }
//                        } else if (moveChoice < 0.8) {
//                            if (abts.getFourBestNodes().get(1) != null) {
//                                board.setBoard(abts.getFourBestNodes().get(1).getBoardState());
//                                check = true;
//                            }
//                        } else if (moveChoice < 0.9) {
//                            if (abts.getFourBestNodes().get(2) != null) {
//                                board.setBoard(abts.getFourBestNodes().get(2).getBoardState());
//                                check = true;
//                            }
//                        } else {
//                            if (abts.getFourBestNodes().get(3) != null) {
//                                board.setBoard(abts.getFourBestNodes().get(3).getBoardState());
//                                check = true;
//                            }
//                        }
//                    }
                    else {
//                        System.out.println(adaptiveFunction.getActionIndex(adaptiveFunction.getGlobalAdaptiveVariable(), adaptiveFunction.getAdaptiveVariable(), mcts.getActionsOrdered().size()));
                        board.setBoard(mcts.getActionsOrdered().get(adaptiveFunction.getActionIndex(adaptiveFunction.getGlobalAdaptiveVariable(), adaptiveFunction.getAdaptiveVariable(), mcts.getActionsOrdered().size())).getBoardState());
                        int[][] currentBoard = board.getGameBoard();
                        double globalVariableAfter = checkers.getGlobalAdaptiveVariable(currentBoard);
                        adaptiveFunction.updateVector(globalVariableBefore, globalVariableAfter, (mcts.getActionsOrdered().size())-1);
                        check = true;
                    }
                }
                currPlayer = Util.changeCurrentPlayer(currPlayer);
                Checkers.currentPlayer = currPlayer;
//                System.out.println();
//                Util.printBoard(checkers.getCheckersBoard().getGameBoard());
//                System.out.println();
            }
            counter++;
            System.out.println();
            System.out.println("counter = " + counter);
            System.out.println();
//            if (counter == 20) {
//                counter = 0;
//                System.out.println();
//                System.out.println("Config = " + config[index]);
//                System.out.println("P1 win ratio = " + counterWinP1/50);
//                System.out.println("P2 win ratio = " + counterWinP2/50);
//                System.out.println();
//                counterWinP1 = 0;
//                counterWinP2 = 0;
//                index++;
//            }
            if (Checkers.currentPlayer == 1) {
                checkers.updateWinnerFile("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_checkers.txt", HomePage.username, false);
                counterWinP2++;
            }
            else {
                checkers.updateWinnerFile("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_checkers.txt", HomePage.username, true);
                counterWinP1++;
            }
        }
//        System.out.println();
//        System.out.println("P1 (MCTS) win ratio = " + counterWinP1/50);
//        System.out.println("P2 (ABTS) win ratio = " + counterWinP2/50);
//        ---------------------------------------------------------------------------------------------------------

//        for (int i = 0; i < 10000; i++) {
//            System.out.print(Util.getGaussian(1, 1) + ", ");
//        }
    }
}

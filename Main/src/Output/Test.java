package Output;

import AI.AlphaBetaTreeSearch.ABTS;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.EvaluationFunction.EvaluationFunction;
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

//        President president = new President();
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


//        for (Card card : president.getPlayer1().getDeck()) {
//            System.out.println(card.getNumber() + " -> " + card.getSymbol());
//        }
//        System.out.println();
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
    }
}

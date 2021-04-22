package Output;

import AI.TreeStructure.GameTree;
import AI.TreeStructure.Node;
import AI.Util;
import Abalone.Game.Abalone;
import Abalone.Game.BoardUI;
import Checkers.Game.Board;
import Checkers.Game.Checkers;

public class Test {

    public static Board board;

    private static int[][] gameBoard = new int[][]{

            {3, 3, 3, 3, 3, 3, 3, 3},
            {3, 0, 2, 0, 0, 0, 2, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 1, 0, 2, 3},
            {3, 1, 0, 1, 0, 0, 0, 3},
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
        board = new Board(6, false);
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

//        CheckersEvalFunction f = new CheckersEvalFunction(gameBoard, 1);
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
        GameTree gameTree = new GameTree(new Checkers(new Board(6, false)), 3);
        gameTree.createTreeDFS();
        System.out.println(gameTree.getNodes().size());
        System.out.println(gameTree.getRootChildrenNodes().size());
        for (int[][] n : gameTree.getFourBestNodes()) {
            Util.printBoard(n);
        }


//        ABTS abts = new ABTS(gameTree);
//        abts.start();
//        for (int[][] arr : abts.getFourBestNodes()) {
//            Util.printBoard(arr);
//        }
    }

}

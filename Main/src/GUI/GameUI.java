package GUI;

import Game.Board;
import Game.Checkers;
import Game.Move;
import Game.MoveDirection;
import Output.Test;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameUI extends Application {

    private int numberOfMarblesOnBoard = 10;

    public static int squareSize = 80;

    private final int WIDTH = (numberOfMarblesOnBoard*squareSize)+(2*squareSize);
    private final int HEIGHT = (numberOfMarblesOnBoard*squareSize)+(2*squareSize);


    public Group pane = new Group();

    private Board board;
    private Checkers checkers;

    @Override
    public void start(Stage primaryStage) {

        this.board = new Board(numberOfMarblesOnBoard);

        displayBoard();
        displayMarbles();

        Scene scene = new Scene(pane ,WIDTH, HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("checkers");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        scene.setOnKeyPressed(t -> {
            Move move;
            KeyCode key = t.getCode();
            switch (key) {
                case ESCAPE:
                    System.exit(0);
                    break;
                case Q:
                    if (board.isSelected) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.TOP_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case E:
                    if (board.isSelected) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.TOP_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case C:
                    if (board.isSelected) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.BOTTOM_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case Z:
                    if (board.isSelected) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.BOTTOM_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
            }
        });

        Thread gameThread = new Thread(() -> {
            checkers = new Checkers(board);
            checkers.runGame();
        });
        gameThread.setDaemon(false);
        gameThread.start();
    }

    public void makeMove(Move move) {

        board.setBoard(move.getNewState());
        board.selected[board.xSelected][board.ySelected] = false;
        //board.drawPossibleMoves(true);
        board.clearStrokes();
        board.drawAllMarbles();
        checkWin();
        if (Checkers.currentPlayer == 1) {
            Checkers.currentPlayer = 2;
        }
        else {
            Checkers.currentPlayer = 1;
        }
    }

    public void checkWin() {

        if (checkers.isVictorious(board.getGameBoard())) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Game is over");
            if (Checkers.currentPlayer == 1) {
                System.out.println("Player 1 won - white");
            }
            else {
                System.out.println("Player 2 won - black");
            }
            System.exit(0);
        }
    }

    public void displayBoard() {

        for (int i = 0; i < board.marbles.length; i++) {
            for (int j = 0; j < board.marbles.length; j++) {
                Rectangle rect = new Rectangle((i + 1) * squareSize, (j + 1) * squareSize, squareSize, squareSize);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(3);
                if (i%2 == 0) {
                    if (j%2 == 0) {
                        rect.setFill(Color.rgb(255, 255, 255, 1.0));
                    }
                    else {
                        rect.setFill(Color.rgb(200, 200, 200, 1.0));
                    }
                    pane.getChildren().add(rect);
                }
                else {
                    if (j%2 == 0) {
                        rect.setFill(Color.rgb(200, 200, 200, 1.0));
                    }
                    else {
                        rect.setFill(Color.rgb(255, 255, 255, 1.0));
                    }
                    pane.getChildren().add(rect);
                }
            }
        }
    }

    public void displayMarbles() {

        for (int i = 0; i < board.marbles.length; i++) {
            for (int j = 0; j < board.marbles.length; j++) {
                pane.getChildren().add(board.marbles[i][j]);
            }
        }
    }
}

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

    private int width = 700;
    private int height = 700;

    public Group pane = new Group();

    private Board board;
    private Checkers checkers;

    @Override
    public void start(Stage primaryStage) {

        this.board = new Board();

        displayBoard();
        displayMarbles();

        Scene scene = new Scene(pane ,width, height);
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
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.TOP_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case W:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.FORWARD, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case E:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.TOP_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case D:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case C:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.BOTTOM_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case X:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.BACKWARD, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case Z:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.BOTTOM_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case A:
                    if (board.isSelected) {
                        move = new Move(board.xSelected, board.ySelected, MoveDirection.LEFT, board.getGameBoard(), Checkers.currentPlayer);
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
        board.drawPossibleMoves(true);
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

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle rect = new Rectangle((i + 1) * 100, (j + 1) * 100, 100, 100);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(3);
                if (i%2 == 0) {
                    if (j%2 == 0) {
                        rect.setFill(Color.rgb(200, 200, 200, 1.0));
                    }
                    else {
                        rect.setFill(Color.rgb(255, 255, 255, 1.0));
                    }
                    pane.getChildren().add(rect);
                }
                else {
                    if (j%2 == 0) {
                        rect.setFill(Color.rgb(255, 255, 255, 1.0));
                    }
                    else {
                        rect.setFill(Color.rgb(200, 200, 200, 1.0));
                    }
                    pane.getChildren().add(rect);
                }
            }
        }
    }

    public void displayMarbles() {

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pane.getChildren().add(board.marbles[i][j]);
            }
        }
    }
}

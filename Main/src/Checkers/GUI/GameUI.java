package Checkers.GUI;

import Checkers.Game.Board;
import Checkers.Game.Checkers;
import Checkers.Game.Move;
import Checkers.Game.MoveDirection;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameUI extends Application {

    private Scene scene;

    public static int squareSize = 80;
    private final int numberOfMarblesOnBoard = 6;
    private final int WIDTH = (numberOfMarblesOnBoard*squareSize)+(2*squareSize) + 250;
    private final int HEIGHT = (numberOfMarblesOnBoard*squareSize)+(2*squareSize);

    public Group pane = new Group();

    private CheckBox box1;
    private CheckBox box2;
    private CheckBox box3;
    private CheckBox box4;

    private Text move1;
    private Text move2;
    private Text move3;
    private Text move4;

    public static Text readyText;

    private Board board;
    private Checkers checkers;

    private boolean flag = false;
    private boolean done = true;

    private int choice;

    @Override
    public void start(Stage primaryStage) {

        this.board = new Board(numberOfMarblesOnBoard, true);
        this.checkers = new Checkers(this.board);

        displayBoard();
        displayMarbles();

        this.box1 = new CheckBox();
        this.box1.setTranslateX(WIDTH - 275);
        this.box1.setTranslateY(100);
        this.box1.setOnAction(event -> {
            if (flag) {
                box2.setSelected(false);
                box3.setSelected(false);
                box4.setSelected(false);
                showSelection(0, checkers.getFourBestMoves());
            }
        });

        this.box2 = new CheckBox();
        this.box2.setTranslateX(WIDTH - 275);
        this.box2.setTranslateY(200);
        this.box2.setOnAction(event -> {
            if (flag) {
                box1.setSelected(false);
                box3.setSelected(false);
                box4.setSelected(false);
                showSelection(1, checkers.getFourBestMoves());
            }
        });

        this.box3 = new CheckBox();
        this.box3.setTranslateX(WIDTH - 275);
        this.box3.setTranslateY(300);
        this.box3.setOnAction(event -> {
            if (flag) {
                box1.setSelected(false);
                box2.setSelected(false);
                box4.setSelected(false);
                showSelection(2, checkers.getFourBestMoves());
            }
        });

        this.box4 = new CheckBox();
        this.box4.setTranslateX(WIDTH - 275);
        this.box4.setTranslateY(400);
        this.box4.setOnAction(event -> {
            if (flag) {
                box1.setSelected(false);
                box2.setSelected(false);
                box3.setSelected(false);
                showSelection(3, checkers.getFourBestMoves());
            }
        });

        this.box1.setFocusTraversable(false);
        this.box2.setFocusTraversable(false);
        this.box3.setFocusTraversable(false);
        this.box4.setFocusTraversable(false);

        pane.getChildren().addAll(box1, box2, box3, box4);

        this.move1 = new Text("Move 1");
        this.move1.setTranslateX(WIDTH - 230);
        this.move1.setTranslateY(115);
        this.move1.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.move2 = new Text("Move 2");
        this.move2.setTranslateX(WIDTH - 230);
        this.move2.setTranslateY(215);
        this.move2.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.move3 = new Text("Move 3");
        this.move3.setTranslateX(WIDTH - 230);
        this.move3.setTranslateY(315);
        this.move3.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.move4 = new Text("Move 4");
        this.move4.setTranslateX(WIDTH - 230);
        this.move4.setTranslateY(415);
        this.move4.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        pane.getChildren().addAll(move1, move2, move3, move4);

        readyText = new Text("Press ENTER to start MCTS");
        readyText.setTranslateX(WIDTH - 280);
        readyText.setTranslateY(500);
        readyText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        pane.getChildren().add(readyText);

        scene = new Scene(pane ,WIDTH, HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("checkers");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        controller();
    }

    public void controller() {

        scene.setOnKeyPressed(t -> {
            Move move;
            KeyCode key = t.getCode();
            switch (key) {
                case DIGIT1:
                    if (flag) {
                        showSelection(0, checkers.getFourBestMoves());
                    }
                    break;
                case DIGIT2:
                    if (flag) {
                        showSelection(1, checkers.getFourBestMoves());
                    }
                    break;
                case DIGIT3:
                    if (flag) {
                        showSelection(2, checkers.getFourBestMoves());
                    }
                    break;
                case DIGIT4:
                    if (flag) {
                        showSelection(3, checkers.getFourBestMoves());
                    }
                    break;
                case SPACE:
                    if (flag) {
                        makeMoveWithMCTS();
                        checkWin();
                        board.isSelectable = true;
                    }
                    break;
                case ENTER:
                    if (done) {
                        new Thread(() -> {
                            board.isSelectable = false;
                            checkers.runMCTS();
                            done = false;
                            flag = true;
                        }).start();
                    }
                    break;
                case ESCAPE:
                    System.exit(0);
                    break;
                case Q:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.TOP_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case E:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.TOP_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case C:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.BOTTOM_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
                case Z:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.BOTTOM_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            makeMove(move);
                        }
                    }
                    break;
            }
        });
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

    public void makeMoveWithMCTS() {

        board.setBoard(checkers.getFourBestMoves().get(choice));
        board.drawAllMarbles();
        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(0), true);
        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(1), true);
        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(2), true);
        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(3), true);
        box1.setSelected(false);
        box2.setSelected(false);
        box3.setSelected(false);
        box4.setSelected(false);
        checkers.getFourBestMoves().clear();
        readyText.setText("Press ENTER to start MCTS");
        done = true;
        flag = false;
        checkWin();
        if (Checkers.currentPlayer == 1) {
            Checkers.currentPlayer = 2;
        }
        else {
            Checkers.currentPlayer = 1;
        }
    }

    public void checkWin() {

        if (checkers.isDone(board.getGameBoard())) {
            new Thread(() -> {
                try {
                    System.out.println("Checkers game is over");
                    if (Checkers.currentPlayer == 2) {
                        System.out.println("Player 1 won - white");
                    }
                    else {
                        System.out.println("Player 2 won - black");
                    }
                    Thread.sleep(2000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
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

    public void showSelection(int choice, ArrayList<int[][]> list) {

        board.drawPossibleMoveFromMCTS(list.get(0), true);
        board.drawPossibleMoveFromMCTS(list.get(1), true);
        board.drawPossibleMoveFromMCTS(list.get(2), true);
        board.drawPossibleMoveFromMCTS(list.get(3), true);
        board.drawPossibleMoveFromMCTS(list.get(choice), false);
        box1.setSelected(false);
        box2.setSelected(false);
        box3.setSelected(false);
        box4.setSelected(false);
        switch (choice) {
            case 0:
                box1.setSelected(true);
                break;
            case 1:
                box2.setSelected(true);
                break;
            case 2:
                box3.setSelected(true);
                break;
            case 3:
                box4.setSelected(true);
                break;
        }
        this.choice = choice;
    }

    public void displayMarbles() {

        for (int i = 0; i < board.marbles.length; i++) {
            for (int j = 0; j < board.marbles.length; j++) {
                pane.getChildren().add(board.marbles[i][j]);
            }
        }
    }
}
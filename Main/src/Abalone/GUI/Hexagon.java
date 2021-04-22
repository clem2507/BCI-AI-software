package Abalone.GUI;

import AI.MonteCarloTreeSearch.MCTS;
import AI.Util;
import Abalone.Game.*;
import Checkers.Game.Checkers;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Hexagon extends Application {

    private final double WIDTH = 1000;
    private final double HEIGHT = 650;

    //Creation of a Border pane to make our scene easier to construct
    public static Group pane = new Group();

    private BoardUI board;
    public static Scene accessableScene;
    public static Text turnText;
    public static Stage primaryStage;

    private CheckBox box1;
    private CheckBox box2;
    private CheckBox box3;
    private CheckBox box4;
    private CheckBox isMCTS;
    private CheckBox isABTS;

    private Text move1;
    private Text move2;
    private Text move3;
    private Text move4;
    private Text isMCTStext;
    private Text isABTStext;
    private Text whosePlaying;
    public static Text readyText;

    public static String player1 = null;
    public static String player2 = null;

    public static int turnCounter = 0;
    public int currentPlayer = 1;
    private int choice;
    public boolean done = true;
    public boolean flag = false;

    private MCTS mcts;
    private Abalone abalone;

    // Hexagon should access Board to obtain Marbles positions, color, etc
    // Board is a backend-only class, while Hexagon is so far the only UI class in the game (thus, we can consider renaming it)

    public BoardUI getBoardUI() {
        return board;
    }  

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        setBackground();

        //Creating an object of Board, which constructs a board
        board = new BoardUI();
        board.hexagon.setTranslateX(-100);
        pane.getChildren().add(board.hexagon);

        PlayerH p1 = new PlayerH();
        PlayerH p2 = new PlayerH();
        abalone = new Abalone(this.board, p1, p2);

        Pane marbles = new Pane();
        for (int i = 0; i < board.circles.length; i++) {
            for (int j = 0; j < board.circles[0].length; j++) {
                if (board.circles[i][j] != null) {
                    marbles.getChildren().add(board.circles[i][j]);
                }
            }
        }
        marbles.setTranslateX(-250);
        pane.getChildren().add(marbles);

        displayWhoseTurn();

        this.box1 = new CheckBox();
        this.box1.setTranslateX(WIDTH - 275);
        this.box1.setTranslateY(100);
        this.box1.setOnAction(event -> {
            box2.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
            if (flag) {
                showSelection(0, abalone.getFourBestMoves());
            }
        });

        this.box2 = new CheckBox();
        this.box2.setTranslateX(WIDTH - 275);
        this.box2.setTranslateY(200);
        this.box2.setOnAction(event -> {
            box1.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
            if (flag) {
                showSelection(1, abalone.getFourBestMoves());
            }
        });

        this.box3 = new CheckBox();
        this.box3.setTranslateX(WIDTH - 275);
        this.box3.setTranslateY(300);
        this.box3.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box4.setSelected(false);
            if (flag) {
                showSelection(2, abalone.getFourBestMoves());
            }
        });

        this.box4 = new CheckBox();
        this.box4.setTranslateX(WIDTH - 275);
        this.box4.setTranslateY(400);
        this.box4.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box3.setSelected(false);
            if (flag) {
                showSelection(3, abalone.getFourBestMoves());
            }
        });

        this.box1.setFocusTraversable(false);
        this.box2.setFocusTraversable(false);
        this.box3.setFocusTraversable(false);
        this.box4.setFocusTraversable(false);

        pane.getChildren().addAll(box1, box2, box3, box4);

        this.isMCTS = new CheckBox();
        this.isMCTS.setTranslateX(100);
        this.isMCTS.setTranslateY(40);
        this.isMCTS.setSelected(true);
        this.isMCTS.setOnAction(event -> {
            if (board.isSelectable) {
                if (isMCTS.isSelected() || isABTS.isSelected()) {
                    isABTS.setSelected(false);
                    if (done) {
                        readyText.setText("Press ENTER to start MCTS");
                    }
                } else {
                    isMCTS.setSelected(true);
                }
            }
            else {
                isMCTS.setSelected(!isMCTS.isSelected());
            }
        });

        this.isABTS = new CheckBox();
        this.isABTS.setTranslateX(370);
        this.isABTS.setTranslateY(40);
        this.isABTS.setOnAction(event -> {
            if (board.isSelectable) {
                if (isMCTS.isSelected() || isABTS.isSelected()) {
                    isMCTS.setSelected(false);
                    if (done) {
                        readyText.setText("Press ENTER to start ABTS");
                    }
                } else {
                    isABTS.setSelected(true);
                }
            }
            else {
                isABTS.setSelected(!isABTS.isSelected());
            }
        });

        this.isMCTStext = new Text("Monte-Carlo Tree Search");
        this.isMCTStext.setTranslateX(140);
        this.isMCTStext.setTranslateY(55);
        this.isMCTStext.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));

        this.isABTStext = new Text("Alpha-Beta Tree Search");
        this.isABTStext.setTranslateX(410);
        this.isABTStext.setTranslateY(55);
        this.isABTStext.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));

        this.isMCTS.setFocusTraversable(false);
        this.isABTS.setFocusTraversable(false);

        pane.getChildren().addAll(isMCTS, isABTS, isMCTStext, isABTStext);

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

        if (isMCTS.isSelected()) {
            readyText = new Text("Press ENTER to start MCTS");
        }
        else {
            readyText = new Text("Press ENTER to start ABTS");
        }
        readyText.setTranslateX(WIDTH - 300);
        readyText.setTranslateY(500);
        readyText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.whosePlaying = new Text ("Black" + " to move");
        this.whosePlaying.setX(WIDTH - 250);
        this.whosePlaying.setY(55);
        this.whosePlaying.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.whosePlaying.setFill(Color.BLACK);

        pane.getChildren().addAll(move1, move2, move3, move4, readyText, whosePlaying);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Abalone");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                KeyCode key = t.getCode();
                switch (key) {
                    case ESCAPE:
                        System.exit(0);
                        break;
                    case ENTER:
                        if (done && isMCTS.isSelected()) {
                            new Thread(() -> {
                                readyText.setText("...");
                                board.isSelectable = false;
                                abalone.runMCTS();
                                done = false;
                                flag = true;
                            }).start();
                        }
                        else if (done) {
                            new Thread(() -> {
                                readyText.setText("...");
                                board.isSelectable = false;
                                abalone.runABTS();
                                done = false;
                                flag = true;
                            }).start();
                        }
                    case SPACE:
                        if (flag) {
                            makeMoveWithAI();
                            board.isSelectable = true;
                            turnCounter++;
                            whosePlaying.setText(getCurrentPlayerColor(Abalone.currentPlayer) + " to play");
                            turnText.setText("Turn number " + turnCounter);
                        }
//                        abalone.makeMove();
                        break;
                    case DIGIT1:
                        if (flag) {
                            showSelection(0, abalone.getFourBestMoves());
                        }
                        break;
                    case DIGIT2:
                        if (flag) {
                            showSelection(1, abalone.getFourBestMoves());
                        }
                        break;
                    case DIGIT3:
                        if (flag) {
                            showSelection(2, abalone.getFourBestMoves());
                        }
                        break;
                    case DIGIT4:
                        if (flag) {
                            showSelection(3, abalone.getFourBestMoves());
                        }
                        break;
                    }
                }
        });
        accessableScene = scene;
    }

    public void showSelection(int choice, ArrayList<int[][]> list) {

        board.drawPossibleMoveFromAI(list.get(0), true);
        board.drawPossibleMoveFromAI(list.get(1), true);
        board.drawPossibleMoveFromAI(list.get(2), true);
        board.drawPossibleMoveFromAI(list.get(3), true);
        board.drawPossibleMoveFromAI(list.get(choice), false);
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

    public void makeMoveWithAI() {

        board.setBoard(abalone.getFourBestMoves().get(choice));
        board.drawAllCells();
        board.drawPossibleMoveFromAI(abalone.getFourBestMoves().get(0), true);
        board.drawPossibleMoveFromAI(abalone.getFourBestMoves().get(1), true);
        board.drawPossibleMoveFromAI(abalone.getFourBestMoves().get(2), true);
        board.drawPossibleMoveFromAI(abalone.getFourBestMoves().get(3), true);
        board.clearStrokes();
        box1.setSelected(false);
        box2.setSelected(false);
        box3.setSelected(false);
        box4.setSelected(false);
        abalone.getFourBestMoves().clear();
        if (isMCTS.isSelected()) {
            readyText.setText("Press ENTER to start MCTS");
        }
        else {
            readyText.setText("Press ENTER to start ABTS");
        }
        done = true;
        flag = false;
        checkWin();
        Abalone.currentPlayer = Util.changeCurrentPlayer(Abalone.currentPlayer);
    }

    public void checkWin() {

        if (abalone.isDone(board.getGameBoard())) {
            new Thread(() -> {
                try {
                    System.out.println("Abalone game is over");
                    if (Abalone.currentPlayer == 2) {
                        System.out.println("Player 1 won - black");
                        readyText.setText("Player 1 won - black");
                    }
                    else {
                        System.out.println("Player 2 won - white");
                        readyText.setText("Player 2 won - white");
                    }
                    Thread.sleep(2000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void displayWhoseTurn() {

        turnText = new Text ("Turn number 0");
        turnText.setX(250);
        turnText.setY(HEIGHT-40);
        turnText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        turnText.setFill(Color.BLACK);
        turnText.setStrokeWidth(2);
        pane.getChildren().add(turnText);
    }

    private void setBackground() {

        try {
            BufferedImage buffer = ImageIO.read(new File("Main/res/grey2.jpg"));
            Image background = SwingFXUtils.toFXImage(buffer, null);
            ImageView view = new ImageView(background);
            pane.getChildren().addAll(view);
        } catch (Exception e) {
            System.out.println("file not found");
            System.exit(0);
        }
    }

    public String getCurrentPlayerColor(int player) {

        if (player == 1) {
            return "Black";
        }
        else {
            return "White";
        }
    }
}


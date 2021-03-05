package Abalone.GUI;

import AI.MCTS;
import Abalone.Game.BoardUI;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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

public class Hexagon extends Application {

    private final double WIDTH = 1270;
    private final double HEIGHT = 700;

    //Creation of a Border pane to make our scene easier to construct
    public static BorderPane pane = new BorderPane();

    private BoardUI board;
    public static Text whosePlaying;
    public static Text turnText;
    public static Stage primaryStage;

    public static String player1 = null;
    public static String player2 = null;

    public int currentPlayer = 1;
    public boolean done = true;
    public boolean flag = false;

    public MCTS mcts;

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
        pane.setCenter(board.hexagon);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                if (board.circles[i][j] != null)
                    pane.getChildren().add(board.circles[i][j]);
        }

        for (int i = 0; i < 6; i++){
            pane.getChildren().add(board.scoredCircles[i][0]);
            pane.getChildren().add(board.scoredCircles[i][1]);
        }

        displayWhosePlaying();
        displayWhoseTurn();

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
                        primaryStage.close();
                        break;
                    case ENTER:
                        if (done) {
                            //mcts = new MCTS(board.getBoard(), currentPlayer, 5000, 10, "Abalone");
                            mcts.start();
                            done = false;
                            flag = true;
                        }
                    case SPACE:
                        if (flag) {
                            makeMove();
                            flag = false;
                            done = true;
                        }
                        break;
                    case DIGIT1:
                        if (flag) {
                            showSelection(0, mcts.getFourBestNodes());
                        }
                        break;
                    }
                }
        });
    }

    public void showSelection(int choice, ArrayList<int[][]> list) {

        drawPossibleMoveFromMCTS(list.get(0), true);
        drawPossibleMoveFromMCTS(list.get(1), true);
        drawPossibleMoveFromMCTS(list.get(2), true);
        drawPossibleMoveFromMCTS(list.get(3), true);
        drawPossibleMoveFromMCTS(list.get(choice), false);
    }

    public void drawPossibleMoveFromMCTS(int[][] board, boolean clear) {

        int opponentPlayer;
        if (currentPlayer == 1) {
            opponentPlayer = 2;
        }
        else {
            opponentPlayer = 1;
        }
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                if (board[i][j] != this.board.getBoard()[i][j]) {
                    if (!clear) {
                        if (board[i][j] != 0) {
                            this.board.getCircles()[i-1][j-1].setStroke(Color.RED);
                        } else {
                            if (this.board.getBoard()[i][j] != opponentPlayer) {
                                this.board.getCircles()[i-1][j-1].setFill(Color.RED);
                            }
                        }
                    }
                    else {
                        this.board.getCircles()[i-1][j-1].setStroke(null);
                        this.board.drawAllCells();
                    }
                }
            }
        }
    }

    public void makeMove() {

        if (currentPlayer == 1) {
            currentPlayer = 2;
        }
        else {
            currentPlayer = 1;
        }
    }

    private void displayWhoseTurn() {
        turnText = new Text ("Turn number 0");
        turnText.setX(485);
        turnText.setY(670);
        turnText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        turnText.setFill(Color.BLACK);
        turnText.setStrokeWidth(2);
        pane.getChildren().add(turnText);
    }

    /**
     * displays message to indicate whose player is playing
     */
    private void displayWhosePlaying() {
        whosePlaying = new Text (displayCurrentPlayer(1));
        whosePlaying.setX(485);
        whosePlaying.setY(640);
        whosePlaying.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        whosePlaying.setFill(Color.BLACK);
        whosePlaying.setStrokeWidth(2);
        pane.getChildren().add(whosePlaying);
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

    public static String displayCurrentPlayer(int player) {
        String currentPlayer = null;

        if(player == 1){
            currentPlayer = player1;
        }else if(player == 2){
            currentPlayer = player2;
        }

        return currentPlayer;
    }
}


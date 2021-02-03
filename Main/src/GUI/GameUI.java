package GUI;

import Game.Board;
import Game.Checkers;
import Game.Move;
import Game.MoveDirection;
import Output.Test;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameUI extends Application {

    private int numberOfMarblesOnBoard = 8;

    public static int squareSize = 80;

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

//    private Button button1;
//    private Button button2;
//    private Button button3;
//    private Button button4;

//    public static Rectangle move1source;
//    public static Rectangle move1target;
//    public static Rectangle move2source;
//    public static Rectangle move2target;
//    public static Rectangle move3source;
//    public static Rectangle move3target;
//    public static Rectangle move4source;
//    public static Rectangle move4target;

    private Board board;
    private Checkers checkers;

    private boolean flag = false;
    private boolean done = true;

    private int choice;

    @Override
    public void start(Stage primaryStage) {

//        Thread gameThread = new Thread(() -> {
//            checkers = new Checkers(board);
//            checkers.runGame();
//        });
//        gameThread.setDaemon(false);
//        gameThread.start();

        this.board = new Board(numberOfMarblesOnBoard);
        this.checkers = new Checkers(this.board);

//        this.button1 = new Button("Move 1");
//        button1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 17));
//        button1.setStyle("-fx-background-color: #FF3333");
//        button1.setTranslateX(WIDTH - 150);
//        button1.setTranslateY(100);
//        button1.setOnAction(event -> {
//            if (flag) {
//                if (checkers.getFourBestMoves().size() > 0) {
//                    board.setBoard(checkers.getFourBestMoves().get(0));
//                    board.drawAllMarbles();
//                    board.drawPossibleMovesFromMCTS(checkers.getFourBestMoves(), true);
//                    checkers.getFourBestMoves().clear();
//                    //checkers.setFlag(true);
//                    done = true;
//                }
//            }
//        });
//
//        this.button2 = new Button("Move 2");
//        button2.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 17));
//        button2.setStyle("-fx-background-color: #3399FF");
//        button2.setTranslateX(WIDTH - 150);
//        button2.setTranslateY(175);
//        button2.setOnAction(event -> {
//            if (flag) {
//                if (checkers.getFourBestMoves().size() > 0) {
//                    board.setBoard(checkers.getFourBestMoves().get(1));
//                    board.drawAllMarbles();
//                    board.drawPossibleMovesFromMCTS(checkers.getFourBestMoves(), true);
//                    checkers.getFourBestMoves().clear();
//                    //checkers.setFlag(true);
//                    done = true;
//                }
//            }
//        });
//
//        this.button3 = new Button("Move 3");
//        button3.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 17));
//        button3.setStyle("-fx-background-color: #33FF33");
//        button3.setTranslateX(WIDTH - 150);
//        button3.setTranslateY(250);
//        button3.setOnAction(event -> {
//            if (flag) {
//                if (checkers.getFourBestMoves().size() > 0) {
//                    board.setBoard(checkers.getFourBestMoves().get(2));
//                    board.drawAllMarbles();
//                    board.drawPossibleMovesFromMCTS(checkers.getFourBestMoves(), true);
//                    checkers.getFourBestMoves().clear();
//                    //checkers.setFlag(true);
//                    done = true;
//                }
//            }
//        });
//
//        this.button4 = new Button("Move 4");
//        button4.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 17));
//        button4.setStyle("-fx-background-color: #B266FF");
//        button4.setTranslateX(WIDTH - 150);
//        button4.setTranslateY(325);
//        button4.setOnAction(event -> {
//            if (flag) {
//                if (checkers.getFourBestMoves().size() > 0) {
//                    board.setBoard(checkers.getFourBestMoves().get(3));
//                    board.drawAllMarbles();
//                    board.drawPossibleMovesFromMCTS(checkers.getFourBestMoves(), true);
//                    checkers.getFourBestMoves().clear();
//                    //checkers.setFlag(true);
//                    done = true;
//                }
//            }
//        });
//
//        pane.getChildren().addAll(button1, button2, button3, button4);

        displayBoard();
        displayMarbles();

        this.box1 = new CheckBox();
        this.box1.setTranslateX(WIDTH - 275);
        this.box1.setTranslateY(100);

        this.box2 = new CheckBox();
        this.box2.setTranslateX(WIDTH - 275);
        this.box2.setTranslateY(200);

        this.box3 = new CheckBox();
        this.box3.setTranslateX(WIDTH - 275);
        this.box3.setTranslateY(300);

        this.box4 = new CheckBox();
        this.box4.setTranslateX(WIDTH - 275);
        this.box4.setTranslateY(400);

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

//        GameUI.move1source = new Rectangle();
//        GameUI.move1target = new Rectangle();
//        GameUI.move2source = new Rectangle();
//        GameUI.move2target = new Rectangle();
//        GameUI.move3source = new Rectangle();
//        GameUI.move3target = new Rectangle();
//        GameUI.move4source = new Rectangle();
//        GameUI.move4target = new Rectangle();
//
//        pane.getChildren().addAll(move1source, move1target, move2source, move2target, move3source, move3target, move4source, move4target);

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
                case DIGIT1:
                    if (flag) {
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(1), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(2), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(3), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(0), false);
                        box1.setSelected(true);
                        box2.setSelected(false);
                        box3.setSelected(false);
                        box4.setSelected(false);
                        choice = 0;
                    }
                    break;
                case DIGIT2:
                    if (flag) {
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(0), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(2), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(3), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(1), false);
                        box1.setSelected(false);
                        box2.setSelected(true);
                        box3.setSelected(false);
                        box4.setSelected(false);
                        choice = 1;
                    }
                    break;
                case DIGIT3:
                    if (flag) {
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(3), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(0), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(1), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(2), false);
                        box1.setSelected(false);
                        box2.setSelected(false);
                        box3.setSelected(true);
                        box4.setSelected(false);
                        choice = 2;
                    }
                    break;
                case DIGIT4:
                    if (flag) {
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(0), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(1), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(2), true);
                        board.drawPossibleMoveFromMCTS(checkers.getFourBestMoves().get(3), false);
                        box1.setSelected(false);
                        box2.setSelected(false);
                        box3.setSelected(false);
                        box4.setSelected(true);
                        choice = 3;
                    }
                    break;
                case SPACE:
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
                    break;
                case ENTER:
                    if (done) {
                        checkers.runMCTS();
                        done = false;
                    }
                    flag = true;
                    break;
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

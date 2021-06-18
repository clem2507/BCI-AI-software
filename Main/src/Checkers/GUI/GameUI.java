package Checkers.GUI;

import AI.EvaluationFunction.Adaptive.AdaptiveFunction;
import AI.EvaluationFunction.Checkers.CheckersEvalFunction;
import AI.TreeStructure.Node;
import AI.Util;
import Checkers.Game.Board;
import Checkers.Game.Checkers;
import Checkers.Game.Move;
import Checkers.Game.MoveDirection;
import Output.HomePage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GameUI extends Application {

    private Stage primaryStage;
    private Scene scene;

    public static int squareSize = 80;
    public static int turnCounter = 0;
    private int numberOfMarblesOnBoard;
    private int WIDTH;
    private int HEIGHT;

    public Group pane = new Group();

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
    private Text turnNumberText;
    private Text whosePlaying;

    public static Text readyText;

    private Board board;
    private Checkers checkers;
    private AdaptiveFunction adaptiveFunction;

    private boolean flag = false;
    private boolean done = true;
    private boolean completeBoard;

    private int choice;
    private int ruleIndex;

    private double globalAdaptive1;
    private double globalAdaptive2;

    public GameUI(int boardSize, boolean completeBoard) {

        this.numberOfMarblesOnBoard = boardSize;
        this.completeBoard = completeBoard;
        this.WIDTH = (numberOfMarblesOnBoard*squareSize)+(2*squareSize) + 250;
        this.HEIGHT = (numberOfMarblesOnBoard*squareSize)+(2*squareSize);
    }

    @Override
    public void start(Stage primaryStage) {

        this.board = new Board(numberOfMarblesOnBoard, completeBoard);
        this.checkers = new Checkers(this.board);

        this.adaptiveFunction = new AdaptiveFunction(checkers);

        setBackground();
        displayBoard();
        displayMarbles();

        this.box1 = new CheckBox();
        this.box1.setTranslateX(WIDTH - 230);
        this.box1.setTranslateY(135);
        this.box1.setOnAction(event -> {
            box2.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
            if (flag) {
                showSelection(0, checkers.getFourBestMoves());
            }
        });

        this.box2 = new CheckBox();
        this.box2.setTranslateX(WIDTH - 230);
        this.box2.setTranslateY(235);
        this.box2.setOnAction(event -> {
            box1.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
            if (flag) {
                showSelection(1, checkers.getFourBestMoves());
            }
        });

        this.box3 = new CheckBox();
        this.box3.setTranslateX(WIDTH - 230);
        this.box3.setTranslateY(335);
        this.box3.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box4.setSelected(false);
            if (flag) {
                showSelection(2, checkers.getFourBestMoves());
            }
        });

        this.box4 = new CheckBox();
        this.box4.setTranslateX(WIDTH - 230);
        this.box4.setTranslateY(435);
        this.box4.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box3.setSelected(false);
            if (flag) {
                showSelection(3, checkers.getFourBestMoves());
            }
        });

        this.box1.setFocusTraversable(false);
        this.box2.setFocusTraversable(false);
        this.box3.setFocusTraversable(false);
        this.box4.setFocusTraversable(false);

        pane.getChildren().addAll(box1, box2, box3, box4);

        this.isMCTS = new CheckBox();
        this.isMCTS.setTranslateX(80);
        this.isMCTS.setTranslateY(25);
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
        this.isABTS.setTranslateX(350);
        this.isABTS.setTranslateY(25);
        this.isABTS.setOnAction(event -> {
            if (checkers.getCurrentPlayer() == 1) {
                if (board.isSelectable) {
                    if (isMCTS.isSelected() || isABTS.isSelected()) {
                        isMCTS.setSelected(false);
                        if (done) {
                            readyText.setText("Press ENTER to start ABTS");
                        }
                    } else {
                        isABTS.setSelected(true);
                    }
                } else {
                    isABTS.setSelected(!isABTS.isSelected());
                }
            }
            else {
                isABTS.setSelected(false);
            }
        });

        this.isMCTStext = new Text("Monte-Carlo Tree Search");
        this.isMCTStext.setTranslateX(120);
        this.isMCTStext.setTranslateY(40);
        this.isMCTStext.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));

        this.isABTStext = new Text("Alpha-Beta Tree Search");
        this.isABTStext.setTranslateX(390);
        this.isABTStext.setTranslateY(40);
        this.isABTStext.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));

        this.isMCTS.setFocusTraversable(false);
        this.isABTS.setFocusTraversable(false);

        pane.getChildren().addAll(isMCTS, isABTS, isMCTStext, isABTStext);

        this.move1 = new Text("Move 1");
        this.move1.setTranslateX(WIDTH - 200);
        this.move1.setTranslateY(150);
        this.move1.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.move2 = new Text("Move 2");
        this.move2.setTranslateX(WIDTH - 200);
        this.move2.setTranslateY(250);
        this.move2.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.move3 = new Text("Move 3");
        this.move3.setTranslateX(WIDTH - 200);
        this.move3.setTranslateY(350);
        this.move3.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.move4 = new Text("Move 4");
        this.move4.setTranslateX(WIDTH - 200);
        this.move4.setTranslateY(450);
        this.move4.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        pane.getChildren().addAll(move1, move2, move3, move4);

        if (isMCTS.isSelected()) {
            readyText = new Text("Press ENTER to start MCTS");
        }
        else {
            readyText = new Text("Press ENTER to start ABTS");
        }
        readyText.setTranslateX(WIDTH - 280);
        readyText.setTranslateY(510);
        readyText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        this.turnNumberText = new Text("Turn number " + turnCounter);
        this.turnNumberText.setTranslateX(80);
        this.turnNumberText.setTranslateY(HEIGHT-30);
        this.turnNumberText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        pane.getChildren().addAll(readyText, turnNumberText);

        this.whosePlaying = new Text ("White" + " to play");
        this.whosePlaying.setX(WIDTH - 230);
        this.whosePlaying.setY(95);
        this.whosePlaying.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.whosePlaying.setFill(Color.BLACK);

        pane.getChildren().add(whosePlaying);

        scene = new Scene(pane ,WIDTH, HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("checkers");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        this.primaryStage = primaryStage;

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
                        int[][] previousBoard = board.getGameBoard();
                        makeMoveWithAI();
                        isABTS.setSelected(false);
                        isMCTS.setSelected(true);
                        int[][] currentBoard = board.getGameBoard();
                        if (checkers.getCurrentPlayer() == 2) {
                            checkers.updateAdaptiveVariable(previousBoard, currentBoard);
                        }
                        board.isSelectable = true;
                        turnCounter++;
                        whosePlaying.setText(getCurrentPlayerColor(Checkers.currentPlayer) + " to play");
                        turnNumberText.setText("Turn number " + turnCounter);
                    }
                    break;
                case ENTER:
                    if (done && isMCTS.isSelected()) {
                        new Thread(() -> {
                            readyText.setText("...");
                            board.isSelectable = false;
                            if (checkers.getCurrentPlayer()==2) {
//                                adaptiveFunction.updateAdaptiveVariable();
//                                globalAdaptive1 = adaptiveFunction.getGlobalAdaptiveFunction();
//                                adaptiveFunction.updateWeights(adaptiveFunction.getGlobalAdaptiveFunction());
//                                ruleIndex = adaptiveFunction.getRuleIndex();
//                                ruleIndex = getActionIndex(globalAdaptive1, checkers.getActions().size());
//                                checkers.runMCTS(adaptiveFunction.getRuleBase().get(ruleIndex).getConfiguration());
                                int[][] previousBoard = board.getGameBoard();
                                double globalVariableBefore = checkers.getGlobalAdaptiveVariable(previousBoard);
//                                double previousEvalFunction1 = new CheckersEvalFunction(previousBoard, 1).evaluate();
//                                double previousEvalFunction2 = new CheckersEvalFunction(previousBoard, 2).evaluate();
                                checkers.runMCTS(new double[]{1, 1000});
                                makeMoveWithAI(adaptiveFunction.getActionIndex(adaptiveFunction.getGlobalAdaptiveVariable(), adaptiveFunction.getAdaptiveVariable(), checkers.getActions().size()));
                                int[][] currentBoard = board.getGameBoard();
                                double globalVariableAfter = checkers.getGlobalAdaptiveVariable(currentBoard);
//                                double afterEvalFunction1 = new CheckersEvalFunction(currentBoard, 1).evaluate();
//                                double afterEvalFunction2 = new CheckersEvalFunction(currentBoard, 2).evaluate();
//                                System.out.println("globalVariableAfter = " + globalVariableAfter);
//                                System.out.println("globalVariableBefore = " + globalVariableBefore);
//                                if (globalVariableAfter > globalVariableBefore && checkers.getActions().size() > 1) {
//                                    System.out.println("ok");


//                                double penalty = 0;
//                                if (globalVariableBefore < 0) {
//                                    if (globalVariableAfter <= 0) {
//                                        if (globalVariableAfter < globalVariableBefore) {
//                                            penalty = globalVariableAfter - globalVariableBefore;
//                                        }
//                                    }
//                                    if (globalVariableAfter > 0) {
//                                        if (globalVariableAfter > Math.abs(globalVariableBefore)) {
//                                            penalty = globalVariableAfter - globalVariableBefore;
//                                        }
//                                    }
//                                }
//                                else {
//                                    if (globalVariableAfter >= 0) {
//                                        if (globalVariableAfter > globalVariableBefore) {
//                                            penalty = globalVariableBefore - globalVariableAfter;
//                                        }
//                                    }
//                                    if (globalVariableAfter < 0) {
//                                        if (Math.abs(globalVariableAfter) > Math.abs(globalVariableBefore)) {
//                                            penalty = globalVariableBefore + globalVariableAfter;
//                                        }
//                                    }
//                                }
//                                if (penalty != 0) {
//                                    adaptiveFunction.updateVector(penalty, (checkers.getActions().size())-1);
//                                    adaptiveFunction.updateFile();
//                                }
//                                else {
//                                    System.out.println();
//                                }

//                                IMPORTANT, KEEP THIS LINE
//                                adaptiveFunction.updateVector(globalVariableBefore, globalVariableAfter, (checkers.getActions().size())-1);

//                                }
                                board.isSelectable = true;
                                turnCounter++;
                                whosePlaying.setText(getCurrentPlayerColor(Checkers.currentPlayer) + " to play");
                                turnNumberText.setText("Turn number " + turnCounter);
                                Platform.runLater(this::checkWin);
                            }
                            else {
                                checkers.runMCTS(new double[] {1, 1000});
                                done = false;
                                flag = true;
                            }
                        }).start();
                    }
                    else if (done) {
                        new Thread(() -> {
                            readyText.setText("...");
                            board.isSelectable = false;
                            checkers.runABTS();
//                            if (checkers.getCurrentPlayer()==1) {
//                                makeMoveWithAI(choice);
//                                board.isSelectable = true;
//                                turnCounter++;
//                                whosePlaying.setText(getCurrentPlayerColor(Checkers.currentPlayer) + " to play");
//                                turnNumberText.setText("Turn number " + turnCounter);
//                                Platform.runLater(this::checkWin);
//                            }
//                            else {
                                done = false;
                                flag = true;
//                            }
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
                            int[][] previousBoard = board.getGameBoard();
                            makeMove(move);
                            int[][] currentBoard = board.getGameBoard();
                            checkers.updateAdaptiveVariable(previousBoard, currentBoard);
                        }
                    }
                    break;
                case E:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.TOP_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            int[][] previousBoard = board.getGameBoard();
                            makeMove(move);
                            int[][] currentBoard = board.getGameBoard();
                            checkers.updateAdaptiveVariable(previousBoard, currentBoard);
                        }
                    }
                    break;
                case C:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.BOTTOM_RIGHT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            int[][] previousBoard = board.getGameBoard();
                            makeMove(move);
                            int[][] currentBoard = board.getGameBoard();
                            checkers.updateAdaptiveVariable(previousBoard, currentBoard);
                        }
                    }
                    break;
                case Z:
                    if (board.isSelected && !flag) {
                        move = new Move(board.xSelected+1, board.ySelected+1, MoveDirection.BOTTOM_LEFT, board.getGameBoard(), Checkers.currentPlayer);
                        if (move.checkMove()) {
                            int[][] previousBoard = board.getGameBoard();
                            makeMove(move);
                            int[][] currentBoard = board.getGameBoard();
                            checkers.updateAdaptiveVariable(previousBoard, currentBoard);
                        }
                    }
                    break;
                case B:
                    HomePage homePage = new HomePage();
                    homePage.start(primaryStage);
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
        turnCounter++;
        turnNumberText.setText("Turn number " + turnCounter);
        whosePlaying.setText(getCurrentPlayerColor(Util.changeCurrentPlayer(Checkers.currentPlayer)) + " to play");
        checkWin();
        Checkers.currentPlayer = Util.changeCurrentPlayer(Checkers.currentPlayer);
    }

    public void makeMoveWithAI() {

        board.setBoard(checkers.getFourBestMoves().get(choice).getBoardState());
        board.drawAllMarbles();
        board.drawPossibleMoveFromAI(checkers.getFourBestMoves().get(0).getBoardState(), true);
        board.drawPossibleMoveFromAI(checkers.getFourBestMoves().get(1).getBoardState(), true);
        board.drawPossibleMoveFromAI(checkers.getFourBestMoves().get(2).getBoardState(), true);
        board.drawPossibleMoveFromAI(checkers.getFourBestMoves().get(3).getBoardState(), true);
        board.clearStrokes();
        box1.setSelected(false);
        box2.setSelected(false);
        box3.setSelected(false);
        box4.setSelected(false);
        if (isMCTS.isSelected()) {
            readyText.setText("Press ENTER to start MCTS");
//            checkers.getActions().clear();
            checkers.getFourBestMoves().clear();
        }
        else {
            readyText.setText("Press ENTER to start ABTS");
//            checkers.getFourBestMoves().clear();
        }
        done = true;
        flag = false;
        checkWin();
        Checkers.currentPlayer = Util.changeCurrentPlayer(Checkers.currentPlayer);
    }

    public void makeMoveWithAI(int index) {

        if (isMCTS.isSelected()) {
            board.setBoard(checkers.getActions().get(index).getBoardState());
        }
        else if (isABTS.isSelected()) {
            board.setBoard(checkers.getFourBestMoves().get(index).getBoardState());
        }
        board.drawAllMarbles();
//        adaptiveFunction.updateAdaptiveVariable();

//        globalAdaptive2 = adaptiveFunction.getGlobalAdaptiveVariable();
        // UPDATE FUNCTION
//        double outcome = globalAdaptive2 - globalAdaptive1;
//        adaptiveFunction.getRuleBase().get(ruleIndex).setScore(adaptiveFunction.getRuleBase().get(ruleIndex).getScore() + outcome);
//        adaptiveFunction.updateFile();
        box1.setSelected(false);
        box2.setSelected(false);
        box3.setSelected(false);
        box4.setSelected(false);
        if (isMCTS.isSelected()) {
            readyText.setText("Press ENTER to start MCTS");
//            checkers.getActions().clear();
            checkers.getFourBestMoves().clear();
        }
        else {
            readyText.setText("Press ENTER to start ABTS");
//            checkers.getFourBestMoves().clear();
        }
        done = true;
        flag = false;
        Checkers.currentPlayer = Util.changeCurrentPlayer(Checkers.currentPlayer);
    }

    public void checkWin() {

        if (checkers.isDone(board.getGameBoard())) {
            System.out.println("Checkers game is over");
            if (Checkers.currentPlayer == 1) {
                System.out.println("Player 1 won - white");
                readyText.setText("Player 1 won - white");
                checkers.updateWinnerFile("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_checkers.txt", HomePage.username, true);
                createPopUpWindow(1);
            }
            else {
                System.out.println("Player 2 won - black");
                readyText.setText("Player 2 won - black");
                checkers.updateWinnerFile("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_checkers.txt", HomePage.username, false);
                createPopUpWindow(2);
            }
        }
    }

    public void createPopUpWindow(int winner) {

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.primaryStage);
        Text text;
        if (winner == 1) {
            text = new Text(
                    "\n \t Player 1 won - white"
            );
        }
        else {
            text = new Text(
                    "\n \t Player 2 won - black"
            );
        }
        text.setFont(Font.font("Arial", 13));
        VBox dialogVbox = new VBox(35);
        dialogVbox.getChildren().add(text);
        Button playAgain = new Button("Play Again");
        playAgain.setTranslateX(55);
        playAgain.setOnAction(event -> {
            GameUI.turnCounter = 0;
            Checkers.currentPlayer = 1;
            HomePage homePage = new HomePage();
            homePage.start(this.primaryStage);
            dialog.close();
        });
        Button exit = new Button("Exit");
        exit.setTranslateX(73);
        exit.setOnAction(event -> System.exit(0));
        dialogVbox.getChildren().addAll(playAgain, exit);
        Scene dialogScene = new Scene(dialogVbox, 190, 190);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();
        dialog.setOnCloseRequest(t -> System.exit(0));
        dialogScene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            switch (keyCode) {
                case ESCAPE:
                    System.exit(0);
                    break;
                case ENTER:
                    playAgain.fire();
                    break;
            }
        });
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

    public void showSelection(int choice, ArrayList<Node> list) {

        board.drawPossibleMoveFromAI(list.get(0).getBoardState(), true);
        board.drawPossibleMoveFromAI(list.get(1).getBoardState(), true);
        board.drawPossibleMoveFromAI(list.get(2).getBoardState(), true);
        board.drawPossibleMoveFromAI(list.get(3).getBoardState(), true);
        board.drawPossibleMoveFromAI(list.get(choice).getBoardState(), false);
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

    public String getCurrentPlayerColor(int player) {

        if (player == 1) {
            return "White";
        }
        else {
            return "Black";
        }
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
}

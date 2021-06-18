package President.GUI;

import AI.EvaluationFunction.Adaptive.AdaptiveFunction;
import AI.TreeStructure.Node;
import Checkers.GUI.GameUI;
import Checkers.Game.Checkers;
import Output.HomePage;
import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class UserInterface extends Application {

    private Stage primaryStage;

    private President president;

    private AdaptiveFunction adaptiveFunction;

    public int turnCounter = 0;
    public static Text readyText;

    private Group pane = new Group();
    private Scene scene;
    private final int WIDTH;
    private final int HEIGHT;

    private Card[] player1DeckUI;
    private Card[] player2DeckUI;
    private Card[] gameStateCardUI;

    private ImageView[] symbols1DeckUI;
    private ImageView[] symbols2DeckUI;
    private ImageView[] symbolsGameDeck;
    private ImageView arrowImage = new ImageView();

    private Text[] player1DeckNumbers;
    private Text[] player2DeckNumbers;
    private Text[] gameStateTextNumber;
    private Text move1;
    private Text move2;
    private Text move3;
    private Text move4;
    private Text isMCTStext;
    private Text isABTStext;
    private Text turnNumberText;
    private Text whosePlaying;

    private CheckBox box1;
    private CheckBox box2;
    private CheckBox box3;
    private CheckBox box4;
    private CheckBox isMCTS;
    private CheckBox isABTS;

    private boolean flag = false;
    private boolean done = true;

    private int choice = -1;
    private int deckSize;

    private double globalVariableBefore;
//    private double globalAdaptive2;

    private int ruleIndex;

    public UserInterface(int deckSize) {

        this.deckSize = deckSize;
        this.WIDTH = 1100 - ((20-deckSize)*20);
        this.HEIGHT = 700;
    }

    @Override
    public void start(Stage primaryStage) {

        setBackground();

        president = new President(deckSize);

        adaptiveFunction = new AdaptiveFunction(president);
//        System.out.println("adaptive variable = " + adaptiveFunction.getGlobalAdaptiveFunction());

        setArrow(president.getPlayer1().toPlay());

        president.getPlayer1().setDeck(sortPlayerDeckUI(president.getPlayer1().getDeck()));

        updateDeckUI(president.getPlayer1());
        updateDeckUI(president.getPlayer2());

        updateGameStateUI(new Tuple(0, 0));

        this.box1 = new CheckBox();
        this.box1.setTranslateX(WIDTH - 230);
        this.box1.setTranslateY(135);
        this.box1.setOnAction(event -> {
            box2.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
            choice = 0;
            showActionUI(president.getFourBestActions(), 0);
        });

        this.box2 = new CheckBox();
        this.box2.setTranslateX(WIDTH - 230);
        this.box2.setTranslateY(235);
        this.box2.setOnAction(event -> {
            box1.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
            choice = 1;
            showActionUI(president.getFourBestActions(), 1);
        });

        this.box3 = new CheckBox();
        this.box3.setTranslateX(WIDTH - 230);
        this.box3.setTranslateY(335);
        this.box3.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box4.setSelected(false);
            choice = 2;
            showActionUI(president.getFourBestActions(), 2);
        });

        this.box4 = new CheckBox();
        this.box4.setTranslateX(WIDTH - 230);
        this.box4.setTranslateY(435);
        this.box4.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box3.setSelected(false);
            choice = 3;
            showActionUI(president.getFourBestActions(), 3);
        });

        this.box1.setFocusTraversable(false);
        this.box2.setFocusTraversable(false);
        this.box3.setFocusTraversable(false);
        this.box4.setFocusTraversable(false);

        pane.getChildren().addAll(box1, box2, box3, box4);

        this.isMCTS = new CheckBox();
        this.isMCTS.setTranslateX(80);
        this.isMCTS.setTranslateY(40);
        this.isMCTS.setSelected(true);
        this.isMCTS.setOnAction(event -> {
            if (isMCTS.isSelected() || isABTS.isSelected()) {
                isABTS.setSelected(false);
                if (done) {
                    readyText.setText("Press ENTER to start MCTS");
                }
            } else {
                isMCTS.setSelected(true);
            }
        });

        this.isABTS = new CheckBox();
        this.isABTS.setTranslateX(350);
        this.isABTS.setTranslateY(40);
        this.isABTS.setOnAction(event -> {
            if (isMCTS.isSelected() || isABTS.isSelected()) {
                isMCTS.setSelected(false);
                if (done) {
                    readyText.setText("Press ENTER to start ABTS");
                }
            } else {
                isABTS.setSelected(true);
            }
        });

        this.isMCTStext = new Text("Monte-Carlo Tree Search");
        this.isMCTStext.setTranslateX(120);
        this.isMCTStext.setTranslateY(55);
        this.isMCTStext.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));

        this.isABTStext = new Text("Alpha-Beta Tree Search");
        this.isABTStext.setTranslateX(390);
        this.isABTStext.setTranslateY(55);
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
        this.turnNumberText.setTranslateY(HEIGHT-40);
        this.turnNumberText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));

        pane.getChildren().addAll(readyText, turnNumberText);

        this.whosePlaying = new Text ("Player 1");
        this.whosePlaying.setX(WIDTH - 200);
        this.whosePlaying.setY(70);
        this.whosePlaying.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.whosePlaying.setFill(Color.BLACK);

        pane.getChildren().add(whosePlaying);

        scene = new Scene(pane ,WIDTH, HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("President Game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        this.primaryStage = primaryStage;

        controller();
    }

    public void controller() {

        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case ESCAPE:
                    System.exit(0);
                    break;
                case ENTER:
                    if (done) {
                        done = false;
                        if (isMCTS.isSelected()) {
                            new Thread(() -> {
                                readyText.setText("...");
                                if (president.getPlayer1().toPlay()) {
                                    president.runMCTS(new double[]{1, 1000});
                                    whosePlaying.setText("Player 1");
                                    flag = true;
                                } else {
//                                    globalAdaptive1 = adaptiveFunction.getGlobalAdaptiveFunction();
//                                    globalAdaptive1 = adaptiveFunction.getAdaptiveVariable(2);
//                                    System.out.println("globalAdaptive1 = " + globalAdaptive1);
//                                    adaptiveFunction.updateWeights(adaptiveFunction.getGlobalAdaptiveVariable());
//                                    ruleIndex = adaptiveFunction.getRuleIndex();
//                                    president.runMCTS(adaptiveFunction.getRuleBase().get(ruleIndex).getConfiguration());
                                    president.runMCTS(new double[]{1, 1000});
                                    whosePlaying.setText("Player 2");
//                                    Player previousPlayer1State = new Player(president.getPlayer1());
//                                    Player previousPlayer2State = new Player(president.getPlayer2());
                                    globalVariableBefore = adaptiveFunction.getGlobalAdaptiveVariable();
//                                    System.out.println("globalVariableBefore = " + globalVariableBefore);
                                    Platform.runLater(this::takeActionWithAI);
//                                    System.out.println("globalVariableAfter = " + globalVariableAfter);
//                                    Player currentPlayer1State = new Player(president.getPlayer1());
//                                    Player currentPlayer2State = new Player(president.getPlayer2());
                                    flag = false;
                                    done = true;
                                }
                            }).start();
                        }
                        else if (isABTS.isSelected()) {
                            new Thread(() -> {
                                readyText.setText("...");
                                president.runABTS();
                                if (president.getPlayer1().toPlay()) {
                                    whosePlaying.setText("Player 1");
                                    flag = true;
                                } else {
                                    whosePlaying.setText("Player 2");
                                    Platform.runLater(this::takeActionWithAI);
                                    flag = false;
                                    done = true;
                                }
                            }).start();
                        }
                    }
                    break;
                case SPACE:
                    if (flag) {
                        Player previousPlayer1State = new Player(president.getPlayer1());
                        Player previousPlayer2State = new Player(president.getPlayer2());
//                        System.out.println(previousPlayer1State.getGameState().getNumber() + " --> " + previousPlayer1State.getGameState().getOccurrence());
//                        System.out.println(previousPlayer2State.getGameState().getNumber() + " --> " + previousPlayer2State.getGameState().getOccurrence());
                        takeActionWithAI();
                        Player currentPlayer1State = new Player(president.getPlayer1());
                        Player currentPlayer2State = new Player(president.getPlayer2());
//                        System.out.println(currentPlayer1State.getGameState().getNumber() + " --> " + currentPlayer1State.getGameState().getOccurrence());
//                        System.out.println(currentPlayer2State.getGameState().getNumber() + " --> " + currentPlayer2State.getGameState().getOccurrence());
                        if (president.getPlayer2().toPlay()) {
//                            president.updateAdaptiveVariable(previousGameState, president);
                            president.updateAdaptiveVariable(previousPlayer1State, previousPlayer2State, currentPlayer1State, currentPlayer2State);
                        }
                        if (president.getPlayer1().toPlay()) {
                            whosePlaying.setText("Player 1");
                        }
                        else {
                            whosePlaying.setText("Player 2");
                        }
                        flag = false;
                        done = true;
                    }
                    break;
                case DIGIT1:
                    if (flag) {
                        showActionUI(president.getFourBestActions(), 0);
                        choice = 0;
                    }
                    break;
                case DIGIT2:
                    if (flag) {
                        showActionUI(president.getFourBestActions(), 1);
                        choice = 1;
                    }
                    break;
                case DIGIT3:
                    if (flag) {
                        showActionUI(president.getFourBestActions(), 2);
                        choice = 2;
                    }
                    break;
                case DIGIT4:
                    if (flag) {
                        showActionUI(president.getFourBestActions(), 3);
                        choice = 3;
                    }
                    break;
                case B:
                    HomePage homePage = new HomePage();
                    homePage.start(primaryStage);
                    break;
            }
        });
    }

    private void takeActionWithAI() {

        if (isMCTS.isSelected()) {
            readyText.setText("Press ENTER to start MCTS");
        }
        else {
            readyText.setText("Press ENTER to start ABTS");
        }
        if (president.getPlayer1().toPlay()) {
            if (president.getFourBestActions().get(choice).getPlayer().getGameState().getNumber() != 0) {
                turnCounter++;
            }
            president.getPlayer1().takeAction(president.getFourBestActions().get(choice).getPlayer().getGameState(), president.getPlayer1().getDeck());
            president.getPlayer2().setOpponentNumberCards(president.getPlayer1().getDeck().size());
            president.getPlayer2().setGameState(president.getFourBestActions().get(choice).getPlayer().getGameState());
            president.getPlayer2().isToPlay(!president.getPlayer1().toPlay());
            president.getPlayer2().setGameDeck(president.getPlayer1().getGameDeck());
            updateDeckUI(president.getPlayer1());
            updateGameStateUI(president.getFourBestActions().get(choice).getPlayer().getGameState());
        }
        else {
            int index = adaptiveFunction.getActionIndex(adaptiveFunction.getGlobalAdaptiveVariable(), adaptiveFunction.getAdaptiveVariable(), president.getActions().size());
            if (president.getActions().get(index).getPlayer().getGameState().getNumber() != 0) {
                turnCounter++;
            }
//            Tuple action = president.getActions().get(adaptiveFunction.getRuleIndex()).getPlayer().getGameState();
            president.getPlayer2().takeAction(president.getActions().get(index).getPlayer().getGameState(), president.getPlayer2().getDeck());
            president.getPlayer1().setOpponentNumberCards(president.getPlayer2().getDeck().size());
            president.getPlayer1().setGameState(president.getActions().get(index).getPlayer().getGameState());
            president.getPlayer1().isToPlay(!president.getPlayer2().toPlay());
            president.getPlayer1().setGameDeck(president.getPlayer2().getGameDeck());
            updateDeckUI(president.getPlayer2());
//            updateGameStateUI(president.getFourBestActions().get(0).getPlayer().getGameState());
            updateGameStateUI(president.getActions().get(index).getPlayer().getGameState());
            double globalVariableAfter = adaptiveFunction.getGlobalAdaptiveVariable();
            adaptiveFunction.updateVector(globalVariableBefore, globalVariableAfter, (president.getActions().size())-1);
//            adaptiveFunction.updateAdaptiveVariable();
//            globalAdaptive2 = adaptiveFunction.getGlobalAdaptiveFunction();
//            globalAdaptive2 = adaptiveFunction.getAdaptiveVariable(2);
//            double outcome = Math.abs(globalAdaptive1) - Math.abs(globalAdaptive2);
//            double outcome = globalAdaptive2 - globalAdaptive1;
//            System.out.println("globalAdaptive2 = " + globalAdaptive2);
//            System.out.println("outcome = " + outcome);
//            System.out.println();
//            if (adaptiveFunction.getRuleBase().get(ruleIndex).getScore() + outcome > 0) {
//                adaptiveFunction.getRuleBase().get(ruleIndex).setScore(adaptiveFunction.getRuleBase().get(ruleIndex).getScore() + outcome);
//            }
//            else {
//                adaptiveFunction.getRuleBase().get(ruleIndex).setScore(0);
//            }
//            if (outcome > 0) {
//                adaptiveFunction.getRuleBase().get(ruleIndex).setScore(adaptiveFunction.getRuleBase().get(ruleIndex).getScore() + outcome);
//            }
//            adaptiveFunction.updateFile();
//            for (Rule rule : adaptiveFunction.getRuleBase()) {
//                System.out.println(rule.getScore());
//            }
//            System.out.println();
        }
        turnNumberText.setText("Turn number " + turnCounter);
        checkWin();
        setArrow(president.getPlayer1().toPlay());
    }

    public void updateDeckUI(Player player) {

        if (player.getId()==1) {
            if (turnCounter>0) {
                pane.getChildren().removeAll(player1DeckUI);
                pane.getChildren().removeAll(player1DeckNumbers);
                pane.getChildren().removeAll(symbols1DeckUI);
            }
            player1DeckUI = new Card[player.getDeck().size()];
            player1DeckNumbers = new Text[player.getDeck().size()];
            symbols1DeckUI = new ImageView[player.getDeck().size()];

            for (int i = 0; i < president.getPlayer1().getDeck().size(); i++) {
                player1DeckUI[i] = new Card(president.getPlayer1().getDeck().get(i).getNumber(), president.getPlayer1().getDeck().get(i).getSymbol());
                player1DeckUI[i].setTranslateX((i + 2) * 35);
                player1DeckUI[i].setTranslateY(500);
                player1DeckUI[i].setWidth(50);
                player1DeckUI[i].setHeight(100);
                player1DeckUI[i].setFill(Color.WHITE);
                player1DeckUI[i].setStroke(Color.BLACK);
                player1DeckUI[i].setArcHeight(15);
                player1DeckUI[i].setArcWidth(15);
                Text number = new Text(String.valueOf(president.getPlayer1().getDeck().get(i).getNumber()));
                switch (president.getPlayer1().getDeck().get(i).getNumber()) {
                    case 11:
                        number.setText("J");
                        break;
                    case 12:
                        number.setText("Q");
                        break;
                    case 13:
                        number.setText("K");
                        break;
                    case 14:
                        number.setText("A");
                        break;
                }
                if (president.getPlayer1().getDeck().get(i).getSymbol() == 3 || president.getPlayer1().getDeck().get(i).getSymbol() == 4) {
                    number.setFill(Color.RED);
                }
                number.setTranslateX(((i + 2) * 35) + 8);
                number.setTranslateY(520);
                player1DeckNumbers[i] = number;
            }
            pane.getChildren().addAll(player1DeckUI);
            pane.getChildren().addAll(player1DeckNumbers);

            for (int i = 0; i < president.getPlayer1().getDeck().size(); i++) {
                symbols1DeckUI[i] = setSymbol(president.getPlayer1().getDeck().get(i).getSymbol(), ((i + 2) * 35) + 6, 530);
            }
            pane.getChildren().addAll(symbols1DeckUI);
        }
        else {
            if (turnCounter>1) {
                pane.getChildren().removeAll(player2DeckUI);
            }
            player2DeckUI = new Card[player.getDeck().size()];

            for (int i = 0; i < president.getPlayer2().getDeck().size(); i++) {
                player2DeckUI[i] = new Card(president.getPlayer2().getDeck().get(i).getNumber(), president.getPlayer2().getDeck().get(i).getSymbol());
                player2DeckUI[i].setTranslateX((i+2)*35);
                player2DeckUI[i].setTranslateY(100);
                player2DeckUI[i].setWidth(50);
                player2DeckUI[i].setHeight(100);
                player2DeckUI[i].setFill(Color.WHITE);
                player2DeckUI[i].setStroke(Color.BLACK);
                player2DeckUI[i].setArcHeight(15);
                player2DeckUI[i].setArcWidth(15);
            }
            pane.getChildren().addAll(player2DeckUI);
        }
    }

    public void updateGameStateUI(Tuple tuple) {

        if (turnCounter>0) {
            pane.getChildren().removeAll(gameStateTextNumber);
            pane.getChildren().removeAll(symbolsGameDeck);
            pane.getChildren().removeAll(gameStateCardUI);
        }
        gameStateTextNumber = new Text[tuple.getOccurrence()];
        symbolsGameDeck = new ImageView[tuple.getOccurrence()];
        gameStateCardUI = new Card[tuple.getOccurrence()];

        for (int i = 0; i < gameStateCardUI.length; i++) {
            gameStateCardUI[i] = president.getPlayer1().getGameDeck().get(president.getPlayer1().getGameDeck().size()-(i+1));
        }

        if (gameStateCardUI.length > 0) {
            int i = 0;
            for (Card card : gameStateCardUI) {
                card.setTranslateX(400 - ((20-deckSize)*20) + (i*35));
                card.setTranslateY(300);
                card.setWidth(50);
                card.setHeight(100);
                card.setFill(Color.WHITE);
                card.setStroke(Color.BLACK);
                card.setArcHeight(15);
                card.setArcWidth(15);

                gameStateTextNumber[i] = new Text(String.valueOf(card.getNumber()));
                switch (card.getNumber()) {
                    case 11:
                        gameStateTextNumber[i].setText("J");
                        break;
                    case 12:
                        gameStateTextNumber[i].setText("Q");
                        break;
                    case 13:
                        gameStateTextNumber[i].setText("K");
                        break;
                    case 14:
                        gameStateTextNumber[i].setText("A");
                }
                gameStateTextNumber[i].setTranslateX(408 - ((20-deckSize)*20) + (i*35));
                gameStateTextNumber[i].setTranslateY(320);
                symbolsGameDeck[i] = setSymbol(card.getSymbol(), 408 - ((20-deckSize)*20) + (i*35), 340);
                i++;
            }
            pane.getChildren().addAll(gameStateCardUI);
            pane.getChildren().addAll(gameStateTextNumber);
            pane.getChildren().addAll(symbolsGameDeck);
        }
        else {
            Card card = new Card(0, 0);
            card.setTranslateX(400 - ((20-deckSize)*20));
            card.setTranslateY(300);
            card.setWidth(50);
            card.setHeight(100);
            card.setFill(Color.WHITE);
            card.setStroke(Color.BLACK);
            card.setArcHeight(15);
            card.setArcWidth(15);
            gameStateCardUI = new Card[]{card};
            pane.getChildren().addAll(gameStateCardUI);
        }
    }

    public void checkWin() {

        if (president.isDone(president.getPlayer1(), president.getPlayer2())) {
            System.out.println("President game is over");
            if (president.isVictorious(president.getPlayer1())) {
                System.out.println("Player 1 won");
                readyText.setText("Player 1 won");
                president.updateWinnerFile("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_president.txt", HomePage.username, true);
                createPopUpWindow(1);
            }
            else {
                System.out.println("Player 2 won");
                readyText.setText("Player 2 won");
                president.updateWinnerFile("/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/UM/Thesis Karim/Code/Main/res/players_win_rate_president.txt", HomePage.username, false);
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
                    "\n \t Player 1 won"
            );
        }
        else {
            text = new Text(
                    "\n \t Player 2 won"
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

    public void showActionUI(ArrayList<Node> fourBestNodes, int index) {

        clearStrokes();
        box1.setSelected(false);
        box2.setSelected(false);
        box3.setSelected(false);
        box4.setSelected(false);
        switch (index) {
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
        int count = 0;
        if (president.getPlayer1().toPlay()) {
            for (Card card : player1DeckUI) {
                if (count < fourBestNodes.get(index).getPlayer().getGameState().getOccurrence()) {
                    if (card.getNumber() == fourBestNodes.get(index).getPlayer().getGameState().getNumber()) {
                        card.setStroke(Color.RED);
                        card.setStrokeWidth(2);
                        card.setTranslateY(card.getTranslateY() - 10);
                        count++;
                    }
                }
            }
        }
    }

    private ArrayList<Card> sortPlayerDeckUI(ArrayList<Card> deck) {

        ArrayList<Card> out = new ArrayList<>();
        int cardNumber = 2;
        while (out.size() < deck.size()) {
            for (Card card : deck) {
                if (card.getNumber() == cardNumber) {
                    out.add(card);
                }
            }
            cardNumber++;
        }
        return out;
    }

    public void clearStrokes() {

        for (Card card : player1DeckUI) {
            if (card.getStroke() == Color.RED) {
                card.setStroke(Color.BLACK);
                card.setStrokeWidth(1);
                card.setTranslateY(card.getTranslateY() + 10);
            }
        }
        for (Card card : player2DeckUI) {
            if (card.getStroke() == Color.RED) {
                card.setStroke(Color.BLACK);
                card.setStrokeWidth(1);
                card.setTranslateY(card.getTranslateY() + 10);
            }
        }
    }

    private ImageView setSymbol(int symbol, int x, int y) {

        ImageView selectedImage = null;
        try {
            selectedImage = new ImageView();
            Image image = null;
            switch (symbol) {
                case 1:
                    image = new Image(new FileInputStream("Main/res/spade.png"));
                    break;
                case 2:
                    image = new Image(new FileInputStream("Main/res/clover.png"));
                    break;
                case 3:
                    image = new Image(new FileInputStream("Main/res/diamond.png"));
                    break;
                case 4:
                    image = new Image(new FileInputStream("Main/res/heart.png"));
            }
            selectedImage.setImage(image);
            selectedImage.setTranslateX(x);
            selectedImage.setTranslateY(y);
            selectedImage.setFitWidth(13);
            selectedImage.setFitHeight(15);
        } catch (Exception e) {
            System.out.println("file not found");
            System.exit(0);
        }
        return selectedImage;
    }

    public void setArrow(boolean isPlayer1ToPlay) {

        if (turnCounter>0) {
            pane.getChildren().remove(arrowImage);
        }
        try {
            Image image;
            if (isPlayer1ToPlay) {
                image = new Image(new FileInputStream("Main/res/arrow.png"));
                arrowImage.setTranslateX(20);
                arrowImage.setTranslateY(538);
            }
            else {
                image = new Image(new FileInputStream("Main/res/arrow.png"));
                arrowImage.setTranslateX(20);
                arrowImage.setTranslateY(133);
            }
            arrowImage.setImage(image);
            arrowImage.setFitWidth(35);
            arrowImage.setFitHeight(28);
            pane.getChildren().add(arrowImage);
        } catch (Exception e) {
            System.out.println("file not found");
            System.exit(0);
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

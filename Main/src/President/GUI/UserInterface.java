package President.GUI;

import President.Game.Card;
import President.Game.Player;
import President.Game.President;
import President.Game.Tuple;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class UserInterface extends Application {

    private President president;

    public static int turnCounter = 0;
    public static Text readyText;

    private Group pane = new Group();
    private Scene scene;
    private final int WIDTH = 1300;
    private final int HEIGHT = 700;

    private Card[] player1DeckUI;
    private Card[] player2DeckUI;
    private Card gameStateCardUI;

    private Text[] player1DeckNumbers;
    private Text[] player2DeckNumbers;
    private Text gameStateTextNumber;
    private Text gameStateTextOccurrence;
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


    @Override
    public void start(Stage primaryStage) {

        setBackground();

        president = new President();

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
        });

        this.box2 = new CheckBox();
        this.box2.setTranslateX(WIDTH - 230);
        this.box2.setTranslateY(235);
        this.box2.setOnAction(event -> {
            box1.setSelected(false);
            box3.setSelected(false);
            box4.setSelected(false);
        });

        this.box3 = new CheckBox();
        this.box3.setTranslateX(WIDTH - 230);
        this.box3.setTranslateY(335);
        this.box3.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box4.setSelected(false);
        });

        this.box4 = new CheckBox();
        this.box4.setTranslateX(WIDTH - 230);
        this.box4.setTranslateY(435);
        this.box4.setOnAction(event -> {
            box1.setSelected(false);
            box2.setSelected(false);
            box3.setSelected(false);
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
//            if (board.isSelectable) {
//                if (isMCTS.isSelected() || isABTS.isSelected()) {
//                    isABTS.setSelected(false);
//                    if (done) {
//                        readyText.setText("Press ENTER to start MCTS");
//                    }
//                } else {
//                    isMCTS.setSelected(true);
//                }
//            }
//            else {
//                isMCTS.setSelected(!isMCTS.isSelected());
//            }
        });

        this.isABTS = new CheckBox();
        this.isABTS.setTranslateX(350);
        this.isABTS.setTranslateY(40);
        this.isABTS.setOnAction(event -> {
//            if (board.isSelectable) {
//                if (isMCTS.isSelected() || isABTS.isSelected()) {
//                    isMCTS.setSelected(false);
//                    if (done) {
//                        readyText.setText("Press ENTER to start ABTS");
//                    }
//                } else {
//                    isABTS.setSelected(true);
//                }
//            }
//            else {
//                isABTS.setSelected(!isABTS.isSelected());
//            }
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

        controller();
    }

    public void controller() {

        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case ESCAPE:
                    System.exit(0);
                    break;
            }
        });
    }

    public void updateDeckUI(Player player) {

        if (player.getNumber()==1) {
            if (turnCounter>0) {
                pane.getChildren().removeAll(player1DeckUI);
                pane.getChildren().removeAll(player1DeckNumbers);
            }
            player1DeckUI = new Card[player.getDeck().size()];
            player1DeckNumbers = new Text[player.getDeck().size()];

            for (int i = 0; i < president.getPlayer1().getDeck().size(); i++) {
                player1DeckUI[i] = new Card(president.getPlayer1().getDeck().get(i).getNumber(), president.getPlayer1().getDeck().get(i).getSymbol());
                player1DeckUI[i].setTranslateX((i + 1) * 35);
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
                }
                if (president.getPlayer1().getDeck().get(i).getSymbol() == 3 || president.getPlayer1().getDeck().get(i).getSymbol() == 4) {
                    number.setFill(Color.RED);
                }
                number.setTranslateX(((i + 1) * 35) + 8);
                number.setTranslateY(520);
                player1DeckNumbers[i] = number;
            }
            pane.getChildren().addAll(player1DeckUI);
            pane.getChildren().addAll(player1DeckNumbers);

            for (int i = 0; i < president.getPlayer1().getDeck().size(); i++) {
                setSymbol(president.getPlayer1().getDeck().get(i).getSymbol(), ((i + 1) * 35) + 6, 530);
            }
        }
        else {
            if (turnCounter>1) {
                pane.getChildren().removeAll(player2DeckUI);
                pane.getChildren().removeAll(player2DeckNumbers);
            }
            player2DeckUI = new Card[player.getDeck().size()];
            player2DeckNumbers = new Text[player.getDeck().size()];

            for (int i = 0; i < president.getPlayer2().getDeck().size(); i++) {
                player2DeckUI[i] = new Card(president.getPlayer2().getDeck().get(i).getNumber(), president.getPlayer2().getDeck().get(i).getSymbol());
                player2DeckUI[i].setTranslateX((i+1)*35);
                player2DeckUI[i].setTranslateY(100);
                player2DeckUI[i].setWidth(50);
                player2DeckUI[i].setHeight(100);
                player2DeckUI[i].setFill(Color.WHITE);
                player2DeckUI[i].setStroke(Color.BLACK);
                player2DeckUI[i].setArcHeight(15);
                player2DeckUI[i].setArcWidth(15);
                Text number = new Text(String.valueOf(president.getPlayer2().getDeck().get(i).getNumber()));
                switch (president.getPlayer2().getDeck().get(i).getNumber()) {
                    case 11:
                        number.setText("J");
                        break;
                    case 12:
                        number.setText("Q");
                        break;
                    case 13:
                        number.setText("K");
                        break;
                }
                if (president.getPlayer2().getDeck().get(i).getSymbol() == 3 || president.getPlayer2().getDeck().get(i).getSymbol() == 4) {
                    number.setFill(Color.RED);
                }
                number.setTranslateX(((i+1)*35)+8);
                number.setTranslateY(120);
                player2DeckNumbers[i] = number;
            }
            pane.getChildren().addAll(player2DeckUI);
            pane.getChildren().addAll(player2DeckNumbers);

            for (int i = 0; i < president.getPlayer2().getDeck().size(); i++) {
                setSymbol(president.getPlayer2().getDeck().get(i).getSymbol(), ((i + 1) * 35) + 6, 130);
            }
        }
    }

    public void updateGameStateUI(Tuple tuple) {

        gameStateCardUI = new Card(tuple.getNumber(), tuple.getOccurrence());
        gameStateCardUI.setTranslateX(450);
        gameStateCardUI.setTranslateY(300);
        gameStateCardUI.setWidth(50);
        gameStateCardUI.setHeight(100);
        gameStateCardUI.setFill(Color.WHITE);
        gameStateCardUI.setStroke(Color.BLACK);
        gameStateCardUI.setArcHeight(15);
        gameStateCardUI.setArcWidth(15);
        pane.getChildren().add(gameStateCardUI);

        if (turnCounter>0) {
            pane.getChildren().remove(gameStateTextNumber);
            pane.getChildren().remove(gameStateTextOccurrence);
        }
        gameStateTextNumber = new Text(String.valueOf(tuple.getNumber()));
        switch (tuple.getNumber()) {
            case 11:
                gameStateTextNumber.setText("J");
                break;
            case 12:
                gameStateTextNumber.setText("Q");
                break;
            case 13:
                gameStateTextNumber.setText("K");
                break;
        }
        gameStateTextNumber.setTranslateX(458);
        gameStateTextNumber.setTranslateY(320);
        pane.getChildren().add(gameStateTextNumber);

        gameStateTextOccurrence = new Text(tuple.getOccurrence() + " x");
        gameStateTextOccurrence.setTranslateX(458);
        gameStateTextOccurrence.setTranslateY(340);
        pane.getChildren().add(gameStateTextOccurrence);
    }

    private void setSymbol(int symbol, int x, int y) {

        try {
            ImageView selectedImage = new ImageView();
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
            pane.getChildren().add(selectedImage);
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

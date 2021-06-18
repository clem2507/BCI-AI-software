package HomePage;

import Checkers.GUI.GameUI;
import President.GUI.UserInterface;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class HomePage extends Application {

    private int width = 500;
    private int height = 400;

    private Group pane = new Group();

    private ComboBox<Integer> boardSize;
    private ComboBox<Integer> cardSize;

    private RadioButton partialBoard;
    private RadioButton completeBoard;

    private Text partialBoardText;
    private Text completeBoardText;

    private boolean visited = false;
    private boolean visited1 = false;
    private boolean visited2 = false;

    public static String username = "";

    private ComboBox<String> namesBox;

    @Override
    public void start(Stage primaryStage) {

        setBackground();

        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setTranslateX(220);
        startButton.setTranslateY(305);
        pane.getChildren().add(startButton);

        updateUsernamesBox("Main/res/players_level_checkers.txt");

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username...");
        usernameInput.setPrefWidth(101);
        usernameInput.setTranslateX(120);
        usernameInput.setTranslateY(160);
        pane.getChildren().add(usernameInput);

        ComboBox<String> choiceBox = new ComboBox<>();
        choiceBox.getItems().add("Checkers");
        choiceBox.getItems().add("President");
        choiceBox.setTranslateX(120);
        choiceBox.setTranslateY(100);
        choiceBox.setValue("Checkers");
        pane.getChildren().add(choiceBox);

        boardSize = new ComboBox<>();
        boardSize.setPromptText("Board size");
        boardSize.getItems().add(6);
        boardSize.getItems().add(8);
        boardSize.getItems().add(10);
        boardSize.setTranslateX(260);
        boardSize.setTranslateY(100);
        pane.getChildren().addAll(boardSize);

        cardSize = new ComboBox<>();
        cardSize.setPromptText("Deck size");
        cardSize.getItems().add(10);
        cardSize.getItems().add(12);
        cardSize.getItems().add(14);
        cardSize.getItems().add(16);
        cardSize.getItems().add(18);
        cardSize.getItems().add(20);
        cardSize.setTranslateX(260);
        cardSize.setTranslateY(100);

        partialBoard = new RadioButton();
        partialBoard.setTranslateX(190);
        partialBoard.setTranslateY(220);
        partialBoard.setSelected(true);
        pane.getChildren().add(partialBoard);

        completeBoard = new RadioButton();
        completeBoard.setTranslateX(190);
        completeBoard.setTranslateY(260);
        pane.getChildren().add(completeBoard);

        partialBoardText = new Text("Partial Board");
        partialBoardText.setTranslateX(225);
        partialBoardText.setTranslateY(233);
        pane.getChildren().add(partialBoardText);

        completeBoardText = new Text("Complete Board");
        completeBoardText.setTranslateX(225);
        completeBoardText.setTranslateY(273);
        pane.getChildren().add(completeBoardText);

        Scene scene = new Scene(pane ,width, height);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Home Page");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        choiceBox.setOnAction(event -> {
            doAction(choiceBox.getValue());
        });

        partialBoard.setOnAction(event -> {
            if (!partialBoard.isSelected()) {
                partialBoard.setSelected(true);
            }
            completeBoard.setSelected(false);
        });

        completeBoard.setOnAction(event -> {
            if (!completeBoard.isSelected()) {
                completeBoard.setSelected(true);
            }
            partialBoard.setSelected(false);
        });

        startButton.setOnAction(event -> {
            boolean check = false;
            if (usernameInput.getText().length() > 0) {
                check = true;
                username = usernameInput.getText();
                if (!namesBox.getItems().contains(usernameInput.getText())) {
                    String path = "";
                    String path_win = "";
                    switch (choiceBox.getValue()) {
                        case "Checkers":
                            path = "Main/res/players_level_checkers.txt";
                            path_win = "Main/res/players_win_rate_checkers.txt";
                            break;
                        case "President":
                            path = "Main/res/players_level_president.txt";
                            path_win = "Main/res/players_win_rate_president.txt";
                            break;
                    }
                    File file = new File(path);
                    File win_file = new File(path_win);
                    try (FileWriter fw = new FileWriter(file, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                         out.println(usernameInput.getText() + ", ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try (FileWriter fw = new FileWriter(win_file, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                         out.println(usernameInput.getText() + ", 0.0, 0.0, 0.0, ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                if (!namesBox.getValue().equals("Username")) {
                    username = namesBox.getValue();
                    check = true;
                }
                else {
                    showWarningText();
                }
            }
            if (check) {
                if (choiceBox.getValue().equals("Checkers")) {
                    int numMarblesOnBoard;
                    if (boardSize.getValue() == null) {
                        numMarblesOnBoard = 6;
                    } else {
                        numMarblesOnBoard = boardSize.getValue();
                    }
                    GameUI gameUI = new GameUI(numMarblesOnBoard, completeBoard.isSelected());
                    gameUI.start(primaryStage);
                } else if (choiceBox.getValue().equals("President")) {
                    int deckSize;
                    if (cardSize.getValue() == null) {
                        deckSize = 14;
                    } else {
                        deckSize = cardSize.getValue();
                    }
                    UserInterface ui = new UserInterface(deckSize);
                    ui.start(primaryStage);
                }
            }
        });

        scene.setOnKeyPressed(t -> {
            KeyCode key = t.getCode();
            switch (key) {
                case ESCAPE:
                    System.exit(0);
                    break;
                case ENTER:
                    startButton.fire();
                    break;
            }
        });
    }

    private void doAction(String item) {

        switch (item) {
            case "Checkers":
                updateUsernamesBox("Main/res/players_level_checkers.txt");
                if (visited) {
                    pane.getChildren().add(boardSize);
                    pane.getChildren().add(partialBoard);
                    pane.getChildren().add(partialBoardText);
                    pane.getChildren().add(completeBoard);
                    pane.getChildren().add(completeBoardText);
                    visited1 = true;
                    if (visited2) {
                        pane.getChildren().remove(cardSize);
                    }
                }
                break;
            case "President":
                updateUsernamesBox("Main/res/players_level_president.txt");
                if (!visited || visited1) {
                    pane.getChildren().remove(boardSize);
                    pane.getChildren().remove(partialBoard);
                    pane.getChildren().remove(partialBoardText);
                    pane.getChildren().remove(completeBoard);
                    pane.getChildren().remove(completeBoardText);
                    visited1 = false;
                }
                pane.getChildren().add(cardSize);
                visited2 = true;
                visited = true;
                break;
        }
    }

    public void showWarningText() {

        Text warningText = new Text("Please enter / select a username");
        warningText.setTranslateX(155);
        warningText.setTranslateY(80);
        warningText.setFill(Color.RED);
        if (!pane.getChildren().contains(warningText)) {
            pane.getChildren().add(warningText);
        }
    }

    private void updateUsernamesBox(String path) {

        if (namesBox != null) {
            pane.getChildren().remove(namesBox);
        }
        ArrayList<String> names = new ArrayList<>();
        namesBox = new ComboBox<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while (line!=null) {
                names.add(line.split(", ")[0]);
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }
        for (String name : names) {
            namesBox.getItems().add(name);
        }
        namesBox.setTranslateX(260);
        namesBox.setTranslateY(160);
        namesBox.setValue("Username");
        pane.getChildren().add(namesBox);
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

package Checkers.GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePage extends Application {

    private int width = 700;
    private int height = 500;

    private Group pane = new Group();

    @Override
    public void start(Stage primaryStage) {

        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setTranslateX(300);
        startButton.setTranslateY(250);
        startButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 13));
        startButton.setOnAction(event -> {
            GameUI gameUI = new GameUI();
            gameUI.start(primaryStage);
        });
        pane.getChildren().add(startButton);

        Scene scene = new Scene(pane ,width, height);
        primaryStage.setResizable(false);
        primaryStage.setTitle("checkers");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}

package President.GUI;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class UserInterface extends Application {

    private Group pane = new Group();
    private final int width = 600;
    private final int height = 800;

    @Override
    public void start(Stage primaryStage) {

        setBackground();

        Scene scene = new Scene(pane ,width, height);
        primaryStage.setResizable(false);
        primaryStage.setTitle("President Game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
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

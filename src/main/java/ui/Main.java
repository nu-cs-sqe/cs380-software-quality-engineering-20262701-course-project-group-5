package ui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.view.StartView;

public class Main extends Application {
    private static final int containerWidth = 640;
    private static final int containerHeight = 480;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StartView(), containerWidth, containerHeight);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
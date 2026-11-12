package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.view.StartView;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Creates primary UI application that controls Player's view of the game.
 */
public class Main extends Application {
  private static final int containerWidth = 800;
  private static final int containerHeight = 600;
  private static final String BUNDLE_NAME = "message";
  private static final Locale ENGLISH = Locale.ENGLISH;

  /**
   * Creates and formats the primary container Players see with
   * a set of views.
   * {@param stage Primary container different views are populated in}
   */
  @Override
  public void start(Stage stage) {
    StartView startView = new StartView();
    ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, ENGLISH);
    startView.updateDisplay(resourceBundle);
    Scene scene = new Scene(startView, containerWidth, containerHeight);
    stage.setScene(scene);
    stage.setFullScreen(true);
    stage.show();
  }

  /**
   * Launches populated application.
   * {@param args Unused}
   */
  public static void main(String[] args) {
    launch();
  }
}
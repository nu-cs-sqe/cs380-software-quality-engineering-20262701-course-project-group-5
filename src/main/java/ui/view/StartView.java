package ui.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Builds Panes for Catan starting page and formats them in a Container.
 */
public class StartView extends StackPane {
  private Text titleCredit;
  private Text titleCatan;
  private Text tagLine;

  private Button startGameButton;
  private Button settingsButton;

  private static final int containerWidth = 500;
  private static final int buttonBoxWidth = 200;
  private static final int containerSpacing = 300;
  private static final int titleBoxSpacing = -5;
  private static final int buttonBoxSpacing = 20;

  /**
   * Constructs a StartView instance out of Panes
   * created with helper methods.
   */
  public StartView() {
    this.getStyleClass().add("root-pane");

    VBox container = new VBox(containerSpacing);
    container.setAlignment(Pos.CENTER);
    container.setMaxWidth(containerWidth);

    VBox titleBox = createTitle();
    VBox buttonBox = createButtons();

    container.getChildren().addAll(
            titleBox, buttonBox
    );
    this.getChildren().add(container);

    this.getStylesheets().add(
            getClass().getResource("/styles/startpage-style.css").toExternalForm()
    );
  }

  /**
   * Finds average of three integers.
   * {@return VBox with Catan credits, title,
   * and tagline styled with CSS}
   */
  private VBox createTitle() {
    VBox titleBox = new VBox(titleBoxSpacing);
    titleBox.setAlignment(Pos.TOP_CENTER);

    titleCredit = new Text("KLAUS TEUBER'S");
    titleCredit.getStyleClass().add("title-credit");

    titleCatan = new Text("CATAN");
    titleCatan.getStyleClass().add("title-catan");

    tagLine = new Text("SETTLE BUILD TRADE");
    tagLine.getStyleClass().add("title-credit");

    titleBox.getChildren().addAll(titleCredit, titleCatan, tagLine);

    return titleBox;
  }

  /**
   * Finds average of three integers.
   * {@return VBox with Start and Settings
   * Buttons styled with CSS}
   */
  private VBox createButtons() {
    VBox buttonBox = new VBox(buttonBoxSpacing);
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setMaxWidth(buttonBoxWidth);

    startGameButton = new Button("START");
    startGameButton.getStyleClass().add("btn");
    startGameButton.setMaxWidth(Double.MAX_VALUE);

    settingsButton = new Button("SETTINGS");
    settingsButton.getStyleClass().add("btn");
    settingsButton.setMaxWidth(Double.MAX_VALUE);

    buttonBox.getChildren().addAll(
            startGameButton, settingsButton
    );
    return buttonBox;
  }
}

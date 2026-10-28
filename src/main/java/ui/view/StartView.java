package ui.view;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

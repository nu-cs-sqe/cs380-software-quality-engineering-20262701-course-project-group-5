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

    private Text subTitleCardGame;
    private Text subLine1;
    private Text subLine2;

    private Button startGameButton;
    private Button settingsButton;

    private static final int containerWidth = 500;
    private static final int buttonBoxWidth = 320;
    private static final int containerSpacing = 25;
    private static final int titleBoxSpacing = -10;
    private static final int descriptionBoxSpacing = 10;
    private static final int buttonBoxSpacing = 20;

    public StartView() {
        this.getStyleClass().add("root-pane");

        VBox container = new VBox(containerSpacing);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(containerWidth);

        VBox titleBox = createTitle();
        VBox descriptionBox = createDescription();
        VBox buttonBox = createButtons();

        container.getChildren().addAll(
                titleBox, descriptionBox, buttonBox
        );
        this.getChildren().add(container);

        this.getStylesheets().add(
                getClass().getResource("/styles/startpage-style.css").toExternalForm()
        );
    }

    private VBox createTitle() {
        VBox titleBox = new VBox(titleBoxSpacing);
        titleBox.setAlignment(Pos.CENTER);

        titleCredit = new Text("KLAUS TEUBER's");
        titleCredit.getStyleClass().add("title-credit");

        titleCatan = new Text("CATAN");
        titleCatan.getStyleClass().add("title-catan");

        tagLine = new Text("SETTLE BUILD TRADE");
        tagLine.getStyleClass().add("title-credit");

        titleBox.getChildren().addAll(titleCredit, titleCatan, tagLine);

        return titleBox;
    }

    private VBox createDescription() {
        VBox descriptionBox = new VBox(descriptionBoxSpacing);
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.getStyleClass().add("description-box");

        subTitleCardGame = new Text();
        subTitleCardGame.getStyleClass().add("subtitle-card-game");

        descriptionBox.setSpacing(descriptionBoxSpacing);

        subLine1 = new Text();
        subLine1.getStyleClass().add("sub-line-detail");

        subLine2 = new Text();
        subLine2.getStyleClass().add("sub-line-detail");

        descriptionBox.getChildren().addAll(subTitleCardGame, subLine1, subLine2);

        return descriptionBox;
    }

    private VBox createButtons() {
        VBox buttonBox = new VBox(buttonBoxSpacing);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMaxWidth(buttonBoxWidth);

        startGameButton = new Button("START");
        startGameButton.getStyleClass().add("btn-main");
        startGameButton.setMaxWidth(Double.MAX_VALUE);

        settingsButton = new Button("SETTINGS");
        settingsButton.getStyleClass().add("btn-sec");
        settingsButton.setMaxWidth(Double.MAX_VALUE);

        buttonBox.getChildren().addAll(
                startGameButton, settingsButton
        );

        return buttonBox;
    }
}

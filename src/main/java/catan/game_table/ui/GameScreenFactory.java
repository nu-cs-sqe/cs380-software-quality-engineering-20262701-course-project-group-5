package catan.game_table.ui;

import catan.game_table.domain.match_model.GameSession;
import catan.game_table.ui.views.GameTableView;
import javafx.scene.Scene;

public class GameScreenFactory {
    public Scene create(GameSession gameSession) {
        GameTableView gameTableView = new GameTableView();
        return new Scene(gameTableView);
    }
}

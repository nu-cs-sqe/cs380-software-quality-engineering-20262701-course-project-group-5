package catan.game_table.ui.controllers;

import catan.game_table.domain.match_model.GameSession;
import catan.game_table.domain.match_model.GameSetup;
import catan.game_table.domain.models.LobbyModel;
import catan.game_table.domain.player.PlayerData;
import catan.game_table.ui.GameScreenFactory;
import catan.game_table.ui.ViewRouter;
import catan.game_table.ui.views.LobbyView;
import javafx.scene.Scene;

import java.util.List;

public class LobbyController {
  public LobbyView lobbyView;
  private LobbyModel lobbyModel;
  private ViewRouter viewRouter;
  private GameSetup gameSetup;
  private GameScreenFactory gameScreenFactory;

  public LobbyController(LobbyView lobbyView, LobbyModel lobbyModel, ViewRouter viewRouter, GameSetup gameSetup, GameScreenFactory gameScreenFactory) {
    this.lobbyView = lobbyView;
    this.lobbyModel = lobbyModel;
    this.viewRouter = viewRouter;
    this.gameSetup = gameSetup;
    this.gameScreenFactory = gameScreenFactory;
  }

  void onStart(List<PlayerData> players) {
    try {
      GameSession session = gameSetup.createGame(players);
      Scene gameScene = gameScreenFactory.create(session);
      viewRouter.show(gameScene);
    } catch (IllegalArgumentException error) {
      lobbyView.showError(error.getMessage());
    }
  }
}

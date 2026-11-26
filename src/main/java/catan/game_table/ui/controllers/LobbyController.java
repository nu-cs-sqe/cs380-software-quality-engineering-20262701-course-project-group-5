package catan.game_table.ui.controllers;

import catan.game_table.domain.Main;
import catan.game_table.domain.match_model.GameSession;
import catan.game_table.domain.match_model.GameSetup;
import catan.game_table.domain.models.LobbyModel;
import catan.game_table.domain.player.PlayerData;
import catan.game_table.ui.ViewRouter;
import catan.game_table.ui.views.LobbyView;
import java.util.List;
import java.util.function.Consumer;

public class LobbyController {
  public LobbyView lobbyView;
  public LobbyModel lobbyModel;
  public ViewRouter viewRouter;
  private GameSetup gameSetup;
  public LobbyController(LobbyView lobbyView, LobbyModel lobbyModel, ViewRouter viewRouter) {
    this.lobbyView = lobbyView;
    this.lobbyModel = lobbyModel;
    this.viewRouter = viewRouter;
    this.gameSetup = new GameSetup();
  }

  void onStart(List<PlayerData> players, Consumer<GameSession> mainCallback) {
    try {
      GameSession gameSession = gameSetup.createGame(players);
      mainCallback.accept(gameSession);
    }
    catch (IllegalArgumentException error){
      lobbyView.showError("Not enough players.");
    }
  }
}

package catan.game_table.domain.match_model;

import catan.game_table.domain.player.PlayerData;
import java.util.List;

public class GameSetup {
  public GameSession createGame(List<PlayerData> player) {
    return new GameSession();
  }
}

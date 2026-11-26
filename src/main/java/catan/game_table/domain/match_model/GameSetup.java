package catan.game_table.domain.match_model;

import catan.game_table.domain.player.PlayerData;

import java.util.List;


public class GameSetup {
  private static final int minPlayerNum = 3;

  public GameSetup() {
  }

  public GameSession createGame(List<PlayerData> player) {
    int playerNum = player.size();
    if(playerNum < minPlayerNum) {
      throw new IllegalArgumentException("Not enough players");
    }
    else {
      return new GameSession();
    }

  }
}

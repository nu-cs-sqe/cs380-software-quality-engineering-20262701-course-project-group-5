package catan.game_table.domain.models;

import catan.game_table.domain.player.PlayerData;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LobbyModel {
  public LobbyModel() {}

  public void capturePlayerDataFromInputs(List<Pair<String, String>> rawInput) {}

  public List<PlayerData> getPlayerData() {
      return List.of();
  }

  public void setPlayerCount(int playerCount) {}

  public int getPlayerCount() {
    return 0;
  }
}

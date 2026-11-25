package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.award.Award;
import java.util.ArrayList;
import java.util.List;

public class GamePlayer {

  private final PlayerData playerData;
  private final PlayerInventory inventory;
  private final ScoreTracker victoryPointTracker;
  private final ScoreTracker armySizeTracker;
  private final ScoreTracker longestRoadTracker;
  private final List<Award> awards;

  public GamePlayer(PlayerData playerData, PlayerInventory inventory) {
    this.playerData = playerData;
    this.inventory = inventory;
    this.awards = new ArrayList<>();
    this.victoryPointTracker = new ScoreTracker(0);
    this.armySizeTracker = new ScoreTracker(0);
    this.longestRoadTracker = new ScoreTracker(0);
  }

  public int getScore() { return this.victoryPointTracker.getScore(); }

  public ScoreTracker getVictoryPointTracker() { return this.victoryPointTracker; }

  public ScoreTracker getLongestRoadTracker() { return this.longestRoadTracker; }

  public ScoreTracker getArmySizeTracker() { return this.armySizeTracker; }

  public PlayerInventory getInventory() { return this.inventory; }

  public Color getColor() { return this.playerData.color(); }

  public String getName() { return this.playerData.name(); }

  public List<Award> getAwards() { return List.copyOf(this.awards); }

  public void addAward(Award award) { this.awards.add(award); }
}

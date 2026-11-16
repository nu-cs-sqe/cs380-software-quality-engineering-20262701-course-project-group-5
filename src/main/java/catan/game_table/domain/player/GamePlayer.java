package catan.game_table.domain.player;

import catan.game_table.domain.Playable;

/**
 * GamePlayer is the model for each individual player.
 * which the PlayerController part of MVC will delegate its logic to
 */
public class GamePlayer {

  /**
   * Plays a playable item in the player's hand if able.
   *
   * @param item: playable item
   */
  public void play(Playable item) {
    item.onPlayEffect();
  }
}

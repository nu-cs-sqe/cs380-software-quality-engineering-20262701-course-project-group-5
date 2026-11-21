package catan.game_table.domain;

/**
 * Playable item with an effect.
 */
public interface Playable {
  /**
   * Trigger development card effect.
   */
  public void onPlayEffect();
}

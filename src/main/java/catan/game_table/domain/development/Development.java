package catan.game_table.domain.development;

import catan.game_table.domain.Playable;
import catan.game_table.domain.bank.Buyable;
import catan.game_table.domain.resource.ResourceStack;
import java.util.ArrayList;
import java.util.List;

/**
 * Development card abstract class.
 */
public abstract class Development implements Playable, Buyable {

  /**
   * Trigger development card effect.
   */
  public void onPlayEffect() {

  }

  /**
   * Gets the cost of the development card.
   */
  public List<ResourceStack> getCost() {
    return new ArrayList<>();
  }
}

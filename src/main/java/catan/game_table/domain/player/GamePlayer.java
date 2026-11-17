package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.Playable;
import catan.game_table.domain.bank.Buyable;
import catan.game_table.domain.bank.PurchaseHelper;
import catan.game_table.domain.board.PlacementHelper;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.trade.TradeHelper;
import catan.game_table.domain.trade.TradeOffer;

/**
 * GamePlayer is the model for each individual player.
 * which the PlayerController part of MVC will delegate its logic to
 */
public class GamePlayer {

  private PurchaseHelper purchaseHelper;

  public GamePlayer(Color color, PlacementHelper placementHelper, TradeHelper tradeHelper, PurchaseHelper purchaseHelper) {
    this.purchaseHelper = purchaseHelper;
  }

  /**
   * Plays a playable item in the player's hand if able.
   *
   * @param item: playable item
   */
  public void play(Playable item) {
    item.onPlayEffect();
  }

  public void buy(Buyable item) throws NotEnoughInventoryException {
    boolean res = this.purchaseHelper.buy(item);
    if (!res) {
      throw new NotEnoughInventoryException();
    }
  }

  public void proposePlayerTrade(Color otherPlayer, TradeOffer trade) {
    throw new InvalidTradeException("Cannot offer to same player as proposed");
  }
}

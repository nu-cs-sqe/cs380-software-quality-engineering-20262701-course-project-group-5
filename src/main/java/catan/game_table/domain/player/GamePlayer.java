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

  private final PurchaseHelper purchaseHelper;
  private final TradeHelper tradeHelper;
  private final Color color;

  public GamePlayer(Color color, PlacementHelper placementHelper, TradeHelper tradeHelper, PurchaseHelper purchaseHelper) {
    this.purchaseHelper = purchaseHelper;
    this.color = color;
    this.tradeHelper = tradeHelper;
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
    if (otherPlayer == Color.None) {
      throw new InvalidTradeException("Trade must go to someone");
    } else if (this.color == otherPlayer) {
      throw new InvalidTradeException("Cannot offer to same player as proposed");
    }

    this.tradeHelper.addTradeOffer(trade);

  }
}

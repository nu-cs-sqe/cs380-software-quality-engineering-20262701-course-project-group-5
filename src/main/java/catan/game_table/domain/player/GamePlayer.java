package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.Playable;
import catan.game_table.domain.bank.Buyable;
import catan.game_table.domain.bank.PurchaseHelper;
import catan.game_table.domain.board.Location;
import catan.game_table.domain.board.PlacementHelper;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.*;
import catan.game_table.domain.resource.ResourceStack;
import catan.game_table.domain.trade.TradeHelper;
import catan.game_table.domain.trade.TradeOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * GamePlayer is the model for each individual player.
 * which the PlayerController part of MVC will delegate its logic to
 */
public class GamePlayer {

  private final PurchaseHelper purchaseHelper;
  private final TradeHelper tradeHelper;
  private final PlacementHelper placementHelper;
  private final Color color;

  public GamePlayer(Color color, PlacementHelper placementHelper, TradeHelper tradeHelper, PurchaseHelper purchaseHelper) {
    this.purchaseHelper = purchaseHelper;
    this.color = color;
    this.tradeHelper = tradeHelper;
    this.placementHelper = placementHelper;
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

  public void acceptPlayerTrade(Color otherPlayer, TradeOffer trade) {
    if (otherPlayer == Color.None) {
      throw new InvalidTradeException("Trade must go to someone");
    } else if (this.color == otherPlayer) {
      throw new InvalidTradeException("Cannot offer to same player as proposed");
    }

    // TODO: this should probably take in the players right?
    this.tradeHelper.conductTrade(trade);
  }

  public List<Location> getValidPlacements(Piece piece) {
    List<Location> res = new ArrayList<>();

    Class<? extends Piece> piece_class = piece.getClass();

    if (piece_class.equals(Harbor.class)) {
      throw new IllegalArgumentException("Cannot move a harbor");
    } else if (piece_class.equals(City.class) || piece_class.equals(Settlement.class)) {
      // stupid ass java does not let me return direcctly
      // because getvalidbuildingspots returns a list of vertices
      // and it is apparently not smart enough to know that vertex inherits from location
      res.addAll(this.placementHelper.getValidBuildingSpots(this.color));
    } else if (piece_class.equals(Robber.class)) {
      res.addAll(this.placementHelper.getValidRobberSpots());
    } else if (piece_class.equals(Road.class)) {
      res.addAll(this.placementHelper.getValidRoadSpots(this.color));
    } else {
      throw new RuntimeException("Invalid piece type");
    }

    return res;
  }

  public void tradeWithBank(ResourceStack give, ResourceStack recieve) {

  }
}

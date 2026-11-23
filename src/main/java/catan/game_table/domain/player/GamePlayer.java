package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.Dice;
import catan.game_table.domain.Playable;
import catan.game_table.domain.bank.Buyable;
import catan.game_table.domain.bank.PurchaseHelper;
import catan.game_table.domain.board.*;
import catan.game_table.domain.exception.InvalidPlacementException;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.*;
import catan.game_table.domain.resource.ResourceStack;
import catan.game_table.domain.trade.BankTradeHelper;
import catan.game_table.domain.trade.PlayerTradeHelper;
import catan.game_table.domain.trade.TradeOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * GamePlayer is the model for each individual player.
 * which the PlayerController part of MVC will delegate its logic to
 */
public class GamePlayer {

  private final PurchaseHelper purchaseHelper;
  private final PlayerTradeHelper playerTradeHelper;
  private final PlacementHelper placementHelper;
  private final BankTradeHelper bankTradeHelper;
  private final Color color;

  public GamePlayer(Color color, PlacementHelper placementHelper, PlayerTradeHelper playerTradeHelper, PurchaseHelper purchaseHelper, BankTradeHelper bankTradeHelper) {
    this.purchaseHelper = purchaseHelper;
    this.color = color;
    this.playerTradeHelper = playerTradeHelper;
    this.placementHelper = placementHelper;
    this.bankTradeHelper = bankTradeHelper;
  }


  public void rollDice(Dice dice) {
    dice.roll();
  }


  public void play(Playable item) {
    item.onPlayEffect();
  }

  public void buy(Buyable item) throws NotEnoughInventoryException {
    boolean res = this.purchaseHelper.buyDevelopment(item);
    if (!res) {
      throw new NotEnoughInventoryException();
    }
  }

  public void proposePlayerTrade(Color otherPlayer, TradeOffer trade) {
    if (otherPlayer == Color.NONE) {
      throw new InvalidTradeException("Trade must go to someone");
    } else if (this.color == otherPlayer) {
      throw new InvalidTradeException("Cannot offer to same player as proposed");
    }

    this.playerTradeHelper.addTradeOffer(trade);

  }

  public void acceptPlayerTrade(Color otherPlayer, TradeOffer trade) {
    if (otherPlayer == Color.NONE) {
      throw new InvalidTradeException("Trade must go to someone");
    } else if (this.color == otherPlayer) {
      throw new InvalidTradeException("Cannot offer to same player as proposed");
    }

    this.playerTradeHelper.conductTrade(trade);
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

  public void build(Piece piece, Location location) {

    Class<? extends Location> location_type = location.getClass();
    Class<? extends Piece> piece_type = piece.getClass();

    boolean is_building = piece_type.equals(City.class) || piece_type.equals(Settlement.class);

    if (piece_type.equals(Robber.class) || piece_type.equals(Harbor.class)) {
      throw new InvalidPlacementException("Cannot build either robber or harbor");
    }

    //? should this logic be passed into the purchaseHelper itself?
    if (is_building && !location_type.equals(Vertex.class)) {
      throw new InvalidPlacementException("Settlements/Cities can only be build on points");
    }

    if (piece_type.equals(Road.class) && !location_type.equals(Edge.class)) {
      throw new InvalidPlacementException("Roads can only be build on edges");
    }

    this.purchaseHelper.build(this.color, piece, location);
  }

  public void tradeWithBank(ResourceStack give, ResourceStack receive) {
    this.bankTradeHelper.tradeWithBank(this.color, give, receive);
  }

}

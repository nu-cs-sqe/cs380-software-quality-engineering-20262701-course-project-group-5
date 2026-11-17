package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.bank.Buyable;
import catan.game_table.domain.bank.PurchaseHelper;
import catan.game_table.domain.board.PlacementHelper;
import catan.game_table.domain.development.*;
import catan.game_table.domain.Playable;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;
import catan.game_table.domain.trade.TradeHelper;
import catan.game_table.domain.trade.TradeOffer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayerTests {

  @Test
  public void play_item_knight() {
    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Playable knight = EasyMock.createMock(Knight.class);

    // this is how you expect a void method
    knight.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(knight);

    player.play(knight);

    EasyMock.verify(knight);
  }

  @Test
  public void play_item_monopoly() {
    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Playable monopoly = EasyMock.createMock(Monopoly.class);

    // this is how you expect a void method
    monopoly.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(monopoly);

    player.play(monopoly);

    EasyMock.verify(monopoly);
  }

  @Test
  public void play_item_road_builder() {
    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Playable roadbuilder = EasyMock.createMock(RoadBuilder.class);

    // this is how you expect a void method
    roadbuilder.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(roadbuilder);

    player.play(roadbuilder);

    EasyMock.verify(roadbuilder);
  }

  @Test
  public void play_item_vp() {
    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Playable vp = EasyMock.createMock(VictoryPoint.class);

    // this is how you expect a void method
    vp.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(vp);

    player.play(vp);

    EasyMock.verify(vp);
  }

  @Test
  public void play_item_yop() {
    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Playable yop = EasyMock.createMock(YearOfPlenty.class);

    // this is how you expect a void method
    yop.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(yop);

    player.play(yop);

    EasyMock.verify(yop);
  }

  @Test
  public void buy_dev_success() {

    PurchaseHelper purchaseHelper = EasyMock.createMock(PurchaseHelper.class);
    Buyable dev = EasyMock.createMock(Knight.class);

    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), purchaseHelper);

    EasyMock.expect(purchaseHelper.buy(dev)).andReturn(true);
    EasyMock.replay(purchaseHelper, dev);

    try {
      player.buy(dev);
    } catch (NotEnoughInventoryException e) {
      throw new RuntimeException("Unexpected exception in test");
    }

    
    EasyMock.verify(purchaseHelper, dev);
  }

  @Test
  public void buy_dev_failure() {

    PurchaseHelper purchaseHelper = EasyMock.createMock(PurchaseHelper.class);
    Buyable dev = EasyMock.createMock(Knight.class);

    GamePlayer player = new GamePlayer(Color.None, new PlacementHelper(), new TradeHelper(), purchaseHelper);

    EasyMock.expect(purchaseHelper.buy(dev)).andReturn(false);
    EasyMock.replay(purchaseHelper, dev);


    Exception expected = new NotEnoughInventoryException();
    Exception actual = null;
    try {
      player.buy(dev);
    } catch (Exception e) {
      actual = e;
    }
    assertNotNull(actual);
    assertEquals(expected.getClass(), actual.getClass());
  }

  @Test
  public void propose_player_trade_same_player_unsuccessful() {
    GamePlayer player = new GamePlayer(Color.Blue, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Exception expected = new InvalidTradeException("Cannot offer to same player as proposed");
    Exception actual = null;
    try {
      ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
      ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
      List<ResourceStack> offered = new ArrayList<>();
      List<ResourceStack> requested = new ArrayList<>();
      player.proposePlayerTrade(Color.Blue, new TradeOffer(inv_a, inv_b, requested, offered));
    } catch (Exception e) {
      actual = e;
    }
    assertNotNull(actual);
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.getMessage(), actual.getMessage());


  }

  @Test
  public void propose_player_trade_to_no_one_unsuccessful() {
    GamePlayer player = new GamePlayer(Color.Blue, new PlacementHelper(), new TradeHelper(), new PurchaseHelper());

    Exception expected = new InvalidTradeException("Trade must go to someone");
    Exception actual = null;
    try {
      ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
      ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
      List<ResourceStack> offered = new ArrayList<>();
      List<ResourceStack> requested = new ArrayList<>();
      player.proposePlayerTrade(Color.None, new TradeOffer(inv_a, inv_b, requested, offered));
    } catch (Exception e) {
      actual = e;
    }
    assertNotNull(actual);
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void propose_player_trade_successful() {
    TradeHelper trader = EasyMock.createMock(TradeHelper.class);
    GamePlayer player = new GamePlayer(Color.Blue, new PlacementHelper(), trader, new PurchaseHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = new ArrayList<>();
    List<ResourceStack> requested = new ArrayList<>();
    TradeOffer offer = new TradeOffer(inv_a, inv_b, requested, offered);

    trader.addTradeOffer(offer);
    EasyMock.expectLastCall();

    EasyMock.replay(trader);


    try {
      player.proposePlayerTrade(Color.Red, offer);
    } catch (Exception e) {
      throw new RuntimeException("Unexpected call failure");
    }

    // I suppose the test is for a call to add the trade offer to the ledger
    EasyMock.verify(trader);


  }


}

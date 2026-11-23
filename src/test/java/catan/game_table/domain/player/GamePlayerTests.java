package catan.game_table.domain.player;

import catan.game_table.domain.*;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.bank.Buyable;
import catan.game_table.domain.bank.PurchaseHelper;
import catan.game_table.domain.board.*;
import catan.game_table.domain.development.*;
import catan.game_table.domain.exception.InvalidPlacementException;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.*;
import catan.game_table.domain.resource.*;
import catan.game_table.domain.trade.BankTradeHelper;
import catan.game_table.domain.trade.PlayerTradeHelper;
import catan.game_table.domain.trade.TradeOffer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayerTests {

  @Test
  public void Roll_Dice_Doesnt_Crash() {
    Dice dice = EasyMock.createMock(Dice.class);
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    EasyMock.expect(dice.roll()).andReturn(4);
    EasyMock.replay(dice);

    assertDoesNotThrow(() -> player.rollDice(dice));

    EasyMock.verify(dice);
  }

  @Test
  public void Play_Item_Knight() {
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Playable knight = EasyMock.createMock(Knight.class);

    // this is how you expect a void method
    knight.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(knight);

    player.play(knight);

    EasyMock.verify(knight);
  }

  @Test
  public void Play_Item_Monopoly() {
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Playable monopoly = EasyMock.createMock(Monopoly.class);

    monopoly.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(monopoly);

    player.play(monopoly);

    EasyMock.verify(monopoly);
  }

  @Test
  public void Play_Item_RoadBuilder() {
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Playable roadbuilder = EasyMock.createMock(RoadBuilder.class);

    roadbuilder.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(roadbuilder);

    player.play(roadbuilder);

    EasyMock.verify(roadbuilder);
  }

  @Test
  public void Play_Item_VP() {
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Playable vp = EasyMock.createMock(VictoryPoint.class);

    vp.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(vp);

    player.play(vp);

    EasyMock.verify(vp);
  }

  @Test
  public void Play_Item_YOP() {
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Playable yop = EasyMock.createMock(YearOfPlenty.class);

    yop.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(yop);

    player.play(yop);

    EasyMock.verify(yop);
  }

  @Test
  public void Buy_Dev_Success() {

    PurchaseHelper purchaseHelper = EasyMock.createMock(PurchaseHelper.class);
    Buyable dev = EasyMock.createMock(Knight.class);

    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaseHelper, new BankTradeHelper());

    EasyMock.expect(purchaseHelper.buyDevelopment(dev)).andReturn(true);
    EasyMock.replay(purchaseHelper, dev);

    try {
      player.buy(dev);
    } catch (NotEnoughInventoryException e) {
      throw new RuntimeException("Unexpected exception in test");
    }


    EasyMock.verify(purchaseHelper, dev);
  }

  @Test
  public void Buy_Dev_Failure() {

    PurchaseHelper purchaseHelper = EasyMock.createMock(PurchaseHelper.class);
    Buyable dev = EasyMock.createMock(Knight.class);

    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaseHelper, new BankTradeHelper());

    EasyMock.expect(purchaseHelper.buyDevelopment(dev)).andReturn(false);
    EasyMock.replay(purchaseHelper, dev);


    Exception expected = new NotEnoughInventoryException();
    Exception actual = assertThrows(expected.getClass(), () -> player.buy(dev));
    assertEquals(expected.getClass(), actual.getClass());
  }

  @Test
  public void Build_City_At_Vertex_Valid() {
    PurchaseHelper purchaser = EasyMock.createMock(PurchaseHelper.class);
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaser, new BankTradeHelper());

    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    // cannot mock edge because actually checking the concrete class
    Location vertex = new Vertex(new Point(0, 1));
    Piece city = new City(new PieceData(Color.BLUE, PieceType.CITY), dist, tracker);

    purchaser.build(Color.NONE, city, vertex);
    EasyMock.expectLastCall();


    EasyMock.replay(purchaser);
    assertDoesNotThrow(() -> player.build(city, vertex));

    EasyMock.verify(purchaser);

  }

  @Test
  public void Build_Settlement_At_Vertex_But_With_Not_Enough_Stuff() {
    PurchaseHelper purchaser = EasyMock.createMock(PurchaseHelper.class);
    GamePlayer player = new GamePlayer(Color.BLUE, new PlacementHelper(), new PlayerTradeHelper(), purchaser, new BankTradeHelper());

    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    // cannot mock edge because actually checking the concrete class
    Location vertex = new Vertex(new Point(0, 1));
    Piece settlement = new Settlement(new PieceData(Color.BLUE, PieceType.CITY), dist, tracker);


    Exception ex = new NotEnoughInventoryException();
    purchaser.build(Color.BLUE, settlement, vertex);
    EasyMock.expectLastCall().andStubThrow(ex);
    EasyMock.replay(purchaser);

    assertThrows(ex.getClass(), () -> player.build(settlement, vertex));


  }

  @Test
  public void Build_Road_At_Edge_Valid() {
    PurchaseHelper purchaser = EasyMock.createMock(PurchaseHelper.class);
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaser, new BankTradeHelper());

    // cannot mock edge because actually checking the concrete class
    Location edge = new Edge(new Point(1, 1), new Point(0, 0));
    Piece road = new Road(EasyMock.createMock(PieceData.class));

    purchaser.build(Color.NONE, road, edge);
    EasyMock.expectLastCall();

    EasyMock.replay(purchaser);
    assertDoesNotThrow(() -> player.build(road, edge));

    EasyMock.verify(purchaser);

  }

  // it is either creating this or having to copy paste even more tests
  static final List<Location> locations_invalid_for_buildings = List.of(new Edge(new Point(0, 0), new Point(1, 0)), new HillsHex(1, new HashSet<>(List.of(new Vertex(new Point(0, 0)), new Vertex(new Point(1, 0)), new Vertex(new Point(2, 0)), new Vertex(new Point(3, 0)), new Vertex(new Point(4, 0)), new Vertex(new Point(5, 0)))), new HashSet<>(List.of(new Edge(new Point(0, 0), new Point(1, 0)), new Edge(new Point(1, 0), new Point(2, 0)), new Edge(new Point(2, 0), new Point(3, 0)), new Edge(new Point(3, 0), new Point(4, 0)), new Edge(new Point(4, 0), new Point(5, 0)), new Edge(new Point(5, 0), new Point(0, 0)))), new ArrayList<>()));

  @ParameterizedTest
  @FieldSource("locations_invalid_for_buildings")
  public void Build_Settlement_At_Hex_Invalid(Location location) {
    PurchaseHelper purchaser = EasyMock.createMock(PurchaseHelper.class);
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaser, new BankTradeHelper());

    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    Piece settlement = new Settlement(new PieceData(Color.BLUE, PieceType.CITY), dist, tracker);

    purchaser.build(Color.NONE, settlement, location);
    EasyMock.expectLastCall();
    EasyMock.replay(purchaser);

    Exception expected = new InvalidPlacementException("Settlements/Cities can only be build on points");
    Exception actual = assertThrows(expected.getClass(), () -> player.build(settlement, location));

    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Build_Road_At_Hex_Invalid() {
    PurchaseHelper purchaser = EasyMock.createMock(PurchaseHelper.class);
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaser, new BankTradeHelper());

    Piece settlement = new Road(new PieceData(Color.BLUE, PieceType.CITY));

    Location location = EasyMock.createMock(Hex.class);
    purchaser.build(Color.NONE, settlement, location);
    EasyMock.expectLastCall();
    EasyMock.replay(purchaser);

    Exception expected = new InvalidPlacementException("Roads can only be build on edges");
    Exception actual = assertThrows(expected.getClass(), () -> player.build(settlement, location));

    assertEquals(expected.getMessage(), actual.getMessage());

  }

  static final List<Piece> invalid_pieces = List.of(new Harbor(new PieceData(Color.BLUE, PieceType.HARBOR)), new Robber(new PieceData(Color.NONE, PieceType.ROBBER)));

  @ParameterizedTest
  @FieldSource("invalid_pieces")
  public void Build_Invalid_Pieces_Test(Piece piece) {
    PurchaseHelper purchaser = EasyMock.createMock(PurchaseHelper.class);
    GamePlayer player = new GamePlayer(Color.NONE, new PlacementHelper(), new PlayerTradeHelper(), purchaser, new BankTradeHelper());

    Location location = EasyMock.createMock(Hex.class);
    purchaser.build(Color.NONE, piece, location);
    EasyMock.expectLastCall();
    EasyMock.replay(purchaser);

    Exception expected = new InvalidPlacementException("Cannot build either robber or harbor");
    Exception actual = assertThrows(expected.getClass(), () -> player.build(piece, location));

    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Propose_Player_Trade_Same_Player_Unsuccessful() {
    GamePlayer player = new GamePlayer(Color.BLUE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Exception expected = new InvalidTradeException("Cannot offer to same player as proposed");

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = new ArrayList<>();
    List<ResourceStack> requested = new ArrayList<>();

    Exception actual = assertThrows(expected.getClass(), () -> {
      player.proposePlayerTrade(Color.BLUE, new TradeOffer(inv_a, inv_b, requested, offered));
    });
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.getMessage(), actual.getMessage());


  }

  @Test
  public void Propose_Player_Trade_To_No_One_Unsuccessful() {
    GamePlayer player = new GamePlayer(Color.BLUE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = new ArrayList<>();
    List<ResourceStack> requested = new ArrayList<>();

    Exception expected = new InvalidTradeException("Trade must go to someone");
    Exception actual = assertThrows(expected.getClass(), () -> {
      player.proposePlayerTrade(Color.NONE, new TradeOffer(inv_a, inv_b, requested, offered));
    });
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Propose_Player_Trade_Successful() {
    PlayerTradeHelper trader = EasyMock.createMock(PlayerTradeHelper.class);
    GamePlayer player = new GamePlayer(Color.BLUE, new PlacementHelper(), trader, new PurchaseHelper(), new BankTradeHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = new ArrayList<>();
    List<ResourceStack> requested = new ArrayList<>();
    TradeOffer offer = new TradeOffer(inv_a, inv_b, requested, offered);

    trader.addTradeOffer(offer);
    EasyMock.expectLastCall();

    EasyMock.replay(trader);


    try {
      player.proposePlayerTrade(Color.RED, offer);
    } catch (Exception e) {
      throw new RuntimeException("Unexpected call failure");
    }

    // I suppose the test is for a call to add the trade offer to the ledger
    EasyMock.verify(trader);
  }

  @Test
  public void Accept_Player_Trade_Successful() {
    PlayerTradeHelper trader = EasyMock.createMock(PlayerTradeHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, new PlacementHelper(), trader, new PurchaseHelper(), new BankTradeHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = List.of(new OreResourceStack(1));
    List<ResourceStack> requested = List.of(new GrainResourceStack(1));
    TradeOffer offer = new TradeOffer(inv_a, inv_b, requested, offered);

    trader.conductTrade(offer);
    EasyMock.expectLastCall();


    EasyMock.replay(trader);

    player.acceptPlayerTrade(Color.BLUE, offer);

    EasyMock.verify(trader);
  }

  @Test
  public void Accept_Player_Trade_To_No_One_Unsuccessful() {
    GamePlayer player = new GamePlayer(Color.BLUE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = new ArrayList<>();
    List<ResourceStack> requested = new ArrayList<>();

    Exception expected = new InvalidTradeException("Trade must go to someone");
    Exception actual = assertThrows(expected.getClass(), () -> {
      player.acceptPlayerTrade(Color.NONE, new TradeOffer(inv_a, inv_b, requested, offered));
    });
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Accept_Player_Trade_To_Same_Player_Unsuccessful() {
    GamePlayer player = new GamePlayer(Color.BLUE, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = new ArrayList<>();
    List<ResourceStack> requested = new ArrayList<>();

    Exception expected = new InvalidTradeException("Cannot offer to same player as proposed");
    Exception actual = assertThrows(expected.getClass(), () -> {
      player.acceptPlayerTrade(Color.BLUE, new TradeOffer(inv_a, inv_b, requested, offered));
    });
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Accept_Player_Trade_Not_Enough_Resources_Unsuccessful() {
    PlayerTradeHelper trader = EasyMock.createMock(PlayerTradeHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, new PlacementHelper(), trader, new PurchaseHelper(), new BankTradeHelper());

    ResourceInventory inv_a = EasyMock.createMock(ResourceInventory.class);
    ResourceInventory inv_b = EasyMock.createMock(ResourceInventory.class);
    List<ResourceStack> offered = List.of(new OreResourceStack(1));
    List<ResourceStack> requested = List.of(new GrainResourceStack(1));
    TradeOffer offer = new TradeOffer(inv_a, inv_b, requested, offered);

    trader.conductTrade(offer);

    Exception expected_ex = new InvalidTradeException("Not enough resources for that trade");
    EasyMock.expectLastCall().andThrow(expected_ex);
    EasyMock.replay(trader);

    boolean exception_seen = false;

    try {
      player.acceptPlayerTrade(Color.BLUE, offer);
    } catch (Exception e) {
      assertEquals(e.getMessage(), expected_ex.getMessage());
      exception_seen = true;
    }
    assertTrue(exception_seen);
    EasyMock.verify(trader);

  }

  @Test
  public void Get_Valid_Placements_Tests_City_At_Least_One() {
    PieceData data = EasyMock.createMock(PieceData.class);
    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    PlacementHelper placer = EasyMock.createMock(PlacementHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, placer, new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Piece piece = new City(data, dist, tracker);

    EasyMock.expect(placer.getValidBuildingSpots(Color.RED)).andReturn(List.of(new Vertex(new Point(0, 0))));
    EasyMock.replay(placer);

    List<Location> actual_locs = player.getValidPlacements(piece);

    assertFalse(actual_locs.isEmpty());


    EasyMock.verify(placer);

  }

  @Test
  public void Get_Valid_Placements_Tests_Road_At_Least_One() {
    PieceData data = EasyMock.createMock(PieceData.class);

    PlacementHelper placer = EasyMock.createMock(PlacementHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, placer, new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Piece piece = new Road(data);

    EasyMock.expect(placer.getValidRoadSpots(Color.RED)).andReturn(List.of(new Edge(new Point(0, 0), new Point(1, 1))));
    EasyMock.replay(placer);

    List<Location> actual_locs = player.getValidPlacements(piece);

    assertFalse(actual_locs.isEmpty());


    EasyMock.verify(placer);

  }

  @Test
  public void Get_Valid_Placements_Test_Settlement_NONE() {
    PieceData data = EasyMock.createMock(PieceData.class);
    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    PlacementHelper placer = EasyMock.createMock(PlacementHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, placer, new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Piece piece = new Settlement(data, dist, tracker);

    EasyMock.expect(placer.getValidBuildingSpots(Color.RED)).andReturn(new ArrayList<>());
    EasyMock.replay(placer);

    List<Location> actual_locs = player.getValidPlacements(piece);

    assertTrue(actual_locs.isEmpty());


    EasyMock.verify(placer);

  }

  @Test
  public void Get_Valid_Placements_Try_To_Move_Harbor() {
    PieceData data = EasyMock.createMock(PieceData.class);

    PlacementHelper placer = EasyMock.createMock(PlacementHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, placer, new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Exception expected = new IllegalArgumentException("Cannot move a harbor");
    Exception actual = assertThrowsExactly(expected.getClass(), () -> player.getValidPlacements(new Harbor(data)));

    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Get_Valid_Placements_Try_To_Move_Other_For_Jacoco_Coverage() {

    PlacementHelper placer = EasyMock.createMock(PlacementHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, placer, new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    Exception expected = new RuntimeException("Invalid piece type");
    Exception actual = assertThrowsExactly(expected.getClass(), () -> player.getValidPlacements(EasyMock.createMock(Piece.class)));

    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void Get_Valid_Placements_Try_To_Move_Robber() {
    PieceData data = EasyMock.createMock(PieceData.class);

    PlacementHelper placer = EasyMock.createMock(PlacementHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, placer, new PlayerTradeHelper(), new PurchaseHelper(), new BankTradeHelper());

    EasyMock.expect(placer.getValidRobberSpots()).andReturn(new ArrayList<>());
    EasyMock.replay(placer);

    List<Location> locs = player.getValidPlacements(new Robber(data));

    assertTrue(locs.isEmpty());
    EasyMock.verify(placer);

  }


  @Test
  public void Bank_Trade_Valid() {

    BankTradeHelper bankTradeHelper = EasyMock.createMock(BankTradeHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), bankTradeHelper);

    ResourceStack give = new OreResourceStack(4);
    ResourceStack receive = new WoolResourceStack(1);

    bankTradeHelper.tradeWithBank(Color.RED, give, receive);
    EasyMock.expectLastCall();

    EasyMock.replay(bankTradeHelper);

    player.tradeWithBank(give, receive);


    EasyMock.verify(bankTradeHelper);


  }

  static List<Arguments> invalid_bank_trades = List.of(Arguments.arguments(new GrainResourceStack(1), new BrickResourceStack(4)), Arguments.arguments(new LumberResourceStack(3), new WoolResourceStack(1)));

  @ParameterizedTest
  @FieldSource("invalid_bank_trades")
  public void Bank_Trade_Invalid_Due_To_Number_Of_Resource(ResourceStack give, ResourceStack receive) {

    BankTradeHelper bankTradeHelper = EasyMock.createMock(BankTradeHelper.class);
    GamePlayer player = new GamePlayer(Color.RED, new PlacementHelper(), new PlayerTradeHelper(), new PurchaseHelper(), bankTradeHelper);

    Exception expected = new InvalidPlacementException("Trade needs to be 4 to 1");

    bankTradeHelper.tradeWithBank(Color.RED, give, receive);
    EasyMock.expectLastCall().andStubThrow(expected);

    EasyMock.replay(bankTradeHelper);

    // I guess for the time being this just tests that the exception would not be swallowed
    Exception actual = assertThrows(expected.getClass(), () -> player.tradeWithBank(give, receive));

    assertEquals(expected.getMessage(), actual.getMessage());

    EasyMock.verify(bankTradeHelper);

  }
}

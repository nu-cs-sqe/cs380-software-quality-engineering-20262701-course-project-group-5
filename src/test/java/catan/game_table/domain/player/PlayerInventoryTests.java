package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.development.*;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.*;
import catan.game_table.domain.resource.*;
import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerInventoryTests {


  @ParameterizedTest
  @CsvSource({"Harbor, player cannot own a harbor", "Robber, player cannot own the robber"})
  public void addPieceWithException(String type, String msg) {
    PlayerInventory inv = new PlayerInventory(new ArrayList<>());

    PieceData data = new PieceData("", Color.None);

    Piece piece;

    if (type.equals("Harbor")) {
      piece = new Harbor(data);
    } else if (type.equals("Robber")) {
      piece = new Robber(data);
    } else {
      throw new IllegalStateException("should only be robber or harbor");
    }


    Exception expected = new IllegalArgumentException(msg);
    Exception actual = null;
    try {
      inv.addPiece(piece);
    } catch (Exception e) {
      actual = e;

    }

    assertNotNull(actual);
    assertEquals(expected.getMessage(), actual.getMessage());

  }


  // constructs a list of pieces from a given pattern
  private List<Piece> listOfPiecesConstruct(String pattern) {
    List<Piece> pieces = new ArrayList<>();

    PieceData data = EasyMock.createMock(PieceData.class);
    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    for (char i : pattern.toCharArray()) {
      switch (i) {
        case 's':
          pieces.add(new Settlement(data, dist, tracker));
          break;
        case 'r':
          pieces.add(new Road(data));
          break;
        case 'c':
          pieces.add(new City(data, dist, tracker));
          break;
        default:
          break;
      }
    }

    return pieces;

  }


  @ParameterizedTest
  @CsvSource({
          "Settlement, e, s", // empty before, 1 settlement after
          "City, e, c", // empty before, 1 city after
          "Road, e, r", // empty before road after
          "Settlement, scr, scrs", // one of each before, 2 settlements after
          "City, scr, scrc", // one of each before, 2 cities after
          "Road, scr, scrr", // one of each before, 2 roads after
          "Settlement, r, rs", // one road before, road and settlement after
          "City, r, rc", // one road before, road and city after
          "Road, r, rr", // one road before, 2 roads after
  })
  public void addPieceTests(String type, String listConstructBefore, String listConstructAfter) {
    PlayerInventory inv = new PlayerInventory(this.listOfPiecesConstruct(listConstructBefore));

    List<Piece> expected_pieces = this.listOfPiecesConstruct(listConstructAfter);

    PieceData data = EasyMock.createMock(PieceData.class);
    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    Piece piece;

    if (type.equals("City")) {
      piece = new City(data, dist, tracker);
    } else if (type.equals("Settlement")) {
      piece = new Settlement(data, dist, tracker);
    } else {
      piece = new Road(data);
    }

    inv.addPiece(piece);

    List<Piece> actual_pieces = inv.getPieces();
    assertEquals(expected_pieces.size(), actual_pieces.size());
    int lst_size = expected_pieces.size();
    for (int i = 0; i < lst_size; i++) {
      assertEquals(expected_pieces.get(i).getClass(), actual_pieces.get(i).getClass());
    }
  }


  @ParameterizedTest
  @CsvSource({"Harbor, player cannot own a harbor", "Robber, player cannot own the robber"})
  public void hasPieceIllegalPieces(String type, String msg) {
    PlayerInventory inv = new PlayerInventory(new ArrayList<>());

    PieceData data = new PieceData("", Color.None);

    Piece piece;

    if (type.equals("Harbor")) {
      piece = new Harbor(data);
    } else if (type.equals("Robber")) {
      piece = new Robber(data);
    } else {
      throw new IllegalStateException("should only be robber or harbor");
    }


    Exception expected = new IllegalArgumentException(msg);
    Exception actual = assertThrows(IllegalArgumentException.class, () -> inv.hasPiece(piece));;
    assertEquals(expected.getMessage(), actual.getMessage());
  }


  @ParameterizedTest
  @CsvSource({
          "Settlement, e, false", // e means emtpy list
          "City, e, false",
          "Road, e, false",
          "Settlement, s, true", // s is list with one settlement
          "City, s, false",
          "Road, s, false",
          "Settlement, r, false", // r is list with one road
          "City, r, false",
          "Road, r, true",
          "Settlement, c, false",
          "City, c, true",
          "Road, c, false",
          "Road, cr, true",
          "City, rs, false"
  })
  public void hasPieceTests(String type, String listConstructBefore, boolean expected_result) {
    PlayerInventory inv = new PlayerInventory(this.listOfPiecesConstruct(listConstructBefore));

    PieceData data = EasyMock.createMock(PieceData.class);
    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    Piece piece;

    if (type.equals("City")) {
      piece = new City(data, dist, tracker);
    } else if (type.equals("Settlement")) {
      piece = new Settlement(data, dist, tracker);
    } else {
      piece = new Road(data);
    }

    boolean actual_result = inv.hasPiece(piece);

    assertEquals(expected_result, actual_result);

  }

  @ParameterizedTest
  @CsvSource({"Harbor, player cannot own a harbor", "Robber, player cannot own the robber"})
  public void removePieceWithExceptions(String type, String msg) {
    PlayerInventory inv = new PlayerInventory(new ArrayList<>());

    PieceData data = new PieceData("", Color.None);

    Piece piece;

    if (type.equals("Harbor")) {
      piece = new Harbor(data);
    } else if (type.equals("Robber")) {
      piece = new Robber(data);
    } else {
      throw new IllegalStateException("should only be robber or harbor");
    }


    Exception expected = new IllegalArgumentException(msg);
    Exception actual = null;
    try {
      inv.removePiece(piece);
    } catch (Exception e) {
      actual = e;

    }

    assertNotNull(actual);
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @ParameterizedTest
  @CsvSource({
          "Road, e, e", // if there is nothing to remove then it is already in a good state
          "Settlement, s, e", // 1 settlement before, nothing after
          "Settlement, r, r", // keeps the road
          "City, c, e", // removes the one city
          "City, r, r", // keeps the orad
          "Road, r, e", // removes the road
          "Road, c, c", // keeps the city
          "Road, cr, c", // removes the one road
          "Settlement, ss, s", // removes one settlement
          "Settlement, cr, cr", // keeps the pieces
          "Settlement, sscr, scr", // keeps the pieces
  })
  public void removePieceTests(String type, String listConstructBefore, String listConstructAfter) {
    PlayerInventory inv = new PlayerInventory(this.listOfPiecesConstruct(listConstructBefore));

    List<Piece> expected_pieces = this.listOfPiecesConstruct(listConstructAfter);

    PieceData data = EasyMock.createMock(PieceData.class);
    BankResourceDistributor dist = EasyMock.createMock(BankResourceDistributor.class);
    ScoreTracker tracker = EasyMock.createMock(ScoreTracker.class);

    Piece piece;

    if (type.equals("City")) {
      piece = new City(data, dist, tracker);
    } else if (type.equals("Settlement")) {
      piece = new Settlement(data, dist, tracker);
    } else {
      piece = new Road(data);
    }

    inv.removePiece(piece);

    List<Piece> actual_pieces = inv.getPieces();
    assertEquals(expected_pieces.size(), actual_pieces.size());
    int lst_size = expected_pieces.size();
    for (int i = 0; i < lst_size; i++) {
      assertEquals(expected_pieces.get(i).getClass(), actual_pieces.get(i).getClass());
    }
  }


  // constructs a list of resources from a pattern
  private List<ResourceStack> listOfResourcesConstruct(String construct) {
    List<ResourceStack> res = new ArrayList<>();

    if (construct.length() % 2 != 0) {
      throw new IllegalArgumentException("must have even nuber of characters");
    }

    int len = construct.length();

    for (int i = 0; i < len; i += 2) {
      char type = construct.charAt(i);
      int amount = (int) (construct.charAt(i + 1) - '0');

      switch (type) {
        case 'l':
          res.add(new LumberResourceStack(amount));
          break;
        case 'o':
          res.add(new OreResourceStack(amount));
          break;
        case 'b':
          res.add(new BrickResourceStack(amount));
          break;
        case 'g':
          res.add(new GrainResourceStack(amount));
          break;
        case 'w':
          res.add(new WoolResourceStack(amount));
          break;
        default:
          break;
      }
    }


    return res;
  }


  @ParameterizedTest
  @CsvSource({
          // notation is <resource type><amount>
          "l1, e0, l1", // adding 1 lumber to empty
          "o1, e0, o1",
          "w1, e0, w1",
          "b1, e0, b1",
          "g1, e0, g1",
          "l1, l1, l2",
          "w1, w1, w2", // adding 1 wool into another wool -> 2 wools
          "g1, g1, g2",
          "b1, b1, b2",
          "g1, g1b1o1w1l1, g2b1o1w1l1", // adding 1 grain into one of each -> 2 grains + one of the rest
          "g1, w1, w1g1"

  })
  public void addResourcesTests(String to_add, String before, String after) {

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), this.listOfResourcesConstruct(before));

    // too much effort to write a separate switch case for all of this
    List<ResourceStack> to_add_as_list = this.listOfResourcesConstruct(to_add);
    ResourceStack to_add_as_stack = to_add_as_list.getFirst();


    inv.addResources(to_add_as_stack);


    List<ResourceStack> actual_resources = inv.getResources();
    List<ResourceStack> expected_resources = this.listOfResourcesConstruct(after);

    assertEquals(actual_resources.size(), expected_resources.size());

    int size = expected_resources.size();
    for (int i = 0; i < size; i++) {
      ResourceStack actual = actual_resources.get(i);
      ResourceStack expected = expected_resources.get(i);

      assertEquals(actual.getSize(), expected.getSize());
      assertEquals(actual.getClass(), expected.getClass());
    }

  }


  @ParameterizedTest
  @CsvSource({
          // notation is <resource type><amount>
          // I don't want to make a complicated parser
          // so I will take the executive decision that INT_MAX = 9 because it doenst really matter that much
          // and testing int overflow can be done in another test I suppose
          "l1, e0, false",
          "w1, e0, false",
          "g1, e0, false",
          "o1, e0, false",
          "b1, e0, false",
          "l1, l1, true",
          "w1, w1, true",
          "g1, g1, true",
          "o1, o1, true",
          "b1, b1, true",
          "l9, l1, false",
          "w9, w1, false",
          "g9, g1, false",
          "o9, o1, false",
          "b9, b1, false",
          "g1, o1, false",
          "g1, o1g1, true",
          "g1, o1l1, false",
          "b1, b2, true",
  })
  public void hasResourcesTests(String to_check, String before, boolean expected_res) {

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), this.listOfResourcesConstruct(before));

    // too much effort to write a separate switch case for all of this
    List<ResourceStack> to_check_as_list = this.listOfResourcesConstruct(to_check);
    ResourceStack to_check_as_stack = to_check_as_list.getFirst();


    boolean actual_res = inv.hasResources(to_check_as_stack);

    assertEquals(expected_res, actual_res);

  }


  @ParameterizedTest
  @CsvSource({
          // notation is <resource type><amount>
          "l1, l1, e0", // 1 lumber, remove 1 lumber, rest is empty
          "w1, w1, e0",
          "b1, b1, e0",
          "o1, o1, e0",
          "b1, b1, e0",
          "w1, w1o1, o1",
          "w1, w2, w1", // have 2 remove 1, leaves 1
          "w2, w5, w3",
          "l1, l2, l1"

  })
  public void removeResourcesTestsForValidCases(String to_remove, String before, String after) {

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), this.listOfResourcesConstruct(before));

    // too much effort to write a separate switch case for all of this
    List<ResourceStack> to_remove_as_list = this.listOfResourcesConstruct(to_remove);
    ResourceStack to_remove_as_stack = to_remove_as_list.getFirst();


    try {
      inv.removeResources(to_remove_as_stack);
    } catch (NotEnoughInventoryException e) {
      System.err.println("this shouldn't happen in this test");
      throw new RuntimeException("This test should not have exceptions");
    }


    List<ResourceStack> actual_resources = inv.getResources();
    List<ResourceStack> expected_resources = this.listOfResourcesConstruct(after);

    assertEquals(actual_resources.size(), expected_resources.size());

    int size = expected_resources.size();
    for (int i = 0; i < size; i++) {
      ResourceStack actual = actual_resources.get(i);
      ResourceStack expected = expected_resources.get(i);

      assertEquals(actual.getSize(), expected.getSize());
      assertEquals(actual.getClass(), expected.getClass());
    }

  }

  @ParameterizedTest
  @CsvSource({
          // notation is <resource type><amount>
          "l1, e0", // withdrawing from empty resources
          "w1, e0",
          "g1, e0",
          "o1, e0",
          "b1, e0",
          "l9, l1", // don't feel like making a more complicated parser, so INT_MAX = 9
          "w9, w1",
          "o9, o1",
          "g9, g1",
          "b9, b1",
          "b1, w1o1"

  })
  public void removeResourcesTestsForInValidCases(String to_remove, String before) {

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), this.listOfResourcesConstruct(before));

    // too much effort to write a separate switch case for all of this
    List<ResourceStack> to_remove_as_list = this.listOfResourcesConstruct(to_remove);
    ResourceStack to_remove_as_stack = to_remove_as_list.getFirst();


    Exception expected_ex = new NotEnoughInventoryException();
    Exception actual_ex = null;
    try {
      inv.removeResources(to_remove_as_stack);
    } catch (Exception e) {
      actual_ex = e;
    }

    assertNotNull(actual_ex);
    assertEquals(expected_ex.getClass(), actual_ex.getClass());

  }


  private List<Development> listOfDevelopmentConstruct(String pattern) {
    List<Development> res = new ArrayList<>();

    for (char i : pattern.toCharArray()) {
      switch (i) {
        case 'k':
          res.add(new Knight());
          break;
        case 'r':
          res.add(new RoadBuilder());
          break;
        case 'm':
          res.add(new Monopoly());
          break;
        case 'v':
          res.add(new VictoryPoint());
          break;
        case 'y':
          res.add(new YearOfPlenty());
          break;
        default:
          break;
      }
    }

    return res;
  }

  @ParameterizedTest
  @CsvSource({
          "k, e, k", // k = knight, e = empty
          "r, e, r", // r = road builder
          "v, e, v", // v = victory point
          "m, e, m", // m = monopoly
          "y, e, y", // y = year of plenty
          "y, k, ky",
          "y, kr, kry"
  })
  public void addDevelopmentTests(String type, String before, String after) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));

    inv.addDevelopment(development);

    List<Development> expected_after = this.listOfDevelopmentConstruct(after);
    List<Development> actual_after = inv.getDevelopments();

    assertEquals(expected_after.size(), actual_after.size());
    int size = expected_after.size();

    for (int i = 0; i < size; i++) {
      Development expected = expected_after.get(i);
      Development actual = actual_after.get(i);
      assertEquals(expected.getClass(), actual.getClass());
    }
  }


  @ParameterizedTest
  @CsvSource({
          "k, e, false",
          "y, e, false",
          "v, e, false",
          "r, e, false",
          "m, e, false",
          "k, k, true",
          "y, y, true",
          "v, v, true",
          "r, r, true",
          "m, m, true",
          "y, r, false",
          "y, rk, false",
          "y, ky, true"
  })
  public void hasDevelopmentTests(String type, String before, boolean expected_result) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));
    boolean actual_result = inv.hasDevelopment(development);


    assertEquals(expected_result, actual_result);

  }


  @ParameterizedTest
  @CsvSource({
          "v, v, e",
          "k, k, e",
          "r, r, e",
          "y, y, e",
          "m, m , e",
          "y, yk, k"
  })
  public void removeDevelopmentTestsForValidExchanges(String type, String before, String after) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));

    try {
      inv.removeDevelopment(development);
    } catch (Exception e) {
      throw new RuntimeException("this test should not have exceptions");
    }

    List<Development> expected_after = this.listOfDevelopmentConstruct(after);
    List<Development> actual_after = inv.getDevelopments();

    assertEquals(expected_after.size(), actual_after.size());
    int size = expected_after.size();

    for (int i = 0; i < size; i++) {
      Development expected = expected_after.get(i);
      Development actual = actual_after.get(i);
      assertEquals(expected.getClass(), actual.getClass());
    }
  }

  @ParameterizedTest
  @CsvSource({
          "k, e",
          "k, r",
          "y, mk"
  })
  public void removeDevelopmentTestsRaisingExceptions(String type, String before) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));

    Exception expected_ex = new NotEnoughInventoryException();
    Exception actual_ex = null;
    try {
      inv.removeDevelopment(development);
    } catch (Exception e) {
      actual_ex = e;
    }

    assertNotNull(actual_ex);
    assertEquals(expected_ex.getClass(), actual_ex.getClass());

  }

}

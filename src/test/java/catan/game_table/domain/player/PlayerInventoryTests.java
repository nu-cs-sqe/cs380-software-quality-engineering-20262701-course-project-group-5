package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;
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
  @CsvSource({
    "Harbor, player cannot own a harbor", // harbor test and error message
    "Robber, player cannot own the robber" // robber test and error message
  })
  public void Add_Piece_With_Exception(String type, String msg) {
    PlayerInventory inv = new PlayerInventory(new ArrayList<>());

    PieceData data = new PieceData(Color.NONE, PieceType.CITY);

    Piece piece;

    if (type.equals("Harbor")) {
      piece = new Harbor(data);
    } else if (type.equals("Robber")) {
      piece = new Robber(data);
    } else {
      throw new IllegalStateException("should only be robber or harbor");
    }


    Exception expected = new IllegalArgumentException(msg);
    Exception actual = assertThrows(expected.getClass(), () -> inv.addPiece(piece));
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
  @CsvSource({"Settlement, e, s", // empty before, 1 settlement after
          "City, e, c", // empty before, 1 city after
          "Road, e, r", // empty before road after
          "Settlement, scr, scrs", // one of each before, 2 settlements after
          "City, scr, scrc", // one of each before, 2 cities after
          "Road, scr, scrr", // one of each before, 2 roads after
          "Settlement, r, rs", // one road before, road and settlement after
          "City, r, rc", // one road before, road and city after
          "Road, r, rr", // one road before, 2 roads after
  })
  public void Add_Piece_Tests(String type, String listConstructBefore, String listConstructAfter) {
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
  @CsvSource({
    "Harbor, player cannot own a harbor", // harbor test and error message
    "Robber, player cannot own the robber" // robber test and error message
  })
  public void Has_Piece_Illegal_Pieces(String type, String msg) {
    PlayerInventory inv = new PlayerInventory(new ArrayList<>());

    PieceData data = new PieceData(Color.NONE, PieceType.CITY);

    Piece piece;

    if (type.equals("Harbor")) {
      piece = new Harbor(data);
    } else if (type.equals("Robber")) {
      piece = new Robber(data);
    } else {
      throw new IllegalStateException("should only be robber or harbor");
    }

    Exception expected = new IllegalArgumentException(msg);
    Exception actual = assertThrows(IllegalArgumentException.class, () -> inv.hasPiece(piece));
    ;
    assertEquals(expected.getMessage(), actual.getMessage());
  }


  @ParameterizedTest
  @CsvSource({
    "Settlement, e, false", // e means emtpy list
    "City, e, false", // no match on empty list
    "Road, e, false", // no match on empty list
    "Settlement, s, true", // s is list with one settlement
    "City, s, false", // no match on list with one settlement
    "Road, s, false", // no match on list with one settlement
    "Settlement, r, false", // r is list with one road
    "City, r, false", // no match on list with one road
    "Road, r, true", // match on list with one road
    "Settlement, c, false", // c is list with one city
    "City, c, true", // match on list with one city
    "Road, c, false", // no match on list with one city
    "Road, cr, true", // list of two elements has one match, return true
    "City, rs, false" // list of two elements has no match, return false
  })
  public void Has_Piece_Tests(String type, String listConstructBefore, boolean expected_result) {
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
  public void Remove_Piece_With_Exceptions(String type, String msg) {
    PlayerInventory inv = new PlayerInventory(new ArrayList<>());

    PieceData data = new PieceData(Color.NONE, PieceType.CITY);

    Piece piece;

    if (type.equals("Harbor")) {
      piece = new Harbor(data);
    } else if (type.equals("Robber")) {
      piece = new Robber(data);
    } else {
      throw new IllegalStateException("should only be robber or harbor");
    }

    Exception expected = new IllegalArgumentException(msg);
    Exception actual = assertThrows(expected.getClass(), () -> inv.removePiece(piece));
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @ParameterizedTest
  @CsvSource({
    "Road, e, e", // if there is nothing to remove then it is already in a good state
    "Settlement, s, e", // 1 settlement before, nothing after
    "Settlement, r, r", // keeps the road
    "City, c, e", // removes the one city
    "City, r, r", // keeps the road
    "Road, r, e", // removes the road
    "Road, c, c", // keeps the city
    "Road, cr, c", // removes the one road
    "Settlement, ss, s", // removes one settlement
    "Settlement, cr, cr", // keeps the pieces
    "Settlement, sscr, scr", // keeps the pieces
  })
  public void Remove_Piece_Tests(String type, String listConstructBefore, String listConstructAfter) {
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
    "o1, e0, o1", // adding 1 ore to empty
    "w1, e0, w1", // adding 1 wool to empty
    "b1, e0, b1", // adding 1 brick to empty
    "g1, e0, g1", // adding 1 grain to empty
    "l1, l1, l2", // adding 1 lumber into another lumber -> 2 lumbers
    "w1, w1, w2", // adding 1 wool into another wool -> 2 wools
    "g1, g1, g2", // adding 1 grain into another grain -> 2 grains
    "b1, b1, b2", // adding 1 brick into another brick -> 2 brick
    "g1, g1b1o1w1l1, g2b1o1w1l1", // adding 1 grain into one of each -> 2 grains + one of the rest
    "g1, w1, w1g1" // adding 1 grain into 1 wool -> 1 wool + 1 grain
  })
  public void Add_Resources_Tests(String to_add, String before, String after) {

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
    "l1, e0, false", // check if empty list has the resource, returns false
    "w1, e0, false", // check if empty list has the resource, returns false
    "g1, e0, false", // check if empty list has the resource, returns false
    "o1, e0, false", // check if empty list has the resource, returns false
    "b1, e0, false", // check if empty list has the resource, returns false
    "l1, l1, true", // list has the resource exactly, returns true 
    "w1, w1, true", // list has the resource exactly, returns true
    "g1, g1, true", // list has the resource exactly, returns true
    "o1, o1, true", // list has the resource exactly, returns true
    "b1, b1, true", // list has the resource exactly, returns true
    "l9, l1, false", // list has that resource but not enough, returns false
    "w9, w1, false", // list has that resource but not enough, returns false
    "g9, g1, false", // list has that resource but not enough, returns false
    "o9, o1, false", // list has that resource but not enough, returns false
    "b9, b1, false", // list has that resource but not enough, returns false
    "g1, o1, false", // list has a different resource, returns false
    "g1, o1g1, true", // list has the resource and another resource, returns true
    "g1, o1l1, false", // list has two unrelated resources, returns false
    "b1, b2, true" // list has more than enough of the resource, returns true
  })
  public void Has_Resources_Tests(String to_check, String before, boolean expected_res) {

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
          "w1, w1, e0", // 1 wool, remove 1 wool, rest is empty
          "b1, b1, e0", // 1 brick, remove 1 brick, rest is empty
          "o1, o1, e0", // 1 ore, remove 1 ore, rest is empty
          "b1, b1, e0", // 1 grain, remove 1 grain, rest is empty
          "w1, w1o1, o1", // have 1 wool and 1 ore, remove 1 wool, leaves 1 ore
          "w1, w2, w1", // have 2 remove 1, leaves 1
          "w2, w5, w3", // have 5 remove 2, leaves 3
          "l1, l2, l1"  // have 2 remove 1, leaves 1 but for lumber

  })
  public void Remove_Resources_Tests_For_Valid_Cases(String to_remove, String before, String after) {

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
    "l1, e0", // withdrawing lumber from empty resources
    "w1, e0", // withdrawing wood from empty resources
    "g1, e0", // withdrawing grain from empty resources
    "o1, e0", // withdrawing ore from empty resources
    "b1, e0", // withdrawing brick from empty resources
    "l9, l1", // withdrawing more lumber than available, don't feel like making a more complicated parser, so INT_MAX = 9
    "w9, w1", // withdrawing more wood than available
    "o9, o1", // withdrawing more ore than available
    "g9, g1", // withdrawing more grain than available  
    "b9, b1", // withdrawing more brick than available
    "b1, w1o1" // withdrawing from a non-empty list that does not have brick
  })
  public void Remove_Resources_Tests_For_Invalid_Cases(String to_remove, String before) {

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), this.listOfResourcesConstruct(before));

    // too much effort to write a separate switch case for all of this
    List<ResourceStack> to_remove_as_list = this.listOfResourcesConstruct(to_remove);
    ResourceStack to_remove_as_stack = to_remove_as_list.getFirst();


    Exception expected_ex = new NotEnoughInventoryException();
    Exception actual_ex = assertThrows(expected_ex.getClass(), () -> inv.removeResources(to_remove_as_stack));
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
    "y, k, ky", // add to non empty list
    "y, kr, kry" // add to non empty list with two elements
  })
  public void Add_Development_Tests(String type, String before, String after) {
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
    "k, e, false", // check if empty inventory has this dev card
    "y, e, false", // check if empty inventory has this dev card
    "v, e, false", // check if empty inventory has this dev card
    "r, e, false", // check if empty inventory has this dev card
    "m, e, false", // check if empty inventory has this dev card
    "k, k, true", // check that one match exists
    "y, y, true", // check that one match exists
    "v, v, true", // check that one match exists
    "r, r, true", // check that one match exists
    "m, m, true", // check that one match exists
    "y, r, false", // check that no matches exist in a non empty inventory
    "y, rk, false", // check that no matches exist in a non empty inventory
    "y, ky, true" // check that one match exists in an inventory with multiple elements
  })
  public void Has_Development_Tests(String type, String before, boolean expected_result) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));
    boolean actual_result = inv.hasDevelopment(development);

    assertEquals(expected_result, actual_result);
  }

  @ParameterizedTest
  @CsvSource({
    "v, v, e", // remove one VP form inventory with one of that exact thing
    "k, k, e", // remove one knight form inventory with one of that exact thing
    "r, r, e", // remove one roadbuilder form inventory with one of that exact thing
    "y, y, e", // remove one year of plenty form inventory with one of that exact thing
    "m, m, e", // remove one monopoly form inventory with one of that exact thing
    "y, yk, k" // remove one year of plenty from inventory with multiple things, still has elements afterwards
  })
  public void Remove_Development_Tests_For_Valid_Exchanges(String type, String before, String after) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));

    assertDoesNotThrow(() -> inv.removeDevelopment(development));

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
    "k, e", // trying to remove knight from empty inventory
    "k, r", // trying to remove a knight that is not in the non-empty inventory
    "y, mk" // trying to remove a year of plenty that is not in the non-empty inventory
  })
  public void Remove_Development_Tests_Raising_Exceptions(String type, String before) {
    Development development = this.listOfDevelopmentConstruct(type).getFirst();

    PlayerInventory inv = new PlayerInventory(new ArrayList<>(), new ArrayList<>(), this.listOfDevelopmentConstruct(before));

    Exception expected_ex = new NotEnoughInventoryException();
    Exception actual_ex = assertThrows(expected_ex.getClass(), () -> inv.removeDevelopment(development));
    assertEquals(expected_ex.getClass(), actual_ex.getClass());

  }

}

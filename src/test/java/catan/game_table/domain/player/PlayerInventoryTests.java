package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.piece.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerInventoryTests {


  @ParameterizedTest
  @CsvSource({"Harbor, player cannot own a harbor", "Robber, player cannot own the robber"})
  public void addPiece_with_exception(String type, String msg) {
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


  private List<Piece> listConstruct(String pattern) {
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
  public void addPiece_tests(String type, String listConstructBefore, String listConstructAfter) {
    PlayerInventory inv = new PlayerInventory(this.listConstruct(listConstructBefore));

    List<Piece> expectedAfter = this.listConstruct(listConstructAfter);

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

    List<Piece> actualPieces = inv.getPieces();
    assertEquals(expectedAfter.size(), actualPieces.size());
    int lst_size = expectedAfter.size();
    for (int i = 0; i < lst_size; i++) {
      assertEquals(expectedAfter.get(i).getClass(), actualPieces.get(i).getClass());
    }
  }


  @ParameterizedTest
  @CsvSource({"Harbor, player cannot own a harbor", "Robber, player cannot own the robber"})
  public void hasPiece_illegal_pieces(String type, String msg) {
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
      boolean res = inv.hasPiece(piece);
    } catch (Exception e) {
      actual = e;

    }

    assertNotNull(actual);
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
  public void hasPiece_tests(String type, String listConstructBefore, boolean expected_result) {
    PlayerInventory inv = new PlayerInventory(this.listConstruct(listConstructBefore));

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
  public void removePiece_with_exception(String type, String msg) {
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
  public void removePiece_tests(String type, String listConstructBefore, String listConstructAfter) {
    PlayerInventory inv = new PlayerInventory(this.listConstruct(listConstructBefore));

    List<Piece> expectedAfter = this.listConstruct(listConstructAfter);

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

    List<Piece> actualPieces = inv.getPieces();
    assertEquals(expectedAfter.size(), actualPieces.size());
    int lst_size = expectedAfter.size();
    for (int i = 0; i < lst_size; i++) {
      assertEquals(expectedAfter.get(i).getClass(), actualPieces.get(i).getClass());
    }
  }


}

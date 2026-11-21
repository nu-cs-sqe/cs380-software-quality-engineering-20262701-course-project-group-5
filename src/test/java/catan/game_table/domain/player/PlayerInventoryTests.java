package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerInventoryTests {


  @Test
  public void addPiece_harbor_exception() {
    PlayerInventory inv = new PlayerInventory();

    Piece harbor = new Harbor(new PieceData("", Color.None));

    Exception expected = new IllegalArgumentException("player cannot own a harbor");
    Exception actual = null;
    try {
      inv.addPiece(harbor);
    } catch (Exception e) {
      actual = e;

    }

    assertNotNull(actual);
    assertEquals(expected.getMessage(), actual.getMessage());

  }

  @Test
  public void addPiece_robber_exception() {

  }

  @Test
  public void addPiece_settlement_no_insides_successful() {

  }


}

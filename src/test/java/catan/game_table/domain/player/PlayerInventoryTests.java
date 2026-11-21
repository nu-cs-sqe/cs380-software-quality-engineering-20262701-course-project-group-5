package catan.game_table.domain.player;

import catan.game_table.domain.Color;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import catan.game_table.domain.piece.Robber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerInventoryTests {


  @ParameterizedTest
  @CsvSource({"Harbor, player cannot own a harbor", "Robber, player cannot own the robber"})
  public void addPiece_with_exception(String type, String msg) {
    PlayerInventory inv = new PlayerInventory();

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


  @Test
  public void addPiece_settlement_no_insides_successful() {

  }


}

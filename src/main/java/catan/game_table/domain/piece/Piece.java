package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;
import java.util.Locale;

public abstract class Piece {
  private final PieceData data = new PieceData(
        Color.NONE,
        PieceType.valueOf(getClass().getSimpleName().toUpperCase(Locale.ROOT)));

  public PieceData getPieceData() {
    return data;
  }
}

package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;
import java.util.Locale;

public abstract class Piece {
  protected final PieceData pieceData;

  public Piece(PieceData data) {
    this.pieceData = data;
  }

  public PieceData getPieceData() {
    return this.pieceData;
  }
}

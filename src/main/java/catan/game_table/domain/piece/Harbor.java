package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;

public class Harbor extends Piece {
  public Harbor() {
    super(new PieceData(Color.NONE, PieceType.HARBOR));
  }

}

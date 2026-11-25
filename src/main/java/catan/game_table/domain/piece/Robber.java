package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;

public class Robber extends Piece {
  public Robber() {
    super(new PieceData(Color.NONE, PieceType.ROBBER));
  }

}

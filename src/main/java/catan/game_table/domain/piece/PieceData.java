package catan.game_table.domain.piece;

import catan.game_table.domain.Color;

public class PieceData {
  private final Color color;
  public PieceData(Color color, String type) {
    this.color = color;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || object.getClass() != this.getClass()){
      return false;
    }
    return color == ((PieceData) object).color;
  }
}

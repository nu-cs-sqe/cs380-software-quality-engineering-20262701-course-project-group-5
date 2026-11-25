package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;

public class PieceData {
  public final Color color;
  public final PieceType type;

  public PieceData(Color color, PieceType type) {
    this.color = color;
    this.type = type;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || object.getClass() != this.getClass()) {
      return false;
    }
    PieceData objectData = (PieceData) object;
    return color == objectData.color && type == objectData.type;
  }

  @Override
  public int hashCode() {
    return color.hashCode() + type.hashCode();
  }

  public Color getColor() {
    return color;
  }
}

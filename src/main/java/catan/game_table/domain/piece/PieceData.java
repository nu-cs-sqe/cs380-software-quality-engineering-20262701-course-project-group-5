package catan.game_table.domain.piece;

import catan.game_table.domain.Color;

public class PieceData {
  private final Color color;
  private final String type;
  public PieceData(Color color, String type) {
    this.color = color;
    this.type = type;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || object.getClass() != this.getClass()){
      return false;
    }
    PieceData objectData = (PieceData) object;
    return color == objectData.color && type.equals(objectData.type);
  }
}

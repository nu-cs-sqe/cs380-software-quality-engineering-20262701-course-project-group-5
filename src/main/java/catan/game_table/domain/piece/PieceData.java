package catan.game_table.domain.piece;

import catan.game_table.domain.Color;

public class PieceData {
  public final String type;
  public final Color color;

  public PieceData(String type, Color color) {
    this.type = type;
    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    return false;
  }


  @Override
  public int hashCode() {
    return 0;
  }
}

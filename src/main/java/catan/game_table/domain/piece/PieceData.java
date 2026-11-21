package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import java.util.Objects;

public class PieceData {
  public final String type;
  public final Color color;

  public PieceData(String type, Color color) {
    this.type = type;
    this.color = color;
  }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PieceData)) {
            return false;
        }
        PieceData otherOne = (PieceData) other;
        return Objects.equals(type, otherOne.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

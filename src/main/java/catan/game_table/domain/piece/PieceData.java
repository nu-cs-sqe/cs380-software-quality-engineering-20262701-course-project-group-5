package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;

public class PieceData {
  final Color color;
  final PieceType type;

  public PieceData(Color color, PieceType type){
      this.color = color;
      this.type = type;
  }

  public Color getColor(){
      return color;
  }
}

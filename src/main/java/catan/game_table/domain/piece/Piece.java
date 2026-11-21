package catan.game_table.domain.piece;

public abstract class Piece {

  private PieceData data;

  public Piece(PieceData data) {
    this.data = data;
  }

  public PieceData getPieceData() {
    return data;
  }

  public void onPlaceEffect() {}

}

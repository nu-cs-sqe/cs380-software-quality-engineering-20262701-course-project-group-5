package catan.game_table.domain.piece;

public interface PieceInventory {

  public void addPiece(Piece piece);

  public boolean hasPiece(Piece piece);

  public void removePiece(Piece piece);

}

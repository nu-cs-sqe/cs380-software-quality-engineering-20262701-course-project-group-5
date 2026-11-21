package catan.game_table.domain.player;

import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceInventory;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;

public class PlayerInventory implements PieceInventory, ResourceInventory {
  @Override
  public void addPiece(Piece piece) {

    if (piece.getClass() == Harbor.class) {
      throw new IllegalArgumentException("player cannot own a harbor");
    }
  }

  @Override
  public boolean hasPiece(Piece piece) {
    return false;
  }

  @Override
  public void removePiece(Piece piece) {

  }

  @Override
  public void addResources(ResourceStack stack) {

  }

  @Override
  public boolean hasResources(ResourceStack stack) {
    return false;
  }

  @Override
  public void removeResources(ResourceStack stack) throws NotEnoughInventoryException {

  }
}

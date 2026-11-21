package catan.game_table.domain.player;

import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceInventory;
import catan.game_table.domain.piece.Robber;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class PlayerInventory implements PieceInventory, ResourceInventory {

  private List<Piece> pieces;
  private List<ResourceStack> resources;


  public PlayerInventory(List<Piece> initialPieces) {
    this.pieces = initialPieces;
  }

  public PlayerInventory(List<Piece> initialPieces, List<ResourceStack> initialResources) {
    this.pieces = initialPieces;
    this.resources = initialResources;
  }

  @Override
  public void addPiece(Piece piece) {

    if (piece.getClass() == Harbor.class) {
      throw new IllegalArgumentException("player cannot own a harbor");
    }
    if (piece.getClass() == Robber.class) {
      throw new IllegalArgumentException("player cannot own the robber");
    }

    this.pieces.add(piece);
  }

  @Override
  public boolean hasPiece(Piece piece) {
    if (piece.getClass() == Harbor.class) {
      throw new IllegalArgumentException("player cannot own a harbor");
    }
    if (piece.getClass() == Robber.class) {
      throw new IllegalArgumentException("player cannot own the robber");
    }

    for (Piece i: this.pieces) {
      if (i.getClass().equals(piece.getClass())) {
        return true;
      }
    }

    return false;
  }

  // package private so that it is only for testing
  List<Piece> getPieces() {
    return this.pieces;
  }

  List<ResourceStack> getResources() {
    return this.resources;
  }

  @Override
  public void removePiece(Piece piece) {

    if (piece.getClass() == Harbor.class) {
      throw new IllegalArgumentException("player cannot own a harbor");
    }
    if (piece.getClass() == Robber.class) {
      throw new IllegalArgumentException("player cannot own the robber");
    }


    int to_remove = -1;
    int size = this.pieces.size();


    // remove from the end
    for (int i = size - 1; i > -1; i--) {
      if (this.pieces.get(i).getClass().equals(piece.getClass())) {
        to_remove = i;
        break;
      }
    }

    if (to_remove != -1) {
      this.pieces.remove(to_remove);
    }
  }

  @Override
  public void addResources(ResourceStack stack) {
    int len = this.resources.size();
    for (int i = 0; i < len; i++) {

      ResourceStack current = this.resources.get(i);
      if (current.getClass().equals(stack.getClass())) {
        this.resources.set(i, current.add(stack));
        return;
      }
    }

    this.resources.add(stack);
  }

  @Override
  public boolean hasResources(ResourceStack stack) {
    for (ResourceStack i : this.resources) {
      // this should be a dictionary imo
      // but gotta stick to design or some crap like that
      if (i.getClass().equals(stack.getClass()) && i.getSize() >= stack.getSize()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void removeResources(ResourceStack stack) throws NotEnoughInventoryException {

  }
}

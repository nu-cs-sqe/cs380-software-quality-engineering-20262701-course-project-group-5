package catan.game_table.domain.player;

import catan.game_table.domain.development.Development;
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
  private List<Development> developments;


  public PlayerInventory(List<Piece> initialPieces) {
    this.pieces = initialPieces;
  }

  public PlayerInventory(List<Piece> initialPieces, List<ResourceStack> initialResources) {
    this.pieces = initialPieces;
    this.resources = initialResources;
  }

  public PlayerInventory(List<Piece> initialPieces, List<ResourceStack> initialResources, List<Development> initialDevCards) {
    this.pieces = initialPieces;
    this.resources = initialResources;
    this.developments = initialDevCards;
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

    for (Piece currentPiece : this.pieces) {
      if (currentPiece.getClass().equals(piece.getClass())) {
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

  List<Development> getDevelopments() {
    return this.developments;
  }

  @Override
  public void removePiece(Piece piece) {

    if (piece.getClass() == Harbor.class) {
      throw new IllegalArgumentException("player cannot own a harbor");
    }
    if (piece.getClass() == Robber.class) {
      throw new IllegalArgumentException("player cannot own the robber");
    }


    int indexToRemove = -1;
    int size = this.pieces.size();


    // remove from the end
    for (int i = size - 1; i > -1; i--) {
      if (this.pieces.get(i).getClass().equals(piece.getClass())) {
        indexToRemove = i;
        break;
      }
    }

    if (indexToRemove != -1) {
      this.pieces.remove(indexToRemove);
    }
  }

  @Override
  public void addResources(ResourceStack stack) {
    int len = this.resources.size();
    for (int i = 0; i < len; i++) {
      ResourceStack currentStack = this.resources.get(i);
      if (currentStack.getClass().equals(stack.getClass())) {
        this.resources.set(i, currentStack.add(stack));
        return;
      }
    }

    this.resources.add(stack);
  }

  @Override
  public boolean hasResources(ResourceStack stack) {
    for (ResourceStack currentStack : this.resources) {
      if (currentStack.getClass().equals(stack.getClass()) && currentStack.getSize() >= stack.getSize()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void removeResources(ResourceStack stack) throws NotEnoughInventoryException {
    int indexToRemove = -1;
    int len = this.resources.size();
    boolean foundAny = false;

    for (int i = 0; i < len; i++) {
      ResourceStack currentStack = this.resources.get(i);

      if (currentStack.getClass().equals(stack.getClass())) {

        int current_size = currentStack.getSize();
        int look_up_size = stack.getSize();
        if (current_size > look_up_size) {
          this.resources.set(i, currentStack.subtract(stack));
          foundAny = true;
          break;
        } else if (current_size == look_up_size) {
          indexToRemove = i;
          foundAny = true;
          break;
        } else {
          throw new NotEnoughInventoryException();
        }
      }
    }

    if (indexToRemove != -1) {
      this.resources.remove(indexToRemove);
    }

    if (!foundAny) {
      throw new NotEnoughInventoryException();
    }

  }

  public void addDevelopment(Development development) {
    this.developments.add(development);
  }

  public boolean hasDevelopment(Development development) {
    for (Development currentDev : this.developments) {
      if (currentDev.getClass().equals(development.getClass())) {
        return true;
      }
    }
    return false;
  }

  public void removeDevelopment(Development development) throws NotEnoughInventoryException {
    int indexToRemove = -1;
    int size = this.developments.size();

    for (int i = 0; i < size; i++) {
      Development current_development = this.developments.get(i);
      if (current_development.getClass().equals(development.getClass())) {
        indexToRemove = i;
        break;
      }
    }

    if (indexToRemove == -1) {
      throw new NotEnoughInventoryException();
    } else {
      this.developments.remove(indexToRemove);
    }
  }

}

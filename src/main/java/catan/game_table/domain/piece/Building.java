package catan.game_table.domain.piece;

import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceProducts;
import catan.game_table.domain.resource.ResourceStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Building extends Piece {

  public Building(PieceData data, BankResourceDistributor distributor) {
    super(data);
  }

  public void produceResources(ResourceProducts products) {

  }

  public List<ResourceStack> getCost() {
    return new ArrayList<>();
  }

}

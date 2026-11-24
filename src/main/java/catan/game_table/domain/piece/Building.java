package catan.game_table.domain.piece;

import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class Building extends Piece {
  protected PieceData pieceData;
  protected BankResourceDistributor bankResourceDistributor;

  public Building(PieceData pieceData, BankResourceDistributor bankResourceDistributor) {
    this.pieceData = pieceData;
    this.bankResourceDistributor = bankResourceDistributor;
  }

  public void produceResources(List<ResourceStack> products) {
  }
}

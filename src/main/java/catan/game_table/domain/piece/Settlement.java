package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class Settlement {
  private PieceData pieceData;
  private BankResourceDistributor bankResourceDistributor;
  private ScoreTracker ownerVictoryPointsTracker;
  private int multiplier;

  public Settlement(PieceData pieceData, BankResourceDistributor bankResourceDistributor, ScoreTracker ownerVictoryPointsTracker){
    this.pieceData = pieceData;
    this.bankResourceDistributor = bankResourceDistributor;
    this.ownerVictoryPointsTracker = ownerVictoryPointsTracker;
    this.multiplier = 2;
  }

  public void produceResources(List<ResourceStack> products){
    ResourceStack resourceStack = products.getFirst();
    for(int i = 1; i < multiplier; i++){
      resourceStack = resourceStack.add(resourceStack);
    }
    products.set(0, resourceStack);
    Color pieceColor = pieceData.color;
    bankResourceDistributor.produceResources(pieceColor, products);
  }
}

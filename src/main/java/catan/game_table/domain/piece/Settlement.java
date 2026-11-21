package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class Settlement extends Building {
  private ScoreTracker ownerVictoryPointsTracker;
  private final int multiplier;

  public Settlement(PieceData pieceData, BankResourceDistributor bankResourceDistributor, ScoreTracker ownerVictoryPointsTracker){
    super(pieceData, bankResourceDistributor);
    this.ownerVictoryPointsTracker = ownerVictoryPointsTracker;
    this.multiplier = 2;
  }

  @Override
  public void produceResources(List<ResourceStack> products){
    ResourceStack resourceStack = products.getFirst();
    for(int i = 1; i < multiplier; i++) {
      resourceStack = resourceStack.add(resourceStack);
    }
    products.set(0, resourceStack);
    Color pieceColor = pieceData.getColor();
    bankResourceDistributor.produceResources(pieceColor, products);
  }
}

package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class Settlement extends Building {
  private ScoreTracker ownerVictoryPointsTracker;

  public Settlement(PieceData pieceData, BankResourceDistributor bankResourceDistributor, ScoreTracker ownerVictoryPointsTracker){
    super(pieceData, bankResourceDistributor);
    this.ownerVictoryPointsTracker = ownerVictoryPointsTracker;
  }

  @Override
  public void produceResources(List<ResourceStack> products){
    Color pieceColor = pieceData.getColor();
    bankResourceDistributor.produceResources(pieceColor, products);
  }
}

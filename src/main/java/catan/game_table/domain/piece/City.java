package catan.game_table.domain.piece;

import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;

import java.util.ArrayList;
import java.util.List;

public class City extends Building {
  public City(PieceData data, BankResourceDistributor distributor, ScoreTracker ownerVictoryPointsTracker) {
    super(data, distributor);
  }

  @Override
  public List<ResourceStack> getCost() {
    return new ArrayList<>();
  }

}
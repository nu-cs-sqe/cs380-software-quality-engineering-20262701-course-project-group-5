package catan.game_table.domain.bank;

import catan.game_table.domain.Color;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceProducts;

import java.util.Map;

public class BankResourceDistributor {

  public BankResourceDistributor(Map<Color, ResourceInventory> playerInventories, ResourceInventory bankInventory) {}


  public void produceResources(Color color, ResourceProducts products) {}

  public void finalizeProduction() {}

  public void abortProduction() {}
}

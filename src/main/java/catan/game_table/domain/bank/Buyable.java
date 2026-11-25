package catan.game_table.domain.bank;

import catan.game_table.domain.resource.ResourceStack;

import java.util.ArrayList;
import java.util.List;

public interface Buyable {
  public List<ResourceStack> getCost();
}

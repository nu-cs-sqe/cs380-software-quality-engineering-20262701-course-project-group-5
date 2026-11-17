package catan.game_table.domain.trade;

import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class TradeOffer {
  private final ResourceInventory proposer;
  private final ResourceInventory receiver;
  private final List<ResourceStack> requestedResources;
  private final List<ResourceStack> offeredResources;
  private boolean accepted;

  public TradeOffer(ResourceInventory proposer, ResourceInventory receiver, List<ResourceStack> requestedResources, List<ResourceStack> offeredResources) {
    this.proposer = proposer;
    this.receiver = receiver;
    this.requestedResources = requestedResources;
    this.offeredResources = offeredResources;
  }
}

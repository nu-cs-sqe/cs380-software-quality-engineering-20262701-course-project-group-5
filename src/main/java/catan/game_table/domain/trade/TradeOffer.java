package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class TradeOffer {

    private final List<ResourceStack> requestedResources;
    private final List<ResourceStack> offeredResources;
    private boolean accepted;

    public TradeOffer(Color proposer, Color receiver, List<ResourceStack> requestedResources,
                      List<ResourceStack> offeredResources) {
        this.requestedResources = List.copyOf(requestedResources);
        this.offeredResources = List.copyOf(offeredResources);
    }

    public List<ResourceStack> getRequestedResources() {
        return requestedResources;
    }

    public List<ResourceStack> getOfferedResources() {
        return offeredResources;
    }

    public boolean isAccepted() {
        return accepted;
    }

    void markAccepted() {
        accepted = true;
    }
}

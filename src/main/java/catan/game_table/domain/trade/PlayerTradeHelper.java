package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.resource.ResourceInventory;
import catan.mvc_util.Observed;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerTradeHelper extends Observed {

    private final Set<TradeOffer> activeTradeOffers = new HashSet<>();
    private final Map<Color, ResourceInventory> playerInventories;
    private final TradeTurnContext turnContext;

    public PlayerTradeHelper(Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext) {
        this.playerInventories = playerInventories;
        this.turnContext = turnContext;
    }

    public Set<TradeOffer> getActiveTradeOffers() {
        return Set.copyOf(activeTradeOffers);
    }

    public void proposeTrade(Color actor, TradeOffer offer) throws InvalidTradeException {
        if (!isActivePlayerOnOpenTurn(actor) || actor != offer.getProposer()) {
            throw new InvalidTradeException();
        }
        if (!isValidPlayerTrade(offer)) {
            throw new InvalidTradeException();
        }
        activeTradeOffers.add(offer);
        notifyObservers();
    }

    private boolean isActivePlayerOnOpenTurn(Color actor) {
        return turnContext.isTradingOpen() && actor == turnContext.getActivePlayerColor();
    }

    private boolean isValidPlayerTrade(TradeOffer offer) {
        return hasEligibleReceiver(offer) && hasResourcesOnBothSides(offer);
    }

    private boolean hasEligibleReceiver(TradeOffer offer) {
        Color receiver = offer.getReceiver();
        return offer.getProposer() != receiver && playerInventories.containsKey(receiver);
    }

    private boolean hasResourcesOnBothSides(TradeOffer offer) {
        return !offer.getRequestedResources().isEmpty() && !offer.getOfferedResources().isEmpty();
    }
}

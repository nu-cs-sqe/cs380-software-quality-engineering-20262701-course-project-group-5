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
    private final TradeTurnContext turnContext;

    public PlayerTradeHelper(Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext) {
        this.turnContext = turnContext;
    }

    public Set<TradeOffer> getActiveTradeOffers() {
        return Set.copyOf(activeTradeOffers);
    }

    public void proposeTrade(Color actor, TradeOffer offer) throws InvalidTradeException {
        if (!turnContext.isTradingOpen()) {
            throw new InvalidTradeException();
        }
        activeTradeOffers.add(offer);
        notifyObservers();
    }
}

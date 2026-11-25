package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;
import catan.mvc_util.Observed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerTradeHelper extends Observed {

    private final Set<TradeOffer> activeTradeOffers = new HashSet<>();
    private final Map<Color, ResourceInventory> playerInventories;
    private final TradeTurnContext turnContext;

    public PlayerTradeHelper(Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext) {
        this.playerInventories = Map.copyOf(playerInventories);
        this.turnContext = turnContext;
    }

    public Set<TradeOffer> getActiveTradeOffers() {
        return Set.copyOf(activeTradeOffers);
    }

    public void proposeTrade(Color actor, TradeOffer offer) throws InvalidTradeException {
        if (!isActivePlayerOnOpenTurn(actor) || actor != offer.getProposer()) {
            throw new InvalidTradeException();
        }
        if (offer.isAccepted() || !isValidPlayerTrade(offer)) {
            throw new InvalidTradeException();
        }
        if (!activeTradeOffers.add(offer)) {
            throw new InvalidTradeException();
        }
        notifyObservers();
    }

    public void counterTrade(Color actor, TradeOffer originalOffer, TradeOffer counterOffer)
            throws InvalidTradeException {
        if (!turnContext.isTradingOpen() || !isPendingOfferForReceiver(actor, originalOffer)) {
            throw new InvalidTradeException();
        }
        if (!hasReversedParties(originalOffer, counterOffer) || !hasValidTradeTerms(counterOffer)) {
            throw new InvalidTradeException();
        }
        if (activeTradeOffers.contains(counterOffer)) {
            throw new InvalidTradeException();
        }
        activeTradeOffers.remove(originalOffer);
        activeTradeOffers.add(counterOffer);
        notifyObservers();
    }

    public void acceptTrade(Color actor, TradeOffer offer) throws InvalidTradeException {
        if (!turnContext.isTradingOpen() || !isPendingOfferForReceiver(actor, offer)) {
            throw new InvalidTradeException();
        }
        offer.markAccepted();
        notifyObservers();
    }

    public void refuseTrade(Color actor, TradeOffer offer) throws InvalidTradeException {
        if (!turnContext.isTradingOpen() || !isPendingOfferForReceiver(actor, offer)) {
            throw new InvalidTradeException();
        }
        activeTradeOffers.remove(offer);
        notifyObservers();
    }

    public void conductTrade(Color actor, TradeOffer offer) throws InvalidTradeException {
        if (actor == offer.getProposer() && !offer.isAccepted()) {
            throw new InvalidTradeException();
        }
        ResourceInventory proposerInventory = playerInventories.get(offer.getProposer());
        ResourceInventory receiverInventory = playerInventories.get(offer.getReceiver());
        List<ResourceStack> offered = combineStacksByType(offer.getOfferedResources());
        List<ResourceStack> requested = combineStacksByType(offer.getRequestedResources());

        if (!hasResources(proposerInventory, offered) || !hasResources(receiverInventory, requested)) {
            throw new InvalidTradeException();
        }
        try {
            transferResources(proposerInventory, receiverInventory, offered);
            transferResources(receiverInventory, proposerInventory, requested);
        } catch (NotEnoughInventoryException cause) {
            InvalidTradeException failure = new InvalidTradeException();
            failure.initCause(cause);
            throw failure;
        }
        if (!offer.isAccepted()) {
            offer.markAccepted();
        }
        activeTradeOffers.remove(offer);
        notifyObservers();
    }

    public void expireTradeOffers() {
        if (activeTradeOffers.isEmpty()) {
            return;
        }
        activeTradeOffers.clear();
        notifyObservers();
    }

    private List<ResourceStack> combineStacksByType(List<ResourceStack> stacks) {
        Map<Class<? extends ResourceStack>, ResourceStack> totals = new HashMap<>();
        for (ResourceStack stack : stacks) {
            totals.merge(stack.getClass(), stack, ResourceStack::add);
        }
        return List.copyOf(totals.values());
    }

    private boolean hasResources(ResourceInventory inventory, List<ResourceStack> resources) {
        return resources.stream().allMatch(inventory::hasResources);
    }

    private void transferResources(ResourceInventory from, ResourceInventory to, List<ResourceStack> resources)
            throws NotEnoughInventoryException {
        for (ResourceStack stack : resources) {
            from.removeResources(stack);
            to.addResources(stack);
        }
    }

    private boolean hasReversedParties(TradeOffer originalOffer, TradeOffer counterOffer) {
        return counterOffer.getProposer() == originalOffer.getReceiver()
                && counterOffer.getReceiver() == originalOffer.getProposer();
    }

    private boolean isPendingOfferForReceiver(Color actor, TradeOffer offer) {
        return actor == offer.getReceiver() && isPendingOffer(offer);
    }

    private boolean isPendingOffer(TradeOffer offer) {
        return activeTradeOffers.contains(offer) && !offer.isAccepted();
    }

    private boolean isActivePlayerOnOpenTurn(Color actor) {
        return turnContext.isTradingOpen() && actor == turnContext.getActivePlayerColor();
    }

    private boolean isValidPlayerTrade(TradeOffer offer) {
        return hasEligibleReceiver(offer) && hasValidTradeTerms(offer);
    }

    private boolean hasEligibleReceiver(TradeOffer offer) {
        Color receiver = offer.getReceiver();
        return offer.getProposer() != receiver && playerInventories.containsKey(receiver);
    }

    private boolean hasValidTradeTerms(TradeOffer offer) {
        return hasResourcesOnBothSides(offer) && hasDisjointResourceTypes(offer);
    }

    private boolean hasResourcesOnBothSides(TradeOffer offer) {
        return hasPositiveResources(offer.getRequestedResources()) && hasPositiveResources(offer.getOfferedResources());
    }

    private boolean hasPositiveResources(List<ResourceStack> resources) {
        return !resources.isEmpty() && resources.stream().allMatch(stack -> stack.getSize() > 0);
    }

    private boolean hasDisjointResourceTypes(TradeOffer offer) {
        Set<Class<? extends ResourceStack>> offeredTypes = new HashSet<>();
        for (ResourceStack stack : offer.getOfferedResources()) {
            offeredTypes.add(stack.getClass());
        }
        return offer.getRequestedResources().stream().noneMatch(stack -> offeredTypes.contains(stack.getClass()));
    }
}

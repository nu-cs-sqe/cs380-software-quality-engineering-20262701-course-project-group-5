package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.resource.BrickResourceStack;
import catan.game_table.domain.resource.OreResourceStack;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;
import catan.mvc_util.Observer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTradeHelperTests {

    @Test
    void ProposeTrade_OnOpenTurnWithoutOfferedCards_QueuesOfferAndNotifiesObserver() throws InvalidTradeException {
        // integration testing, since I want the quantities to be specified
        ResourceStack brick = new BrickResourceStack(1);
        ResourceStack ore = new OreResourceStack(1);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        EasyMock.expect(redInventory.hasResources(brick)).andStubReturn(false);

        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.BLUE, List.of(brick), List.of(ore));
        EasyMock.replay(redInventory, blueInventory);

        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        Observer observer = EasyMock.createMock(Observer.class);
        observer.update();
        EasyMock.expectLastCall().andAnswer(() -> {
            assertEquals(Set.of(offer), helper.getActiveTradeOffers());
            return null;
        });
        EasyMock.replay(observer);
        helper.addObserver(observer);
        assertTrue(helper.getActiveTradeOffers().isEmpty());

        helper.proposeTrade(Color.RED, offer);

        assertEquals(Set.of(offer), helper.getActiveTradeOffers());
        assertFalse(offer.isAccepted());
        EasyMock.verify(observer, redInventory, blueInventory, offer);
    }

    @Test
    void ProposeTrade_AfterTradingCloses_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, false);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.BLUE,
                List.of(new BrickResourceStack(1)), List.of(new OreResourceStack(1)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_OnAnotherPlayersOpenTurn_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.BLUE, Color.RED,
                List.of(new OreResourceStack(1)), List.of(new BrickResourceStack(2)));

        assertProposalRejectedWithoutSideEffects(Color.BLUE, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenActorDiffersFromProposer_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.BLUE, Color.RED,
                List.of(new OreResourceStack(1)), List.of(new BrickResourceStack(2)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenProposerAndReceiverAreTheSame_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.RED,
                List.of(new BrickResourceStack(1)), List.of(new OreResourceStack(1)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenReceiverHasNoInventoryEntry_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.ORANGE,
                List.of(new BrickResourceStack(1)), List.of(new OreResourceStack(1)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenRequestedResourcesAreEmpty_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.BLUE,
                List.of(new BrickResourceStack(1)), List.of());

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenOfferedResourcesAreEmpty_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.BLUE,
                List.of(), List.of(new OreResourceStack(1)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenOfferedStackHasZeroQuantity_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.BLUE,
                List.of(new BrickResourceStack(0)), List.of(new OreResourceStack(1)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    private static void assertProposalRejectedWithoutSideEffects(Color actor, TradeOffer offer,
                                                                 TradeTurnContext turnContext) {
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        EasyMock.replay(redInventory, blueInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.addObserver(observer);
        Set<TradeOffer> previousOffers = helper.getActiveTradeOffers();

        assertThrows(InvalidTradeException.class, () -> helper.proposeTrade(actor, offer));

        assertEquals(previousOffers, helper.getActiveTradeOffers());
        EasyMock.verify(observer, redInventory, blueInventory, offer);
    }

    private static TradeTurnContext stubTurnContext(Color activePlayer, boolean tradingOpen) {
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(tradingOpen);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(activePlayer);
        EasyMock.replay(turnContext);
        return turnContext;
    }

    private static TradeOffer stubUnansweredOffer(Color proposer, Color receiver,
                                                  List<ResourceStack> offered, List<ResourceStack> requested) {
        TradeOffer offer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(offer.getProposer()).andStubReturn(proposer);
        EasyMock.expect(offer.getReceiver()).andStubReturn(receiver);
        EasyMock.expect(offer.getOfferedResources()).andStubReturn(offered);
        EasyMock.expect(offer.getRequestedResources()).andStubReturn(requested);
        EasyMock.expect(offer.isAccepted()).andStubReturn(false);
        EasyMock.replay(offer);
        return offer;
    }
}

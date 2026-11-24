package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.resource.BrickResourceStack;
import catan.game_table.domain.resource.LumberResourceStack;
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

    //tests for proposeTrade()
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

    @Test
    // this test prevents players from trading matching resources, which is banned in Catan
    void ProposeTrade_WhenResourceTypeAppearsOnBothSides_RejectsOfferWithoutSideEffects() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = stubUnansweredOffer(Color.RED, Color.BLUE,
                List.of(new LumberResourceStack(1), new BrickResourceStack(1)),
                List.of(new OreResourceStack(1), new BrickResourceStack(2)));

        assertProposalRejectedWithoutSideEffects(Color.RED, offer, turnContext);
    }

    @Test
    void ProposeTrade_WhenSameOfferInstanceIsSubmittedTwice_RejectsSecondSubmission() throws InvalidTradeException {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = new TradeOffer(Color.RED, Color.BLUE,
                List.of(new OreResourceStack(1)), List.of(new BrickResourceStack(1)));
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        observer.update();
        EasyMock.expectLastCall().once();
        EasyMock.replay(redInventory, blueInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.addObserver(observer);

        helper.proposeTrade(Color.RED, offer);
        assertEquals(Set.of(offer), helper.getActiveTradeOffers());

        assertThrows(InvalidTradeException.class, () -> helper.proposeTrade(Color.RED, offer));

        assertEquals(Set.of(offer), helper.getActiveTradeOffers());
        assertFalse(offer.isAccepted());
        EasyMock.verify(observer, redInventory, blueInventory);
    }

    // tests for acceptTrade()
    @Test
    void AcceptTrade_WhenReceiverHasNoRequestedCards_RecordsAgreementWithoutMovingCards()
            throws InvalidTradeException {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        ResourceStack ore = new OreResourceStack(1);
        TradeOffer offer = new TradeOffer(Color.RED, Color.BLUE,
                List.of(ore), List.of(new BrickResourceStack(1)));
        TradeOffer otherOffer = new TradeOffer(Color.RED, Color.BLUE,
                List.of(new LumberResourceStack(1)), List.of(new BrickResourceStack(1)));
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        EasyMock.expect(blueInventory.hasResources(ore)).andStubReturn(false);
        EasyMock.replay(redInventory, blueInventory);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.proposeTrade(Color.RED, offer);
        helper.proposeTrade(Color.RED, otherOffer);

        Observer observer = EasyMock.createMock(Observer.class);
        observer.update();
        EasyMock.expectLastCall().andAnswer(() -> {
            assertTrue(offer.isAccepted());
            assertEquals(Set.of(offer, otherOffer), helper.getActiveTradeOffers());
            return null;
        });
        EasyMock.replay(observer);
        helper.addObserver(observer);

        helper.acceptTrade(Color.BLUE, offer);

        assertTrue(offer.isAccepted());
        assertFalse(otherOffer.isAccepted());
        assertEquals(Set.of(offer, otherOffer), helper.getActiveTradeOffers());
        EasyMock.verify(observer, redInventory, blueInventory);
    }

    @Test
    void AcceptTrade_WhenActorIsNotTheReceiver_RejectsOfferWithoutSideEffects() throws InvalidTradeException {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = new TradeOffer(Color.RED, Color.BLUE,
                List.of(new OreResourceStack(1)), List.of(new BrickResourceStack(1)));
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory whiteInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        EasyMock.replay(redInventory, blueInventory, whiteInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory, Color.WHITE, whiteInventory), turnContext);
        helper.proposeTrade(Color.RED, offer);
        helper.addObserver(observer);

        assertThrows(InvalidTradeException.class, () -> helper.acceptTrade(Color.WHITE, offer));

        assertEquals(Set.of(offer), helper.getActiveTradeOffers());
        assertFalse(offer.isAccepted());
        EasyMock.verify(observer, redInventory, blueInventory, whiteInventory);
    }

    @Test
    void AcceptTrade_WhenOfferIsAnUnregisteredCopy_RejectsOfferWithoutSideEffects() throws InvalidTradeException {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        List<ResourceStack> requested = List.of(new OreResourceStack(1));
        List<ResourceStack> offered = List.of(new BrickResourceStack(1));
        TradeOffer pendingOffer = new TradeOffer(Color.RED, Color.BLUE, requested, offered);
        TradeOffer unregisteredOffer = new TradeOffer(Color.RED, Color.BLUE, requested, offered);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        EasyMock.replay(redInventory, blueInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.proposeTrade(Color.RED, pendingOffer);
        helper.addObserver(observer);

        assertThrows(InvalidTradeException.class, () -> helper.acceptTrade(Color.BLUE, unregisteredOffer));

        assertEquals(Set.of(pendingOffer), helper.getActiveTradeOffers());
        assertFalse(pendingOffer.isAccepted());
        assertFalse(unregisteredOffer.isAccepted());
        EasyMock.verify(observer, redInventory, blueInventory);
    }

    @Test
    void AcceptTrade_AfterTradingCloses_RejectsOfferWithoutSideEffects() throws InvalidTradeException {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = new TradeOffer(Color.RED, Color.BLUE,
                List.of(new OreResourceStack(1)), List.of(new BrickResourceStack(1)));
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        EasyMock.replay(redInventory, blueInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.proposeTrade(Color.RED, offer);
        helper.addObserver(observer);

        EasyMock.reset(turnContext);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(false);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.acceptTrade(Color.BLUE, offer));

        assertEquals(Set.of(offer), helper.getActiveTradeOffers());
        assertFalse(offer.isAccepted());
        EasyMock.verify(observer, redInventory, blueInventory);
    }

    @Test
    void AcceptTrade_WhenOfferIsAlreadyAccepted_RejectsSecondAcceptance() throws InvalidTradeException {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, true);
        TradeOffer offer = new TradeOffer(Color.RED, Color.BLUE,
                List.of(new OreResourceStack(1)), List.of(new BrickResourceStack(1)));
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        observer.update();
        EasyMock.expectLastCall().once();
        EasyMock.replay(redInventory, blueInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.proposeTrade(Color.RED, offer);
        helper.addObserver(observer);
        helper.acceptTrade(Color.BLUE, offer);
        assertTrue(offer.isAccepted());

        assertThrows(InvalidTradeException.class, () -> helper.acceptTrade(Color.BLUE, offer));

        assertEquals(Set.of(offer), helper.getActiveTradeOffers());
        assertTrue(offer.isAccepted());
        EasyMock.verify(observer, redInventory, blueInventory);
    }

    // tests for expireTradeOffers()
    @Test
    void ExpireTradeOffers_WhenNoOffersAreActive_LeavesOfferSetEmpty() {
        TradeTurnContext turnContext = stubTurnContext(Color.RED, false);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        ResourceInventory blueInventory = EasyMock.createMock(ResourceInventory.class);
        Observer observer = EasyMock.createMock(Observer.class);
        EasyMock.replay(redInventory, blueInventory, observer);
        PlayerTradeHelper helper = new PlayerTradeHelper(
                Map.of(Color.RED, redInventory, Color.BLUE, blueInventory), turnContext);
        helper.addObserver(observer);

        helper.expireTradeOffers();

        assertTrue(helper.getActiveTradeOffers().isEmpty());
        EasyMock.verify(observer, redInventory, blueInventory);
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

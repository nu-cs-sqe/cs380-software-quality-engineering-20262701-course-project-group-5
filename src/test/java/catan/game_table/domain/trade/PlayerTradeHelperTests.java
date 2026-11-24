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

        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);

        TradeOffer offer = EasyMock.createMock(TradeOffer.class);
        EasyMock.expect(offer.getProposer()).andStubReturn(Color.RED);
        EasyMock.expect(offer.getReceiver()).andStubReturn(Color.BLUE);
        EasyMock.expect(offer.getOfferedResources()).andStubReturn(List.of(brick));
        EasyMock.expect(offer.getRequestedResources()).andStubReturn(List.of(ore));
        EasyMock.expect(offer.isAccepted()).andStubReturn(false);
        EasyMock.replay(redInventory, blueInventory, turnContext, offer);

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
}

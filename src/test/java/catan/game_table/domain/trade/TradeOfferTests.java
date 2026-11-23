package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.resource.BrickResourceStack;
import catan.game_table.domain.resource.OreResourceStack;
import catan.game_table.domain.resource.ResourceStack;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TradeOfferTests {

    @Test
    void Constructor_WhenInputListsAreCleared_PreservesReadOnlyTradeTerms() {
        ResourceStack ore = EasyMock.createMock(OreResourceStack.class);
        ResourceStack brick = EasyMock.createMock(BrickResourceStack.class);
        EasyMock.replay(ore, brick);
        List<ResourceStack> requestedResources = new ArrayList<>(List.of(ore));
        List<ResourceStack> offeredResources = new ArrayList<>(List.of(brick));
        TradeOffer offer = new TradeOffer(Color.RED, Color.BLUE, requestedResources, offeredResources);

        requestedResources.clear();
        offeredResources.clear();
        List<ResourceStack> savedRequestedResources = offer.getRequestedResources();
        List<ResourceStack> savedOfferedResources = offer.getOfferedResources();

        assertEquals(List.of(ore), savedRequestedResources);
        assertEquals(List.of(brick), savedOfferedResources);
        assertThrows(UnsupportedOperationException.class, savedRequestedResources::clear);
        assertThrows(UnsupportedOperationException.class, savedOfferedResources::clear);
    }

    @Test
    void IsAccepted_WhenOneOfTwoOffersIsMarkedAccepted_ReturnsTrueOnlyForThatOffer() {
        ResourceStack ore = EasyMock.createMock(OreResourceStack.class);
        ResourceStack brick = EasyMock.createMock(BrickResourceStack.class);
        EasyMock.replay(ore, brick);
        List<ResourceStack> requestedResources = List.of(ore);
        List<ResourceStack> offeredResources = List.of(brick);
        TradeOffer firstOffer = new TradeOffer(Color.RED, Color.BLUE, requestedResources, offeredResources);
        TradeOffer secondOffer = new TradeOffer(Color.RED, Color.BLUE, requestedResources, offeredResources);

        assertFalse(firstOffer.isAccepted());
        assertFalse(secondOffer.isAccepted());

        firstOffer.markAccepted();

        assertTrue(firstOffer.isAccepted());
        assertFalse(secondOffer.isAccepted());
    }
}

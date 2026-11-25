package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.bank.Bank;
import catan.game_table.domain.board.GameBoard;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.resource.BrickResourceStack;
import catan.game_table.domain.resource.GrainResourceStack;
import catan.game_table.domain.resource.LumberResourceStack;
import catan.game_table.domain.resource.OreResourceStack;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankTradeHelperTests {

    @Test
    void TradeWithBank_OnOpenTurnWithStockForStandardRate_ExchangesResources()
            throws InvalidTradeException, NotEnoughInventoryException {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        Bank bank = EasyMock.createMock(Bank.class);
        EasyMock.expect(redInventory.hasResources(lumber)).andReturn(true);
        EasyMock.expect(bank.hasResources(ore)).andReturn(true);
        redInventory.removeResources(lumber);
        bank.addResources(lumber);
        bank.removeResources(ore);
        redInventory.addResources(ore);

        GameBoard board = EasyMock.createMock(GameBoard.class);
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(redInventory, bank, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        helper.tradeWithBank(Color.RED, lumber, ore);

        EasyMock.verify(redInventory, bank, board);
    }

    @Test
    void TradeWithBank_WhenPlayerHasLumberHarbor_StillAllowsStandardRate()
            throws InvalidTradeException, NotEnoughInventoryException {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        Bank bank = EasyMock.createMock(Bank.class);
        EasyMock.expect(redInventory.hasResources(lumber)).andReturn(true);
        EasyMock.expect(bank.hasResources(ore)).andReturn(true);
        redInventory.removeResources(lumber);
        bank.addResources(lumber);
        bank.removeResources(ore);
        redInventory.addResources(ore);

        Harbor lumberHarbor = new Harbor(new LumberResourceStack(1));
        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(Set.of(lumberHarbor));
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(redInventory, bank, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        helper.tradeWithBank(Color.RED, lumber, ore);

        EasyMock.verify(redInventory, bank, board);
    }

    @Test
    void TradeWithBank_WhenPlayerHasGenericHarbor_AllowsThreeForOneRate()
            throws InvalidTradeException, NotEnoughInventoryException {
        ResourceStack grain = new GrainResourceStack(3);
        ResourceStack brick = new BrickResourceStack(1);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        Bank bank = EasyMock.createMock(Bank.class);
        EasyMock.expect(redInventory.hasResources(grain)).andReturn(true);
        EasyMock.expect(bank.hasResources(brick)).andReturn(true);
        redInventory.removeResources(grain);
        bank.addResources(grain);
        bank.removeResources(brick);
        redInventory.addResources(brick);

        Harbor genericHarbor = new Harbor();
        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andReturn(Set.of(genericHarbor));
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(redInventory, bank, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        helper.tradeWithBank(Color.RED, grain, brick);

        EasyMock.verify(redInventory, bank, board);
    }

    @Test
    void TradeWithBank_WhenPlayerHasMatchingAndGenericHarbors_AllowsTwoForOneRate()
            throws InvalidTradeException, NotEnoughInventoryException {
        ResourceStack lumber = new LumberResourceStack(2);
        ResourceStack ore = new OreResourceStack(1);
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        Bank bank = EasyMock.createMock(Bank.class);
        EasyMock.expect(redInventory.hasResources(lumber)).andReturn(true);
        EasyMock.expect(bank.hasResources(ore)).andReturn(true);
        redInventory.removeResources(lumber);
        bank.addResources(lumber);
        bank.removeResources(ore);
        redInventory.addResources(ore);

        Harbor genericHarbor = new Harbor();
        Harbor lumberHarbor = new Harbor(new LumberResourceStack(1));
        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andReturn(Set.of(genericHarbor, lumberHarbor));
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(redInventory, bank, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        helper.tradeWithBank(Color.RED, lumber, ore);

        EasyMock.verify(redInventory, bank, board);
    }

    @Test
    void TradeWithBank_WhenPlayerHasOnlyGenericHarbor_RejectsTwoForOneRate() {
        assertStockedTradeRejected(new LumberResourceStack(2), new OreResourceStack(1), Set.of(new Harbor()));
    }

    @Test
    void TradeWithBank_WhenHarborRequiresDifferentResource_RejectsTwoForOneRate() {
        Harbor brickHarbor = new Harbor(new BrickResourceStack(1));
        assertStockedTradeRejected(new LumberResourceStack(2), new OreResourceStack(1), Set.of(brickHarbor));
    }

    @Test
    void TradeWithBank_WhenPlayerHasOnlyLumberHarbor_RejectsThreeForOneRate() {
        Harbor lumberHarbor = new Harbor(new LumberResourceStack(1));
        assertStockedTradeRejected(new LumberResourceStack(3), new OreResourceStack(1), Set.of(lumberHarbor));
    }

    @Test
    void TradeWithBank_WhenPlayerHasNoHarbor_RejectsThreeForOneRate() {
        assertStockedTradeRejected(new LumberResourceStack(3), new OreResourceStack(1), Set.of());
    }

    @Test
    void TradeWithBank_WhenPlayerHasLumberHarbor_RejectsOneForOneRate() {
        Harbor lumberHarbor = new Harbor(new LumberResourceStack(1));
        assertStockedTradeRejected(new LumberResourceStack(1), new OreResourceStack(1), Set.of(lumberHarbor));
    }

    @Test
    void TradeWithBank_WhenPlayerHasNoHarbor_RejectsFiveForOneRate() {
        assertStockedTradeRejected(new LumberResourceStack(5), new OreResourceStack(1), Set.of());
    }

    @Test
    void TradeWithBank_WhenPlayerRequestsZeroResources_RejectsTrade() {
        assertStockedTradeRejected(new LumberResourceStack(4), new OreResourceStack(0), Set.of());
    }

    @Test
    void TradeWithBank_WhenPlayerRequestsTwoResources_RejectsTrade() {
        assertStockedTradeRejected(new LumberResourceStack(4), new OreResourceStack(2), Set.of());
    }

    @Test
    void TradeWithBank_WhenPlayerOffersLumberForLumber_RejectsTrade() {
        assertStockedTradeRejected(new LumberResourceStack(4), new LumberResourceStack(1), Set.of());
    }

    @Test
    void TradeWithBank_WhenPlayerIsOneLumberShort_RejectsTrade() {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        Bank redInventory = new Bank(List.of(new LumberResourceStack(3), new BrickResourceStack(1)));
        Bank bank = new Bank(List.of(ore));

        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(Set.of());
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.tradeWithBank(Color.RED, lumber, ore));

        assertEquals(3, redInventory.getResourceCount(LumberResourceStack.class));
        assertEquals(1, redInventory.getResourceCount(BrickResourceStack.class));
        assertEquals(0, redInventory.getResourceCount(OreResourceStack.class));
        assertEquals(0, bank.getResourceCount(LumberResourceStack.class));
        assertEquals(1, bank.getResourceCount(OreResourceStack.class));
    }

    @Test
    void TradeWithBank_WhenBankHasNoOre_RejectsTrade() {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        Bank redInventory = new Bank(List.of(lumber));
        Bank bank = new Bank(List.of(new BrickResourceStack(1)));

        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(Set.of());
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.tradeWithBank(Color.RED, lumber, ore));

        assertEquals(4, redInventory.getResourceCount(LumberResourceStack.class));
        assertEquals(0, redInventory.getResourceCount(OreResourceStack.class));
        assertEquals(0, bank.getResourceCount(LumberResourceStack.class));
        assertEquals(0, bank.getResourceCount(OreResourceStack.class));
        assertEquals(1, bank.getResourceCount(BrickResourceStack.class));
    }

    @Test
    void TradeWithBank_WhenBlueTradesDuringRedsTurn_RejectsTrade() {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        Bank blueInventory = new Bank(List.of(lumber));
        Bank bank = new Bank(List.of(ore));

        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.BLUE)).andStubReturn(Set.of());
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.BLUE, blueInventory), turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.tradeWithBank(Color.BLUE, lumber, ore));

        assertEquals(4, blueInventory.getResourceCount(LumberResourceStack.class));
        assertEquals(0, blueInventory.getResourceCount(OreResourceStack.class));
        assertEquals(0, bank.getResourceCount(LumberResourceStack.class));
        assertEquals(1, bank.getResourceCount(OreResourceStack.class));
    }

    @Test
    void TradeWithBank_AfterTradingCloses_RejectsTrade() {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        Bank redInventory = new Bank(List.of(lumber));
        Bank bank = new Bank(List.of(ore));

        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(Set.of());
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(false);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.tradeWithBank(Color.RED, lumber, ore));

        assertEquals(4, redInventory.getResourceCount(LumberResourceStack.class));
        assertEquals(0, redInventory.getResourceCount(OreResourceStack.class));
        assertEquals(0, bank.getResourceCount(LumberResourceStack.class));
        assertEquals(1, bank.getResourceCount(OreResourceStack.class));
    }

    @Test
    void TradeWithBank_WhenBankWithdrawalFails_RestoresPlayerResources() throws NotEnoughInventoryException {
        ResourceStack lumber = new LumberResourceStack(4);
        ResourceStack ore = new OreResourceStack(1);
        Bank redInventory = new Bank(List.of(lumber));
        Bank bank = EasyMock.createMock(Bank.class);
        EasyMock.expect(bank.hasResources(ore)).andReturn(true);
        bank.removeResources(ore);
        EasyMock.expectLastCall().andAnswer(() -> {
            assertEquals(0, redInventory.getResourceCount(LumberResourceStack.class));
            throw new NotEnoughInventoryException();
        });

        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(Set.of());
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(bank, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.tradeWithBank(Color.RED, lumber, ore));

        assertEquals(4, redInventory.getResourceCount(LumberResourceStack.class));
        assertEquals(0, redInventory.getResourceCount(OreResourceStack.class));
        EasyMock.verify(bank);
    }

    private void assertStockedTradeRejected(ResourceStack give, ResourceStack receive, Set<Harbor> harbors) {
        ResourceInventory redInventory = EasyMock.createMock(ResourceInventory.class);
        Bank bank = EasyMock.createMock(Bank.class);
        EasyMock.expect(redInventory.hasResources(give)).andStubReturn(true);
        EasyMock.expect(bank.hasResources(receive)).andStubReturn(true);

        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(harbors);
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(redInventory, bank, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        assertThrows(InvalidTradeException.class, () -> helper.tradeWithBank(Color.RED, give, receive));

        EasyMock.verify(redInventory, bank, board);
    }
}

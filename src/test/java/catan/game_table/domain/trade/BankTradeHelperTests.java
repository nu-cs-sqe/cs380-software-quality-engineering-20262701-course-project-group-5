package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.bank.Bank;
import catan.game_table.domain.board.GameBoard;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.resource.LumberResourceStack;
import catan.game_table.domain.resource.OreResourceStack;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class BankTradeHelperTests {

    // tests for tradeWithBank()
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

        Harbor lumberHarbor = EasyMock.createMock(Harbor.class);
        EasyMock.expect(lumberHarbor.getInputTrade()).andStubReturn(new LumberResourceStack(1));
        GameBoard board = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(board.getHarborsFor(Color.RED)).andStubReturn(Set.of(lumberHarbor));
        TradeTurnContext turnContext = EasyMock.createMock(TradeTurnContext.class);
        EasyMock.expect(turnContext.isTradingOpen()).andStubReturn(true);
        EasyMock.expect(turnContext.getActivePlayerColor()).andStubReturn(Color.RED);
        EasyMock.replay(redInventory, bank, lumberHarbor, board, turnContext);
        BankTradeHelper helper = new BankTradeHelper(board, bank, Map.of(Color.RED, redInventory), turnContext);

        helper.tradeWithBank(Color.RED, lumber, ore);

        EasyMock.verify(redInventory, bank, board, lumberHarbor);
    }
}

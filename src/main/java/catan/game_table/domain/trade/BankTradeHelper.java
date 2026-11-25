package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.bank.Bank;
import catan.game_table.domain.board.GameBoard;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;

import java.util.Map;

public class BankTradeHelper {

    private static final int STANDARD_TRADE_RATE = 4;

    private final GameBoard board;
    private final Bank bank;
    private final Map<Color, ResourceInventory> playerInventories;

    public BankTradeHelper(GameBoard board, Bank bank, Map<Color, ResourceInventory> playerInventories,
                           TradeTurnContext turnContext) {
        this.board = board;
        this.bank = bank;
        this.playerInventories = Map.copyOf(playerInventories);
    }

    public void tradeWithBank(Color actor, ResourceStack give, ResourceStack receive) throws InvalidTradeException {
        if (!requestsSingleDifferentResource(give, receive) || !hasAvailableTradeRate(actor, give)) {
            throw new InvalidTradeException();
        }
        ResourceInventory playerInventory = playerInventories.get(actor);
        if (!playerInventory.hasResources(give) || !bank.hasResources(receive)) {
            throw new InvalidTradeException();
        }

        try {
            withdrawTradeResources(playerInventory, give, receive);
        } catch (NotEnoughInventoryException cause) {
            InvalidTradeException failure = new InvalidTradeException();
            failure.initCause(cause);
            throw failure;
        }
        bank.addResources(give);
        playerInventory.addResources(receive);
    }

    private boolean requestsSingleDifferentResource(ResourceStack give, ResourceStack receive) {
        return receive.getSize() == 1 && give.getClass() != receive.getClass();
    }

    private boolean hasAvailableTradeRate(Color actor, ResourceStack give) {
        return switch (give.getSize()) {
            case STANDARD_TRADE_RATE -> true;
            case Harbor.GENERIC_TRADE_RATE -> hasGenericHarbor(actor);
            case Harbor.RESOURCE_TRADE_RATE -> hasMatchingHarbor(actor, give);
            default -> false;
        };
    }

    private boolean hasGenericHarbor(Color actor) {
        return board.getHarborsFor(actor).stream()
                .anyMatch(harbor -> harbor.getTradeRate() == Harbor.GENERIC_TRADE_RATE);
    }

    private boolean hasMatchingHarbor(Color actor, ResourceStack give) {
        return board.getHarborsFor(actor).stream()
                .flatMap(harbor -> harbor.getInputTrade().stream())
                .anyMatch(inputTrade -> inputTrade.getClass() == give.getClass());
    }

    private void withdrawTradeResources(ResourceInventory playerInventory, ResourceStack give, ResourceStack receive)
            throws NotEnoughInventoryException {
        playerInventory.removeResources(give);
        try {
            bank.removeResources(receive);
        } catch (NotEnoughInventoryException cause) {
            playerInventory.addResources(give);
            throw cause;
        }
    }
}

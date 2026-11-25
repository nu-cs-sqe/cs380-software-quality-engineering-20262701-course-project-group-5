package catan.game_table.domain.trade;

import catan.game_table.domain.Color;
import catan.game_table.domain.bank.Bank;
import catan.game_table.domain.board.GameBoard;
import catan.game_table.domain.exception.InvalidTradeException;
import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;

import java.util.Map;

public class BankTradeHelper {

    private static final int GENERIC_HARBOR_RATE = 3;
    private static final int RESOURCE_HARBOR_RATE = 2;

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
        if (give.getSize() == GENERIC_HARBOR_RATE && !hasGenericHarbor(actor)) {
            throw new InvalidTradeException();
        }
        if (give.getSize() == RESOURCE_HARBOR_RATE && !hasMatchingHarbor(actor, give)) {
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

    private boolean hasGenericHarbor(Color actor) {
        return board.getHarborsFor(actor).stream().anyMatch(harbor -> harbor.getInputTrade() == null);
    }

    private boolean hasMatchingHarbor(Color actor, ResourceStack give) {
        return board.getHarborsFor(actor).stream().anyMatch(harbor -> {
            ResourceStack harborInput = harbor.getInputTrade();
            return harborInput != null && harborInput.getClass() == give.getClass();
        });
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

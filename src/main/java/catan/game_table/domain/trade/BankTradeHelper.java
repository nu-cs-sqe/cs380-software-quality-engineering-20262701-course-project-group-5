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

    private final Bank bank;
    private final Map<Color, ResourceInventory> playerInventories;

    public BankTradeHelper(GameBoard board, Bank bank, Map<Color, ResourceInventory> playerInventories,
                           TradeTurnContext turnContext) {
        this.bank = bank;
        this.playerInventories = Map.copyOf(playerInventories);
    }

    public void tradeWithBank(Color actor, ResourceStack give, ResourceStack receive) throws InvalidTradeException {
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

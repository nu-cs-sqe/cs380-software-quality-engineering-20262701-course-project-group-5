package catan.game_table.domain.piece;

import catan.game_table.domain.resource.ResourceStack;

import java.util.Optional;

public class Harbor extends Piece {
    public static final int GENERIC_TRADE_RATE = 3;
    public static final int RESOURCE_TRADE_RATE = 2;

    private final int tradeRate;
    private final Optional<ResourceStack> inputTrade;

    public Harbor() {
        tradeRate = GENERIC_TRADE_RATE;
        inputTrade = Optional.empty();
    }

    public Harbor(ResourceStack inputTrade) {
        tradeRate = RESOURCE_TRADE_RATE;
        this.inputTrade = Optional.of(inputTrade);
    }

    public int getTradeRate() {
        return tradeRate;
    }

    public Optional<ResourceStack> getInputTrade() {
        return inputTrade;
    }
}

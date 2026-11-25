package catan.game_table.domain.piece;

import catan.game_table.domain.resource.ResourceStack;

public class Harbor extends Piece {
    private final ResourceStack inputTrade;

    public Harbor() {
        this(null);
    }

    public Harbor(ResourceStack inputTrade) {
        this.inputTrade = inputTrade;
    }

    public ResourceStack getInputTrade() {
        return inputTrade;
    }
}

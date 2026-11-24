package catan.game_table.domain.trade;

import catan.game_table.domain.Color;

public interface TradeTurnContext {
    Color getActivePlayerColor();

    boolean isTradingOpen();
}

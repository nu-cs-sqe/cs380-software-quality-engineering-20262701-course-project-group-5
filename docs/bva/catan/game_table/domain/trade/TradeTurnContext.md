# Interface TradeTurnContext

Classpath: `catan.game_table.domain.trade.TradeTurnContext`

Implementing class in the design: `GameTableController`

## `public interface TradeTurnContext`

Provides the turn state used by `PlayerTradeHelper` and `BankTradeHelper`.

### `public Color getActivePlayerColor()`

Returns the color of the player whose turn it is.

### `public boolean isTradingOpen()`

Returns `true` during the regular action phase after the dice roll and any required resolution, and `false` otherwise.

## Boundary-Value Analysis

Test when trading opens and closes in `GameTableController`.

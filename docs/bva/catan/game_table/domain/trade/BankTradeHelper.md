# Class BankTradeHelper

Classpath: `catan.game_table.domain.trade.BankTradeHelper`

## `public class BankTradeHelper`

Executes immediate exchanges between the active player and the bank. Callers supply valid arguments. `GameBoard.getHarborsFor` supplies the player's eligible harbors.

### `public BankTradeHelper(GameBoard board, Bank bank, Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext)`

Uses the supplied board, bank, player inventories, and turn context for bank exchanges.

### `public void tradeWithBank(Color actor, ResourceStack give, ResourceStack receive) throws InvalidTradeException`

Only the active player may trade with the bank while trading is open. The player gives four cards of one type at the standard rate, three with a generic harbor, or two with a harbor matching the resource given. The bank gives one card of a different type. Check both inventories before transferring cards.

### `private boolean isValidBankTrade(Color actor, ResourceStack give, ResourceStack receive)`

Checks that the requested exchange gives the player one card of a different type and that the offered quantity matches a rate available to `actor`. Tested indirectly through `tradeWithBank()`.

## Boundary-Value Analysis

### Test: `public void tradeWithBank(Color actor, ResourceStack give, ResourceStack receive) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `give`, and `receive`, with trading status, active player, accessible harbors, and both inventories. | Transfers cards in both directions or rejects the exchange without changing either inventory. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| actor | Cases |
| cards given | Interval `[2, 4]` |
| cards received | Interval `[1, 1]` |
| resource types given and received | Cases |
| harbor access | Cases |
| player and bank stock | Pairs of intervals, pairs of counts |

**Operations:**
Searching the available harbors for one that permits the requested discount.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| actor | active player, other player |
| cards given | 1 (just below), 2, 3 (special case due to harbor), 4, 5 (just above) |
| cards received | 0, 1, 2 |
| resource types given and received | different, same |
| harbor access | none, generic, matching specific, nonmatching specific |
| player and bank stock | exact stock, surplus, player short by one, bank short by one |

**Step 4: Each choice** — Pair discounted rates with the corresponding harbor cases. Keep other inputs valid when testing a single reason for rejection.

|              | Inputs                                                                                                                         | Expected output                                                                                    | Implemented? |
|--------------|--------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | During Red's open turn, Red offers 4 lumber for 1 ore without a harbor, and both sides have exactly enough stock.              | Red gives the bank 4 lumber and receives 1 ore, leaving Red with 0 lumber and the bank with 0 ore. | :x:          |
| Test Case 2  | During Red's open turn, Red offers 4 lumber for 1 ore with access to a lumber harbor, 5 lumber in hand, and 2 ore in the bank. | Red gives the bank 4 lumber and receives 1 ore, retaining 1 lumber and leaving 1 ore in the bank.  | :x:          |
| Test Case 3  | During Red's open turn, Red offers 3 lumber for 1 ore with only a generic harbor and enough stock.                             | Red gives the bank 3 lumber and receives 1 ore.                                                    | :x:          |
| Test Case 4  | During Red's open turn, Red offers 2 lumber for 1 ore with both generic and lumber harbors and enough stock.                   | Red gives the bank 2 lumber and receives 1 ore.                                                    | :x:          |
| Test Case 5  | During Red's open turn, Red offers 2 lumber for 1 ore with only a generic harbor and enough stock.                             | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 6  | During Red's open turn, Red offers 2 lumber for 1 ore with only a brick harbor and enough stock.                               | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 7  | During Red's open turn, Red offers 3 lumber for 1 ore with only a lumber harbor and enough stock.                              | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 8  | During Red's open turn, Red offers 3 lumber for 1 ore without a harbor and with enough stock.                                  | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 9  | During Red's open turn, Red offers 1 lumber for 1 ore with a lumber harbor and enough stock.                                   | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 10 | During Red's open turn, Red offers 5 lumber for 1 ore without a harbor and with enough stock.                                  | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 11 | During Red's open turn, Red offers 4 lumber for an ore stack of size 0 without a harbor and with enough stock.                 | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 12 | During Red's open turn, Red offers 4 lumber for 2 ore without a harbor and with enough stock.                                  | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 13 | During Red's open turn, Red offers 4 lumber for 1 lumber without a harbor and with enough stock.                               | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 14 | During Red's open turn, Red offers 4 lumber for 1 ore without a harbor, with only 3 lumber in hand and 1 ore in the bank.      | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 15 | During Red's open turn, Red offers 4 lumber for 1 ore without a harbor, with 4 lumber in hand and 0 ore in the bank.           | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 16 | During Red's open turn, Blue attempts a stocked 4 lumber for 1 ore bank trade without a harbor.                                | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |
| Test Case 17 | After trading closes on Red's turn, Red attempts a stocked 4 lumber for 1 ore bank trade without a harbor.                     | Throws `InvalidTradeException` without changing either inventory.                                  | :x:          |

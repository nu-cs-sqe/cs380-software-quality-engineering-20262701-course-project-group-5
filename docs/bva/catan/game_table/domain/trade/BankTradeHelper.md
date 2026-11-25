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
| who requests the bank trade | Cases |
| cards given | Interval `[2, 4]` |
| cards received | Interval `[1, 1]` |
| resource types given and received | Cases |
| harbor access | Cases |
| player stock of the offered resource and bank stock of the requested resource | Two counts, each from 0 to 19 with the standard resource supply |

**Operations:**
Searching the available harbors for one that permits the requested discount.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| who requests the bank trade | player whose turn it is, a player trading during someone else's turn |
| cards given | 1 , 2, 3 (special case due to harbor), 4, 5 |
| cards received | 0, 1, 2 |
| resource types given and received | the player offers and requests different resources, the player offers and requests the same resource |
| harbor access | no harbor, generic 3-for-1 harbor, 2-for-1 harbor for the offered resource, 2-for-1 harbor for another resource |
| player and bank stock | For a 4-for-1 trade, stocks `(4, 1)` are exactly sufficient, `(5, 2)` leave a surplus, `(3, 1)` isolate the player shortage, and `(4, 0)` isolate the bank shortage. For successful 3-for-1 and 2-for-1 trades, use exact stock `(3, 1)` and `(2, 1)`. |

For a fixed offer, each stock count is checked against its own required quantity. The two shortage cases keep the other side stocked so each check can independently reject the trade. With stocks `(0, 0)` either shortage can explain rejection, and `(19, 19)` exercises the same stock checks as the surplus case. 

**Step 4: Each choice**

|              | Inputs                                                                                                                                                 | Expected output                                                                                                | Implemented?       |
|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1  | During Red's open turn, Red offers 4 lumber for 1 ore without a harbor. Red has 4 lumber and 0 ore, and the bank has 0 lumber and 1 ore.               | Red ends with 0 lumber and 1 ore. The bank ends with 4 lumber and 0 ore.                                       | :white_check_mark: |
| Test Case 2  | During Red's open turn, Red offers 4 lumber for 1 ore with access to a lumber harbor. Red has 5 lumber and 1 ore, and the bank has 1 lumber and 2 ore. | Red ends with 1 lumber and 2 ore. The bank ends with 5 lumber and 1 ore.                                       | :white_check_mark: |
| Test Case 3  | During Red's open turn, Red offers 3 grain for 1 brick with only a generic harbor, exactly 3 grain in hand, and exactly 1 brick in the bank.           | Red's grain decreases by 3 and brick increases by 1. The bank's grain increases by 3 and brick decreases by 1. | :white_check_mark: |
| Test Case 4  | During Red's open turn, Red offers 2 lumber for 1 ore with both generic and lumber harbors, exactly 2 lumber in hand, and exactly 1 ore in the bank.   | Red's lumber decreases by 2 and ore increases by 1. The bank's lumber increases by 2 and ore decreases by 1.   | :white_check_mark: |
| Test Case 5  | During Red's open turn, Red offers 2 lumber for 1 ore with only a generic harbor and enough stock.                                                     | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 6  | During Red's open turn, Red offers 2 lumber for 1 ore with only a brick harbor and enough stock.                                                       | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 7  | During Red's open turn, Red offers 3 lumber for 1 ore with only a lumber harbor and enough stock.                                                      | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 8  | During Red's open turn, Red offers 3 lumber for 1 ore without a harbor and with enough stock.                                                          | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 9  | During Red's open turn, Red offers 1 lumber for 1 ore with a lumber harbor and enough stock.                                                           | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 10 | During Red's open turn, Red offers 5 lumber for 1 ore without a harbor and with enough stock.                                                          | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 11 | During Red's open turn, Red offers 4 lumber for an ore stack of size 0 without a harbor and with enough stock.                                         | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 12 | During Red's open turn, Red offers 4 lumber for 2 ore without a harbor and with enough stock.                                                          | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 13 | During Red's open turn, Red offers 4 lumber for 1 lumber without a harbor and with enough stock.                                                       | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 14 | During Red's open turn, Red offers 4 lumber for 1 ore without a harbor. Red has 3 lumber and 1 brick, and the bank has 1 ore.                          | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 15 | During Red's open turn, Red offers 4 lumber for 1 ore without a harbor. Red has 4 lumber, and the bank has 0 ore and 1 brick.                          | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 16 | During Red's open turn, Blue attempts a stocked 4 lumber for 1 ore bank trade without a harbor.                                                        | Throws `InvalidTradeException` without changing either inventory.                                              | :white_check_mark: |
| Test Case 17 | After trading closes on Red's turn, Red attempts a stocked 4 lumber for 1 ore bank trade without a harbor.                                             | Throws `InvalidTradeException` without changing either inventory.                                              | :x:                |

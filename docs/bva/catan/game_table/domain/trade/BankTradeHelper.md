# Class BankTradeHelper

Classpath: `catan.game_table.domain.trade.BankTradeHelper`

## `public class BankTradeHelper`

Executes immediate exchanges between the active player and the bank. `GameBoard.getHarborsFor` supplies the player's eligible harbors.

### `public BankTradeHelper(GameBoard board, Bank bank, Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext)`

Uses the supplied board, bank, player inventories, and turn context for bank exchanges.

### `public void tradeWithBank(Color actor, ResourceStack give, ResourceStack receive)`

Only the active player may trade with the bank while trading is open. The player gives four cards of one type at the standard rate, three with a generic harbor, or two with a harbor matching the resource given. The bank gives one card of a different type. Check both inventories before transferring cards.

## Boundary-Value Analysis

Assume valid arguments and nonnegative resource stack sizes. **Rejected** means `IllegalStateException` with both inventories unchanged. All cases are planned (`:x:`).

Unless stated otherwise, Red is active, trading is open, and Red offers 4 lumber for 1 ore without a harbor. Both inventories have enough cards for the stated exchange. A successful trade updates both inventories by the stated amounts.

### Test: `public void tradeWithBank(Color actor, ResourceStack give, ResourceStack receive)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `give`, `receive`. State: trading status, active player, accessible harbors, and both inventories. | No return value. Transfers the specified cards in both directions or rejects the exchange without changing either inventory. |

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
| cards given | 1 (just below), 2, 3, 4, 5 (just above) |
| cards received | 0, 1, 2 |
| resource types given and received | different, same |
| harbor access | none, generic, matching specific, nonmatching specific |
| player and bank stock | exact stock, surplus, player short by one, bank short by one |

**Step 4: Each choice** — Pair discounted rates with the corresponding harbor cases. Keep other inputs valid when testing a single reason for rejection.

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-40 | Red has exactly 4 lumber and the bank has exactly 1 ore. Red trades 4 lumber for 1 ore without a harbor. | Exchange succeeds. Red's lumber and the bank's ore both reach 0. | :x: |
| Test Case TH-41 | Red has 5 lumber and can use a lumber harbor. The bank has 2 ore. Red requests a 4:1 trade. | Exchange succeeds at 4:1. Red keeps 1 lumber and the bank keeps 1 ore. | :x: |
| Test Case TH-42 | Give 3 lumber with a generic harbor. | 3:1 exchange. | :x: |
| Test Case TH-43 | Give 2 lumber with access to a generic and a lumber harbor. | 2:1 exchange. | :x: |
| Test Case TH-44 | Give 2 lumber with only a generic harbor. | Rejected. | :x: |
| Test Case TH-45 | Give 2 lumber with only a brick harbor. | Rejected. | :x: |
| Test Case TH-46 | Give 3 lumber with only a lumber harbor. | Rejected. | :x: |
| Test Case TH-47 | Give 3 lumber with no accessible harbor. | Rejected. | :x: |
| Test Case TH-48 | Give 1 lumber with access to a lumber harbor. | Rejected. | :x: |
| Test Case TH-49 | Give 5 lumber without a harbor. | Rejected. | :x: |
| Test Case TH-50 | Request 0 ore. | Rejected. | :x: |
| Test Case TH-51 | Request 2 ore. | Rejected. | :x: |
| Test Case TH-52 | Request 1 lumber in exchange for 4 lumber. | Rejected. | :x: |
| Test Case TH-53 | Red has only 3 lumber for a 4:1 trade. | Rejected. | :x: |
| Test Case TH-54 | The bank has 0 ore for an otherwise valid trade. | Rejected. Red keeps all 4 lumber. | :x: |
| Test Case TH-55 | Blue requests an otherwise valid bank trade during Red's action phase. | Rejected. | :x: |
| Test Case TH-56 | Red requests an otherwise valid trade while trading is closed. | Rejected. | :x: |

# Class PlayerTradeHelper

Classpath: `catan.game_table.domain.trade.PlayerTradeHelper`

## `public class PlayerTradeHelper extends Observed`

Manages domestic offers using the current turn context and player inventories. Callers supply valid arguments. Changes to offer membership or agreement notify observers after the new state is visible.

### `public PlayerTradeHelper(Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext)`

Creates a helper with no active offers. Game setup supplies the player inventories and turn context.

### `public Set<TradeOffer> getActiveTradeOffers()`

Returns a read-only set of the current unanswered and accepted offers. The caller cannot change offer membership through this set.

### `public void proposeTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

Registers an unanswered offer from the active player to a different configured player while trading is open. Each side gives resources in positive quantities, and no resource type appears on both sides. Proposing does not reserve or transfer cards.

### `public void counterTrade(Color actor, TradeOffer original, TradeOffer counter) throws InvalidTradeException`

While trading is open, the addressed player may replace an unanswered active offer with a fresh counteroffer. The participants reverse roles and the new offer starts unanswered. The same resource-term rules apply.

### `public void acceptTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

While trading is open, the addressed player records agreement to an active offer. The accepted offer remains active until finalization or expiry, and inventories stay as they were.

### `public void refuseTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

While trading is open, the addressed player may remove an unanswered active offer by refusing it. Inventories stay as they were.

### `public void conductTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

Only the active player may finalize an active offer involving them while trading is open. A proposal from the active player must already be accepted. When the active player is the receiver of a counteroffer, finalization can record agreement and complete the exchange in one call. Check both sides' current stocks before moving any cards. Add together stacks of the same type on the same side. On success, move the resources in both directions and remove that offer.

### `public void expireTradeOffers()`

At turn end, clear all outstanding offers, including accepted ones. The controller may call this after trading closes. Inventories stay as they were.

### `private boolean isValidPlayerTrade(TradeOffer trade)`

Checks that the offer involves two distinct configured players, each side gives resources in positive quantities, and no resource type appears on both sides. Tested indirectly through `proposeTrade()` and `counterTrade()`.

## Boundary-Value Analysis

### Test: `public Set<TradeOffer> getActiveTradeOffers()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| Active-offer state, with no arguments. | Read-only set of active offers. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| active offers | Collections |
| offer status | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| active offers | empty, one offer, multiple offers |
| offer status | unanswered, accepted and awaiting finalization |

**Step 4: Each choice**

|             | Inputs                                                                                                      | Expected output                                                                                  | Implemented? |
|-------------|-------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Read the active offers when none exist.                                                                     | Returns an empty set.                                                                            | :x:          |
| Test Case 2 | Read the set with one accepted offer, add an unanswered offer, then read again and try to clear the result. | The first read shows one offer, the second shows both, and the clear attempt leaves both active. | :x:          |

### Test: `public void proposeTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with the active player, trading status, and configured players. | Registers or rejects the offer without moving cards. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| actor relative to active player and offer | Cases |
| receiver | Cases |
| offered and requested resources | Pairs of collections |

**Operations:**
Appending an offer to the active set.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| actor relative to active player and offer | active proposer, non-active proposer, active player named as receiver |
| receiver | different configured player, proposer, player color without an inventory |
| offered and requested resources | one stack on each side, offers cards but requests none, requests cards but offers none, both sides include the same resource type, a stack of size 0 |

**Step 4: Each choice**

|              | Inputs                                                                                                  | Expected output                                                                        | Implemented? |
|--------------|---------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|--------------|
| Test Case 1  | On Red's open turn, with no active offers and no brick in hand, Red offers Blue 1 brick for 1 ore.      | The helper registers the unanswered offer and notifies observers without moving cards. | :x:          |
| Test Case 2  | Red offers Blue 1 brick for 1 ore after trading closes on Red's turn.                                   | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 3  | Blue starts an offer to trade 1 ore for 2 brick with Red during Red's open turn.                        | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 4  | During Red's open turn, Red submits an offer naming Blue as the proposer of 1 ore for 2 brick from Red. | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 5  | During Red's open turn, Red offers to trade 1 brick for 1 ore with themself.                            | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 6  | During Red's open turn, Red offers 1 brick for 1 ore to Orange, who has no inventory entry.             | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 7  | During Red's open turn, Red offers Blue 1 brick and asks for no cards.                                  | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 8  | During Red's open turn, Red asks Blue for 1 ore and offers no cards.                                    | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 9  | During Red's open turn, Red offers Blue a zero-size brick stack for 1 ore.                              | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |
| Test Case 10 | During Red's open turn, Red offers Blue 1 brick and 1 lumber for 1 brick and 1 ore.                     | Throws `InvalidTradeException` without changing offers or inventories.                 | :x:          |

### Test: `public void counterTrade(Color actor, TradeOffer original, TradeOffer counter) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `original`, and `counter`, with trading and original offer status. | Replaces the original with the unanswered counteroffer or rejects the request, with inventories unchanged. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| original offer status | Cases |
| actor role | Cases |
| counteroffer parties and terms | Cases |

**Operations:**
Searching for the original and replacing that entry.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| original offer status | unanswered and active, no longer active, accepted |
| actor role | original receiver, another player |
| counteroffer parties and terms | valid reversed roles, wrong receiver, wrong proposer, requests cards but offers none |

**Step 4: Each choice**

|              | Inputs                                                                                                                          | Expected output                                                                                                                      | Implemented? |
|--------------|---------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | During Red's open turn, Blue counters an unanswered offer from Red, then Red counters back while another offer is pending.      | Each unanswered counteroffer replaces its predecessor and notifies observers, leaving the unrelated offer and inventories unchanged. | :x:          |
| Test Case 2  | During Red's open turn, Blue counters a Red-to-Blue offer that is absent from the active set.                                   | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |
| Test Case 3  | During Red's open turn, White tries to counter an unanswered offer from Red to Blue.                                            | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |
| Test Case 4  | During Red's open turn, Blue attempts to counter an unanswered offer from Red with a counteroffer addressed to White.           | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |
| Test Case 5  | During Red's open turn, Blue attempts to counter an unanswered offer from Red with a counteroffer naming White as its proposer. | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |
| Test Case 6  | During Red's open turn, Blue counters an unanswered offer from Red by asking for 2 brick and offering no cards.                 | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |
| Test Case 7  | Blue counters an unanswered offer from Red after trading closes on Red's turn.                                                  | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |
| Test Case 8  | During Red's open turn, Blue counters an active offer from Red that Blue has already accepted.                                  | Throws `InvalidTradeException` without changing offers or inventories.                                                               | :x:          |

### Test: `public void acceptTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with trading status and whether the offer is active. | Records agreement or rejects the request. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| actor role | Cases |
| offer membership | Cases |

**Operations:**
Searching for the offer.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| actor role | receiver, another player |
| offer membership | registered offer, distinct unregistered offer |

**Step 4: Each choice**

|              | Inputs                                                                                                                                  | Expected output                                                                                                  | Implemented? |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | During Red's open turn, Blue accepts an unanswered offer that requests 1 ore from Blue, who holds none, while another offer is pending. | The addressed offer becomes accepted, both remain active, and observers see the change without any cards moving. | :x:          |
| Test Case 2  | During Red's open turn, White tries to accept an unanswered offer from Red to Blue.                                                     | Throws `InvalidTradeException` without changing offers or inventories.                                           | :x:          |
| Test Case 3  | During Red's open turn, Blue tries to accept an unregistered copy of a pending offer from Red.                                          | Throws `InvalidTradeException` without changing offers or inventories.                                           | :x:          |
| Test Case 4  | Blue tries to accept an unanswered offer from Red after trading closes.                                                                 | Throws `InvalidTradeException` without changing offers or inventories.                                           | :x:          |

### Test: `public void refuseTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with trading and offer status. | Removes the offer or rejects the request. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| actor role | Cases |
| offer status | Cases |

**Operations:**
Searching for and deleting the offer.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| actor role | receiver, another player |
| offer status | unanswered and active, no longer active, accepted |

**Step 4: Each choice**

|              | Inputs                                                                                                | Expected output                                                                          | Implemented? |
|--------------|-------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | During Red's open turn, Blue refuses an unanswered offer from Red while another offer is pending.     | Only the unrelated offer remains, and observers see the change without any cards moving. | :x:          |
| Test Case 2  | During Red's open turn, Blue tries to refuse a Red-to-Blue offer that was already removed.            | Throws `InvalidTradeException` without changing offers or inventories.                   | :x:          |
| Test Case 3  | During Red's open turn, White tries to refuse an unanswered offer from Red to Blue.                   | Throws `InvalidTradeException` without changing offers or inventories.                   | :x:          |
| Test Case 4  | Blue tries to refuse an unanswered offer from Red after trading closes.                               | Throws `InvalidTradeException` without changing offers or inventories.                   | :x:          |
| Test Case 5  | During Red's open turn, Blue tries to refuse an active offer from Red that Blue has already accepted. | Throws `InvalidTradeException` without changing offers or inventories.                   | :x:          |

### Test: `public void conductTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with trading status, active player, offer status, and both inventories. | Transfers cards and removes the offer or rejects the exchange without changing offers or inventories. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| actor and agreement | Cases |
| offer membership | Cases |
| both inventories | Pairs of intervals, pairs of counts |
| resource lists | Pairs of collections |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| actor and agreement | active proposer with or without agreement, active counteroffer receiver, non-active receiver |
| offer membership | active, already completed |
| both inventories | each side at or above the required total, proposer short by one, receiver short by one |
| resource lists | one stack per side, multiple stacks with repeated or distinct resource types |

**Step 4: Each choice**

|              | Inputs                                                                                                                                                                         | Expected output                                                                                                                  | Implemented? |
|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | During Red's open turn, Red finalizes an accepted trade of `[1 brick, 1 brick]` for `[1 ore, 1 grain]` with enough stock, 2 ore held by Blue, and another offer pending.       | Red gives Blue 2 brick and receives 1 ore and 1 grain, Blue keeps 1 ore, and observers see only the unrelated offer.             | :x:          |
| Test Case 2  | During Red's open turn, Red tries to finalize Blue's unanswered counteroffer of `[1 ore, 1 ore]` for 2 brick when Blue has only 1 ore and Red has 2 brick.                     | Throws `InvalidTradeException` without changing offers or inventories.                                                           | :x:          |
| Test Case 3  | During Red's open turn, Red tries to finalize an accepted trade of `[1 brick, 1 brick]` for `[1 ore, 1 grain]` when both players have every required card except Blue's grain. | Throws `InvalidTradeException` without changing offers or inventories.                                                           | :x:          |
| Test Case 4  | During Red's open turn, Red tries to finalize a stocked trade that Blue has yet to accept.                                                                                     | Throws `InvalidTradeException` without changing offers or inventories.                                                           | :x:          |
| Test Case 5  | During Red's open turn, Red finalizes Blue's unanswered counteroffer to give Red 1 ore for 2 brick with enough stock on both sides.                                            | Blue gives Red 1 ore, Red gives Blue 2 brick, and the counteroffer is removed.                                                   | :x:          |
| Test Case 6  | During Red's open turn, Blue tries to finalize a stocked, accepted offer from Red.                                                                                             | Throws `InvalidTradeException` without changing offers or inventories.                                                           | :x:          |
| Test Case 7  | During Red's open turn, Red twice tries to finalize their accepted offer to give Blue 1 brick for 1 ore, starting with enough stock for two exchanges.                         | The first call transfers the cards and removes the offer, and the second throws `InvalidTradeException` without further changes. | :x:          |
| Test Case 8  | After trading closes on Red's turn, Red tries to finalize a stocked, accepted offer to Blue.                                                                                   | Throws `InvalidTradeException` without changing offers or inventories.                                                           | :x:          |

### Test: `public void expireTradeOffers()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| Outstanding offers at turn end, with no arguments. | The active-offer set becomes empty. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| active-offer set | Collections |
| offer status | Cases |

**Operations:**
Deleting all entries from the set.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| active-offer set | empty, multiple offers |
| offer status | unanswered, accepted and awaiting finalization |

**Step 4: Each choice**

|              | Inputs                                                                                     | Expected output                                                                    | Implemented? |
|--------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|--------------|
| Test Case 1  | Expire offers at turn end with no active offers.                                           | The active-offer set stays empty.                                                  | :x:          |
| Test Case 2  | Expire offers after trading closes at turn end with one accepted and one unanswered offer. | Both offers are removed, and observers see the empty set without any cards moving. | :x:          |

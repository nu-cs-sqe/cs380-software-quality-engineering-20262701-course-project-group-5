# Class PlayerTradeHelper

Classpath: `catan.game_table.domain.trade.PlayerTradeHelper`

## `public class PlayerTradeHelper extends Observed`

Manages domestic offers using the current turn context and player inventories. Successful changes to offer membership or agreement notify observers after the new state is visible.

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

## Boundary-Value Analysis

Assume valid arguments and nonnegative resource stack sizes. **Rejected** means the checked `InvalidTradeException` is thrown and the offer set, agreement, and inventories stay unchanged. All cases are planned (`:x:`).

Unless stated otherwise, Red is active, trading is open, and Red, Blue, and White have enough stock. `A` is Red's offer to give Blue 1 brick for 1 ore. `B` is a separate Red-to-White offer to give 1 brick for 1 grain. `C` is Blue's counteroffer to give Red 1 ore for 2 brick. Offers start unanswered unless the case says otherwise.

Use the catalog's Each choice strategy for observable behavior. Rows marked “See” share an existing test case, as in the Lab 03 approach of observing one method through another.

### Test: `public Set<TradeOffer> getActiveTradeOffers()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| No arguments. State: active offers. | Read-only set of active offers. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-01 | Read the active offers on a new helper. | Empty set. | :x: |
| See TH-03 | Read after proposing the first offer. | Exactly `{A}`. | :x: |
| Test Case TH-02 | `A` is accepted and `B` is unanswered. Read the set and attempt to clear it. | Returns exactly `{A, B}`. The caller cannot remove offers by clearing the returned set. | :x: |

### Test: `public void proposeTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `trade`. State: active player, trading status, configured participants. | No return value. Registers the offer or rejects the proposal. Inventories stay as they were. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-03 | Red proposes `A` to Blue with an empty offer set and 0 brick in Red's inventory. | `{A}` is active and unanswered. No cards move. An observer can read the new offer. | :x: |
| Test Case TH-04 | Trading has closed. Red proposes `A`. | Rejected. | :x: |
| Test Case TH-05 | Blue opens a new Blue-to-Red offer during Red's action phase. | Rejected. | :x: |
| Test Case TH-06 | Red submits a new offer that names Blue as proposer and Red as receiver. | Rejected. | :x: |
| Test Case TH-07 | Red proposes a trade with Red. | Rejected. | :x: |
| Test Case TH-08 | Red proposes `A`'s terms to Orange, who has no inventory entry. | Rejected. | :x: |
| Test Case TH-09 | Red offers 1 brick and requests no cards. | Rejected. | :x: |
| Test Case TH-10 | Red offers no cards and requests 1 ore. | Rejected. | :x: |
| Test Case TH-11 | Red offers a brick stack of size 0 for 1 ore. | Rejected. | :x: |
| Test Case TH-12 | Red offers 1 brick and 1 lumber for 1 brick and 1 ore. | Rejected. | :x: |

### Test: `public void counterTrade(Color actor, TradeOffer original, TradeOffer counter) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `original`, `counter`. State: trading status and original offer status. | No return value. Replaces the original with the unanswered counteroffer or rejects the request. Inventories stay as they were. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-13 | `A` and `B` are unanswered. Blue counters `A` with `C`. Red then counters `C` by offering Blue 3 brick for 1 ore. | After Blue's counter, the set is `{B, C}`. After Red's counter, only `B` and Red's new offer remain. Both replacements start unanswered, and observers can read the new set after each change. | :x: |
| Test Case TH-14 | Only `B` is active. Blue tries to counter `A` with `C`. | Rejected. | :x: |
| Test Case TH-15 | `A` is unanswered. White tries to counter it. | Rejected. | :x: |
| Test Case TH-16 | Blue counters `A` with a Blue-to-White offer. | Rejected. | :x: |
| Test Case TH-17 | Blue counters `A` with a White-to-Red offer. | Rejected. | :x: |
| Test Case TH-18 | Blue counters `A` by asking Red for 2 brick and offering no cards in return. | Rejected. | :x: |
| Test Case TH-19 | `A` is unanswered and trading is closed. Blue counters with `C`. | Rejected. | :x: |
| Test Case TH-20 | Blue tries to counter `A` after accepting it. | Rejected. | :x: |

### Test: `public void acceptTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `trade`. State: trading status and membership of `trade`. | No return value. Records agreement or rejects the request. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-21 | Blue accepts unanswered `A` while holding 0 ore. `B` is also active. | Both offers stay active. Only `A` becomes accepted. No cards move, and an observer can read the agreement. | :x: |
| Test Case TH-22 | White tries to accept `A`, which is addressed to Blue. | Rejected. | :x: |
| Test Case TH-23 | Blue tries to accept a separate unregistered offer with the same terms as active `A`. | Rejected. `A` remains unanswered. | :x: |
| Test Case TH-24 | Blue tries to accept active `A` after trading closes. | Rejected. | :x: |

### Test: `public void refuseTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `trade`. State: trading status and offer status. | No return value. Removes the offer or rejects the request. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-25 | Blue refuses unanswered `A` while `B` is also active. | Only `B` remains. No cards move, and an observer can read the updated set. | :x: |
| Test Case TH-26 | Blue tries to refuse `A` after it was removed. | Rejected. | :x: |
| Test Case TH-27 | White tries to refuse `A`, which is addressed to Blue. | Rejected. | :x: |
| Test Case TH-28 | Blue tries to refuse unanswered `A` after trading closes. | Rejected. | :x: |
| Test Case TH-29 | Blue tries to refuse `A` after accepting it. | Rejected. | :x: |

### Test: `public void conductTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `trade`. State: trading status, active player, offer membership and agreement, and both inventories. | No return value. Transfers the agreed resources and removes the offer, or rejects the exchange without changing either inventory or the offer. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-30 | Red finalizes an accepted offer giving `[1 brick, 1 brick]` for `[1 ore, 1 grain]`. Red has exactly 2 brick. Blue has 2 ore and 1 grain. `B` is also pending. | Red loses 2 brick and gains 1 ore and 1 grain. Blue gains 2 brick, retains 1 ore, and has 0 grain. The completed offer is removed, and an observer sees only `B`. | :x: |
| Test Case TH-31 | Red tries to finalize Blue's unanswered counteroffer to give `[1 ore, 1 ore]` for `[2 brick]`. Blue has 1 ore and Red has 2 brick. | Rejected because Blue owes 2 ore. The counteroffer remains active and unanswered. | :x: |
| Test Case TH-32 | Use TH-30 with Blue holding 0 grain. | Rejected. No cards move, and the accepted offer stays active. | :x: |
| Test Case TH-33 | Red tries to finalize unanswered `A`. | Rejected. | :x: |
| Test Case TH-34 | Red finalizes unanswered counteroffer `C` while it is active. | Red loses 2 brick and gains 1 ore. Blue loses 1 ore and gains 2 brick. `C` is removed. | :x: |
| Test Case TH-35 | Blue tries to finalize accepted `A` during Red's turn. | Rejected. | :x: |
| Test Case TH-36 | Red finalizes accepted `A` and then finalizes the same offer again. Red starts with 2 brick and Blue starts with 2 ore. | The first call completes the exchange. The second is rejected even though both players still have sufficient cards. | :x: |
| Test Case TH-37 | Red tries to finalize accepted `A` after trading closes. | Rejected. | :x: |

### Test: `public void expireTradeOffers()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| No arguments. State: outstanding offers at turn end. | No return value. The active-offer set is empty. |

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

|              | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case TH-38 | Call with an empty active-offer set at turn end. | The set remains empty. | :x: |
| Test Case TH-39 | At turn end, `A` is accepted and `B` is unanswered. Trading has closed. | Both offers are removed. No cards move, and an observer can read the empty set. | :x: |

# Class PlayerTradeHelper

Classpath: `catan.game_table.domain.trade.PlayerTradeHelper`

## `public class PlayerTradeHelper extends Observed`

Manages domestic offers using the current turn context and player inventories. Callers supply non-null arguments. Each offer is identified by its instance, so two separate offers may have identical terms. Changes to offer membership or agreement notify observers after the new state is visible. Rejected operations leave membership, agreement, and inventories unchanged and send no notification.

### `public PlayerTradeHelper(Map<Color, ResourceInventory> playerInventories, TradeTurnContext turnContext)`

Creates a helper with no active offers. Game setup supplies the player inventories and turn context.

### `public Set<TradeOffer> getActiveTradeOffers()`

Returns a read-only set of the current unanswered and accepted offers. The caller cannot change offer membership through this set.

### `public void proposeTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

Registers an offer from the active player to a different configured player while trading is open. The offer must have no recorded acceptance and must not already be in the active set. Each side gives resources in positive quantities, and no resource type appears on both sides. Proposing does not reserve or transfer cards.

### `public void counterTrade(Color actor, TradeOffer original, TradeOffer counter) throws InvalidTradeException`

While trading is open, the recipient may replace an active offer they have yet to accept. The original recipient makes the counteroffer to the original proposer. The counteroffer must have no recorded acceptance and must not already be in the active set. The same resource-term rules apply.

### `public void acceptTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

While trading is open, the addressed player records agreement to an unanswered active offer. The accepted offer remains active until finalization or expiry, and inventories stay as they were.

### `public void refuseTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

While trading is open, the addressed player may remove an unanswered active offer by refusing it. Inventories stay as they were.

### `public void conductTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

Only the active player may finalize an active offer involving them while trading is open. A proposal from the active player must already be accepted. When the active player is the receiver of a counteroffer, finalization can record agreement and complete the exchange in one call. Check both sides' current stocks before moving any cards. Add together stacks of the same type on the same side. On success, move the resources in both directions and remove that offer.

### `public void expireTradeOffers()`

At turn end, clear all outstanding offers, including accepted ones. The controller may call this after trading closes. Inventories stay as they were.

### `private boolean isValidPlayerTrade(TradeOffer trade)`

Checks that the offer involves two distinct configured players, each side gives resources in positive quantities, and no resource type appears on both sides. Tested indirectly through `proposeTrade()` and `counterTrade()`.

## Boundary-Value Analysis

### Test: `public void proposeTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with the active player, trading status, configured players, and current offers. | Registers or rejects the offer without moving cards. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| who submits the proposal | Cases |
| player named as the recipient | Cases |
| offered and requested resources | Pairs of collections |
| status of the submitted offer | Cases |
| active offers before proposal | Collection |

**Operations:**
Appending an offer to the active set.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| who submits the proposal | active player submits their own offer, another player submits their own offer during the active player's turn, active player submits an offer naming someone else as proposer |
| player named as the recipient | a different player with an inventory, the proposer, a player color with no inventory |
| offered and requested resources | nonempty disjoint lists, either side empty, same resource type on both sides, a stack of size 0 |
| status of the submitted offer | new offer awaiting a response, the same offer submitted again while still active, previously accepted offer that has expired |
| active offers before proposal | empty, one accepted offer with identical terms |

The one-sided empty tests isolate each list requirement. Either one already rejects an offer with both lists empty.

**Step 4: Each choice**

|              | Inputs                                                                                                                                                                                 | Expected output                                                                                                                                                               | Implemented?       |
|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1  | On Red's open turn, with no active offers and no brick in hand, Red offers Blue 1 brick for 1 ore.                                                                                     | The new unanswered offer is the only active offer. Observers see it, and no cards move.                                                                                       | :white_check_mark: |
| Test Case 2  | Red offers Blue 1 brick for 1 ore after trading closes on Red's turn.                                                                                                                  | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 3  | Blue starts an offer to trade 1 ore for 2 brick with Red during Red's open turn.                                                                                                       | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 4  | During Red's open turn, Red submits an offer naming Blue as the proposer of 1 ore for 2 brick from Red.                                                                                | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 5  | During Red's open turn, Red offers to trade 1 brick for 1 ore with themself.                                                                                                           | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 6  | During Red's open turn, Red offers 1 brick for 1 ore to Orange, who has no inventory entry.                                                                                            | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 7  | During Red's open turn, Red offers Blue 1 brick and asks for no cards.                                                                                                                 | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 8  | During Red's open turn, Red asks Blue for 1 ore and offers no cards.                                                                                                                   | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 9  | During Red's open turn, Red offers Blue a zero-size brick stack for 1 ore.                                                                                                             | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 10 | During Red's open turn, Red offers Blue `[1 lumber, 1 brick]` for `[1 ore, 2 brick]`.                                                                                                  | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                        | :white_check_mark: |
| Test Case 11 | During Red's open turn, Red proposes the same unanswered offer instance twice.                                                                                                         | The first call registers it. The second throws `InvalidTradeException`, leaves the offer pending, and causes no inventory change or additional observer notification.         | :white_check_mark: |
| Test Case 12 | On a later open turn for Red, Red proposes the same instance of an accepted offer to Blue that expired at the end of an earlier turn.                                                  | Throws `InvalidTradeException` without registering the offer or changing inventories.                                                                                         | :white_check_mark: |
| Test Case 13 | During Red's open turn, Red creates and proposes a new offer to Blue with the same terms as an active, accepted offer. Then try to clear the set returned by `getActiveTradeOffers()`. | Both instances appear in the set. The original remains accepted and the new one is unanswered. `clear()` throws `UnsupportedOperationException`, and both offers stay active. | :white_check_mark: |

### Test: `public void counterTrade(Color actor, TradeOffer original, TradeOffer counter) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor`, `original`, and `counter`, with trading and original offer status. | Replaces the original with the unanswered counteroffer or rejects the request, with inventories unchanged. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| status of the original offer | Cases |
| who submits the counteroffer | Cases |
| counteroffer participants, terms, and status | Cases |

**Operations:**
Searching for the original and replacing that entry.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| status of the original offer | active offer awaiting a response, copy that was never registered, active offer already accepted |
| who submits the counteroffer | the original recipient, a player other than the original recipient |
| counteroffer participants, terms, and status | original recipient counters to the original proposer, counter names the wrong recipient, counter names the wrong proposer, counter includes a zero-size resource stack, the same counteroffer is already active |

**Step 4: Each choice**

|             | Inputs                                                                                                                                                      | Expected output                                                                                                                                                                           | Implemented?       |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | During Red's open turn, Blue counters an unanswered offer from Red, then Red counters back while another offer is pending.                                  | Each unanswered counteroffer replaces its predecessor and notifies observers, leaving the unrelated offer and inventories unchanged.                                                      | :white_check_mark: |
| Test Case 2 | During Red's open turn, Blue submits a valid Blue-to-Red counteroffer for an unregistered copy of a pending Red-to-Blue offer.                              | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                                    | :white_check_mark: |
| Test Case 3 | During Red's open turn, White submits an otherwise valid Blue-to-Red counteroffer for an unanswered offer from Red to Blue.                                 | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                                    | :white_check_mark: |
| Test Case 4 | During Red's open turn, Blue attempts to counter an unanswered offer from Red with a counteroffer addressed to White.                                       | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                                    | :white_check_mark: |
| Test Case 5 | During Red's open turn, Blue attempts to counter an unanswered offer from Red with a counteroffer naming White as its proposer.                             | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                                    | :white_check_mark: |
| Test Case 6 | During Red's open turn, Blue counters an unanswered offer from Red by asking for 2 brick and offering a zero-size ore stack.                                | Throws `InvalidTradeException` without removing the original or changing inventories.                                                                                                     | :white_check_mark: |
| Test Case 7 | Blue counters an unanswered offer from Red after trading closes on Red's turn.                                                                              | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                                    | :white_check_mark: |
| Test Case 8 | During Red's open turn, Blue counters an active offer from Red that Blue has already accepted.                                                              | Throws `InvalidTradeException` without changing offers or inventories.                                                                                                                    | :white_check_mark: |
| Test Case 9 | During Red's open turn, Blue counters one of two unanswered offers from Red, then tries to use that same active counteroffer instance to replace the other. | Throws `InvalidTradeException`. The second original and the first counteroffer remain active, inventories stay unchanged, and observers receive no notification for the rejected attempt. | :white_check_mark: |

### Test: `public void acceptTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with trading status, offer membership, and agreement status. | Records agreement or rejects the request. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| who tries to accept | Cases |
| whether this offer is active | Cases |
| whether the recipient already accepted | Boolean |

**Operations:**
Searching for the offer.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| who tries to accept | the offer's recipient, a player other than the recipient |
| whether this offer is active | the active offer itself, a separate copy with identical terms that was never registered |
| whether the recipient already accepted | awaiting the recipient's response, recipient already accepted |

**Step 4: Each choice**

|             | Inputs                                                                                                                                  | Expected output                                                                                                                                    | Implemented?       |
|-------------|-----------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | During Red's open turn, Blue accepts an unanswered offer that requests 1 ore from Blue, who holds none, while another offer is pending. | The addressed offer becomes accepted, both remain active, and observers see the change without any cards moving.                                   | :white_check_mark: |
| Test Case 2 | During Red's open turn, White tries to accept an unanswered offer from Red to Blue.                                                     | Throws `InvalidTradeException` without changing offers or inventories.                                                                             | :white_check_mark: |
| Test Case 3 | During Red's open turn, Blue tries to accept an unregistered copy of a pending offer from Red.                                          | Throws `InvalidTradeException` without changing offers or inventories.                                                                             | :white_check_mark: |
| Test Case 4 | Blue tries to accept an unanswered offer from Red after trading closes.                                                                 | Throws `InvalidTradeException` without changing offers or inventories.                                                                             | :white_check_mark: |
| Test Case 5 | During Red's open turn, Blue tries to accept the same active offer from Red again.                                                      | Throws `InvalidTradeException`. The offer stays accepted and active, inventories stay unchanged, and observers receive no additional notification. | :white_check_mark: |

### Test: `public void refuseTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with trading and offer status. | Removes the offer or rejects the request. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| who tries to refuse | Cases |
| status of the offer | Cases |

**Operations:**
Searching for and deleting the offer.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| who tries to refuse | the offer's recipient, a player other than the recipient |
| status of the offer | active offer awaiting a response, offer already removed from the active set, active offer already accepted |

**Step 4: Each choice**

|             | Inputs                                                                                                | Expected output                                                                          | Implemented?       |
|-------------|-------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | During Red's open turn, Blue refuses an unanswered offer from Red while another offer is pending.     | Only the unrelated offer remains, and observers see the change without any cards moving. | :white_check_mark: |
| Test Case 2 | During Red's open turn, Blue tries to refuse a Red-to-Blue offer that was already removed.            | Throws `InvalidTradeException` without changing offers or inventories.                   | :white_check_mark: |
| Test Case 3 | During Red's open turn, White tries to refuse an unanswered offer from Red to Blue.                   | Throws `InvalidTradeException` without changing offers or inventories.                   | :white_check_mark: |
| Test Case 4 | Blue tries to refuse an unanswered offer from Red after trading closes.                               | Throws `InvalidTradeException` without changing offers or inventories.                   | :white_check_mark: |
| Test Case 5 | During Red's open turn, Blue tries to refuse an active offer from Red that Blue has already accepted. | Throws `InvalidTradeException` without changing offers or inventories.                   | :white_check_mark: |

### Test: `public void conductTrade(Color actor, TradeOffer trade) throws InvalidTradeException`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `actor` and `trade`, with trading status, active player, offer status, and both inventories. | Transfers cards and removes the offer or rejects the exchange without changing offers or inventories. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| trading open | Boolean |
| who tries to finalize and whether the offer was accepted | Cases |
| whether this offer is active | Cases |
| stock of each resource owed by either player | Counts |
| resource lists | Pairs of collections |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| trading open | true, false |
| who tries to finalize and whether the offer was accepted | active player finalizes their own accepted offer, active player tries to finalize their own offer before the recipient accepts, active player finalizes a counteroffer addressed to them, another player tries to finalize an accepted offer addressed to them |
| whether this offer is active | offer still in the active set, offer already completed and removed |
| stock of each resource owed by either player | exactly enough or surplus, proposer short by one on a repeated resource, receiver short by one on a required resource |
| resource lists | one stack per side, multiple stacks with repeated or distinct resource types |

Repeated stacks of the same resource on one side interact because their quantities must be added before checking stock.

**Step 4: Each choice**

|             | Inputs                                                                                                                                                                                             | Expected output                                                                                                                                           | Implemented?       |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | During Red's open turn, Red finalizes an accepted trade of `[1 brick, 1 brick]` for `[1 ore, 1 grain]`. Red holds 2 brick, and Blue holds 2 ore and 1 grain. Another offer is pending.             | Red loses 2 brick and gains 1 ore and 1 grain. Blue gains 2 brick and keeps 1 ore and 0 grain. Observers see only the unrelated offer after the exchange. | :white_check_mark: |
| Test Case 2 | During Red's open turn, Red tries to finalize Blue's unanswered counteroffer of `[1 ore, 1 ore]` for 2 brick when Blue has only 1 ore and Red has 2 brick.                                         | Throws `InvalidTradeException`. The counteroffer stays unanswered and active, and neither inventory changes.                                              | :white_check_mark: |
| Test Case 3 | During Red's open turn, Blue accepts Red's offer of `[1 brick, 1 brick]` for `[1 ore, 1 grain]` while both players have enough stock. Blue spends the required grain before Red tries to finalize. | Throws `InvalidTradeException`. The offer stays accepted and active, and both inventories stay as they were immediately before finalization.              | :white_check_mark: |
| Test Case 4 | During Red's open turn, Red tries to finalize a stocked trade that Blue has yet to accept.                                                                                                         | Throws `InvalidTradeException` without changing offers or inventories.                                                                                    | :x:                |
| Test Case 5 | During Red's open turn, Red finalizes Blue's unanswered counteroffer to give Red 1 ore for 2 brick with enough stock on both sides.                                                                | Blue gives Red 1 ore, Red gives Blue 2 brick, and the counteroffer is removed.                                                                            | :x:                |
| Test Case 6 | During Red's open turn, Blue tries to finalize a stocked, accepted offer from Red.                                                                                                                 | Throws `InvalidTradeException` without changing offers or inventories.                                                                                    | :x:                |
| Test Case 7 | During Red's open turn, Red twice tries to finalize their accepted offer to give Blue 1 brick for 1 ore, starting with enough stock for two exchanges.                                             | The first call transfers the cards and removes the offer, and the second throws `InvalidTradeException` without further changes.                          | :x:                |
| Test Case 8 | After trading closes on Red's turn, Red tries to finalize a stocked, accepted offer to Blue.                                                                                                       | Throws `InvalidTradeException` without changing offers or inventories.                                                                                    | :x:                |

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
| offer status | awaiting the recipient's response, accepted but still waiting to be finalized |

**Step 4: Each choice**

|             | Inputs                                                                                     | Expected output                                                                                | Implemented?       |
|-------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | Expire offers at turn end with no active offers.                                           | The active-offer set stays empty, inventories stay unchanged, and observers receive no update. | :white_check_mark: |
| Test Case 2 | Expire offers after trading closes at turn end with one accepted and one unanswered offer. | Both offers are removed, and observers see the empty set without any cards moving.             | :white_check_mark: |

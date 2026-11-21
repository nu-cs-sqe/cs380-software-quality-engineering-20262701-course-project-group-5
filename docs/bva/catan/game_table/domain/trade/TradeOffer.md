# Class TradeOffer

Classpath: `catan.game_table.domain.trade.TradeOffer`

## `public class TradeOffer`

Stores the terms and acceptance status of a player-to-player offer. Callers supply valid arguments, and `PlayerTradeHelper` checks whether the terms satisfy the trade rules.

### `public TradeOffer(Color proposer, Color receiver, List<ResourceStack> requestedResources, List<ResourceStack> offeredResources)`

Stores the parties and takes read-only copies of both resource lists. A new offer starts unanswered.

### `public Color getProposer()`

Returns the color of the player who made the offer.

### `public Color getReceiver()`

Returns the color of the player addressed by the offer.

### `public List<ResourceStack> getRequestedResources()`

Returns the read-only list of resources the proposer asks the receiver to give.

### `public List<ResourceStack> getOfferedResources()`

Returns the read-only list of resources the proposer offers to give the receiver.

### `public boolean isAccepted()`

Returns whether the recipient's agreement has been recorded.

### `void markAccepted()`

Records acceptance on this offer for `PlayerTradeHelper` within the trade package.

## Boundary-Value Analysis

### Test: `public TradeOffer(Color proposer, Color receiver, List<ResourceStack> requestedResources, List<ResourceStack> offeredResources)`

**To verify that edits through either the original lists or returned lists leave the offer intact. 

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `proposer`, `receiver`, `requestedResources`, and `offeredResources`, with list edits attempted after construction. | A new offer that retains its original terms. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| proposer and receiver | Cases |
| offered and requested lists | Pairs of collections |
| source of edit | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| proposer and receiver | `RED` and `BLUE` |
| offered and requested lists | `[1 brick]` offered and `[1 ore]` requested |
| source of edit | original lists, lists exposed by the offer |

**Step 4: Each choice**

|             | Inputs                                                                                                                         | Expected output                                                                                                                    | Implemented? |
|-------------|--------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Create a Red-to-Blue offer of 1 brick for 1 ore from mutable lists, clear the originals, then try to clear each returned list. | Both returned lists reject `clear()` with `UnsupportedOperationException`, and the terms stay 1 brick offered for 1 ore requested. | :x:          |

### Test: `public boolean isAccepted()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| The offer's response status, with no arguments. | Whether the recipient has accepted. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| response status | Cases |
| accepted (output) | Boolean |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| response status | unanswered, accepted through `markAccepted()` |
| accepted (output) | `false`, `true` |

**Step 4: Each choice**

|             | Inputs                                                                                                       | Expected output                                                               | Implemented? |
|-------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|--------------|
| Test Case 1 | Check acceptance on a new Red-to-Blue offer, Red proposed but Blue hasn't agreed.                            | Returns `false`.                                                              | :x:          |
| Test Case 2 | Create two Red-to-Blue offers for 1 brick in exchange for 1 ore and call `markAccepted()` on the first only. | `isAccepted()` returns `true` for the first offer and `false` for the second. | :x:          |

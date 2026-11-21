# Class TradeOffer

Classpath: `catan.game_table.domain.trade.TradeOffer`

## `public class TradeOffer`

Stores the terms and acceptance status of a player-to-player offer. Callers supply non-null parties and lists of non-null stacks. `PlayerTradeHelper` checks whether the terms satisfy the trade rules.

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

### Test: `TradeOffer(...)`, `getRequestedResources()`, and `getOfferedResources()`

The constructor takes a copy of each list so a caller can reuse its lists without changing an existing offer. The getters expose the saved terms as read-only lists.

|             | Inputs                                                                                                                                                                                                  | Expected output                                                                                                                                                                  | Implemented? |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Pass mutable lists containing `[1 ore]` requested and `[1 brick]` offered to `new TradeOffer(...)`. Clear both original lists, then call both resource getters and try `clear()` on each returned list. | `getRequestedResources()` still returns `[1 ore]` and `getOfferedResources()` still returns `[1 brick]`. Both returned lists throw `UnsupportedOperationException` on `clear()`. | :x:          |

### Test: `isAccepted()`

Package-private to use markAccepted()

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| Two initially unanswered offers with identical terms. Record acceptance on one. | Only the selected offer records agreement, as observed through `isAccepted()`. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| whether acceptance was recorded for this offer | Cases |
| `isAccepted()` result | Boolean |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| whether acceptance was recorded for this offer | still awaiting the recipient's response, acceptance recorded by `markAccepted()` |
| `isAccepted()` result | `false` while awaiting a response, `true` after acceptance |

**Step 4: Each choice**

|             | Inputs                                                                                                                                                           | Expected output                                                                                                 | Implemented? |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Create two Red-to-Blue offers for 1 brick in exchange for 1 ore. Call `isAccepted()` on both. Then call `markAccepted()` on the first only and check both again. | Both return `false` initially. After acceptance, the first returns `true` and the second still returns `false`. | :x:          |

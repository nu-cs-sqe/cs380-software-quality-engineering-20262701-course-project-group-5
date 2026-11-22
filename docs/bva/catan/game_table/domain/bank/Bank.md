# Class Bank

Classpath: `catan.game_table.domain.bank.Bank`

## `public class Bank extends Observed implements ResourceInventory`

Holds the resource supply and the development deck. Game setup supplies the starting resource stacks and the shuffled deck in draw order, with the top card first.

### `public Bank(List<ResourceStack> startingResources, List<Development> startingDevelopments)`

Initializes the resource supply and deck from the supplied lists.

### `public void addResources(ResourceStack stack)`

Returns the indicated cards to the supply, adding to the count for that resource type.

### `public boolean hasResources(ResourceStack stack)`

Returns whether the bank has at least the requested number of cards of that resource type.

### `public void removeResources(ResourceStack stack) throws NotEnoughInventoryException`

Withdraws the requested quantity of that resource. If stock is insufficient, throws `NotEnoughInventoryException` and leaves the supply as it was.

### `public Development drawDevelopment() throws NotEnoughInventoryException`

Removes and returns the top development card. Throws `NotEnoughInventoryException` when the deck is empty. **(can we use this exception here?)**

### `public boolean hasDevelopmentsRemaining()`

Returns whether the deck contains any cards.

### `public int getResourceCount(Class<? extends ResourceStack> resourceType)`

Returns the stock of the requested resource type, including zero for a depleted resource.

### `public int getDevelopmentCount()`

Returns the number of development cards left in the deck.

## Boundary-Value Analysis

### Test: `addResources(ResourceStack stack)`

**Step 1: Inputs / outputs**

| Inputs                                                                     | Outputs                                                                                         |
|----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| Resource type and quantity being returned, with the bank's starting stock. | The count of that resource rises by the quantity returned. Other resource counts stay the same. |

**Step 2: Data types**

| Variable                                    | Type  |
|---------------------------------------------|-------|
| resource being returned                     | Cases |
| cards already in the bank for that resource | Count |
| cards returned for that resource            | Count |

**Step 3: Concrete values**

| Variable                             | Values                                                                                                                              |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| resource being returned              | A depleted resource and a resource with stock, with an unrelated resource present                                                   |
| starting stock and quantity returned | `0 + 1` adds to a depleted supply. `17 + 2` restores the standard supply of 19 cards of one type. I picked min and max values here. |

**Step 4: Each choice**

|             | Inputs                                         | Expected output                 | Implemented? |
|-------------|------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | The bank has 0 ore and 4 wool. Return 1 ore.   | The bank has 1 ore and 4 wool.  | :x:          |
| Test Case 2 | The bank has 17 wool and 4 ore. Return 2 wool. | The bank has 19 wool and 4 ore. | :x:          |

### Test: `hasResources(ResourceStack stack)`

**Step 1: Inputs / outputs**

| Inputs                                                                | Outputs                                                                                           |
|-----------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| Resource type and quantity requested, with the bank's starting stock. | Whether the bank has at least the requested quantity of that resource. The stock stays unchanged. |

**Step 2: Data types**

| Variable                                                       | Type                               |
|----------------------------------------------------------------|------------------------------------|
| cards available for the requested resource and cards requested | Counts that interact at thresholds |
| presence of the requested resource                             | Cases                              |
| availability result                                            | Boolean                            |

**Step 3: Concrete values**

| Variable                                      | Values                                                                                                          |
|-----------------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| cards available compared with cards requested | Exact stock `(2, 2)`, one extra card `(3, 2)`, one card short `(1, 2)`, none of the requested resource `(0, 1)` |
| other resources in the bank                   | At least one other type is present in each case                                                                 |
| availability result                           | `true` for enough stock, `false` for insufficient stock                                                         |

**Step 4: Each choice**

|             | Inputs                                               | Expected output  | Implemented? |
|-------------|------------------------------------------------------|------------------|--------------|
| Test Case 3 | The bank has 2 ore and 4 wool. Check for 2 ore.      | Returns `true`.  | :x:          |
| Test Case 4 | The bank has 3 brick and 4 wool. Check for 2 brick.  | Returns `true`.  | :x:          |
| Test Case 5 | The bank has 1 ore and 4 wool. Check for 2 ore.      | Returns `false`. | :x:          |
| Test Case 6 | The bank has 4 wool and no grain. Check for 1 grain. | Returns `false`. | :x:          |

### Test: `removeResources(ResourceStack stack)`

Reuse the starting stock, requested stacks, and concrete boundaries from `hasResources()` above. Call `removeResources()` directly and use `getResourceCount()` to check the resulting stock.

**Step 4: Each choice**

|              | Inputs                                          | Expected output                                                             | Implemented? |
|--------------|-------------------------------------------------|-----------------------------------------------------------------------------|--------------|
| Test Case 7  | Same starting stock and request as Test Case 3. | Ore drops to 0 and wool stays at 4.                                         | :x:          |
| Test Case 8  | Same starting stock and request as Test Case 4. | Brick drops to 1 and wool stays at 4.                                       | :x:          |
| Test Case 9  | Same starting stock and request as Test Case 5. | Throws `NotEnoughInventoryException`. Ore stays at 1 and wool stays at 4.   | :x:          |
| Test Case 10 | Same starting stock and request as Test Case 6. | Throws `NotEnoughInventoryException`. Grain stays at 0 and wool stays at 4. | :x:          |

### Test: `drawDevelopment()`

**Step 1: Inputs / outputs**

| Inputs                                      | Outputs                                                                               |
|---------------------------------------------|---------------------------------------------------------------------------------------|
| The current development deck in draw order. | The top card and a deck reduced by one card, or an exception if no card is available. |

**Step 2: Data types**

| Variable                              | Type               |
|---------------------------------------|--------------------|
| development deck                      | Ordered collection |
| number of remaining development cards | Count              |
| draw result                           | Cases              |

**Step 3: Concrete values**

| Variable                | Values                                                                                                                          |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| deck size before a draw | 2 distinguishable cards, 1 last card, 0 cards                                                                                   |
| draw result             | First card in the supplied order, last remaining card, `NotEnoughInventoryException` **(see comment in method specifications)** |

**Step 4: Each choice**

Check `getDevelopmentCount()` and `hasDevelopmentsRemaining()` as the deck changes.

|              | Inputs                                                          | Expected output                                                                                                                                              | Implemented? |
|--------------|-----------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 11 | Start with a Knight on top of a Victory Point card. Draw twice. | First draw returns the Knight, leaving 1 card and availability `true`. Second draw returns the Victory Point card, leaving 0 cards and availability `false`. | :x:          |
| Test Case 12 | Draw from an empty deck while the bank holds 4 wool.            | Throws `NotEnoughInventoryException`. The deck stays empty and wool stays at 4.                                                                              | :x:          |

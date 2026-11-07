# Class Settlement

Classpath: catan.game_table.domain.piece.Settlement

## `class Settlement extends Building`

A subclass of Building that costs a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one to build. When adjacent to a `Hex` that can produce and was triggered by a production roll, produces two `ResourceProducts` that is `tier1` of the `ResourceStack` type of the triggered `Hex`.

### `public Settlement(data : PieceData, bankInventory : ResourceInventory, ownerInventory : ResourceInventory, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a Settlement object with set `PieceData` and access to the `ResourceInventory`s of a bank and Player, and access to the `ScoreTracker` of the Player that owns it.

### `public void produceResources(products : ResourceProducts)`

Updates the `bankResources` and `ownerResources` `ResourceInventory`s by one based on the given `products`.

### `public void onBoughtEffect()`

Decrements the Resources of the Player purchasing the Settlement by its cost and increments the Banks Resources.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a Settlement in terms of Resources, in this case a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one.

## Boundary-Value Analysis

### Method under test: `produce(products : ResourceProducts)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|`products : ResourceProducts`| No concrete outputs, only updates to state: `bankResources`, state: `ownerResources`. Inventories should update by one tier 1 product.|

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases: only `tier1Products` list that can hold 5 different types of products, `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
|`bankResources`| Collection |
|`ownerResources`| Collection |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
| `bankResources` | list of one, list of size greater than one, list of nineteen (max number of resource cards for a given type) |
| `ownerResources` | list of one, list of size greater than one, list of nineteen (max number of resource cards for a given type |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `products` stack type: `WoolResourceStack`, `bankResources` = {}, `ownerResources` = {} |  | :x: |
| Test Case 2 |  |  | :x: |
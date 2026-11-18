# Class City

Classpath: catan.game_table.domain.piece.City

## `class City extends Building`

A subclass of `Building` that costs a `GrainResourceStack` of size two and `OreResourceStack` of size three to build. When an adjacent `Hex` produces on a roll, it yields `ResourceProducts` of that hex’s resource type: `baseProducts` plus `cityBonusProducts`.

### `public City(data : PieceData, bankResourceDistributor : BankResourceDistributor, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a `City` object with set `PieceData`, access to the `BankResourceDistributor`, and access to the `ScoreTracker` of the `GamePlayer` that owns it.

### `public void produceResources(products : ResourceProducts)`

Given `products` calls the `bankResourceDistributor` to attempt to increase the amount of the given `products` in the `PlayerInventory` of the player that owns the City by two and decrease `bankInventory` by two. If `Bank` does not have enough resources to produce for all player, nothing will be produced.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a City in terms of `ResourcesStack`s, in this case a `GrainResourceStack` of size two and `OreResourceStack` of size three

## Boundary-Value Analysis

### Method under test: `produce(products : ResourceProducts)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `products : ResourceProducts` | No concrete outputs, but calls `bankResourceDistributor`'s `produce()` to try and increment/decrement respectively `bankInventory` and `PlayerInventory`.|

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases |
| Output | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | a valid `ResourceProducts` |
| Output | Cases: `bankResourceDistributor` `produce()` is called |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1  | `products` is a valid `ResourceProducts`| `bankResourceDistributor` `produce()` is called | :x: |

# Class Settlement

Classpath: catan.game_table.domain.piece.Settlement

## `class Settlement extends Building`

A subclass of Building that costs a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one to build. When an adjacent `Hex` produces on a roll, it yields a `List<ResourceStack>` of size one of that hex’s resource type.

### `public Settlement(data : PieceData, bankResourceDistributor : BankResourceDistributor, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a Settlement object with set `PieceData`, access to the `BankResourceDistributor`, and access to the `ScoreTracker` of the `GamePlayer` that owns it.

### `public void produceResources(products : List<ResourceStack>)`

Given `products` calls the `bankResourceDistributor` to attempt to increase the amount of the given `products` in the `PlayerInventory` of the player that owns the Settlement by one and decrease `bankInventory` by the same amount. If `Bank` does not have enough resources to produce for all player, nothing will be produced.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a Settlement in terms of Resources, in this case a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one.

## Boundary-Value Analysis

### Method under test: `produce(products : List<ResourceStack>)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `products : List<ResourceStack>` | No concrete outputs, but calls `bankResourceDistributor:produceResources()` to try and increment/decrement respectively `bankInventory` and `PlayerInventory`.|

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases |
| Output | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | a valid `List<ResourceStack>` of size one |
| Output | Cases: `bankResourceDistributor:produceResources()` is called with the right `color` and `products` |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1  | `products` is a valid `List<ResourceStack>`| `bankResourceDistributor:produceResources()` is called with the right `color` and `products` | :white_check_mark: |
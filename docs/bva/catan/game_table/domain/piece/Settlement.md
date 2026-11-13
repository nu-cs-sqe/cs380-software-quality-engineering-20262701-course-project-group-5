# Class Settlement

Classpath: catan.game_table.domain.piece.Settlement

## `class Settlement extends Building`

A subclass of Building that costs a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one to build. When adjacent to a `Hex` that can produce and was triggered by a production roll, produces two `ResourceProducts` that is `baseProducts` of the `ResourceStack` type of the triggered `Hex`.

### `public Settlement(data : PieceData, bankResourceDistributor : BankResourceDistributor, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a Settlement object with set `PieceData`, access to the `BankResourceDistributor`, and access to the `ScoreTracker` of the `GamePlayer` that owns it.

### `public void produceResources(products : ResourceProducts)`

Given `products` calls the `bankResourceDistributor` to attempt to increase the amount of the given `products` in the `PlayerInventory` of the player that owns the Settlement by one and decrease `bankInventory` by the same amount. If `Bank` does not have enough resources to produce for all player, nothing will be produced.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a Settlement in terms of Resources, in this case a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one.

## Boundary-Value Analysis

### Method under test: `produce(products : ResourceProducts)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|`products : ResourceProducts`| No concrete outputs, only updates to state: `bankInventory`, state: `PlayerInventory` through `bankResourcesDistributor`. Inventories should update by one `baseProducts`.|

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases:`baseProducts` list can only hold 5 different types of products, `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
|`bankInventory`| Count |
|`PlayerInventory`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
| `bankInventory` | zero, one, greater than one, eighteen (max number of resource cards for a given type minus one) |
| `PlayerInventory` | one, greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{1}}, `PlayerInventory` = {WoolResourceStack{0}} | `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{1}} | :x: |
| Test Case 2 | `products` stack type: `GrainResourceStack`, `bankInventory` = {GrainResourceStack{2}}, `PlayerInventory` = {GrainResourceStack{1}} | `bankInventory` = {GrainResourceStack{1}}, `PlayerInventory` = {GrainResourceStack{2}} | :x: |
| Test Case 3 | `products` stack type: `OreResourceStack`, `bankInventory` = {OreResourceStack{5}}, `PlayerInventory` = {OreResourceStack{2}} | `bankInventory` = {OreResourceStack{4}}, `PlayerInventory` = {OreResourceStack{3}} | :x: |
| Test Case 4 | `products` stack type: `LumberResourceStack`, `bankInventory` = {LumberResourceStack{19}}, `PlayerInventory` = {LumberResourceStack{0}} | `bankInventory` = {LumberResourceStack{18}}, `PlayerInventory` = {LumberResourceStack{1}} | :x: |
| Test Case 5 | `products` stack type: `BrickResourceStack`, `bankInventory` = {BrickResourceStack{1}}, `PlayerInventory` = {BrickResourceStack{18}} | `bankInventory` = {BrickResourceStack{0}}, `PlayerInventory` = {BrickResourceStack{19}} | :x: |
| Test Case 6 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{2}} | `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{2}} | :x: |
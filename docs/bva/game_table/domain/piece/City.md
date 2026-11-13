# Class City

Classpath: catan.game_table.domain.piece.City

## `class City extends Building`

A subclass of `Building` that costs a `GrainResourceStack` of size two and `OreResourceStack` of size three to build. When adjacent to a `Hex` that can produce and was triggered by a production roll, produces two `baseProducts` and `cityBonusProducts` `ResourceProducts` of the `ResourceStack` type of triggerred `Hex`.

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
|`products : ResourceProducts`| No concrete outputs, only updates through `bankResourceDistributor` to state: `bankInventory`, state: `PlayerInventory` of player owning `City`. Inventories should update by two `baseProducts` and `cityBonusProducts` product.|

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases: `baseProducts` and `cityBonusProducts` lists can hold 5 different types of products, `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
|`bankInventory`| Count |
|`PlayerInventory`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
| `bankInventory` | zero, one, greater than one, seventeen (max number of resource cards for a given type minus two) |
| `PlayerInventory` | greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{2}}, `PlayerInventory` = {WoolResourceStack{0}} | `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{2}} | :x: |
| Test Case 2 | `products` stack type: `GrainResourceStack`, `bankInventory` = {GrainResourceStack{3}}, `PlayerInventory` = {GrainResourceStack{5}} | `bankInventory` = {GrainResourceStack{1}}, `PlayerInventory` = {GrainResourceStack{7}} | :x: |
| Test Case 3 | `products` stack type: `OreResourceStack`, `bankInventory` = {OreResourceStack{19}}, `PlayerInventory` = {OreResourceStack{0}} | `bankInventory` = {OreResourceStack{17}}, `PlayerInventory` = {OreResourceStack{2}} | :x: |
| Test Case 4 | `products` stack type: `LumberResourceStack`, `bankInventory` = {LumberResourceStack{6}}, `PlayerInventory` = {LumberResourceStack{4}} | `bankInventory` = {LumberResourceStack{4}}, `PlayerInventory` = {LumberResourceStack{6}} | :x: |
| Test Case 5 | `products` stack type: `BrickResourceStack`, `bankInventory` = {BrickResourceStack{7}}, `PlayerInventory` = {BrickResourceStack{1}} | `bankInventory` = {BrickResourceStack{5}}, `PlayerInventory` = {BrickResourceStack{3}} | :x: |
| Test Case 6 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{3}} | `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{3}} | :x: |
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
| `products` | Cases: Either `Bank` has enough resources to produce the given `product` or it does not. Note that all `Hex` types that can produce behave the same so we will arbitrarily pick to observe pasture hexes that can produce `WoolResourceStack`.|
|`bankInventory`| Count |
|`PlayerInventory`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | `bankInventory` for `products` type is less than 2 to start, `bankInventory` for `products` type is 2 or greater to start |
| `bankInventory` | zero, one, greater than one, seventeen (max number of resource cards for a given type minus two) |
| `PlayerInventory` | greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{2}}, `PlayerInventory` = {WoolResourceStack{0}} | `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{2}} | :x: |
| Test Case 2 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{3}}, `PlayerInventory` = {WoolResourceStack{5}} | `bankInventory` = {WoolResourceStack{1}}, `PlayerInventory` = {WoolResourceStack{7}} | :x: |
| Test Case 3 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{19}}, `PlayerInventory` = {WoolResourceStack{0}} | `bankInventory` = {WoolResourceStack{17}}, `PlayerInventory` = {WoolResourceStack{2}} | :x: |
| Test Case 4 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{6}}, `PlayerInventory` = {WoolResourceStack{4}} | `bankInventory` = {WoolResourceStack{4}}, `PlayerInventory` = {WoolResourceStack{6}} | :x: |
| Test Case 5 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{7}}, `PlayerInventory` = {WoolResourceStack{1}} | `bankInventory` = {WoolResourceStack{5}}, `PlayerInventory` = {WoolResourceStack{3}} | :x: |
| Test Case 6 | `products` stack type: `WoolResourceStack`, `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{3}} | `bankInventory` = {WoolResourceStack{0}}, `PlayerInventory` = {WoolResourceStack{3}} | :x: |
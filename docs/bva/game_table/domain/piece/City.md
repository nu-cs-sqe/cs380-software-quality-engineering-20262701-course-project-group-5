# Class City

Classpath: catan.game_table.domain.piece.City

## `class City extends Building`

A subclass of `Building` that costs a `GrainResourceStack` of size two and `OreResourceStack` of size three to build. When adjacent to a `Hex` that can produce and was triggered by a production roll, produces two `tier1` and `tier2` `ResourceProducts` of the `ResourceStack` type of triggerred `Hex`.

### `public City(data : PieceData, bankInventory : ResourceInventory, ownerInventory : ResourceInventory, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a `City` object with set `PieceData` and access to the `ResourceInventory`s of a bank and `GamePlayer`, and access to the `ScoreTracker` of the `GamePlayer` that owns it.

### `public void produceResources(products : ResourceProducts)`

Updates the `bankResources` and `ownerResources` `ResourceInventory`s by two based on the given `products`.

### `public void onBoughtEffect()`

Decrements the `ResourceStacks` of the Player purchasing the City by its cost.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a City in terms of `ResourcesStack`s, in this case a `GrainResourceStack` of size two and `OreResourceStack` of size three

## Boundary-Value Analysis

### Method under test: `produce(products : ResourceProducts)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|`products : ResourceProducts`| No concrete outputs, only updates to state: `bankResources`, state: `ownerResources`. Inventories should update by one tier 1 product.|

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases: `tier1Products` and `tier2Products` lists can hold 5 different types of products, `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
|`bankResources`| Count |
|`ownerResources`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
| `bankResources` | zero, one, greater than one, seventeen (max number of resource cards for a given type minus two) |
| `ownerResources` | greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `products` stack type: `WoolResourceStack`, `bankResources` = {WoolResourceStack{2}}, `ownerResources` = {WoolResourceStack{0}} | `bankResources` = {WoolResourceStack{0}}, `ownerResources` = {WoolResourceStack{2}} | :x: |
| Test Case 2 | `products` stack type: `GrainResourceStack`, `bankResources` = {GrainResourceStack{3}}, `ownerResources` = {GrainResourceStack{5}} | `bankResources` = {GrainResourceStack{1}}, `ownerResources` = {GrainResourceStack{7}} | :x: |
| Test Case 3 | `products` stack type: `OreResourceStack`, `bankResources` = {OreResourceStack{19}}, `ownerResources` = {OreResourceStack{0}} | `bankResources` = {OreResourceStack{17}}, `ownerResources` = {OreResourceStack{2}} | :x: |
| Test Case 4 | `products` stack type: `LumberResourceStack`, `bankResources` = {LumberResourceStack{6}}, `ownerResources` = {LumberResourceStack{4}} | `bankResources` = {LumberResourceStack{4}}, `ownerResources` = {LumberResourceStack{6}} | :x: |
| Test Case 4 | `products` stack type: `BrickResourceStack`, `bankResources` = {BrickResourceStack{7}}, `ownerResources` = {BrickResourceStack{1}} | `bankResources` = {BrickResourceStack{5}}, `ownerResources` = {BrickResourceStack{3}} | :x: |


### Method under test: `onBoughtEffect()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| No concrete inputs, only state: `bankResources`, state: `ownerResources` | No concrete outputs, `ownerResources` should update to have two less Grain and three less Ore and `bankResources` should have that amount more. |

**Step 2: Data types**
| Variable | Type |
|---|---|
|`bankResources`| Count |
|`ownerResources`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `bankResources` | zero, one, greater than one, sixteen (for Ore max), seventeen (for Grain max) |
| `ownerResources` | greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `bankResources` = {GrainResourceStack{0}, OreResourceStack{0}}, `ownerResources` = {GrainResourceStack{19}, OreResourceStack{19}} | `bankResources` = {GrainResourceStack{2}, OreResourceStack{3}}, `ownerResources` = {GrainResourceStack{17}, OreResourceStack{16}} | :x: |
| Test Case 2 | `bankResources` = {GrainResourceStack{1}, OreResourceStack{1}}, `ownerResources` = {GrainResourceStack{2}, OreResourceStack{3}} | `bankResources` = {GrainResourceStack{3}, OreResourceStack{4}}, `ownerResources` = {GrainResourceStack{0}, OreResourceStack{0}} | :x: |
| Test Case 3 | `bankResources` = {GrainResourceStack{5}, OreResourceStack{7}}, `ownerResources` = {GrainResourceStack{3}, OreResourceStack{5}} | `bankResources` = {GrainResourceStack{7}, OreResourceStack{10}}, `ownerResources` = {GrainResourceStack{2}, OreResourceStack{2}} | :x: |
| Test Case 4 | `bankResources` = {GrainResourceStack{17}, OreResourceStack{16}}, `ownerResources` = {GrainResourceStack{2}, OreResourceStack{3}} | `bankResources` = {GrainResourceStack{19}, OreResourceStack{19}}, `ownerResources` = {GrainResourceStack{0}, OreResourceStack{0}} | :x: |

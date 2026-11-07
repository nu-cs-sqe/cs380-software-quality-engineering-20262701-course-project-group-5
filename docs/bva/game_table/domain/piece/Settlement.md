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
|`bankResources`| Count |
|`ownerResources`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | `WoolResourceStack`, `GrainResourceStack`, `OreResourceStack`, `LumberResourceStack`, `BrickResourceStack` |
| `bankResources` | zero, one, greater than one, eighteen (max number of resource cards for a given type minus one) |
| `ownerResources` | one, greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `products` stack type: `WoolResourceStack`, `bankResources` = {WoolResourceStack{1}}, `ownerResources` = {WoolResourceStack{0}} | `bankResources` = {WoolResourceStack{0}}, `ownerResources` = {WoolResourceStack{1}} | :x: |
| Test Case 2 | `products` stack type: `GrainResourceStack`, `bankResources` = {GrainResourceStack{2}}, `ownerResources` = {GrainResourceStack{1}} | `bankResources` = {GrainResourceStack{1}}, `ownerResources` = {GrainResourceStack{2}} | :x: |
| Test Case 3 | `products` stack type: `OreResourceStack`, `bankResources` = {OreResourceStack{5}}, `ownerResources` = {OreResourceStack{2}} | `bankResources` = {OreResourceStack{4}}, `ownerResources` = {OreResourceStack{3}} | :x: |
| Test Case 4 | `products` stack type: `LumberResourceStack`, `bankResources` = {LumberResourceStack{19}}, `ownerResources` = {LumberResourceStack{0}} | `bankResources` = {LumberResourceStack{18}}, `ownerResources` = {LumberResourceStack{1}} | :x: |
| Test Case 4 | `products` stack type: `BrickResourceStack`, `bankResources` = {BrickResourceStack{1}}, `ownerResources` = {BrickResourceStack{18}} | `bankResources` = {BrickResourceStack{0}}, `ownerResources` = {BrickResourceStack{19}} | :x: |


### Method under test: `onBoughtEffect()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| No concrete inputs, only state: `bankResources`, state: `ownerResources` | No concrete outputs, `ownerResources` should update to have one less Grain, Lumber, Brick, Wool and `bankResources` should have one more. |

**Step 2: Data types**
| Variable | Type |
|---|---|
|`bankResources`| Count |
|`ownerResources`| Count |
| Output | void |

**Step 3: Concrete values**
| Variable | Values |
| `bankResources` | zero, one, greater than one, eighteen (max number of resource cards for a given type minus one) |
| `ownerResources` | one, greater than one, nineteen (max number of resource cards for a given type) |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | `bankResources` = {WoolResourceStack{0}, GrainResourceStack{0}, BrickResourceStack{0}, LumberResourceStack{0}}, `ownerResources` = {WoolResourceStack{19}, GrainResourceStack{19}, BrickResourceStack{19}, LumberResourceStack{19}} | `bankResources` = {WoolResourceStack{1}, GrainResourceStack{1}, BrickResourceStack{1}, LumberResourceStack{1}}, `ownerResources` = {WoolResourceStack{18}, GrainResourceStack{18}, BrickResourceStack{18}, LumberResourceStack{18}} | :x: |
| Test Case 2 | `bankResources` = {WoolResourceStack{1}, GrainResourceStack{1}, BrickResourceStack{1}, LumberResourceStack{1}}, `ownerResources` = {WoolResourceStack{1}, GrainResourceStack{1}, BrickResourceStack{1}, LumberResourceStack{1}} | `bankResources` = {WoolResourceStack{2}, GrainResourceStack{2}, BrickResourceStack{2}, LumberResourceStack{2}}, `ownerResources` = {WoolResourceStack{0}, GrainResourceStack{0}, BrickResourceStack{0}, LumberResourceStack{0}} | :x: |
| Test Case 3 | `bankResources` = {WoolResourceStack{4}, GrainResourceStack{5}, BrickResourceStack{6}, LumberResourceStack{7}}, `ownerResources` = {WoolResourceStack{2}, GrainResourceStack{3}, BrickResourceStack{4}, LumberResourceStack{5}} | `bankResources` = {WoolResourceStack{5}, GrainResourceStack{6}, BrickResourceStack{7}, LumberResourceStack{8}}, `ownerResources` = {WoolResourceStack{1}, GrainResourceStack{2}, BrickResourceStack{3}, LumberResourceStack{4}} | :x: |
| Test Case 4 | `bankResources` = {WoolResourceStack{18}, GrainResourceStack{18}, BrickResourceStack{18}, LumberResourceStack{18}}, `ownerResources` = {WoolResourceStack{1}, GrainResourceStack{1}, BrickResourceStack{1}, LumberResourceStack{1}} | `bankResources` = {WoolResourceStack{19}, GrainResourceStack{19}, BrickResourceStack{19}, LumberResourceStack{19}}, `ownerResources` = {WoolResourceStack{0}, GrainResourceStack{0}, BrickResourceStack{0}, LumberResourceStack{0}} | :x: |


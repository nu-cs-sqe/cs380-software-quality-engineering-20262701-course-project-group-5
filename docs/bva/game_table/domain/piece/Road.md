# Class Road

Classpath: catan.game_table.domain.piece.Road

## `class Road extends Piece`

A subclass of `Piece` that costs a `BrickResourceStack` and `WoodResourceStack` of size one.

### `public void onBoughtEffect()`

Decrements the `ResourceStack`s of the `GamePlayer` purchasing the `Road` by its cost.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a `Road` in terms of `ResourceStack`s, in this case one `BrickResourceStack` and `LumberResourceStack`.

## Boundary-Value Analysis

### Method under test: `onBoughtEffect()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| No concrete inputs, only state: `bankResources`, state: `ownerResources` | No concrete outputs, `ownerResources` should update to have one less Lumber and Brick and `bankResources` should have one more. |

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
| Test Case 1 | `bankResources` = {BrickResourceStack{0}, LumberResourceStack{0}}, `ownerResources` = {BrickResourceStack{19}, LumberResourceStack{19}} | `bankResources` = {BrickResourceStack{1}, LumberResourceStack{1}}, `ownerResources` = {BrickResourceStack{18}, LumberResourceStack{18}} | :x: |
| Test Case 2 | `bankResources` = {BrickResourceStack{1}, LumberResourceStack{1}}, `ownerResources` = {BrickResourceStack{1}, LumberResourceStack{1}} | `bankResources` = {BrickResourceStack{2}, LumberResourceStack{2}}, `ownerResources` = {BrickResourceStack{0}, LumberResourceStack{0}} | :x: |
| Test Case 3 | `bankResources` = {BrickResourceStack{6}, LumberResourceStack{7}}, `ownerResources` = {BrickResourceStack{4}, LumberResourceStack{5}} | `bankResources` = {BrickResourceStack{7}, LumberResourceStack{8}}, `ownerResources` = {BrickResourceStack{3}, LumberResourceStack{4}} | :x: |
| Test Case 4 | `bankResources` = {BrickResourceStack{18}, LumberResourceStack{18}}, `ownerResources` = {BrickResourceStack{1}, LumberResourceStack{1}} | `bankResources` = {BrickResourceStack{19}, LumberResourceStack{19}}, `ownerResources` = {BrickResourceStack{0}, LumberResourceStack{0}} | :x: |
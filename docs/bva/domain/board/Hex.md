# Class Hex
Classpath: catan.game_table.domain.board.Hex

Direct known subclasses: `HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`, `DesertHex`, `OceanHex`

## `public abstract class Hex extends Location`

A terrain tile with a token number, six vertices, six edges, and a set of resource products. Distributes its products to the pieces occupying it when produced, and can hold the Robber (through `Location`).

### `public Hex(Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`
Initializes a `Hex`. Throws `IllegalArgumentException` if `vertices` or `edges` does not contain exactly 6 elements (including OceanHex).

### `public boolean canProduce()`
Returns whether this hex currently produces resources (false while the robber sits on it, or for desert and ocean).

### `public int getTokenValue()`
Returns the token number of this hex.

### `public void setCanProduce(boolean canProduce)`
Sets the `canProduce` flag to the given value. `produceResources()` only runs when the flag is true. Assumes the boolean parameter (missing from diagram). `DesertHex` and `OceanHex` override it.

### `public void produceResources()`
If `canProduce()`, passes `products` to each of its vertices, and the `Settlement` or `City` on each vertex gives its owner resources: a `Settlement` takes the tier-1 products and a `City` takes the tier-1 and tier-2 products. Otherwise does nothing.

## Boundary-Value Analysis
Inherited methods: see `Location`.

### Test: `public Hex(Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `vertices : Set<Vertex>`, `edges : Set<Edge>`, `products : ResourceProducts` | New `Hex`. Outcome is one of: hex created, `IllegalArgumentException`. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `vertices` | Collection (fixed size 6) |
| `edges` | Collection (fixed size 6) |
| `products` | Cases (a valid `ResourceProducts`) |
| outcome (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `vertices` | empty, exactly one, 5, 6, 7 |
| `edges` | empty, exactly one, 5, 6, 7 |
| `products` | a valid `ResourceProducts` |
| outcome (output) | hex created, IllegalArgumentException |

**Step 4: Each choice**

|             | Input                                  | Expected output            | Implemented? |
|-------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | 6 vertices, 6 edges, valid products    | hex created                | :x:          |
| Test Case 2 | `vertices` empty, valid others         | `IllegalArgumentException` | :x:          |
| Test Case 3 | `vertices` has 1 element               | `IllegalArgumentException` | :x:          |
| Test Case 4 | `vertices` has 5 elements              | `IllegalArgumentException` | :x:          |
| Test Case 5 | `vertices` has 7 elements              | `IllegalArgumentException` | :x:          |
| Test Case 6 | `edges` has 5 elements                 | `IllegalArgumentException` | :x:          |
| Test Case 7 | `edges` has 7 elements                 | `IllegalArgumentException` | :x:          |


### Test: `public boolean canProduce()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| state: `canProduce` flag | `boolean` |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `canProduce` flag | Boolean |
| returned value (output) | Boolean |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `canProduce` flag (need both ocean and desert hexes) | true, false |
| returned value (output) | true, false |

**Step 4: Each choice**

|             | Input                        | Expected output | Implemented? |
|-------------|------------------------------|-----------------|--------------|
| Test Case 1 | `ForestHex` just constructed | true            | :x:          |
| Test Case 2 | `DesertHex` just constructed | false           | :x:          |
| Test Case 3 | `OceanHex` just constructed  | false           | :x:          |


### Test: `public int getTokenValue()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| none; state: `tokenValue`, kind of hex (token-bearing, desert or ocean) | `int` |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `tokenValue` | Interval `[2, 12]` (excluding 7 on producing hexes) |
| hex kind | Cases (token-bearing hex, non-producing hex) |
| returned value (output) | Interval `[2, 12]`, or 0 on non-producing hexes |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `tokenValue` | 2 (LOW), 12 (HIGH) |
| hex kind | token-bearing hex, non-producing hex (desert and ocean) |
| returned value (output) | same as `tokenValue` (0 for desert and ocean; 1, 13 and 7 on a producing hex can't be set) |

**Step 4: Each choice**

|             | Input                       | Expected output | Implemented? |
|-------------|-----------------------------|-----------------|--------------|
| Test Case 1 | `ForestHex` with token `2`  | `2`             | :x:          |
| Test Case 2 | `ForestHex` with token `12` | `12`            | :x:          |
| Test Case 3 | `OceanHex`                  | `0`             | :x:          |
| Test Case 4 | `DesertHex`                 | `0`             | :x:          |

### Test: `public void setCanProduce(boolean canProduce)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `canProduce : boolean`; state: current flag, concrete class of the hex the method is called on | No return value. The flag holds the new value (always false on `DesertHex` and `OceanHex`). |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `canProduce` | Boolean |
| current flag | Boolean |
| concrete class | Cases (resource hex, `DesertHex`, `OceanHex`) |
| flag after the call (output) | Boolean |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `canProduce` | true, false |
| current flag | true, false |
| concrete class | `ForestHex` (chosen arbitrarily), `DesertHex`, `OceanHex` |
| flag after the call (output) | true, false |

**Step 4: All combinations** of `canProduce` x current flag for `ForestHex` Each choice for the other classes.

|             | Input                                      | Expected output                              | Implemented? |
|-------------|--------------------------------------------|----------------------------------------------|--------------|
| Test Case 1 | `ForestHex` with flag true, input `false`  | `canProduce()` false                         | :x:          |
| Test Case 2 | `ForestHex` with flag false, input `true`  | `canProduce()` true                          | :x:          |
| Test Case 3 | `ForestHex` with flag true, input `true`   | `canProduce()` true                          | :x:          |
| Test Case 4 | `ForestHex` with flag false, input `false` | `canProduce()` false                         | :x:          |
| Test Case 5 | `DesertHex` with flag true, input `false`  | `canProduce()` false                         | :x:          |
| Test Case 6 | `DesertHex` with flag false, input `true`  | `canProduce()` false (desert never produces) | :x:          |
| Test Case 7 | `OceanHex` with flag true, input `false`   | `canProduce()` false                         | :x:          |
| Test Case 8 | `OceanHex` with flag false, input `true`   | `canProduce()` false (ocean never produces)  | :x:          |

### Test: `public void produceResources()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| state: `canProduce`, buildings on the hex's vertices, `products`, concrete class of the hex | No return value. Outcome is one of: no resources change, Settlement owner receives tier-1 products, City owner receives tier-1 and tier-2 products. Also how many pieces are paid. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `canProduce` | Boolean |
| buildings on the hex's vertices | Collection |
| piece type | Cases |
| `products` | Collection of tier-1 and tier-2 stacks |
| concrete class | Cases (`HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`, `DesertHex`, `OceanHex`) |
| outcome (output) | Cases |
| pieces paid (output) | Count |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `canProduce` | true, false |
| buildings on the hex's vertices | empty, exactly one, more than one, 6 (maximum number of buildings bc 6 vertices) |
| piece type | Settlement, City, at least one of each together |
| `products` | tier 1 and tier 2 each non-empty |
| concrete class | `HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`, `DesertHex`, `OceanHex` |
| outcome (output) | no change, tier-1 only (Settlement), tier-1 and tier-2 (City) |
| pieces paid (output) | 0, 1, more than one, 6 |

**Step 4: All combinations** of `canProduce` x buildings on the hex's vertices (empty, exactly one, more than one, 6). Each choice for the piece type, the concrete class and the outputs. Test Cases 1 to 7, 20 and 21 use a `HillsHex` with tier 1 and tier 2 each `[BrickResourceStack size 1]`, so a Settlement receives size 1 and a City receives size 2 (tier 1 plus tier 2).

|              | Input                                                                                                       | Expected output                                                                                    | Implemented? |
|--------------|-------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | `canProduce` true, no buildings on the hex's vertices                                                       | no resources change, 0 pieces paid                                                                 | :x:          |
| Test Case 2  | `canProduce` true, one `Settlement` on one of the hex's vertices                                            | owner gains tier-1 products only, 1 piece paid                                                     | :x:          |
| Test Case 3  | `canProduce` true, one `City` on one of the hex's vertices                                                  | owner gains tier-1 and tier-2 products, 1 piece paid                                               | :x:          |
| Test Case 4  | `canProduce` true, buildings on 3 of the hex's vertices: two `Settlement` and one `City`                    | Settlement owners gain tier-1 products, City owner gains tier-1 and tier-2 products, 3 pieces paid | :x:          |
| Test Case 5  | `canProduce` true, a `Settlement` on each of the hex's 6 vertices                                           | each owner gains tier-1 products, 6 pieces paid                                                    | :x:          |
| Test Case 6  | `canProduce` false, one `Settlement` on one of the hex's vertices                                           | no resources change, 0 pieces paid                                                                 | :x:          |
| Test Case 7  | `canProduce` false, no buildings on the hex's vertices                                                      | no resources change, no exception, 0 pieces paid                                                   | :x:          |
| Test Case 8  | `HillsHex` with tier 1 and tier 2 each `[BrickResourceStack size 1]`, `canProduce` true, one `Settlement`   | owner gains `BrickResourceStack` with size `1`, 1 piece paid                                       | :x:          |
| Test Case 9  | same `HillsHex`, one `City`                                                                                 | owner gains `BrickResourceStack` with size `2`, 1 piece paid                                       | :x:          |
| Test Case 10 | `ForestHex` with tier 1 and tier 2 each `[LumberResourceStack size 1]`, `canProduce` true, one `Settlement` | owner gains `LumberResourceStack` with size `1`, 1 piece paid                                      | :x:          |
| Test Case 11 | same `ForestHex`, one `City`                                                                                | owner gains `LumberResourceStack` with size `2`, 1 piece paid                                      | :x:          |
| Test Case 12 | `MountainsHex` with tier 1 and tier 2 each `[OreResourceStack size 1]`, `canProduce` true, one `Settlement` | owner gains `OreResourceStack` with size `1`, 1 piece paid                                         | :x:          |
| Test Case 13 | same `MountainsHex`, one `City`                                                                             | owner gains `OreResourceStack` with size `2`, 1 piece paid                                         | :x:          |
| Test Case 14 | `FieldsHex` with tier 1 and tier 2 each `[GrainResourceStack size 1]`, `canProduce` true, one `Settlement`  | owner gains `GrainResourceStack` with size `1`, 1 piece paid                                       | :x:          |
| Test Case 15 | same `FieldsHex`, one `City`                                                                                | owner gains `GrainResourceStack` with size `2`, 1 piece paid                                       | :x:          |
| Test Case 16 | `PastureHex` with tier 1 and tier 2 each `[WoolResourceStack size 1]`, `canProduce` true, one `Settlement`  | owner gains `WoolResourceStack` with size `1`, 1 piece paid                                        | :x:          |
| Test Case 17 | same `PastureHex`, one `City`                                                                               | owner gains `WoolResourceStack` with size `2`, 1 piece paid                                        | :x:          |
| Test Case 18 | `DesertHex`, one `Settlement` on one of its vertices                                                        | no resources change, 0 pieces paid                                                                 | :x:          |
| Test Case 19 | `OceanHex`, one `Settlement` on one of its vertices                                                         | no resources change, 0 pieces paid                                                                 | :x:          |
| Test Case 20 | `canProduce` false, buildings on 3 of the hex's vertices: two `Settlement` and one `City`                   | no resources change, 0 pieces paid                                                                 | :x:          |
| Test Case 21 | `canProduce` false, a `Settlement` on each of the hex's 6 vertices                                          | no resources change, 0 pieces paid                                                                 | :x:          |
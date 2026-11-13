# Class Hex
Classpath: catan.game_table.domain.board.Hex

Direct known subclasses: `HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`, `DesertHex`, `OceanHex`

## `public abstract class Hex extends Location`

A terrain tile with a token number, six vertices, six edges, and a set of resource products. Passes its products to its vertices when produced, and can hold the Robber (through `Location`). Distributing resources to players is not done here.

### `public Hex(int tokenValue, Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`
Initializes a `Hex` with the given token number. Throws `IllegalArgumentException` if `vertices` or `edges` does not contain exactly 6 elements (including OceanHex), or if they do not form a hexagon: the 6 edges must connect the 6 vertices in one closed ring.

### `public boolean canProduce()`
Returns whether this hex currently produces resources (false while the robber sits on it, or for desert and ocean).

### `public int getTokenValue()`
Returns the token number of this hex.

### `public void setCanProduce(boolean canProduce)`
Sets the `canProduce` flag to the given value. `produceResources()` only runs when the flag is true. Assumes the boolean parameter (missing from diagram). `DesertHex` and `OceanHex` override it.

### `public void produceResources()`
If `canProduce()`, calls `Vertex.produceResources()` on each of its 6 vertices, passing `products`. Otherwise does nothing.

## Boundary-Value Analysis
Inherited methods: see `Location`.

### Test: `public Hex(int tokenValue, Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `tokenValue : int`, `vertices : Set<Vertex>`, `edges : Set<Edge>`, `products : ResourceProducts` | New `Hex`. Outcome is one of: hex created, `IllegalArgumentException`. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `vertices` | Collection (fixed size 6) |
| `edges` | Collection (fixed size 6) |
| edges' endpoints vs `vertices` | Collection built with references (the structure must be circular) |
| `products` | Cases (a valid `ResourceProducts`) |
| `tokenValue` | Cases (a valid token; the boundary values are tested through `getTokenValue()`) |
| outcome (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `vertices` | empty, exactly one, 5, 6, 7 |
| `edges` | empty, exactly one, 5, 6, 7 |
| edges' endpoints vs `vertices` | one closed ring through all 6 vertices; circular but split into two rings; not circular (open chain); an endpoint that is not in `vertices` |
| `products` | a valid `ResourceProducts` |
| `tokenValue` | a valid token |
| outcome (output) | hex created, IllegalArgumentException |

**Step 4: Each choice**

|              | Input                                                                               | Expected output            | Implemented?       |
|--------------|-------------------------------------------------------------------------------------|----------------------------|--------------------|
| Test Case 1  | 6 vertices, 6 edges forming one closed ring through them, valid products and token  | hex created                | :white_check_mark: |
| Test Case 2  | `vertices` empty, valid others                                                      | `IllegalArgumentException` | :white_check_mark: |
| Test Case 3  | `vertices` has 1 element                                                            | `IllegalArgumentException` | :white_check_mark: |
| Test Case 4  | `vertices` has 5 elements                                                           | `IllegalArgumentException` | :white_check_mark: |
| Test Case 5  | `vertices` has 7 elements                                                           | `IllegalArgumentException` | :white_check_mark: |
| Test Case 6  | `edges` has 5 elements                                                              | `IllegalArgumentException` | :white_check_mark: |
| Test Case 7  | `edges` has 7 elements                                                              | `IllegalArgumentException` | :white_check_mark: |
| Test Case 8  | 6 vertices, 6 edges, one edge has an endpoint that is not in `vertices`             | `IllegalArgumentException` | :white_check_mark: |
| Test Case 9  | 6 vertices, 6 edges forming two separate triangles (circular but not one hexagon)   | `IllegalArgumentException` | :white_check_mark: |
| Test Case 10 | 6 vertices, 6 edges not forming a ring: open chain of 5 edges plus a duplicate edge | `IllegalArgumentException` | :white_check_mark: |
| Test Case 11 | 6 vertices, no edges, valid products and token                                      | `IllegalArgumentException` | :white_check_mark: |
| Test Case 12 | 6 vertices, exactly one edge joining two of them, valid products and token          | `IllegalArgumentException` | :white_check_mark: |


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
| Test Case 5 | `DesertHex` with flag false, input `false` | `canProduce()` false                         | :x:          |
| Test Case 6 | `DesertHex` with flag false, input `true`  | `canProduce()` false (desert never produces) | :x:          |
| Test Case 7 | `OceanHex` with flag false, input `false`  | `canProduce()` false                         | :x:          |
| Test Case 8 | `OceanHex` with flag false, input `true`   | `canProduce()` false (ocean never produces)  | :x:          |

### Test: `public void produceResources()`

`Hex` only passes its products to its vertices.

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| state: `canProduce`, the hex's 6 vertices, `products`, concrete class of the hex | No return value. Outcome is one of: `Vertex.produceResources()` is called on all 6 vertices with `products`, no vertex is called. Also how many vertices are called. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `canProduce` | Boolean |
| the hex's vertices | Collection (fixed size 6) |
| `products` | Cases (a valid `ResourceProducts`) |
| concrete class | Cases (resource hex, `DesertHex`, `OceanHex`) |
| outcome (output) | Cases |
| vertices called (output) | Count |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `canProduce` | true, false |
| the hex's vertices | 6 |
| `products` | a valid `ResourceProducts` |
| concrete class | `ForestHex` (chosen arbitrarily), `DesertHex`, `OceanHex` |
| outcome (output) | all 6 vertices called with `products`, no vertex called |
| vertices called (output) | 0, 6 (maximum) |

**Step 4: All combinations** of `canProduce` for `ForestHex`. Each choice for the other classes.

|             | Input                               | Expected output                                                                       | Implemented? |
|-------------|-------------------------------------|---------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `ForestHex` with `canProduce` true  | `Vertex.produceResources()` called once on each of the 6 vertices, passing `products` | :x:          |
| Test Case 2 | `ForestHex` with `canProduce` false | no vertex is called                                                                   | :x:          |
| Test Case 3 | `DesertHex`                         | no vertex is called                                                                   | :x:          |
| Test Case 4 | `OceanHex`                          | no vertex is called                                                                   | :x:          |

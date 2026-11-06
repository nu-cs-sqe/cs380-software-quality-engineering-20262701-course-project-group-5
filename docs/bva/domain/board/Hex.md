# Class Hex
Classpath: catan.game_table.domain.board.Hex

Direct known subclasses: `HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`, `DesertHex`, `OceanHex`

## `public abstract class Hex`

A terrain tile with a token number, six vertices, six edges, and a set of resource products. Distributes its products to the pieces occupying it when produced.

### `public Hex(Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`
Initializes a `Hex`. Throws `IllegalArgumentException` if any argument is `null` or if `vertices` or `edges` does not contain exactly 6 elements (this might need to change for the OceanHex?).

### `public boolean canProduce()`
Returns whether this hex currently produces resources (false while the robber sits on it, or for desert and ocean).

### `public int getTokenValue()`
Returns the token number of this hex.

### `public void setCanProduce(boolean canProduce)`
Sets the `canProduce` flag to the given value. `produceResources()` only runs when the flag is true. Assumes the boolean parameter (missing from diagram). `DesertHex` and `OceanHex` override it.

### `public void produceResources()`
If `canProduce()`, sends `products` to each piece in `occupyingPieces`. Otherwise does nothing. A `Settlement` takes the tier-1 products and a `City` takes the tier-1 and tier-2 products. Resource subclasses (`HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`) override this to produce their specific resource.

### Test: `public Hex(Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `vertices : Set<Vertex>`, `edges : Set<Edge>`, `products : ResourceProducts` | New `Hex`. Outcome is one of: hex created, `IllegalArgumentException`. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `vertices` | Pointer, Collection (fixed size 6) |
| `edges` | Pointer, Collection (fixed size 6) |
| `products` | Pointer |
| outcome (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `vertices` | null, empty, exactly one, 5, 6, 7 |
| `edges` | null, empty, exactly one, 5, 6, 7 |
| `products` | null, real object |
| outcome (output) | hex created, IllegalArgumentException |

**Step 4: Each choice**

|              | Input                                  | Expected output            | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1  | 6 vertices, 6 edges, valid products    | hex created                | :x:          |
| Test Case 2  | `vertices` is `null`                   | `IllegalArgumentException` | :x:          |
| Test Case 3  | `edges` is `null`                      | `IllegalArgumentException` | :x:          |
| Test Case 4  | `products` is `null`                   | `IllegalArgumentException` | :x:          |
| Test Case 5  | `vertices` empty, valid others         | `IllegalArgumentException` | :x:          |
| Test Case 6  | `vertices` has 1 element               | `IllegalArgumentException` | :x:          |
| Test Case 7  | `vertices` has 5 elements              | `IllegalArgumentException` | :x:          |
| Test Case 8  | `vertices` has 7 elements              | `IllegalArgumentException` | :x:          |
| Test Case 9  | `edges` has 5 elements                 | `IllegalArgumentException` | :x:          |
| Test Case 10 | `edges` has 7 elements                 | `IllegalArgumentException` | :x:          |


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
| `canProduce` flag | true, false |
| returned value (output) | true, false |

**Step 4: Each choice**

|             | Input                        | Expected output | Implemented? |
|-------------|------------------------------|-----------------|--------------|
| Test Case 1 | `ForestHex` just constructed | true            | :x:          |
| Test Case 2 | `DesertHex` just constructed | false           | :x:          |


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
| hex kind | token-bearing hex, non-producing hex (desert or ocean) |
| returned value (output) | same as `tokenValue` (0 for desert and ocean; 1, 13 and 7 on a producing hex can't be set) |

**Step 4: Each choice**

|             | Input                       | Expected output | Implemented? |
|-------------|-----------------------------|-----------------|--------------|
| Test Case 1 | `ForestHex` with token `2`  | `2`             | :x:          |
| Test Case 2 | `ForestHex` with token `12` | `12`            | :x:          |
| Test Case 4 | `DesertHex`                 | `0`             | :x:          |
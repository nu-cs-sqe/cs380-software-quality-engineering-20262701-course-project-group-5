# Class Vertex
Classpath: catan.game_table.domain.board.Vertex

## `public class Vertex extends Location`

A corner of one or more hexes where buildings and harbors may sit.

### `public Vertex(Point endpoint)`
Initializes a `Vertex` at `endpoint`. Throws `IllegalArgumentException` if `endpoint` is `null`.

### `public Point getPoint()`
Returns this vertex's point.

### `public void produceResources(ResourceProducts products)`
Passes `products` to every `Building` at this vertex: a `Settlement` takes the tier-1 products and a `City` takes the tier-1 and tier-2 products. Pieces that are not buildings (a `Harbor`) are skipped. Throws `IllegalArgumentException` if `products` is `null`.

## Boundary-Value Analysis

Inherited methods: see `Location`.

### Test: `public Vertex(Point endpoint)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `endpoint : Point` | New `Vertex`. Outcome is one of: vertex created, `IllegalArgumentException`. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `endpoint` | Pointer |
| outcome (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `endpoint` | null, true object |
| outcome (output) | vertex created, IllegalArgumentException |

**Step 4: Each choice**

|             | Input                      | Expected output                | Implemented? |
|-------------|----------------------------|--------------------------------|--------------|
| Test Case 1 | `endpoint` is `null`       | `IllegalArgumentException`     | :x:          |
| Test Case 2 | `endpoint` is `Point(0, 0)` | vertex created with no pieces | :x:          |

### Test: `public Point getPoint()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| none; state: the vertex's point | `Point` |

**Step 2: Data types**
| Variable | Type |
|---|---|
| point | Pointer |
| returned value (output) | Pointer |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| point | true object (null can't be set: the constructor rejects it) |
| returned value (output) | true object |

**Step 4: Each choice**

|             | Input                          | Expected output           | Implemented? |
|-------------|--------------------------------|---------------------------|--------------|
| Test Case 1 | vertex built from `Point(3, 4)` | the same `Point` object  | :x:          |

### Test: `public void produceResources(ResourceProducts products)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `products : ResourceProducts` (its tier-1 and tier-2 products); state: the pieces at the vertex | none (`void`). Outcome is one of: `IllegalArgumentException`, no resources change, owner receives tier-1 products only, owner receives tier-1 and tier-2 products. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Pointer |
| tier-1 products | Collection |
| tier-2 products | Collection |
| pieces at vertex | Collection |
| piece type | Cases (Settlement, City, Harbor) |
| outcome (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | null, true object |
| tier-1 products | empty, non-empty |
| tier-2 products | empty, non-empty |
| pieces at vertex | empty, exactly one, more than one (the maximum is 2: a building plus a Harbor) |
| piece type | Settlement, City, Harbor |
| outcome (output) | IllegalArgumentException, no change, tier-1 only, tier-1 and tier-2 |

**Step 4: Each choice** (each piece type is exercised with non-empty products)

|             | Input                                                    | Expected output                                           | Implemented? |
|-------------|----------------------------------------------------------|-----------------------------------------------------------|--------------|
| Test Case 1 | `products` is `null`                                     | `IllegalArgumentException`                                | :x:          |
| Test Case 2 | no pieces, valid products                                | no resources change                                       | :x:          |
| Test Case 3 | one `Settlement`, tier-1 and tier-2 both non-empty       | owner gains tier-1 products only                          | :x:          |
| Test Case 4 | one `City`, tier-1 and tier-2 both non-empty             | owner gains tier-1 and tier-2 products                    | :x:          |
| Test Case 5 | one `Settlement`, tier-1 and tier-2 both empty           | no resources change                                       | :x:          |
| Test Case 6 | one `Harbor` only, tier-1 and tier-2 both non-empty      | no resources change                             | :x:          |
| Test Case 7 | a `Settlement` then a `Harbor`, both tiers non-empty     | `Settlement` owner gains tier-1 products only, `Harbor` ignored | :x: |
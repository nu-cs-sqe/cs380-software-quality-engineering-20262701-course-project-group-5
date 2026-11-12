# Class Vertex
Classpath: catan.game_table.catan.game_table.board.domain.Vertex

## `public class Vertex extends Location`

A corner of one or more hexes where a building (`Settlement` or `City`) may sit.

### `public Vertex(Point endpoint)`
Initializes a `Vertex` at `endpoint`.

### `public Point getPoint()`
Returns this vertex's point.

### `public void produceResources(ResourceProducts products)`
Calls `Building.produceResources()` on the building at this vertex, passing `products`. Does nothing if there is no building.

## Boundary-Value Analysis

Inherited methods: see `Location`.

### Test: `public void produceResources(ResourceProducts products)`

`Vertex` only passes `products` to its building. What the building does with them (including sending them on to the `Bank`) is tested in `Building`, `Settlement` and `City`.

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `products : ResourceProducts`; state: the pieces at the vertex | No return value. Outcome is one of: `Building.produceResources()` is called on the building with `products`, no call. Also what buildings are called. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | Cases (a valid `ResourceProducts`) |
| pieces at vertex | Collection |
| outcome (output) | Cases |
| buildings called (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | a valid `ResourceProducts` |
| pieces at vertex | empty, exactly one (a vertex holds at most one building, so more than one can't be set) |
| outcome (output) | building called with `products`, no call |
| buildings called (output) | None, Settlement, City |

**Step 4: Each choice**

|             | Input                            | Expected output                                                     | Implemented? |
|-------------|----------------------------------|---------------------------------------------------------------------|--------------|
| Test Case 1 | no pieces, valid products        | no building is called, no exception                                 | :x:          |
| Test Case 2 | one `Settlement`, valid products | `Building.produceResources()` called once on it, passing `products` | :x:          |
| Test Case 3 | one `City`, valid products       | `Building.produceResources()` called once on it, passing `products` | :x:          |
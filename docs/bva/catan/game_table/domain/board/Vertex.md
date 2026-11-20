# Class Vertex
Classpath: catan.game_table.domain.board.Vertex

## `public class Vertex extends Location`

A corner of one or more hexes where a building (`Settlement` or `City`) may sit.

### `public Vertex(Point endpoint)`
Initializes a `Vertex` at `endpoint`.

### `public Point getPoint()`
Returns this vertex's point.

### `public void produceResources(List<ResourceStack> products)`
Each producing hex calls this method separately with a list containing one resource stack of size 1. If a building occupies the vertex, `Vertex` passes the list unchanged to `Building.produceResources()` once per call. The building determines the final production quantity. An empty vertex makes no production call.

## Boundary-Value Analysis

Inherited methods: see `Location`.

### Test: `public void produceResources(List<ResourceStack> products)`

`Vertex` only passes `products` to its building. What the building does with them (including sending them on to the `Bank`) is tested in `Building`, `Settlement` and `City`.

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `products : List<ResourceStack>`; state: the pieces at the vertex | No return value. Outcome is one of: `Building.produceResources()` is called on the building with `products`, no call. Also what buildings are called. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `products` | List containing exactly one resource stack per call |
| pieces at vertex | Collection |
| outcome (output) | Cases |
| buildings called (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `products` | a list containing one brick stack of size 1 |
| pieces at vertex | empty, exactly one (a vertex holds at most one building, so more than one can't be set) |
| outcome (output) | building called with `products`, no call |
| buildings called (output) | None, Settlement, City |

**Step 4: Each choice**

|             | Input                                               | Expected output                                           | Implemented?       |
|-------------|-----------------------------------------------------|-----------------------------------------------------------|--------------------|
| Test Case 1 | No building, list with one brick of size 1          | No building is called                                     | :white_check_mark: |
| Test Case 2 | One `Settlement`, list with a brick stack of size 1 | The settlement receives the one-stack list unchanged once | :x:                |
| Test Case 3 | One `City`, list with a brick stack of size 1       | The city receives the one-stack list unchanged once       | :x:                |

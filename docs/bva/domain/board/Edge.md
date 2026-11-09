# Class Edge
Classpath: catan.game_table.domain.board.Edge

## `public class Edge extends Location`

A side shared by up to two hexes. Roads and harbors are placed here.

### `public Edge(Point endpoint1, Point endpoint2)`
Initializes an `Edge` between two points. Throws `IllegalArgumentException` if the two points are equal, or if the two points are not adjacent.

### `public Set<Point> getEndpoints()`
Returns the two endpoints as a set (order does not matter).

## Boundary-Value Analysis

Inherited methods: see `Location`.

### Test: `public Edge(Point endpoint1, Point endpoint2)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `endpoint1 : Point`, `endpoint2 : Point` | New `Edge`. Outcome is one of: edge created, `IllegalArgumentException`. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `endpoint1` | Cases (a valid `Point`) |
| `endpoint2` | Cases (a valid `Point`) |
| `endpoint1` vs `endpoint2` | Pair of References |
| `endpoint1` vs `endpoint2` (adjacency) | Cases (adjacent, not adjacent) |
| outcome (output) | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `endpoint1` | a valid `Point` |
| `endpoint2` | a valid `Point` |
| `endpoint1` vs `endpoint2` | two references to the same object, different objects with equal x and y, different points |
| `endpoint1` vs `endpoint2` (adjacency) | different points that are adjacent, different points that are not adjacent |
| outcome (output) | edge created, IllegalArgumentException |

**Step 4: Each choice**

|             | Input                              | Expected output                          | Implemented? |
|-------------|------------------------------------|------------------------------------------|--------------|
| Test Case 1 | the same `Point` object for both   | `IllegalArgumentException`               | :x:          |
| Test Case 2 | two `Point(1, 1)` objects          | `IllegalArgumentException`               | :x:          |
| Test Case 3 | `Point(0, 0)` and `Point(1, 0)`    | edge created                             | :x:          |
| Test Case 4 | `Point(0, 0)` and `Point(5, 5)`    | `IllegalArgumentException`               | :x:          |
# Class PlacementHelper
Classpath: catan.game_table.domain.board.PlacementHelper

## `class PlacementHelper`
Package-private helper that decides where pieces may legally be placed and performs placement on the `GameBoard`. Uses `GamePhaseHolder` to know whether the game is in `SETUP` or `REGULAR` phase.

### `public List<Vertex> getValidBuildingSpots()`
Returns vertices where the active player may place a building. A vertex is valid if it is unoccupied and no adjacent vertex has a building (using the distance rule). In `REGULAR` phase it must also connect to one of the player's own roads. In `SETUP` phase no road is required.

### `public List<Edge> getValidRoadSpots()`
Returns unoccupied edges where the active player may place a road. In `REGULAR` phase the edge must connect to the player's own pieces. In `SETUP` phase the edge must touch the settlement just placed.

### `public void place(Piece piece, Location location)`
Places `piece` at `location` via the board. Throws `IllegalArgumentException` if `location` is not currently a valid spot for `piece`. A `City` is valid only on a vertex holding the active player's own `Settlement`.

## Boundary-Value Analysis

Own refers to the active player's color. I use Red for the active player and Blue for an opponent (arbitrary).

### Test: `public List<Vertex> getValidBuildingSpots()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| state: `gamePhase`, distance from each vertex to the nearest building (0 means the vertex itself is occupied), who owns that building, and the roads touching each vertex (how many are the active player's, and who owns them) | `List<Vertex>`. The tests check which vertices are returned as well as its size: empty, exactly one element, more than one element, maximum. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `gamePhase` | Cases (SETUP, REGULAR) |
| distance to nearest building | Count (edges away) |
| owner of the nearest building | Cases (own, other color) |
| own roads touching vertex | Count |
| owner of a road touching vertex | Cases (own, other color) |
| returned list (output) | Collection |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `gamePhase` | SETUP, REGULAR |
| distance to nearest building | 0 (vertex occupied), 1 (too close), 2 (smallest allowed distance) |
| owner of the nearest building | own, other color |
| own roads touching vertex | 0, 1, more than one |
| owner of a road touching vertex | own, other color |
| returned list (output) | empty, exactly one element, more than one element, maximum (54 vertices on an empty board) |

**Step 4: All combinations** of `gamePhase` x own roads touching vertex since road rule only applies in REGULAR phase. Each choice for all else.

|              | Input                                                                                 | Expected output                                                                                    | Implemented? |
|--------------|---------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | SETUP, empty board                                                                    | all 54 vertices                                                                                    | :x:          |
| Test Case 2  | REGULAR, empty board, no roads                                                        | empty list                                                                                         | :x:          |
| Test Case 3  | SETUP, a `RED` settlement on vertex V                                                 | every vertex except V (distance 0) and V's neighbors (distance 1); vertices at distance 2 included | :x:          |
| Test Case 4  | REGULAR, one `RED` road between vertices A and B, no buildings anywhere               | exactly `[A, B]`                                                                                   | :x:          |
| Test Case 5  | REGULAR, one `RED` road between A and B, a `BLUE` settlement on A                     | empty list (A is occupied, B is distance 1 from the settlement)                                    | :x:          |
| Test Case 6  | REGULAR, two separate `RED` roads A to B and C to D, no buildings, far apart          | exactly `[A, B, C, D]`, no duplicates                                                              | :x:          |
| Test Case 7  | REGULAR, `RED` roads A to B and B to C, no buildings                                  | exactly `[A, B, C]`, with B appearing once                                                         | :x:          |
| Test Case 8  | SETUP, every vertex is occupied or next to a building                                 | empty list                                                                                         | :x:          |
| Test Case 9  | REGULAR, one `BLUE` road between A and B, `RED` has no roads, no buildings            | empty list (only own roads count)                                                                  | :x:          |
| Test Case 10 | REGULAR, `RED` settlement S with `RED` roads S to V1 and V1 to V2, no other buildings | exactly `[V2]` (V1 is too close to S)                                                              | :x:          |
| Test Case 11 | SETUP, one `RED` road between A and B, no buildings                                   | all 54 vertices (a road is not required in SETUP)                                                  | :x:          |
| Test Case 12 | SETUP, `RED` roads A to B and B to C, no buildings                                    | all 54 vertices, B appearing once                                                                  | :x:          |

# Class PlacementHelper
Classpath: catan.game_table.domain.board.PlacementHelper

## `class PlacementHelper`
Package-private helper that decides where pieces may legally be placed and performs placement on the `GameBoard`.

### `public List<Vertex> getValidBuildingSpots(Color color)`
Returns vertices where the player with `color` may place a building. A vertex is valid if it is unoccupied and no adjacent vertex has a building (using the distance rule). In `REGULAR` phase it must also connect to one of that player's own roads. In `SETUP` phase no road is required.

### `public List<Edge> getValidRoadSpots(Color color)`
Returns unoccupied edges where the player with `color` may place a road. In `REGULAR` phase the edge must connect to that player's own pieces. In `SETUP` phase the edge must touch the settlement just placed.

### `public void place(Piece piece, Location location) throws InvalidPlacementException`
Places `piece` at `location` via the board. Throws the checked exception `IllegalPlacementException` if `location` is not currently a valid spot for `piece`. A spot is valid only if all of these hold: `location` is on this board; the piece type fits the location type (`Road` and `Harbor` on `Edge` only; `Settlement` and `City` on `Vertex` only; `Robber` on `Hex` only); and `location` does not already hold a second building (`Settlement`/`City`), `Road` or `Harbor`. 
A `City` is valid only on a vertex holding the active player's own `Settlement`. A `Road` and a `Harbor` may share an `Edge` (assumed). The `Robber` rules are assumed.

## Boundary-Value Analysis

Own refers to the `color` passed to `getValidBuildingSpots` and `getValidRoadSpots` (the active player's color). I use Red for active player's color and Blue for an opponent (arbitrary).

### Test: `public List<Vertex> getValidBuildingSpots(Color color)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `color : Color`; state: `gamePhase`, distance from each vertex to the nearest building (0 means the vertex itself is occupied), who owns that building, and the roads touching each vertex (how many belong to `color`, and who owns them) | `List<Vertex>`. The tests check which vertices are returned: empty, exactly one element, more than one element, maximum. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `color` | Cases (RED, Not RED) |
| `gamePhase` | Cases (SETUP, REGULAR) |
| distance to nearest building | Count (edges away) |
| owner of the nearest building | Cases (own, other color) |
| own roads touching vertex | Count |
| owner of a road touching vertex | Cases (own, other color) |
| returned list (output) | Collection |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `color` | RED, BLUE (the other player colors behave the same) |
| `gamePhase` | SETUP, REGULAR |
| distance to nearest building | 0 (vertex occupied), 1 (too close), 2 (smallest allowed distance) |
| owner of the nearest building | own, other color |
| own roads touching vertex | 0, 1, more than one |
| owner of a road touching vertex | own, other color |
| returned list (output) | empty, exactly one element, more than one element, maximum (54 vertices in SETUP on an empty board) |

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
| Test Case 13 | REGULAR, called with `BLUE`, one `BLUE` road between A and B, no buildings anywhere   | exactly `[A, B]` (own means the color passed in)                                                   | :x:          |

### Test: `public List<Edge> getValidRoadSpots(Color color)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `color : Color`; state: `gamePhase`, whether each edge is occupied, the pieces touching each edge (how many belong to `color`, and who owns them), the settlement just placed in SETUP | `List<Edge>`. The tests check which edges are returned: empty, exactly one element, more than one element. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `color` | Cases (RED, Not RED) |
| `gamePhase` | Cases (SETUP, REGULAR) |
| own pieces touching edge | Count |
| owner of a piece touching edge | Cases (own, other color) |
| edge occupied | Boolean |
| edge touches the just-placed settlement (SETUP) | Boolean |
| returned list (output) | Collection |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `color` | RED, BLUE (the other player colors behave the same) |
| `gamePhase` | SETUP, REGULAR |
| own pieces touching edge | 0, 1, more than one (a building at one end plus a road at the other) |
| owner of a piece touching edge | own, other color |
| edge occupied | true, false |
| edge touches the just-placed settlement | true, false |
| returned list (output) | empty, exactly one element, more than one element, maximum (3 in SETUP since a vertex has 3 max edges) |

**Step 4: All combinations** of `gamePhase` x own pieces touching edge since the rules differ by phase, except SETUP with "more than one" (only the just-placed settlement matters in SETUP). Each choice for the rest. 

|              | Input                                                                                     | Expected output                                                                                 | Implemented? |
|--------------|-------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | SETUP, one `RED` settlement just placed on a vertex with 3 adjacent edges                 | exactly the settlement's 3 adjacent edges                                                       | :x:          |
| Test Case 2  | SETUP, two `RED` settlements, only the second is "just placed"                            | only the edges adjacent to the second settlement                                                | :x:          |
| Test Case 3  | SETUP, no settlement placed                                                               | empty list                                                                                      | :x:          |
| Test Case 4  | REGULAR, `RED` owns no pieces                                                             | empty list                                                                                      | :x:          |
| Test Case 5  | REGULAR, one `RED` settlement, no roads, all adjacent edges free                          | exactly the edges adjacent to the settlement                                                    | :x:          |
| Test Case 6  | REGULAR, a `RED` settlement at one end of a free edge E and a `RED` road at E's other end | E is in the list once                                                                           | :x:          |
| Test Case 7  | REGULAR, every edge adjacent to `RED`'s pieces is already occupied                        | empty list                                                                                      | :x:          |
| Test Case 8  | REGULAR, one `RED` settlement and one `BLUE` settlement far apart, all edges free         | exactly the edges adjacent to the `RED` settlement (the `BLUE` settlement's edges are excluded) | :x:          |
| Test Case 9  | REGULAR, one `RED` settlement with 3 adjacent edges, two of them occupied by `BLUE` roads | exactly the one remaining free edge                                                             | :x:          |
| Test Case 10 | REGULAR, called with `BLUE`, one `BLUE` settlement, all adjacent edges free               | exactly the edges adjacent to the `BLUE` settlement (own means the color passed in)             | :x:          |

### Test: `public void place(Piece piece, Location location)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece : Piece`, `location : Location`; state: the valid-spot list for that piece, the pieces already at the location (for Cities, we need to check owner of Settlement) | No return value. Outcome is one of: `IllegalPlacementException`, piece placed (board updated). |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Cases (Settlement, City, Road, Harbor, Robber) |
| `location` | Cases (Vertex, Edge, Hex) |
| location on this board | Boolean |
| piece already at location | Cases (none, Settlement, City, Road, Harbor) |
| valid-spot list | Collection |
| owner of the `Settlement` at `location` (when placing a City) | Cases (own, other color) |
| outcome (output) | Cases |

**Operations:** Searching (the valid-spot list for `location`)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | Settlement, City, Road, Harbor, Robber |
| `location` | Vertex, Edge, Hex |
| location on this board | true, false |
| piece already at location | none, Settlement, City, Road, Harbor |
| valid-spot list | `location` not found, exactly one match, exactly one match in first position, exactly one match in last position |
| owner of the `Settlement` at `location` (when placing a City) | own, other color |
| outcome (output) | IllegalPlacementException, piece placed |

**Step 4**
All combinations of piece type (Settlement, City, Road, Harbor, Robber) x location type (Vertex, Edge, Hex). Each choice for everything else.

|              | Input                                                                        | Expected output                                                                            | Implemented? |
|--------------|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|--------------|
| Test Case 1  | `Settlement`, first vertex of `getValidBuildingSpots()`                      | piece placed there                                                                         | :x:          |
| Test Case 2  | `Settlement`, last vertex of `getValidBuildingSpots()`                       | piece placed there                                                                         | :x:          |
| Test Case 3  | `Settlement`, vertex adjacent to an existing building                        | `IllegalPlacementException` "Cannot place %s at %s: too close to an existing building"     | :x:          |
| Test Case 4  | `Settlement`, a `Vertex` already holding a `Settlement`                      | `IllegalPlacementException` "Cannot place %s at %s: location already holds a %s"           | :x:          |
| Test Case 5  | `Settlement`, a `Vertex` already holding a `City`                            | `IllegalPlacementException` "Cannot place %s at %s: location already holds a %s"           | :x:          |
| Test Case 6  | `Settlement`, a `Vertex` not on this board                                   | `IllegalPlacementException` "Cannot place %s at %s: location is not on this board"         | :x:          |
| Test Case 7  | `Settlement`, an `Edge`                                                      | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 8  | `Settlement`, a `Hex`                                                        | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 9  | `City`, vertex holding the active player's own `Settlement`                  | piece placed there                                                                         | :x:          |
| Test Case 10 | `City`, vertex holding a `BLUE` `Settlement`                                 | `IllegalPlacementException` "Cannot place %s at %s: not your color"                        | :x:          |
| Test Case 11 | `City`, an empty `Vertex`                                                    | `IllegalPlacementException` "Cannot place %s at %s: %s cannot be placed on empty location" | :x:          |
| Test Case 12 | `City`, a `Vertex` already holding a `City`                                  | `IllegalPlacementException` "Cannot place %s at %s: location already holds a %s"           | :x:          |
| Test Case 13 | `City`, an `Edge`                                                            | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 14 | `City`, a `Hex`                                                              | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 15 | `Road`, a valid edge that is neither first nor last in `getValidRoadSpots()` | piece placed there                                                                         | :x:          |
| Test Case 16 | `Road`, an edge already holding a `Road`                                     | `IllegalPlacementException` "Cannot place %s at %s: location already holds a %s"           | :x:          |
| Test Case 17 | `Road`, an edge holding only a `Harbor`                                      | piece placed there                                                                         | :x:          |
| Test Case 18 | `Road`, a `Vertex`                                                           | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 19 | `Road`, a `Hex`                                                              | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 20 | `Harbor`, an empty `Edge`                                                    | piece placed there                                                                         | :x:          |
| Test Case 21 | `Harbor`, an `Edge` holding a `Road`                                         | piece placed there (assumed)                                                               | :x:          |
| Test Case 22 | `Harbor`, an `Edge` already holding a `Harbor`                               | `IllegalPlacementException` "Cannot place %s at %s: location already holds a %s"           | :x:          |
| Test Case 23 | `Harbor`, a `Vertex`                                                         | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 24 | `Harbor`, a `Hex`                                                            | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 25 | `Robber`, a `Vertex`                                                         | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 26 | `Robber`, an `Edge`                                                          | `IllegalPlacementException` "Cannot place %s at %s: location does not fit a %s"            | :x:          |
| Test Case 27 | `Robber`, a `Hex`                                                            | piece placed there                                                                         | :x:          |
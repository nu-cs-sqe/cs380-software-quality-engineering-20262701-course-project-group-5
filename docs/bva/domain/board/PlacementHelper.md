# Class PlacementHelper
Classpath: catan.game_table.domain.board.PlacementHelper

## `public class PlacementHelper extends Observed`
Helper that decides where pieces may legally be placed and performs placement on the `GameBoard`.

### `public PlacementHelper(GameBoard board, GamePhaseHolder gamePhaseHolder)`
Initializes a PlacementHelper for `board` and with access to the `GamePhase` provided by `gamePhaseHolder`.

### `public Set<? extends Location> getAndCacheValidSpots()`

Saves the locations where the given `piece` can be placed using the `GameBoard` instance, and notifies observers.
Returns the last saved set of valid locations for the piece that `cacheValidSpots(…)` was called with.

### `Set<Vertex> getValidSettlementSpots(Color color)`

Returns vertices where the player with `color` may place a settlement. A vertex is valid if it is unoccupied and no
adjacent vertex has a building (using the distance rule). In `REGULAR` phase it must also connect to one of that
player's own roads. In `SETUP` phase no road is required.

### `Set<Vertex> getValidCitySpots(Color color)`

Returns vertices where the player with `color` may place a city. A vertex is valid if the game is in `REGULAR` phase and
the player's own settlement is present at the vertex. In `SETUP` phase no location is valid.

### `Set<Edge> getValidRoadSpots(Color color)`
Returns unoccupied edges where the player with `color` may place a road. In `REGULAR` phase the edge must connect to that player's own pieces. In `SETUP` phase the edge must touch the settlement just placed.

### `Set<Hex> getValidRobberSpots()`

Returns `Hex`es that a `Robber` piece can be placed on, which equates to all but the `Hex` currently occupied by a
`Robber`.

### `public void place(Piece piece, Location location) throws InvalidPlacementException`
Places `piece` at `location` via the board. Throws the checked exception `InvalidPlacementException` if `location` is not currently a valid spot for `piece`. A spot is valid only if all of these hold: `location` is on this board; the piece type fits the location type (`Road` and `Harbor` on `Edge` only; `Settlement` and `City` on `Vertex` only; `Robber` on `Hex` only); and `location` does not already hold a second building (`Settlement`/`City`), `Road` or `Harbor`.
A `City` is valid only on a vertex holding the active player's own `Settlement`. A `Road` and a `Harbor` may share an `Edge`.
On success, a `Settlement`, `Road` or `City` is placed on the board and removed from the inventory of the player whose color the piece has. Placing a `City` first removes that player's `Settlement` from the vertex and returns it to the player's inventory, and only then places the `City`. `Harbor` and `Robber` are not taken from a player's inventory. If an exception is thrown, neither the board nor the inventory changes.

### `PieceData getFirstPieceDataWithType(Location location, PieceType pieceType)`

Returns the `PieceData` of the first `Piece` held by `location` and with `pieceType`. Throws `RuntimeException` if no
matching `Piece` is found.

### TODO: remove() function ?

## Boundary-Value Analysis

Own refers to the `color` passed to `getValidSettlementSpots` and `getValidRoadSpots` (the active player's color). I use
Red for active player's color and Blue for an opponent (arbitrary).

### Test: `public Set<? extends Location> getAndCacheValidSpots(Piece piece)`

|             | Input                                                                     | Expected output                                                                                              | Implemented?       |
|-------------|---------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | piece is a `RED` Settlement                                               | `getValidSettlementSpots()` is called with `RED`, result returned, observers notified                        | :white_check_mark: |
| Test Case 2 | piece is a `BLUE` Road                                                    | `getValidRoadSpots()` is called with `BLUE`, result returned, observers notified                             | :white_check_mark: |
| Test Case 3 | piece is a `WHITE` City                                                   | `getValidCitySpots()` is called with `WHITE`, result returned, observers notified                            | :white_check_mark: |
| Test Case 4 | piece is a Robber                                                         | `getValidRobberSpots()` is called, result returned, observers notified                                       | :white_check_mark: |
| Test Case 5 | piece is a Harbor                                                         | `IllegalArgumentException` "No defined behavior exists for this piece type"                                  | :white_check_mark: |
| Test Case 6 | piece is a `RED` Settlement, this method was already called on this piece | no other method is called, saved result returned, observers notified each time                               | :white_check_mark: |
| Test Case 7 | `RED` Settlement followed by `BLUE` Settlement                            | `getValidSettlementSpots()` is called once per piece with each result returned, observers notified each time | :white_check_mark: |

### Test: `Set<Vertex> getValidSettlementSpots(Color color)`

**Step 1: Inputs / outputs**

| Inputs                                                                                                                                                                                                                                     | Outputs                                                                                                                  |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| `color : Color`; state: `gamePhase`, distance from each vertex to the nearest building (0 means the vertex itself is occupied), who owns that building, and the roads touching each vertex (how many belong to `color`, and who owns them) | `List<Vertex>`. The tests check which vertices are returned: empty, exactly one element, more than one element, maximum. |

**Step 2: Data types**

| Variable                        | Type                     |
|---------------------------------|--------------------------|
| `color`                         | Cases (RED, Not RED)     |
| `gamePhase`                     | Cases (SETUP, REGULAR)   |
| distance to nearest building    | Count (edges away)       |
| owner of the nearest building   | Cases (own, other color) |
| own roads touching vertex       | Count                    |
| owner of a road touching vertex | Cases (own, other color) |
| returned list (output)          | Collection               |

**Step 3: Concrete values**

| Variable                        | Values                                                                                               |
|---------------------------------|------------------------------------------------------------------------------------------------------|
| `color`                         | RED, BLUE (the other player colors behave the same)                                                  |
| `gamePhase`                     | SETUP, REGULAR                                                                                       |
| distance to nearest building    | 0 (vertex occupied), 1 (too close), 2 (smallest allowed distance)                                    |
| owner of the nearest building   | own, other color                                                                                     |
| own roads touching vertex       | 0, 1, more than one                                                                                  |
| owner of a road touching vertex | own, other color                                                                                     |
| returned list (output)          | empty, exactly one element, more than one element, maximum (all vertices in SETUP on an empty board) |

**Step 4: All combinations** of `gamePhase` x own roads touching vertex since road rule only applies in REGULAR phase. Each choice for all else.

|              | Input                                                                                                    | Expected output                                                                                    | Implemented?       |
|--------------|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1  | SETUP, called with `RED`, empty board                                                                    | all vertices                                                                                       | :white_check_mark: |
| Test Case 2  | REGULAR, called with `RED`, empty board, no roads                                                        | empty list                                                                                         | :white_check_mark: |
| Test Case 3  | SETUP, called with `RED`, a `RED` settlement on vertex V                                                 | every vertex except V (distance 0) and V's neighbors (distance 1); vertices at distance 2 included | :white_check_mark: |
| Test Case 4  | REGULAR, called with `RED`, one `RED` road between vertices A and B, no buildings anywhere               | exactly `[A, B]`                                                                                   | :white_check_mark: |
| Test Case 5  | REGULAR, called with `RED`, one `RED` road between A and B, a `BLUE` settlement on A                     | empty list (A is occupied, B is distance 1 from the settlement)                                    | :white_check_mark: |
| Test Case 6  | REGULAR, called with `RED`, two separate `RED` roads A to B and C to D, no buildings, far apart          | exactly `[A, B, C, D]`, no duplicates                                                              | :white_check_mark: |
| Test Case 7  | REGULAR, called with `RED`, `RED` roads A to B and B to C, no buildings                                  | exactly `[A, B, C]`, with B appearing once                                                         | :white_check_mark: |
| Test Case 8  | SETUP, called with `RED`, every vertex is occupied or next to a building                                 | empty list                                                                                         | :white_check_mark: |
| Test Case 9  | REGULAR, called with `RED`, one `BLUE` road between A and B, `RED` has no roads, no buildings            | empty list (only own roads count)                                                                  | :white_check_mark: |
| Test Case 10 | REGULAR, called with `RED`, `RED` settlement S with `RED` roads S to V1 and V1 to V2, no other buildings | exactly `[V2]` (V1 is too close to S)                                                              | :white_check_mark: |
| Test Case 11 | SETUP, called with `RED`, one `RED` road between A and B, no buildings                                   | all vertices (a road is not required in SETUP)                                                     | :white_check_mark: |
| Test Case 12 | SETUP, called with `RED`, `RED` roads A to B and B to C, no buildings                                    | all vertices, B appearing once                                                                     | :white_check_mark: |
| Test Case 13 | REGULAR, called with `BLUE`, one `BLUE` road between A and B, no buildings anywhere                      | exactly `[A, B]` (own means the color passed in)                                                   | :white_check_mark: |
| Extra Case 1 | SETUP, called with `RED`, one `RED` road on a vertex                                                     | all vertices                                                                                       | :white_check_mark: |

### Test: `Set<Vertex> getValidCitySpots(Color color)`

**Step 1: Inputs / outputs**

| Inputs                                                                      | Outputs                                                                                                                  |
|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| `color : Color`; state: `gamePhase`, what building is currently on a vertex | `List<Vertex>`. The tests check which vertices are returned: empty, exactly one element, more than one element, maximum. |

**Step 2: Data types**

| Variable                       | Type                                       |
|--------------------------------|--------------------------------------------|
| `color`                        | Cases (RED, Not RED)                       |
| `gamePhase`                    | Cases (SETUP, REGULAR)                     |
| existing building type         | Cases (none, settlement, not a settlement) |
| owner of the existing building | Cases (none, own, other color)             |
| returned list (output)         | Collection                                 |

**Step 3: Concrete values**

| Variable                       | Values                                                                                               |
|--------------------------------|------------------------------------------------------------------------------------------------------|
| `color`                        | RED, BLUE (the other player colors behave the same)                                                  |
| `gamePhase`                    | SETUP, REGULAR                                                                                       |
| existing building type         | none, Settlement, City                                                                               |
| owner of the existing building | own, other color                                                                                     |
| returned list (output)         | empty, exactly one element, more than one element, maximum (all vertices in SETUP on an empty board) |

**Step 4: All combinations** of `gamePhase` x own roads touching vertex since road rule only applies in REGULAR phase.
Each choice for all else.

|             | Input                                                                                              | Expected output          | Implemented?       |
|-------------|----------------------------------------------------------------------------------------------------|--------------------------|--------------------|
| Test Case 1 | SETUP, called with `RED`, empty board                                                              | empty list               | :white_check_mark: |
| Test Case 2 | REGULAR, called with `RED`, empty board                                                            | empty list               | :white_check_mark: |
| Test Case 3 | SETUP, called with `RED`, a `RED` settlement on vertex V                                           | empty list               | :white_check_mark: |
| Test Case 4 | REGULAR, called with `RED`, a `RED` settlement on vertex V                                         | list containing vertex V | :white_check_mark: |
| Test Case 5 | REGULAR, called with `RED`, two `RED` settlements on vertex A and B                                | exactly `[A, B]`         | :white_check_mark: |
| Test Case 6 | REGULAR, called with `RED`, one `RED` settlement on vertex A and one `BLUE` settlement on vertex B | list containing vertex A | :white_check_mark: |
| Test Case 7 | REGULAR, called with `RED`, one `RED` city on vertex A and one `BLUE` city on vertex B             | empty list               | :white_check_mark: |

### Test: `Set<Edge> getValidRoadSpots(Color color)`

**Step 1: Inputs / outputs**

| Inputs                                                                                                                                                                                 | Outputs                                                                                                    |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| `color : Color`; state: `gamePhase`, whether each edge is occupied, the pieces touching each edge (how many belong to `color`, and who owns them), the settlement just placed in SETUP | `List<Edge>`. The tests check which edges are returned: empty, exactly one element, more than one element. |

**Step 2: Data types**

| Variable                                        | Type                     |
|-------------------------------------------------|--------------------------|
| `color`                                         | Cases (RED, Not RED)     |
| `gamePhase`                                     | Cases (SETUP, REGULAR)   |
| own pieces touching edge                        | Count                    |
| owner of a piece touching edge                  | Cases (own, other color) |
| edge occupied                                   | Boolean                  |
| edge touches the just-placed settlement (SETUP) | Boolean                  |
| returned list (output)                          | Collection               |

**Step 3: Concrete values**

| Variable                                | Values                                                                                                 |
|-----------------------------------------|--------------------------------------------------------------------------------------------------------|
| `color`                                 | RED, BLUE (the other player colors behave the same)                                                    |
| `gamePhase`                             | SETUP, REGULAR                                                                                         |
| own pieces touching edge                | 0, 1, more than one (a building at one end plus a road at the other)                                   |
| owner of a piece touching edge          | own, other color                                                                                       |
| edge occupied                           | true, false                                                                                            |
| edge touches the just-placed settlement | true, false                                                                                            |
| returned list (output)                  | empty, exactly one element, more than one element, maximum (3 in SETUP since a vertex has 3 max edges) |

**Step 4: All combinations** of `gamePhase` x own pieces touching edge since the rules differ by phase, except SETUP with "more than one" (only the just-placed settlement matters in SETUP). Each choice for the rest.

|              | Input                                                                                                        | Expected output                                                                                 | Implemented?       |
|--------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1  | SETUP, called with `RED`, one `RED` settlement just placed on a vertex with 3 adjacent edges                 | exactly the settlement's 3 adjacent edges                                                       | :white_check_mark: |
| Test Case 2  | SETUP, called with `RED`, two `RED` settlements, only the second is "just placed"                            | only the edges adjacent to the second settlement                                                | :white_check_mark: |
| Test Case 3  | SETUP, called with `RED`, no settlement placed                                                               | empty list                                                                                      | :white_check_mark: |
| Test Case 4  | REGULAR, called with `RED`, `RED` owns no pieces                                                             | empty list                                                                                      | :white_check_mark: |
| Test Case 5  | REGULAR, called with `RED`, one `RED` settlement, no roads, all adjacent edges free                          | exactly the edges adjacent to the settlement                                                    | :white_check_mark: |
| Test Case 6  | REGULAR, called with `RED`, a `RED` road placed                                                              | exactly the edges adjacent to the road                                                          | :white_check_mark: |
| Test Case 7  | REGULAR, called with `RED`, every edge adjacent to `RED`'s pieces is already occupied                        | empty list                                                                                      | :white_check_mark: |
| Test Case 8  | REGULAR, called with `RED`, one `RED` settlement and one `BLUE` settlement far apart, all edges free         | exactly the edges adjacent to the `RED` settlement (the `BLUE` settlement's edges are excluded) | :white_check_mark: |
| Test Case 9  | REGULAR, called with `RED`, one `RED` settlement with 3 adjacent edges, two of them occupied by `BLUE` roads | exactly the one remaining free edge                                                             | :white_check_mark: |
| Test Case 10 | REGULAR, called with `BLUE`, one `BLUE` settlement, all adjacent edges free                                  | exactly the edges adjacent to the `BLUE` settlement (own means the color passed in)             | :white_check_mark: |
| Test Case 11 | REGULAR, called with `RED`, one `RED` road adjacent to a `BLUE` settlement                                   | only the free edges not adjacent to the settlement                                              | :white_check_mark: |
| Extra Case 1 | REGULAR, called with `RED`, one `RED` road on a vertex                                                       | no edges                                                                                        | :white_check_mark: |

### Test: `Set<Hex> getValidRobberSpots()`

Test board will have only 2 hexes.

|              | Input                  | Expected output          | Implemented?       |
|--------------|------------------------|--------------------------|--------------------|
| Test Case 1  | empty board            | all land hexes           | :white_check_mark: |
| Test Case 2  | robber placed on hex A | all land hexes but hex A | :white_check_mark: |
| Extra Case 1 | road placed on hex     | all land hexes           | :white_check_mark: |

### Test: `public void place(Piece piece, Location location)`

**Step 1: Inputs / outputs**

| Inputs                                                                            | Outputs                                                                                                                                                                |
|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `piece : Piece`, `location : Location`; state: the valid-spot list for that piece | No return value. Outcome is one of: `InvalidPlacementException` (board and player inventory unchanged), piece placed (board and player inventory updated, see Step 4). |

**Step 2: Data types**

| Variable                          | Type                                             |
|-----------------------------------|--------------------------------------------------|
| `piece`                           | Cases (Settlement, City, Road, Harbor, Robber)   |
| valid spots result for `location` | Cases (location is valid, location is not valid) |
| outcome (output)                  | Cases                                            |

**Operations:** Searching (the valid-spot list for `location`)

**Step 3: Concrete values**

| Variable                          | Values                                  |
|-----------------------------------|-----------------------------------------|
| `piece`                           | Settlement, City, Road, Harbor, Robber  |
| valid spots result for `location` | valid, invalid                          |
| outcome (output)                  | InvalidPlacementException, piece placed |

**Step 4**
Each choice of piece type (Settlement, City, Road, Harbor, Robber) x valid spots result (valid, invalid) except Harbor +
invalid. Each choice for everything else.

|             | Input                                                                 | Expected output                                                         | Implemented?       |
|-------------|-----------------------------------------------------------------------|-------------------------------------------------------------------------|--------------------|
| Test Case 1 | `Settlement`, a valid location for a settlement                       | `Settlement` placed on that vertex                                      | :white_check_mark: |
| Test Case 2 | `Settlement`, an invalid location for a settlement                    | `InvalidPlacementException` "Cannot place %s at %s"                     | :white_check_mark: |
| Test Case 3 | `City`, a valid location holding the active player's own `Settlement` | the `Settlement` is removed from and the `City` is placed on the vertex | :white_check_mark: |
| Test Case 4 | `City`, an invalid location for a city                                | `InvalidPlacementException` "Cannot place %s at %s"                     | :white_check_mark: |
| Test Case 5 | `Road`, a valid location for a road                                   | `Road` placed on that edge                                              | :white_check_mark: |
| Test Case 6 | `Road`, an invalid location for a road                                | `InvalidPlacementException` "Cannot place %s at %s"                     | :white_check_mark: |
| Test Case 7 | `Harbor`, any location                                                | `Harbor` placed on that location                                        | :white_check_mark: |
| Test Case 8 | `Robber`, a valid location for a robber                               | `Robber` placed on that hex                                             | :white_check_mark: |
| Test Case 9 | `Robber`, an invalid location for a robber                            | `InvalidPlacementException` "Cannot place %s at %s"                     | :white_check_mark: |

### Test: `PieceData getFirstPieceDataWithType(Location location, PieceType pieceType)`

|             | Input                                                                    | Expected output                     | Implemented?       |
|-------------|--------------------------------------------------------------------------|-------------------------------------|--------------------|
| Test Case 1 | PieceType `SETTLEMENT`, a location containing a `Settlement`             | The `PieceData` of the `Settlement` | :white_check_mark: |
| Test Case 2 | PieceType `CITY`, a location containing a `City` and then a `Settlement` | The `PieceData` of the `CITY`       | :white_check_mark: |
| Test Case 3 | PieceType `CITY`, a location containing a `Settlement` and then a `City` | The `PieceData` of the `CITY`       | :white_check_mark: |
| Test Case 4 | PieceType `SETTLEMENT`, a location containing a `City`                   | `RuntimeException`                  | :x:                |
| Test Case 5 | PieceType `SETTLEMENT`, a location containing a no pieces                | `RuntimeException`                  | :x:                |
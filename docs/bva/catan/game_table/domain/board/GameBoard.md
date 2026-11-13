# Class GameBoard
Classpath: catan.game_table.catan.game_table.board.domain.GameBoard

## `public class GameBoard extends Observed`

Holds the board graph (`Hex`,`Vertex`,`Edge`) and the pieces placed on it. Notifies observers when it changes.

### `public GameBoard(Set<Hex> hexes, Set<Vertex> vertices, Set<Edge> edges)`
Initializes the board with the given hexes, vertices and edges, and builds the neighbor map from `edges` by calling `createNeighbors()`.

### `public void produceResourcesForToken(int tokenValue)`
Asks every `Hex` whose token equals `tokenValue` to produce resources. Throws `IllegalArgumentException` if `tokenValue` is outside `[2, 12]`. A value of `7` produces nothing.

### `public void addPieceAtLocation(Piece piece, Location location)`
Adds `piece` to `location` and notifies observers. It does not check whether the placement is valid: `PlacementHelper` decides that before this is called.

### `public void removePieceFromLocation(PieceData pieceData, Location location)`
Removes the piece with matching `pieceData` from `location`. No operation happens if no piece matches.

### `private void createNeighbors()`
For every `Edge`, finds the two `Vertex` objects at its endpoints and records each as a neighbor of the other. Every vertex has an entry.

### `public Set<Vertex> getNeighbors(Vertex vertex)`
Returns the vertices joined to `vertex` by exactly one edge. Assumes `vertex` is on this board. Called by `PlacementHelper` for the distance rule and road connectivity.

## Boundary-Value Analysis

`GameBoard` is only called with valid arguments. Null cases and invalid-placement cases are not tested here (see `PlacementHelper`).

### Test: `public void produceResourcesForToken(int tokenValue)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `tokenValue : int`; state: `hexes` and each hex's token | No return value. Outcome is one of: `IllegalArgumentException`; valid call where no hex is asked to produce; valid call where one or more hexes are asked to produce. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `tokenValue` | Interval `[2, 12]` |
| `hexes` | Collection |
| hexes matching the token | Collection (subset of `hexes`) |
| outcome (output) | Cases |
| number of hexes asked to produce (output) | Count |

**Operations:**
Searching (for the hexes matching the token)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `tokenValue` | 1, 2, 7 (special case), 12, 13 |
| `hexes` | empty, exactly one, more than one, maximum (25 hexes: 19 land hexes plus 6 `OceanHex` frame pieces) |
| hexes matching the token | not found, exactly one match, exactly one match (first position), exactly one match (last position), exactly two matches (the maximum since no token appears on more than two hexes) |
| outcome (output) | IllegalArgumentException; valid, no hex asked to produce; valid, hexes asked to produce |
| hexes asked to produce (output) | 0, 1, 2 (maximum) |

**Step 4: Each choice**

|              | Inputs                                                                                                         | Expected output                                                                 | Implemented?       |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|--------------------|
| Test Case 1  | Board with more than one hex, input `1`                                                                        | `IllegalArgumentException`: "Token value %d is outside the valid range [2, 12]" | :white_check_mark: |
| Test Case 2  | Board with more than one hex, input `13`                                                                       | `IllegalArgumentException`: "Token value %d is outside the valid range [2, 12]" | :white_check_mark: |
| Test Case 3  | More than one hex, only the first hex has token `2`, input `2`                                                 | only the first hex is asked to produce                                          | :white_check_mark: |
| Test Case 4  | More than one hex, only the last hex has token `12`, input `12`                                                | only the last hex is asked to produce                                           | :white_check_mark: |
| Test Case 5  | More than one hex, input `7`                                                                                   | no hex is asked to produce                                                      | :white_check_mark: |
| Test Case 6  | More than one hex, none has token `6`, input `6`                                                               | no hex is asked to produce                                                      | :white_check_mark: |
| Test Case 7  | Board with exactly two hexes, both with token `8`, input `8`                                                   | both hexes are asked to produce                                                 | :white_check_mark: |
| Test Case 8  | Board with no hexes, input `6`                                                                                 | nothing happens, no exception                                                   | :white_check_mark: |
| Test Case 9  | Board with exactly one hex, token `8`, input `8`                                                               | that hex is asked to produce                                                    | :white_check_mark: |
| Test Case 10 | Standard board with the maximum 25 hexes (19 land plus 6 `OceanHex`), two land hexes with token `8`, input `8` | exactly those two hexes are asked to produce                                    | :white_check_mark: |
| Test Case 11 | More than one hex, only a hex in the middle (neither first nor last) has token `5`, input `5`                  | only that hex is asked to produce                                               | :white_check_mark: |

### Test: `public void addPieceAtLocation(Piece piece, Location location)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece : Piece`, `location : Location`; state: location's pieces | No return value. The piece is stored in the location (observers notified). Also how many pieces the location holds afterward. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Cases (Settlement, City, Road, Harbor, Robber) |
| `location` | Cases (Vertex, Edge, Hex) |
| location's pieces | Collection |
| pieces at location after the call (output) | Count |

**Operations:**
Appending a single element (to the location's pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | Settlement, City, Road, Harbor, Robber |
| `location` | Vertex, Edge, Hex |
| location's pieces | empty, exactly one |
| pieces at location after the call (output) | 1, 2 |

**Step 4: Each choice**

|             | Input                                       | Expected output                                              | Implemented?       |
|-------------|---------------------------------------------|--------------------------------------------------------------|--------------------|
| Test Case 1 | `Settlement`, empty `Vertex`                | vertex is told to add exactly that piece, observers notified | :white_check_mark: |
| Test Case 2 | `City`, empty `Vertex`                      | vertex is told to add exactly that piece                     | :white_check_mark: |
| Test Case 3 | `Road`, empty `Edge`                        | edge is told to add exactly that piece                       | :white_check_mark: |
| Test Case 4 | `Harbor`, empty `Edge`                      | edge is told to add exactly that piece                       | :white_check_mark: |
| Test Case 5 | `Robber`, empty `Hex`                       | hex is told to add exactly that piece                        | :white_check_mark: |
| Test Case 6 | `Harbor`, `Edge` already holding one `Road` | edge is told to add both pieces                              | :white_check_mark: |

### Test: `public void removePieceFromLocation(PieceData pieceData, Location location)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `pieceData : PieceData`, `location : Location`; state: location's pieces | No return value. Outcome is one of: no change (nothing matched), matching piece removed (observers notified). Also how many pieces remain at the location |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `pieceData` | Cases (a valid `PieceData`) |
| `location` | Cases (Vertex, Edge, Hex) |
| location's pieces | Collection |
| pieces matching `pieceData` | Collection (subset of the location's pieces) |
| `pieceData` vs a stored piece's data | Pair of References (object equality) |
| outcome (output) | Cases |
| pieces at location after the call (output) | Count |

**Operations:**
Searching for the pieces matching `pieceData`.
Deleting elements from any position (from the location's pieces).

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `pieceData` | a valid `PieceData` |
| `location` | Vertex, Edge, Hex |
| location's pieces | empty (nothing to delete), exactly one, more than one |
| pieces matching `pieceData` | no match, exactly one match, exactly one match in first position, exactly one match in last position (more than one match can't be set: a location never holds two pieces of the same kind) |
| `pieceData` vs a stored piece's data | same object, different object with equal contents |
| outcome (output) | no change, piece removed |
| pieces at location after the call (output) | 0, 1 |

**Step 4: Each choice.**

|             | Input                                                                                 | Expected output                                         | Implemented?       |
|-------------|---------------------------------------------------------------------------------------|---------------------------------------------------------|--------------------|
| Test Case 1 | `Vertex` with no pieces                                                               | no change, no exception                                 | :white_check_mark: |
| Test Case 2 | `Vertex` with exactly one piece, its own `pieceData`                                  | vertex is told to remove that piece, observers notified | :white_check_mark: |
| Test Case 3 | `Edge` with a `Road` then a `Harbor`, `pieceData` matches the `Road` only (first)     | edge to told to remove `Road`                           | :white_check_mark: |
| Test Case 4 | `Edge` with a `Road` then a `Harbor`, `pieceData` matches the `Harbor` only (last)    | edge to told to remove `Harbor`                         | :white_check_mark: |
| Test Case 5 | `Vertex` with one piece, `pieceData` matches none                                     | no change                                               | :white_check_mark: |
| Test Case 6 | `Vertex` with one piece, a different `PieceData` object with equal `type` and `color` | vertex is told to remove that piece                     | :white_check_mark: |
| Test Case 7 | `Edge` with exactly one `Road`, its own `pieceData`                                   | edge is told to remove that piece                       | :white_check_mark: |
| Test Case 8 | `Hex` with the `Robber`, its own `pieceData`                                          | hex is told to remove that piece                        | :white_check_mark: |

### Test: `private void createNeighbors()`

`A` to `J` are vertices at distinct points. The board is only called with valid arguments.

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| state: the board's `vertices` and `edges` | No return value. Each vertex's neighbor set holds the vertices joined to it by one edge. Also how many neighbors each vertex has. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `vertices` | Collection |
| `edges` | Collection |
| `vertices` and `edges` together | Pair of collections |
| edges and vertices that refer to each other | Collection built with references |
| edges touching one vertex | Interval `[2, 3]` (every corner is in a hex ring; 3 is the maximum on a hex grid) |
| neighbors of a vertex (output) | Count |

**Operations:**
Searching (for the vertex at each edge endpoint)
Appending a single element (to a vertex's neighbor set)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `vertices` | empty, more than one (6, 10), maximum (54: the land vertices of a standard board) |
| `edges` | empty, more than one (6, 11), maximum (72: the land edges of a standard board) |
| `vertices` and `edges` together | both empty, both more than one. |
| edges and vertices that refer to each other | circular (a hex ring) |
| edges touching one vertex | 2 (minimum), 3 (maximum). |
| neighbors of a vertex (output) | 2 (minimum), 3 (maximum) |

**Step 4: Each choice**

|             | Inputs                                                                                                                    | Expected output                                            | Implemented? |
|-------------|---------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------|--------------|
| Test Case 1 | Empty `vertices`, empty `edges`                                                                                           | no exception                                               | :x:          |
| Test Case 2 | One hex ring `A-B`, `B-C`, `C-D`, `D-E`, `E-F`, `F-A`, vertices given in the order `A`..`F`; query `A`, then `F`          | `A` has `{B, F}` and `F` has `{E, A}`                      | :x:          |
| Test Case 3 | The ring above plus a second hex sharing `C-D`: `D-G`, `G-H`, `H-I`, `I-J`, `J-C`; vertices `J`..`A`; query `C`, then `D` | `C` has `{B, D, J}` and `D` has `{C, E, G}`                | :x:          |
| Test Case 4 | All 54 land vertices and 72 land edges of a standard board                                                                | 36 vertices have exactly 3 neighbors and 18 have exactly 2 | :x:          |
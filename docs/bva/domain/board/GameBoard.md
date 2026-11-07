# Class GameBoard
Classpath: catan.game_table.domain.board.GameBoard

## `public class GameBoard extends Observed`

Holds the board graph (`Hex`,`Vertex`,`Edge`) and the pieces placed on it. Notifies observers when it changes.

### `public void produceResourcesForToken(int tokenValue)`
Asks every `Hex` whose token equals `tokenValue` to produce resources. Throws `IllegalArgumentException` if `tokenValue` is outside `[2, 12]`. A value of `7` produces nothing.

### `public void addPieceAtLocation(Piece piece, Location location)`
Adds `piece` to `location`. Throws `IllegalArgumentException` if either is `null`, if `location` is not on this board, if the piece type does not fit the location type (`Road` on `Edge` only; `Settlement`, `City` and `Harbor` on `Vertex` only; `Robber` on `Hex` only), or if `location` already holds a building (`Settlement`/`City`) or `Road`. Other game rules are enforced by `PlacementHelper`.

### `public void removePieceFromLocation(PieceData pieceData, Location location)`
Removes the piece with matching `pieceData` from `location`. Throws `IllegalArgumentException` if either is `null` or `location` is not on this board. No operation happens if no piece matches.

## Boundary-Value Analysis

### Test: `public void produceResourcesForToken(int tokenValue)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `tokenValue : int`; state: `hexes`, each hex's token and `canProduce` | none (`void`). Outcome is one of: `IllegalArgumentException`; valid call where no hex produces; valid call where one or more hexes produce. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `tokenValue` | Interval `[2, 12]` |
| `hexes` | Collection |
| hexes matching the token | Collection (subset of `hexes`) |
| matching hex `canProduce` | Boolean |
| outcome (output) | Cases |
| number of hexes that produce (output) | Count |

**Operations:**
Searching (for the hexes matching the token)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `tokenValue` | 1, 2, 7 (special case), 12, 13 |
| `hexes` | empty, exactly one, more than one, maximum (25 hexes: 19 land hexes plus 6 `OceanHex` frame pieces) |
| hexes matching the token | not found, exactly one match, exactly one match (first position), exactly one match (last position), exactly two matches (the maximum since no token appears on more than two hexes) |
| `canProduce` | true, false |
| outcome (output) | IllegalArgumentException; valid, nothing produces; valid, production happens |
| hexes that produce (output) | 0, 1, 2 (maximum) |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1  | Board with more than one hex, input `1`                                                                        | `IllegalArgumentException`      | :x:          |
| Test Case 2  | Board with more than one hex, input `13`                                                                       | `IllegalArgumentException`      | :x:          |
| Test Case 3  | More than one hex, only the first hex has token `2`, input `2`                                                 | only the first hex produces     | :x:          |
| Test Case 4  | More than one hex, only the last hex has token `12`, input `12`                                                | only the last hex produces      | :x:          |
| Test Case 5  | More than one hex, input `7`                                                                                   | no hex produces                 | :x:          |
| Test Case 6  | More than one hex, none has token `6`, input `6`                                                               | no hex produces                 | :x:          |
| Test Case 7  | Board with exactly two hexes, both with token `8`, input `8`                                                   | both hexes produce              | :x:          |
| Test Case 8  | One hex with token `8` and `canProduce` false, input `8`                                                       | hex does not produce            | :x:          |
| Test Case 9  | Board with no hexes, input `6`                                                                                 | nothing happens, no exception   | :x:          |
| Test Case 10 | Board with exactly one hex, token `8`, `canProduce` true, input `8`                                            | that hex produces               | :x:          |
| Test Case 11 | Standard board with the maximum 25 hexes (19 land plus 6 `OceanHex`), two land hexes with token `8`, input `8` | exactly those two hexes produce | :x:          |
| Test Case 12 | More than one hex, only a hex in the middle (neither first nor last) has token `5`, input `5`                  | only that hex produces          | :x:          |

### Test: `public void addPieceAtLocation(Piece piece, Location location)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece : Piece`, `location : Location`; state: location's pieces | No return value. Outcome is one of: `IllegalArgumentException`, piece stored in the location (observers notified). Another output would be how many pieces the location holds afterward (I included this but not sure)? |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Pointer, Case (Settlement, City, Road, Robber, Harbor) |
| `location` | Pointer, Case (Vertex, Edge), on-board Boolean |
| location's pieces | Collection |
| piece already at location | Cases (Settlement, City, Road, Harbor) |
| outcome (output) | Cases |
| pieces at location after the call (output) | Count |

**Operations:**
Appending a single element (to the location's pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | null, real object; each concrete type |
| `location` | null, real object; Vertex, Edge; on board, not on board |
| location's pieces | empty, exactly one, exactly two (the maximum: a building plus a Harbor) |
| piece already at location | Settlement, City, Road, Harbor |
| outcome (output) | exception, piece stored |
| pieces at location after the call (output) | 1, 2 (0 only on the exception path, where the location is unchanged) |

**Step 4**
All combinations of piece type (5) x location type (Vertex, Edge, Hex). Each choice for everything else.

|              | Input                                                 | Expected output                                                               | Implemented? |
|--------------|-------------------------------------------------------|-------------------------------------------------------------------------------|--------------|
| Test Case 1  | `piece` is `null`, valid empty `Vertex`               | `IllegalArgumentException`                                                    | :x:          |
| Test Case 2  | valid `Settlement`, `location` is `null`              | `IllegalArgumentException`                                                    | :x:          |
| Test Case 3  | valid `Settlement`, `Vertex` not on this board        | `IllegalArgumentException`                                                    | :x:          |
| Test Case 4  | `Settlement`, empty `Vertex` on board                 | vertex holds exactly that piece                                               | :x:          |
| Test Case 5  | `Settlement`, `Edge` on board                         | `IllegalArgumentException`                                                    | :x:          |
| Test Case 6  | `City`, empty `Vertex` on board                       | vertex holds exactly that piece                                               | :x:          |
| Test Case 7  | `City`, `Edge` on board                               | `IllegalArgumentException`                                                    | :x:          |
| Test Case 8  | `Road`, `Vertex` on board                             | `IllegalArgumentException`                                                    | :x:          |
| Test Case 9  | `Road`, empty `Edge` on board                         | edge holds exactly that piece                                                 | :x:          |
| Test Case 10 | `Harbor`, empty `Vertex` on board                     | vertex holds exactly that piece (assumed)                                     | :x:          |
| Test Case 11 | `Harbor`, `Edge` on board                             | `IllegalArgumentException` (assumed)                                          | :x:          |
| Test Case 12 | `Robber`, `Vertex` on board                           | `IllegalArgumentException` (Robber is only valid on a Hex)                    | :x:          |
| Test Case 13 | `Robber`, `Edge` on board                             | `IllegalArgumentException` (Robber is only valid on a Hex)                    | :x:          |
| Test Case 14 | `Harbor`, `Vertex` already holding one `Settlement`   | vertex holds both pieces (assumed)                                            | :x:          |
| Test Case 15 | `Settlement`, `Vertex` already holding a `Settlement` | `IllegalArgumentException`                                                    | :x:          |
| Test Case 16 | `City`, `Vertex` already holding a `Settlement`       | `IllegalArgumentException` (assumed: an upgrade removes the settlement first) | :x:          |
| Test Case 17 | `Settlement`, `Vertex` already holding a `Harbor`     | vertex holds both pieces (assumed)                                            | :x:          |
| Test Case 18 | `Road`, `Edge` already holding a `Road`               | `IllegalArgumentException`                                                    | :x:          |
| Test Case 19 | `Settlement`, `Hex` on board                          | `IllegalArgumentException`                                                    | :x:          |
| Test Case 20 | `City`, `Hex` on board                                | `IllegalArgumentException`                                                    | :x:          |
| Test Case 21 | `Road`, `Hex` on board                                | `IllegalArgumentException`                                                    | :x:          |
| Test Case 22 | `Harbor`, `Hex` on board                              | `IllegalArgumentException`                                                    | :x:          |
| Test Case 23 | `Robber`, empty `Hex` on board                        | hex holds exactly that piece                                                  | :x:          |

### Test: `public void removePieceFromLocation(PieceData pieceData, Location location)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `pieceData : PieceData`, `location : Location`; state: location's pieces | No return value. Outcome is one of: `IllegalArgumentException`, no change (nothing matched), matching piece removed (observers notified). Also how many pieces remain at the location |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `pieceData` | Pointer |
| `location` | Pointer, on-board Boolean |
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
| `pieceData`, `location` | null, real object; location not on board |
| pieces at location | empty (nothing to delete), exactly one, more than one |
| pieces matching `pieceData` | no match, exactly one match, exactly one match in first position, exactly one match in last position, more than one match |
| `pieceData` vs a stored piece's data | same object, different object with equal contents |
| outcome (output) | exception, no change, piece removed |
| pieces at location after the call (output) | 0, 1 |

**Step 4: Each choice.**

|              | Input                                                                                 | Expected output                                                 | Implemented? |
|--------------|---------------------------------------------------------------------------------------|-----------------------------------------------------------------|--------------|
| Test Case 1  | `pieceData` is `null`, valid `Vertex`                                                 | `IllegalArgumentException`                                      | :x:          |
| Test Case 2  | valid `pieceData`, `location` is `null`                                               | `IllegalArgumentException`                                      | :x:          |
| Test Case 3  | valid `pieceData`, `Vertex` not on this board                                         | `IllegalArgumentException`                                      | :x:          |
| Test Case 4  | `Vertex` with no pieces                                                               | no change, no exception                                         | :x:          |
| Test Case 5  | `Vertex` with exactly one piece, its own `pieceData`                                  | vertex is empty                                                 | :x:          |
| Test Case 6  | `Vertex` with two pieces, `pieceData` matches the first only                          | only the second remains                                         | :x:          |
| Test Case 7  | `Vertex` with two pieces, `pieceData` matches the last only                           | only the first remains                                          | :x:          |
| Test Case 8  | `Vertex` with one piece, `pieceData` matches none                                     | no change                                                       | :x:          |
| Test Case 9  | `Vertex` with one piece, a different `PieceData` object with equal `type` and `color` | piece removed                                                   | :x:          |
| Test Case 10 | `Vertex` with two pieces whose `PieceData` are equal, input that data                 | exactly one piece is removed (the first), one remains (assumed) | :x:          |
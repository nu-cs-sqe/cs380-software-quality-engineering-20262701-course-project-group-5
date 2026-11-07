# Class Location
Classpath: catan.game_table.domain.board.Location

Direct known subclasses: `Vertex`, `Edge`, `Hex`

## `public abstract class Location`

A place on the board that holds pieces.

### `public List<PieceData> getPieceData()`
Returns the `PieceData` of each piece at this location, in the order added. Returns an empty list if there are none.

### `public void addPiece(Piece piece)`
Adds `piece` to this location. Throws `IllegalArgumentException` if `piece` is `null` or if this location already holds that same `piece` object. A different piece may be added (a building and a `Harbor` can share a `Vertex`). The one-building-or-road-per-location limit is enforced by `GameBoard.addPieceAtLocation`.

### `public void removePieceWithData(PieceData data)`
Removes the first piece whose data equals `data`. Does nothing if none match. Throws `IllegalArgumentException` if `data` is `null`.

## Boundary-Value Analysis

`Location` is abstract, so every test below is run three times: once on a `Vertex`, once on an `Edge` and once on a `Hex`. Examples use `Vertex`; substitute `Edge` (with a `Road` instead of a `Settlement`) for the second run, and `Hex` (with the `Robber` instead of a `Settlement`) for the third. `Location` itself does not limit how many different pieces it holds, since game rules are enforced by `GameBoard` and `PlacementHelper`. A `Hex` only ever holds the `Robber`, which `GameBoard` enforces.

### Test: `public List<PieceData> getPieceData()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| none; state: the pieces at the location | `List<PieceData>`, one entry per piece, in the order added. Its size is one of: empty, exactly one, more than one. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| pieces at location | Collection |
| returned list (output) | Collection |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| pieces at location | empty, exactly one, more than one |
| returned list (output) | empty, exactly one element, more than one element |

**Step 4: Each choice**

|             | Input                                        | Expected output               | Implemented? |
|-------------|----------------------------------------------|-------------------------------|--------------|
| Test Case 1 | `Vertex` with no pieces                      | empty list                    | :x:          |
| Test Case 2 | `Vertex` with one `Settlement`               | list with that piece's data   | :x:          |
| Test Case 3 | `Vertex` with a `Settlement` then a `Harbor` | list with both, in that order | :x:          |
| Test Case 4 | `Hex` with the `Robber`                      | list with the Robber's data   | :x:          |

### Test: `public void addPiece(Piece piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece : Piece`; state: the pieces at the location | No return value. Outcome is one of: `IllegalArgumentException`, piece added. Also how many pieces the location holds afterward. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Pointer |
| pieces at location | Collection |
| `piece` vs a piece already at the location | Pair of References |
| outcome (output) | Cases |
| pieces at location after the call (output) | Count |

**Operations:** Appending a single element (to the location's pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | null, true object |
| pieces at location | empty, not empty |
| `piece` vs a piece already at the location | same object, different object |
| outcome (output) | IllegalArgumentException, piece added |
| pieces at location after the call (output) | 1, 2 (unchanged on the exception path) |

**Step 4: Each choice**

|             | Input                                         | Expected output                                                                    | Implemented? |
|-------------|-----------------------------------------------|------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `piece` is `null`                             | `IllegalArgumentException`                                                         | :x:          |
| Test Case 2 | empty location, add a `Settlement`            | location holds exactly that piece                                                  | :x:          |
| Test Case 3 | location with one piece, add a different one  | location holds both                                                                | :x:          |
| Test Case 4 | add the same `Settlement` object twice        | `IllegalArgumentException`, location still holds one piece                         | :x:          |
| Test Case 5 | empty `Hex`, add the `Robber`                 | hex holds exactly that piece                                                       | :x:          |

### Test: `public void removePieceWithData(PieceData data)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `data : PieceData`; state: the pieces at the location | No return value. Outcome is one of: `IllegalArgumentException`, no change (nothing matched), matching piece removed. Also how many pieces remain. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `data` | Pointer |
| pieces at location | Collection |
| pieces matching `data` | Collection (subset of the location's pieces) |
| `data` vs a stored piece's data | Pair of References |
| outcome (output) | Cases |
| pieces at location after the call (output) | Count |

**Operations:** Searching (for the pieces matching `data`), Deleting elements from any position (from the location's pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `data` | null, true object |
| pieces at location | empty (nothing to delete), exactly one, more than one |
| pieces matching `data` | no match, exactly one match, exactly one match in first position, exactly one match in last position, more than one match |
| `data` vs a stored piece's data | same object, different object with equal contents |
| outcome (output) | IllegalArgumentException, no change, piece removed |
| pieces at location after the call (output) | 0, 1, 2 |

**Step 4: Each choice**

|             | Input                                                                           | Expected output                                  | Implemented? |
|-------------|---------------------------------------------------------------------------------|--------------------------------------------------|--------------|
| Test Case 1 | `data` is `null`                                                                | `IllegalArgumentException`                       | :x:          |
| Test Case 2 | no pieces                                                                       | no change, no exception                          | :x:          |
| Test Case 3 | one piece, `data` is that piece's own data                                      | location is empty                                | :x:          |
| Test Case 4 | two pieces, `data` matches the first only                                       | only the second remains                          | :x:          |
| Test Case 5 | two pieces, `data` matches the last only                                        | only the first remains                           | :x:          |
| Test Case 6 | three pieces, `data` matches the middle one only                                | the first and the last remain                    | :x:          |
| Test Case 7 | two pieces, `data` matches neither                                              | no change                                        | :x:          |
| Test Case 8 | two pieces with equal data, remove that data                                    | exactly one is removed (the first), one remains  | :x:          |
| Test Case 9 | one piece, a different `PieceData` object with equal `type` and `color`         | piece removed, location is empty                 | :x:          |
| Test Case 10 | `Hex` with the `Robber`, `data` is the Robber's data                           | hex is empty                                     | :x:          |
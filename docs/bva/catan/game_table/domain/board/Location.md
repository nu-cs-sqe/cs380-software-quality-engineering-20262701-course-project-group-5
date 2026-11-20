# Class Location
Classpath: catan.game_table.domain.board.Location

Direct known subclasses: `Vertex`, `Edge`, `Hex`

## `public abstract class Location`

A place on the board that holds pieces.

### `public List<Piece> getPieces()`
Returns the pieces at this location in the order added. Returns an empty list if there are none.

### `public boolean holdsPieceWithData(PieceData data)`
Returns true if any piece at this location has the same `type` and `color` as `data`. Otherwise returns false.

### `public void addPiece(Piece piece)`
Adds `piece` to the end of this location's pieces. It does not check whether the placement is valid.

### `public void removePieceWithData(PieceData data)`
Removes the first piece whose data equals `data`, meaning the same `type` and the same `color`. Does nothing if none match.

## Boundary-Value Analysis

`Location` is abstract, so the tests below are run on a `Vertex` (with a `Settlement`), an `Edge` (with a `Road`) and a `Hex` (with the `Robber`). Cases with two pieces use an `Edge` holding a `Road` and a `Harbor`. `Location` itself does not limit what or how many pieces it holds, assume responsibilities lie in `PlacementHelper`.

### Test: `public boolean holdsPieceWithData(PieceData data)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `data : PieceData`; state: the pieces at the location | `boolean` indicating whether any piece has matching data |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `data` | Cases (a valid `PieceData`) |
| pieces at location | Collection |
| `data` vs a stored piece's data | Pair of References (object equality) |
| returned value (output) | Boolean |

**Operations:** Searching for a piece whose data equals `data`.

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| pieces at location | empty, exactly one, two |
| `data` vs a stored piece's data | same object, different object with equal contents, same type with a different color, same color with a different type |
| matching piece | none, only piece, first of two, last of two |
| returned value (output) | true, false |

**Step 4: Each choice**

|             | Input                                                                                                  | Expected output | Implemented?       |
|-------------|--------------------------------------------------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Empty `Vertex`, query with settlement data                                                             | false           | :white_check_mark: |
| Test Case 2 | `Vertex` with one `Settlement`, query with the settlement data                                         | true            | :white_check_mark: |
| Test Case 3 | `Edge` with a `Road` then a `Harbor`, query with the road's data                                       | true            | :x:                |
| Test Case 4 | `Edge` with a `Road` then a `Harbor`, query with the harbor's data                                     | true            | :x:                |
| Test Case 5 | `Edge` with a `Road` then a `Harbor`, query with settlement data                                       | false           | :x:                |
| Test Case 6 | `Vertex` with one `Settlement`, query with a different `PieceData` object with the same type and color | true            | :x:                |
| Test Case 7 | `Edge` with one `Road`, query with the same type and a different color                                 | false           | :x:                |
| Test Case 8 | `Vertex` with one `Settlement`, query with the same color and a different type                         | false           | :x:                |

### Test: `public void addPiece(Piece piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece : Piece`; state: the pieces at the location | No return value. The piece is stored after the existing pieces. Also how many pieces the location holds afterward. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Cases (a valid `Piece`) |
| pieces at location | Collection |
| pieces at location after the call (output) | Count |

**Operations:** Appending a single element (to the location's pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | a valid `Piece` |
| pieces at location | empty, not empty |
| pieces at location after the call (output) | 1, 2 |

**Step 4: Each choice**

|             | Input                                  | Expected output                                | Implemented?       |
|-------------|----------------------------------------|------------------------------------------------|--------------------|
| Test Case 1 | empty `Vertex`, add a `Settlement`     | vertex holds exactly that piece                | :white_check_mark: |
| Test Case 2 | `Edge` with one `Road`, add a `Harbor` | edge holds both, the `Harbor` after the `Road` | :white_check_mark: |
| Test Case 3 | empty `Hex`, add the `Robber`          | hex holds exactly that piece                   | :white_check_mark: |

### Test: `public void removePieceWithData(PieceData data)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `data : PieceData`; state: the pieces at the location | No return value. Outcome is one of: no change (nothing matched), matching piece removed. Also how many pieces remain. |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `data` | Cases (a valid `PieceData`) |
| pieces at location | Collection |
| pieces matching `data` | Collection (subset of the location's pieces) |
| `data` vs a stored piece's data | Pair of References (object equality) |
| outcome (output) | Cases |
| pieces at location after the call (output) | Count |

**Operations:** Searching (for the pieces matching `data`), Deleting elements from any position (from the location's pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `data` | a valid `PieceData` |
| pieces at location | empty (nothing to delete), exactly one, more than one |
| pieces matching `data` | no match, no match (same `type`, different `color`), exactly one match, exactly one match in first position, exactly one match in last position |
| `data` vs a stored piece's data | same object, different object with equal contents |
| outcome (output) | no change, piece removed |
| pieces at location after the call (output) | 0, 1, 2 |

**Step 4: Each choice**

|             | Input                                                                         | Expected output                  | Implemented? |
|-------------|-------------------------------------------------------------------------------|----------------------------------|--------------|
| Test Case 1 | no pieces                                                                     | no change, no exception          | :x:          |
| Test Case 2 | one piece, `data` is that piece's own data                                    | location is empty                | :x:          |
| Test Case 3 | `Edge` with a `Road` then a `Harbor`, `data` matches the `Road` only (first)  | only the `Harbor` remains        | :x:          |
| Test Case 4 | `Edge` with a `Road` then a `Harbor`, `data` matches the `Harbor` only (last) | only the `Road` remains          | :x:          |
| Test Case 5 | `Edge` with a `Road` then a `Harbor`, `data` matches neither                  | no change, both remain           | :x:          |
| Test Case 6 | one piece, a different `PieceData` object with equal `type` and `color`       | piece removed, location is empty | :x:          |
| Test Case 7 | `Hex` with the `Robber`, `data` is the Robber's data                          | hex is empty                     | :x:          |
| Test Case 8 | `Edge` with one `Road`, `data` has the same `type` but a different `color`    | no change, the `Road` remains    | :x:          |

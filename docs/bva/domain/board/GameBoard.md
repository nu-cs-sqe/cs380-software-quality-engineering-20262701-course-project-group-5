# Class GameBoard
Classpath: catan.game_table.domain.board.GameBoard

## `public class GameBoard extends Observed`

Holds the board graph (`Hex`,`Vertex`,`Edge`) and the pieces placed on it. Notifies observers when it changes.

### `public void produceResourcesForToken(int tokenValue)`
Asks every `Hex` whose token equals `tokenValue` to produce resources. Throws `IllegalArgumentException` if `tokenValue` is outside `[2, 12]`. A value of `7` produces nothing.

### `public void addPieceAtLocation(Piece piece, Location location)`
Adds `piece` to `location`. Throws `IllegalArgumentException` if either is `null`, if `location` is not on this board, or if the piece type does not fit the location type (`Road` on `Edge` only; `Settlement`/`City` on `Vertex` only). Game rules are enforced by `PlacementHelper`.

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
| hexes matching the token | Searching |
| matching hex `canProduce` | Boolean |
| outcome (output) | Cases |
| number of hexes that produce (output) | Count |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `tokenValue` | 1, 2, 7 (special case), 12, 13 |
| `hexes` | empty, exactly one, more than one, maximum (25 hexes: 19 land hexes plus 6 `OceanHex` frame pieces) |
| search | not found, exactly one match (first position), exactly one match (last position), exactly two matches (the maximum since no token appears on more than two hexes) |
| `canProduce` | true, false |
| outcome (output) | IllegalArgumentException; valid, nothing produces; valid, production happens |
| hexes that produce (output) | 0, 1, 2 (maximum) |

**Step 4: Each choice** 

| | Inputs | Expected output | Implemented? |
|---|---|---|---|
| Test Case 1 | Board with more than one hex, input `1` | `IllegalArgumentException` | :x: |
| Test Case 2 | Board with more than one hex, input `13` | `IllegalArgumentException` | :x: |
| Test Case 3 | More than one hex, only the first hex has token `2`, input `2` | only the first hex produces | :x: |
| Test Case 4 | More than one hex, only the last hex has token `12`, input `12` | only the last hex produces | :x: |
| Test Case 5 | More than one hex, input `7` | no hex produces | :x: |
| Test Case 6 | More than one hex, none has token `6`, input `6` | no hex produces | :x: |
| Test Case 7 | Board with exactly two hexes, both with token `8`, input `8` | both hexes produce | :x: |
| Test Case 8 | One hex with token `8` and `canProduce` false, input `8` | hex does not produce | :x: |
| Test Case 9 | Board with no hexes, input `6` | nothing happens, no exception | :x: |
| Test Case 10 | Board with exactly one hex, token `8`, `canProduce` true, input `8` | that hex produces | :x: |
| Test Case 11 | Standard board with the maximum 25 hexes (19 land plus 6 `OceanHex`), two land hexes with token `8`, input `8` | exactly those two hexes produce | :x: |
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

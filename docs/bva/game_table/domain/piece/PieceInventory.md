# Interface PieceInventory

Classpath: catan.game_table.domain.piece.PieceInventory

## `interface PieceInventory`

Tracks the `Piece`s a `GamePlayer` possess.

### `public addPiece(piece : Piece)`

Adds `piece` to a given `PlayerInventory`.

### `public hasPiece(piece : Piece)`

Checks if `piece` is in a given `PlayerInventory`.

### `public removePiece(piece : Piece)`

Removes `piece` from a given `PlayerInventory`. If the piece is not present in the `PlayerInventory` throws a `NotEnoughInventoryException`.

## Boundary-Value Analysis
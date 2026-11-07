# Abstract Class Piece

Classpath: catan.game_table.domain.piece.Piece

## `abstract class Piece`

General form of a `Piece` that could be bought or placed on the board, at their base level all pieces are defined by type and color.

Direct known subclasses: `Building`, `Harbor`, `Road`, `Robber`

### `public Piece(data : PieceData)`

Constructs a `Piece` object using `PieceData` comprised of a `type` and `color`.

### `public getPieceData(data : PieceData)`

An accessor for the data field of piece which is a `PieceData` object.

### `public onPlaceEffect()`

Called when a `Piece` is place on the `GameBoard` by a `GamePlayer`. Empty method defined by inheriting class.

## Boundary-Value Analysis


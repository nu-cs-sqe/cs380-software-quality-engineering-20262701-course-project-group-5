# Class PieceData

Classpath: catan.game_table.domain.piece.PieceData

## `public class PieceData`

A holder for a Piece objects color and type data, the former referencing an Enum and the latter being a string.

## Boundary-Value Analysis

The Class features no methods to analyze, only static fields. Color is pulled from an Enum of size 5 containing values None, Red, Blue, White, and Orange. Type refers to the class type of the object which must be a class that extends Piece.
# Class Harbor

Classpath: catan.game_table.domain.piece.Harbor

## `class Harbor extends Piece`

A subclass of Piece that a Player can trade with in preset ratios of Resources.

### `public Harbor()`

Constructs an unclaimed Harbor piece, ie a Player cannot trade with it. 

### `public Harbor(inputTrade:ResourceStack)`

Constructs a claimed Harbor piece, that a given Player can trade with through making an offer of a ResourceStack.

### `public getInputTrade()`

Accessor for the inputTrade field to see the ResourceStack that was offered.

## Boundary-Value Analysis


# Class Harbor

Classpath: catan.game_table.domain.piece.Harbor

## `class Harbor extends Piece`

A subclass of `Piece` that a `GamePlayer` can trade with in preset ratios of `ResourceStack`s.

### `public Harbor()`

Constructs a `Harbor` piece with the default 3:1 trader ratio. 

### `public Harbor(inputTrade : ResourceStack)`

Constructs `Harbor` piece with an `inputTrade` that allows for a trade of ratio 2:1.

### `public ResourceStack getInputTrade()`

Accessor for the `inputTrade` field to see the `ResourceStack` that was offered.

## Boundary-Value Analysis


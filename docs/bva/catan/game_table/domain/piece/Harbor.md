# Class Harbor

Classpath: catan.game_table.domain.piece.Harbor

## `class Harbor extends Piece`

A subclass of `Piece` that a `GamePlayer` can trade with in preset ratios of `ResourceStack`s.

### `public Harbor()`

Constructs a generic harbor with a 3:1 trade rate for any resource type.

### `public Harbor(inputTrade : ResourceStack)`

Constructs a harbor with a 2:1 trade rate for the resource type specified by `inputTrade`.

### `public int getTradeRate()`

Returns the number of resources the player gives for one resource from the bank.

### `public Optional<ResourceStack> getInputTrade()`

Returns the required resource for a 2:1 harbor. A generic harbor returns an empty optional because its 3:1 rate applies to any resource type.

## Boundary-Value Analysis


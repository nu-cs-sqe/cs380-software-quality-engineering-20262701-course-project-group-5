# Class Road

Classpath: catan.game_table.domain.piece.Road

## `class Road extends Piece`

A subclass of `Piece` that costs a `BrickResourceStack` and `WoodResourceStack` of size one.

### `public void onBoughtEffect()`

Decrements the `ResourceStack`s of the `GamePlayer` purchasing the `Road` by its cost.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a `Road` in terms of `ResourceStack`s, in this case one `BrickResourceStack` and `WoodResourceStack`.

## Boundary-Value Analysis
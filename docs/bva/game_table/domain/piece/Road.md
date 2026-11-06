# Class Road

Classpath: catan.game_table.domain.piece.Road

## `class Road extends Piece`

A subclass of Piece that costs a BrickStack and WoodStack of size one.

### `public onBoughtEffect()`

Decrements the Resources of the Player purchasing the Road by its cost.

### `public getCost()`

Accessor for the cost of a Road in terms of Resources, in this case one Brick and Wood.

## Boundary-Value Analysis
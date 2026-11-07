# Abstract Class Building

Classpath: catan.game_table.domain.piece.Building

## `abstract class Building`

General form of a building that `GamePlayer`s can buy and place to produce a `ResourceProducts` or trigger updates to Victory Points.

Direct known subclasses: `City`, `Settlement`

### `public Building(data : PieceData, bankInventory : ResourceInventory, ownerInventory : ResourceInventory)`

Constructs a `Building` object with set `PieceData` and access to the `ResourceInventory`s of a `Bank` and `GamePlayer`. 

### `public void produce(products : ResourceProducts)`

Updates the `bankResources` and `ownerResources` `ResourceInventory`s based on the given `products`. The amount updated by and product type depend on the inheriting class.

## Boundary-Value Analysis
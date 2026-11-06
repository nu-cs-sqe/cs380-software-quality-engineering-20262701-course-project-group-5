# Abstract Class Building

Classpath: catan.game_table.domain.piece.Building

## `abstract class Building`

General form of a building that Players can buy and place to produce a `ResourceProduct` or trigger updates to Victory Points.

Direct known subclasses: `City`, `Settlement`

### `public Building(data:PieceData, bankInventory:ResourceInventory, ownerInventory:ResourceInventory)`

Constructs a Building object with set `PieceData` and access to the `ResourceInventory`s of a bank and Player. 

### `public produce(products:ResourceProduct)`

Given a `product` updates a `ResourceStack` list of either tier1 or tier2 products, the amount by which and product type depending on the inheriting class.

## Boundary-Value Analysis
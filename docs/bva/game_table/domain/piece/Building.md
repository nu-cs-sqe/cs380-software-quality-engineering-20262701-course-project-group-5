# Abstract Class Building

Classpath: catan.game_table.domain.piece.Building

## `abstract class Building`

General form of a building that `GamePlayer`s can buy and place to produce a `ResourceProducts` or trigger updates to Victory Points.

Direct known subclasses: `City`, `Settlement`

### `public Building(data : PieceData, bankResourceDistributor : BankResourceDistributor)`

Constructs a `Building` object with set `PieceData` and access to the `BankResourceDistributor`. 

### `public void produce(products : ResourceProducts)`

Given `products` checks with the `BankResourceDistributor` to try and produce resources. The amount updated by and product type depend on the inheriting class.

## Boundary-Value Analysis

N/A for this class.
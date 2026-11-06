# Class Settlement

Classpath: catan.game_table.domain.piece.Settlement

## `class Settlement extends Building`

A subclass of Building that costs a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one to build. Can produce two `ResourceProducts` that is `tier1` of the `ResourceStack` type of `Hex` the City is placed on.

### `public Settlement(data : PieceData, bankInventory : ResourceInventory, ownerInventory : ResourceInventory, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a Settlement object with set `PieceData` and access to the `ResourceInventory`s of a bank and Player, and access to the `ScoreTracker` of the Player that owns it.

### `public produceResources(products : ResourceProducts)`

Given a `products` increments a `ResourceStack` list of tier1 products by one.

### `public onBoughtEffect()`

Decrements the Resources of the Player purchasing the Settlement by its cost.

### `public getCost()`

Accessor for the cost of a Settlement in terms of Resources, in this case a `GrainResourceStack` of size one, `BrickResourceStack` of size one, `LumberResourceStack` of size one, and `WoolResourceStack` of size one.

## Boundary-Value Analysis
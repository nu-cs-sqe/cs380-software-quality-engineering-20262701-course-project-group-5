# Class City

Classpath: catan.game_table.domain.piece.City

## `class City extends Building`

A subclass of `Building` that costs a `GrainResourceStack` of size two and `OreResourceStack` of size three to build. Can produce two `ResourceProducts` that is `tier1` or `tier2` of the `ResourceStack` type of `Hex` the `City` is placed on.

### `public City(data : PieceData, bankInventory : ResourceInventory, ownerInventory : ResourceInventory, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a `City` object with set `PieceData` and access to the `ResourceInventory`s of a bank and `GamePlayer`, and access to the `ScoreTracker` of the `GamePlayer` that owns it.

### `public produceResources(products : ResourceProducts)`

Given a `products` increments a `ResourceStack` list of either `tier1` or `tier2` products by two.

### `public onBoughtEffect()`

Decrements the `ResourceStacks` of the Player purchasing the City by its cost.

### `public getCost()`

Accessor for the cost of a City in terms of `ResourcesStack`s, in this case a `GrainResourceStack` of size two and `OreResourceStack` of size three

## Boundary-Value Analysis
# Class City

Classpath: catan.game_table.domain.piece.City

## `class City extends Building`

A subclass of `Building` that costs a `GrainResourceStack` of size two and `OreResourceStack` of size three to build. When adjacent to a `Hex` that can produce and was triggered by a production roll, produces two `tier1` and `tier2` `ResourceProducts` of the `ResourceStack` type of triggerred `Hex`.

### `public City(data : PieceData, bankInventory : ResourceInventory, ownerInventory : ResourceInventory, ownerVictoryPointsTracker : ScoreTracker)`

Constructs a `City` object with set `PieceData` and access to the `ResourceInventory`s of a bank and `GamePlayer`, and access to the `ScoreTracker` of the `GamePlayer` that owns it.

### `public void produceResources(products : ResourceProducts)`

Updates the `bankResources` and `ownerResources` `ResourceInventory`s by two based on the given `products`.

### `public void onBoughtEffect()`

Decrements the `ResourceStacks` of the Player purchasing the City by its cost.

### `public List<ResourceStack> getCost()`

Accessor for the cost of a City in terms of `ResourcesStack`s, in this case a `GrainResourceStack` of size two and `OreResourceStack` of size three

## Boundary-Value Analysis

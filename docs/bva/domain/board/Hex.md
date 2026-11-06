# Class Hex
Classpath: catan.game_table.domain.board.Hex

Direct known subclasses: `HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`, `DesertHex`, `OceanHex`

## `public abstract class Hex`

A terrain tile with a token number, six vertices, six edges, and a set of resource products. Distributes its products to the pieces occupying it when produced.

### `public Hex(Set<Vertex> vertices, Set<Edge> edges, ResourceProducts products)`
Initializes a `Hex`. Throws `IllegalArgumentException` if any argument is `null` or if `vertices` or `edges` does not contain exactly 6 elements (this might need to change for the OceanHex?).

### `public boolean canProduce()`
Returns whether this hex currently produces resources (false while the robber sits on it, or for desert and ocean).

### `public int getTokenValue()`
Returns the token number of this hex.

### `public void setCanProduce(boolean canProduce)`
Sets the `canProduce` flag to the given value. `produceResources()` only runs when the flag is true. Assumes the boolean parameter (missing from diagram). `DesertHex` and `OceanHex` override it.

### `public void produceResources()`
If `canProduce()`, sends `products` to each piece in `occupyingPieces`. Otherwise does nothing. A `Settlement` takes the tier-1 products and a `City` takes the tier-1 and tier-2 products. Resource subclasses (`HillsHex`, `ForestHex`, `MountainsHex`, `FieldsHex`, `PastureHex`) override this to produce their specific resource.

# Class ResourceProducts
Classpath: catan.game_table.domain.resource.ResourceProducts

## `public class ResourceProducts`

A data holder for passing `ResourceStack` lists around, to be used when a building produces resources during the game. This class identifies Tier 1 products to be those produced by any default building (e.g. settlements), and Tier 2 products to be those additionally produced by an upgraded building (e.g. cities).

### `public ResourceProducts(List<ResourceStack> tier1Products, List<ResourceStack> tier2Products)`
Initializes a `ResourceProducts` with the given `tier1Products` and `tier2Products`.

### `public List<ResourceStack> getTier1Products()`
Returns the list of Tier 1 products held by this `ResourceProducts`.

### `public List<ResourceStack> getTier2Products()`
Returns the list of Tier 2 products held by this `ResourceProducts`.

## Boundary-Value Analysis

N/A for this data class.
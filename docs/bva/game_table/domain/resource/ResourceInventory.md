# Interface ResourceInventory
Classpath: catan.game_table.domain.resource.ResourceInventory

All known implementing classes: `Bank`, `PlayerInventory`

## `public interface ResourceInventory`

This interface should be implemented by any class that intends to store a mutable collection of in-game resources.

### `public void addResources(ResourceStack stack)`

Adds `stack`'s resources to this `ResourceInventory`.

### `public boolean hasResources(ResourceStack stack)`

Returns `true` if this `ResourceInventory` contains at least enough resources of the type and amount represented by `stack`. Returns `false` otherwise.

### `public void removeResources(ResourceStack stack) throws NotEnoughInventoryException`

Removes `stack`'s resources from this `ResourceInventory`. Throws a `NotEnoughInventoryException` if this `ResourceInventory` does not contain the resources represented by `stack`.
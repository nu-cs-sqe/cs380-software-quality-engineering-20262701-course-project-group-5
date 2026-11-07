# Class PlayerInventory

Classpath: catan.game_table.domain.player.PlayerInventory

## class PlayerInventory

Represents one player's inventory, containing all their resources, pieces, and development cards.

### `public addPiece(piece : Piece)` : void

Adds piece to inventory.

### `public hasPiece(piece : Piece)` : boolean

Checks if equivalent piece exists in the inventory.

### `public removePiece(piece : Piece)` : void

Removes equivalent piece if able, throws `IllegalStateException` if empty.

### `public addResources(stack : ResourceStack)` : void

Adds resources to the inventory.

### `public hasResources(stack : ResourceStack)` : boolean

Checks if inventory has a greater than or equal resource stack to the one specified.

### `public removeResources(stack : ResourceStack)` : void

Remove resources from inventory if able, throws `IllegalStateException` if empty.

### `public addDevelopment(development : Development)` : void

Adds a development card.

### `public hasDevelopment(development : Development)` : boolean

Checks if equivalent development card exists in the inventory.

### `public removeDevelopment(development : Development)` : void

Removes development card from inventory if able, throws `IllegalStateException` if empty.

## Boundary-Value Analysis
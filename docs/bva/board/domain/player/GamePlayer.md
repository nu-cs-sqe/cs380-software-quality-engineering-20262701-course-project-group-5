# Class GamePlayer

Classpath: catan.game_table.domain.player.GamePlayer

## class GamePlayer

Represents one of the players in the game and stores references to their inventories

### `public GamePlayer(color : Color, placementHelper : PlacementHelper, tradeHelper : TradeHelper)`

Constructor for the game player. Initializes color and injects dependencies for the road/building placement helper and the trade manager.

### `public getInventory()` : PlayerInventory

Gets player inventory.

### `public rollDice(dice : Dice)` : void

Roll dice to start a turn.

### `public play(item : Playable)` : void

Play a development card or other playable if able.

### `public buy(item : Buyable)` : void

Buy a road, settlement, city, or development card from the bank if able.

### `public getValidPlacements(piece : Piece)` : List<Location>

Get possible placement locations for a given piece for said player.

### `public place(piece : Piece, location : Location)` : void

Place the piece at the given location if able.

### `public proposePlayerTrade(otherPlayer : Color, trade : TradeOffer)` : void

Propose a trade deal to another player.

### `public acceptPlayerTrade(proposingPlayer : Color, trade : TradeOffer)` : void

Accept a trade deal from another player.

### `public tradeWithBank(give : ResourceStack, receive : ResourceStack)` : void

Trade resources with bank if able.


## Boundary-Value Analysis
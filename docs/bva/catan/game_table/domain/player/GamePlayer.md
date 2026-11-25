# Class GamePlayer

Classpath: catan.game_table.domain.player.GamePlayer

## class GamePlayer

Dataclass which represents one of the players in the game and stores references to their inventories.

### `public GamePlayer(playerData: PlayerData, inventory: PlayerInventory)`

Constructor for the game player stores, player data and inventory.

### `public getInventory(): PlayerInventory`

Gets player inventory.

### `public getScore(): int`

Gets number of victory points so far.

### `public getVictoryPointTracker(): ScoreTracker`

Gets ScoreTracker object which tracks victory points.

### `public getLongestRoadTracker(): ScoreTracker`

Gets ScoreTracker object which longest road.

### `public getArmySizeTracker(): ScoreTracker`

Gets ScoreTracker object which army size / amount of knights.

### `public getColor(): Color`

Gets player color.

### `public getName(): String`

Gets player name.

### `public getAwards(): List<Award>`

Gets player awards list.

### `public addAward(award: Award): void`

Adds an award to its awards list.

## Boundary-Value Analysis

data class with only getters and setters
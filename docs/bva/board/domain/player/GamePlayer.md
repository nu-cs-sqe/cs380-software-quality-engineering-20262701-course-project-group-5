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

### Method under test: `getInventory()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|internal state of the inventory| none (if this somehow errors there is a problem with the design)|


**Step 2: Data types**
| Variable | Type |
|---|---|
|interal inventory|pointer PlayerInventory|

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|internal inventory| PlayerInventory{pieces:{}, resourceStacks:{}, developments{}}, and PlayerInventory{pieces:{Road}, resoourceStacks:{OreResourceStack{1}}, developments{Knight}} |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
|Test Case 1| internal inventory: PlayerInventory{pieces:{}, resourceStacks:{}, developments{}}| PlayerInventory{pieces:{}, resourceStacks:{}, developments{}} | :x: |
|Test Case 2| internal inventory: PlayerInventory{pieces:{Road}, resoourceStacks:{OreResourceStack{1}}, developments{Knight}}| PlayerInventory{pieces:{Road}, resoourceStacks:{OreResourceStack{1}}, developments{Knight}} | :x: |


### Method under test: `rollDice(dice : Dice)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|dice| void (I suppose side effects on the dice roll functions)|


**Step 2: Data types**
| Variable | Type |
|---|---|
|dice|pointer Dice|

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|dice| Dice{lastDiceRoll:0}, Dice{lastDiceRoll:INT_MAX}|

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1| dice: Dice{lastDiceRoll:0}| state of dice: Dice{lastDiceRoll:4}| :x: |
| Test Case 1| dice: Dice{lastDiceRoll:INT_MAX}| state of dice: Dice{lastDiceRoll:2}| :x: |


### Method under test: `play(item : Playable)`

this is more of an integration test IMO, not really unit testable other than
"doesn't crash"

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|item, state of board, state of player| state of board, state of player|


**Step 2: Data types**
| Variable | Type |
|---|---|
|item|pointer Playable|

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|playable| one of Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty|
|state of board| robber on a desert square, player on 9 VP, player on 0VP|

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | playable: Knight, state of board | board with the Robber somewhere else | :x: |
| Test Case 2 | playable: RoadBuilder, state of board| board with two more roads| :x: |
| Test Case 3 | playable: Victory point, player on 0 VP| `IllegalStateException` | :x: |
| Test Case 4 | playable: Victory Point, player on 9 VP | said player wins the game | :x: |
| Test Case 5 | playable: Monopoly | player gets a bunch of OreResource | :x: |
| Test Case 6 | playable: YearOfPlenty, player with 0 resources | player resources whose size sum equals 2 | :x: |


### Method under test: `getValidPlacements(piece : Piece)`

this is more of an integration test IMO, not really unit testable other than
"doesn't crash"

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|piece, state of board, state of player| state of board, state of player|


**Step 2: Data types**
| Variable | Type |
|---|---|
|piece|pointer Piece(Settlement, City, Road, Robber, Harbor)|
|state of board| COllections of Points, edges, and hexes|
|state of player| pointer to player inventory |

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|piece|  Settlement, City, Road, Robber, Harbor |
|state of board| robber on a desert square,  robber on a non desert square, player is landlocked on both settlements, player is not landlocked|
| player inventory | 0 resources, enough resources to do the operation in the test case |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1| piece: City, state of board: player is not landlocked, player has resources | locations { # at least one, the board is too hard to vizualize ahead of time lol} | :x: |
| Test Case 2| piece: settlement, state of board: player is not landlocked, player does not have any settlements left | locations : empty | :x: |
| Test Case 3 | piece: Robber, state of board: robber on a desert | state of board: Robber moved to a different spot | :x: |
| Test Case 4| piece: Harbor| `IllegalArgumentException`| :x: |



### Method under test: `place(piece : Piece, location : Location)`

this is more of an integration test IMO, not really unit testable other than
"doesn't crash"

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|piece, location, state of board, state of player| state of board|


**Step 2: Data types**
| Variable | Type |
|---|---|
|piece|pointer Piece(Settlement, City, Road, Robber, Harbor)|
|state of board| COllections of Points, edges, and hexes|
|location| pointer to Location|

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|piece|  Settlement, City, Road, Robber, Harbor |
|Location| Location{no pieces}, location {has piece}|
|state of board| minimum locations occupied (2 points and 2 edges per player), maximum locations occupied, robber on desert, robber not on desert |
| player inventory | 0 resources, enough resources to do the operation in the test case |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1| piece: Settlement, min locations occupied, location: adjacent to current player road and not to other adversarial player stuff | board has settlement at that location | :x: |
| Test Case 2| piece: City, min locations occupied, location: adjacent to current player road and not to other adversarial player stuff  | board has city at that location | :x: |
| Test Case 3| piece: Robber, min locations occupied | board has robber at that location | :x: |
| Test Case 3| piece: harbor | `IllegalArgumentException` | :x: |
|  Test Case 4 | piece: Settlement, max locations occuplied | `IllegalStateException` | :x: |
| Test Case 5 | piece: Road, min locations occupied, location: adjacent to settlement of player | board has road at that location | :x: |
| Test Case 6 | piece: road, min locations occupied, location: not adjacent to player settlement | `IllegalStateException`| :x: |
| Test Case 7 | piece: road, max locations occupied| `IllegalStateException`| :x: |
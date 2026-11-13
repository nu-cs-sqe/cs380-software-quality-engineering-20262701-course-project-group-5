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

Play a development card or other playable if able. Can throw `IllegalPlayException`.

### `public buy(item : Buyable)` : void

Buy a road, settlement, city, or development card from the bank if able. Can Throw `NotEnoughResourcesException`.

### `public getValidPlacements(piece : Piece)` : List<Location>

Get possible placement locations for a given piece for said player.

### `public place(piece : Piece, location : Location)` : void

Place the piece at the given location if able. Can throw `PlacementException`.

### `public proposePlayerTrade(otherPlayer : Color, trade : TradeOffer)` : void

Propose a trade deal to another player. Can throw `MalformedTradeException`.

### `public acceptPlayerTrade(proposingPlayer : Color, trade : TradeOffer)` : void

Accept a trade deal from another player. Can throw `MalformedTradeException`.

### `public tradeWithBank(give : ResourceStack, receive : ResourceStack)` : void

Trade resources with bank if able. Can throw `BankTradeException`


## Boundary-Value Analysis

### Method under test: `rollDice(dice : Dice)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| dice | void (I suppose side effects on the dice roll functions) |


**Step 2: Data types**
| Variable | Type |
|---|---|
| dice | Dice |

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|dice| Dice{lastDiceRoll:0}, Dice{lastDiceRoll:INT_MAX} |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | dice: Dice{lastDiceRoll:0}| state of dice: Dice{lastDiceRoll:4} | :x: |
| Test Case 1 | dice: Dice{lastDiceRoll:INT_MAX}| state of dice: Dice{lastDiceRoll:2} | :x: |


### Method under test: `play(item : Playable)`

this is more of an integration test IMO, not really unit testable other than
"doesn't crash"

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| item, state of board, state of player | state of board, state of player |


**Step 2: Data types**
| Variable | Type |
|---|---|
| item | Playable |

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| playable | one of Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty |
| state of board | robber on a desert square, player on 9 VP, player on 0VP |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | playable: Knight, state of board | board with the Robber somewhere else | :x: |
| Test Case 2 | playable: RoadBuilder, state of board| board with two more roads | :x: |
| Test Case 3 | playable: Victory point, player on 0 VP| `IllegalPlayException` | :x: |
| Test Case 4 | playable: Victory Point, player on 9 VP | said player wins the game | :x: |
| Test Case 5 | playable: Monopoly | player gets a bunch of OreResource | :x: |
| Test Case 6 | playable: YearOfPlenty, player with 0 resources | player resources whose size sum equals 2 | :x: |



### Method under test: `buy(item : Buyable)`

this is more of an integration test IMO, not really unit testable other than
"doesn't crash"

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| item, state of player | state of player, or `NotEnoughResourcesException` |


**Step 2: Data types**
| Variable | Type |
|---|---|
| item | Buyable |

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| item | case: Development, Road, Settlement, City |
| state of player | has enough resources for purchase or doesn't |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | item: Development, state of player: has enough resources | add development to player inventory | :x: |
| Test Case 2 | item: Road, state of player: has enough resources | add road to player inventory | :x: |
| Test Case 3 | item: Road, state of player: doesn't have enough resources | throw `NotEnoughResourcesException` | :x: |


### Method under test: `getValidPlacements(piece : Piece)`

this is more of an integration test IMO, not really unit testable other than
"doesn't crash"

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| piece, state of board, state of player | state of board, state of player |


**Step 2: Data types**
| Variable | Type |
|---|---|
| piece | Piece(Settlement, City, Road, Robber, Harbor) |
| state of board | COllections of Points, edges, and hexes |
| state of player |  player inventory |

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| piece |  Settlement, City, Road, Robber, Harbor |
| state of board | robber on a desert square,  robber on a non desert square, player is landlocked on both settlements, player is not landlocked |
| player inventory | 0 resources, enough resources to do the operation in the test case |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | piece: City, state of board: player is not landlocked, player has resources | locations { # at least one, the board is too hard to vizualize ahead of time lol} | :x: |
| Test Case 2 | piece: settlement, state of board: player is not landlocked, player does not have any settlements left | locations : empty | :x: |
| Test Case 3 | piece: Robber, state of board: robber on a desert | state of board: Robber moved to a different spot | :x: |
| Test Case 4 | piece: Harbor | `IllegalArgumentException`| :x: |



### Method under test: `place(piece : Piece, location : Location)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| piece, location, state of board, state of player | state of board |


**Step 2: Data types**
| Variable | Type |
|---|---|
| piece | Piece(Settlement, City, Road, Robber, Harbor) |
| state of board | Collections of Points, edges, and hexes |
| location |  Location |

**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| piece |  Settlement, City, Road, Robber, Harbor |
| Location | Location{no pieces}, location {has piece} |
| state of board | minimum locations occupied (2 points and 2 edges per player), maximum locations occupied, robber on desert, robber not on desert |
| player inventory | 0 resources, enough resources to do the operation in the test case |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | piece: Settlement, min locations occupied, location: adjacent to current player road and not to other adversarial player stuff | board has settlement at that location | :x: |
| Test Case 2 | piece: City, min locations occupied, location: adjacent to current player road and not to other adversarial player stuff  | board has city at that location | :x: |
| Test Case 3 | piece: Robber, min locations occupied | board has robber at that location | :x: |
| Test Case 4 | piece: harbor | `IllegalArgumentException` | :x: |
| Test Case 5 | piece: Settlement, max locations occuplied | `PlacementException` | :x: |
| Test Case 6 | piece: Road, min locations occupied, location: adjacent to settlement of player | board has road at that location | :x: |
| Test Case 7 | piece: road, min locations occupied, location: not adjacent to player settlement | `PlacementException`| :x: |
| Test Case 8 | piece: road, max locations occupied | `PlacementException`| :x: |



### Method under test: `proposePlayerTrade(otherPlayer : Color, trade : TradeOffer)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| OtherPlayer, TradeOffer, current player | state of trader helper |


**Step 2: Data types**
| Variable | Type |
|---|---|
| OtherPlayer | Color |
| TradeOffer |  trade offer which has {2x inventory, 2x collection of resourceStack} |
| location |  Location |
| Current player| gamePlayer |
| state of helper / output | collection of trade offers // IllegalArgumentException |

**Operations:**
Adding element to list

**Step 3: Concrete values**
| Variable | Values |
| other Player | Red, Blue, White , Orange , None |
| state of trade helper | empty list, list with one offer|
| TradeOffer | Proposer and receiver inventories match current player and otherPlayer respectively (and also no match), list of requestedResources is empty / has one thing, list of offered resources is empty / has one thing |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | current player: Blue, otherPlayer: Blue | `MalformedTradeException(cannot go to same player)` |:x:|
| Test Case 2 | current Player: Blue, otherPlayer: None | `MalformedTradeException(trade must go to someone)` |:x:|
| Test Case 3 | current Player Blue, otherPlayer Red, TradeOffer{proposer: blue, receiver: red, requested{Ore:1}, offered{Lumber:1}} | TradeHelper offer list: TradeOffer{proposer: blue, receiver: red, requested{Ore:1}, offered{Lumber:1}} | :x: |
| Test Case 4 | current player blue, other player red, TradeOffe{proposer: white, reciever: none} | `MalformedTradeException(trade offer doesn't match players)` | :x: |



### Method under test: `acceptPlayerTrade(proposingPlayer : Color, trade : TradeOffer)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| proposingPlayer, TradeOffer, current player, inventories of both players | state of trader helper |


**Step 2: Data types**
| Variable | Type |
|---|---|
| proposing | Color |
| TradeOffer |  trade offer which has {2x inventory, 2x collection of resourceStack} |
| location |  Location |
| Current player | gamePlayer |
| state of helper / output | collection of trade offers // IllegalArgumentException |
| inventories of each player |  Inventory which has 3 collections of devCards, Resources, Pieces |

**Operations:**
Removing element from list

**Step 3: Concrete values**
| Variable | Values |
| other Player | Red, Blue, White , Orange , None |
| state of trade helper | empty list, list with one offer |
| TradeOffer | Proposer and receiver inventories match current player and otherPlayer respectively (and also no match), list of requestedResources is empty / has one thing, list of offered resources is empty / has one thing |
| inventories | have enough resources, or don't |
| Trade Offer manager | trade offer exists, it doesn't exist |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | player1 Red (has 1 Ore), player2: Blue (Has 1 Grain), TradeOffer {1 Ore for 1 Grain}, that offer is in the TradeHelper log | TradeHelper offer log is empty | :x: |
| Test Case 2 | current player: Blue, otherPlayer: Blue | `MalformedTradeException(cannot go to same player)`|:x:|
| Test Case 3 | current Player: Blue, otherPlayer: None | `MalformedTradeException(trade must go to someone)`|:x:|
| Test Case 4 | current player blue, other player red, TradeOffer{proposer: white, reciever: none} | `MalformedTradeException(trade offer doesn't match players)` | :x: |
| Test Case 5 | player1 Red (has 1 Ore), player2: Blue (Has 0 Grain), TradeOffer {1 Ore for 1 Grain}, that offer is in the TradeHelper log | `MalformedTradeException(not enough resources for that trade)` | :x: |





### Method under test: `tradeWithBank(give : ResourceStack, receive : ResourceStack)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| give, receive, PlayerInventory, bankInventory | state of PlayerInventory, bankInventory |


**Step 2: Data types**
| Variable | Type |
|---|---|
| give |  OreStack/LumberStack/WoolStack/BrickStack/GrainStack |
| receive |  OreStack/LumberStack/WoolStack/BrickStack/GrainStack |
| player inventory |   collections of resource stacks |
| bank inventory |   collections of resource stacks |


**Operations:**
none

**Step 3: Concrete values**
| Variable | Values |
| give | {1, 4} convolved with {Ore, Grain, Lumber, Wool, Brick} |
| recieve | {1, 4} convolved with {Ore, Grain, Lumber, Wool, Brick} |
| player inventory | empty, has enough for a 4-1 trade of ore to brick |
| bank inventory | empty, enough for the trade to happen |

**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | give{Grain 1}, receive {4 Brick} |  `BankTradeException(needs to be 4 to 1)` | :x: |
| Test Case 2 | give{Ore 4}, receive {1 Wool}, playerInv has 4 ore, bank has 1 wool |  player inventory{-4 Ore, +1 wool}, bank inventory{+4 ore, -1 wool} | :x: |
| Test Case 3 | give{Lumber 3}, receive {1 Wool} |  `BankTradeException(needs to be 4 to 1)` | :x: |
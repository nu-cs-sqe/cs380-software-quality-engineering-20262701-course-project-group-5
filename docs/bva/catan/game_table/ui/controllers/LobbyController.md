# Class LobbyController

Classpath: catan.game_table.ui.controllers.LobbyController

## `public class LobbyController`

The `LobbyController` links the `LobbyView`, `Main`, and `GameSetup` classes with the help of a `ViewRouter` and `GameScreenFactory`. When changes to player number, color selection, or language are made in `LobbyView`, the `LobbyController` triggers the view to update. Once the players enter all the relevant information and hit start, `LobbyView` prompts `LobbyController` to route it on to `GameSetup` to create a `GameSession` and new set of views that can be routed to with `ViewRouter`.

### `public LobbyController(LobbyView lobbyView, LobbyModel lobbyModel, ViewRouter viewRouter, GameSetup gameSetup, GameScreenFactory gameScreenFactory)`

Constructs a LobbyController to link the `lobbyView` and `lobbyModel` and also to be able to set up the game of Catan and route users on to the next relevant views.
 
### `private void onStart(List<PlayerData> players)`

When a player hits the start button in the `LobbyView` after entering the number of players and what colors they chose, this information is routed through `onStart()` so it can be passed to `GameSetup:createGame()` to make a new `GameSession`. If the session is successfully made, `GameScreenFactory:create()` will be called with the new session to build a Scene of the game, which will then be displayed with `ViewRouter:show()`. If the given number of players passed on to `GameSetup:createGame()` is not large enough, an error will be displayed to let the players know.

### `private void onPlayerDataEntry()`

When a player enters data such as a new or colors for a player, the information is puller from `LobbyModel` and `StartView` is updated for the players to see.

### `private void onPlayerCountChosen()`

When the player count is changed, the new count is pulled from `LobbyModel` and `StartView` is updated for the players to see.

### `private void onLanguageChosen()`

When the game language is changed, the updated language bundle is pulled from `LobbyModel` and `StartView` is updated for the players.

## Boundary-Value Analysis

### Method under test: `onStart(List<PlayerData> players)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `players : List<PlayerData>` | `GameSetup:createGame()`->`GameScreenFactory:create()`->`ViewRouter:show()` are called in succession or `GameSetup:createGame()` errors and `LobbyView:showError()` is called  |

**Step 2: Data types**
| Variable | Type |
|---|---|
| Input  | Cases |
| Output  | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| Input | Invalid num of players (< 3), valid num of players (3/4) |
| Output | `GameSetup:createGame()` errors and `LobbyView:showError()` is called,`GameSetup:createGame()`->`GameScreenFactory:create()`->`ViewRouter:show()` are called |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | `players` list of size 2 | `GameSetup:createGame()` errors and `LobbyView:showError()` is called | :white_check_mark: |
| Test Case 2  | `players` list of size 4 | `GameSetup:createGame()`->`GameScreenFactory:create()`->`ViewRouter:show()` are called | :white_check_mark: |


### Method under test: `onPlayerDataEntry()`
**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | Player selects a color and enters their name | The entered data is propagated to `LobbyModel:capturePlayerDataFromInputs()` from `LobbyView:getRawPlayerNameInputs()` to update the player list internally and for players to see | :x: |

### Method under test: `onPlayerCountChosen()`
**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | Player count is changed | The new player count is accessed with `LobbyModel:getPlayerCount()` and then passed to `StartView:updatePlayerCountButtons()` which takes the count and an IntConsumer callback to update the slots where player data can be input | :x: |

### Method under test: `onLanguageChosen()`
**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | Language is changed from English | `StartView:updateDisplay()` is called with the models new ResourceBundle | :x: |

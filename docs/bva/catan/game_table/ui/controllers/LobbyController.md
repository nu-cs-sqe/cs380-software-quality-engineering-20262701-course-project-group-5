# Abstract Class LobbyController

Classpath: catan.game_table.ui.controllers.LobbyController

## `public class LobbyController`

The `LobbyController` links the `LobbyView`, `Main`, and `GameSetup` classes. The controller triggers the `LobbyView` to populate when given the signal from `Main` and allows users to input what color player they want to be. Once the players enter this information, `LobbyView` prompts `LobbyController` to route it back to `Main` and on to `GameSetup`.

### `private void onStart(List<PlayerData> players)`

When a player hits the start button in the `LobbyView` after entering the number of players and what colors they chose, this information is routed through `onStart()` so it can be passed to `GameSetup` to trigger the creation of a new `GameSession` that is then passed back to `LobbyController` and then to `Main`.

### `private void onDataEntry()`

When a player enters data such as choosing a number of player, a language, or colors for player, `StartView` is updated for the players to see.

## Boundary-Value Analysis

### Method under test: `onStart(List<PlayerData> players)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `players : List<PlayerData>` | `GameSetup:createGame()` is called |

**Step 2: Data types**
| Variable | Type |
|---|---|
| Input  | Cases |
| Output  | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| Input | List of size 3, list of size 4 |
| Output | `GameSetup:createGame()` is called |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | `players` list of size 3 | `GameSetup:createGame(players)` is called | :x: |
| Test Case 2  | `players` list of size 4 | `GameSetup:createGame(players)` is called | :x: |


### Method under test: `onDataEntry()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| Player enters player count, color choice, or language selection | `StartView:updateDisplay()`, `StartView:updateCountButtons()`, and `StartView:updateColorSelections()` are called |

**Step 2: Data types**
| Variable | Type |
|---|---|
| Input  | Cases |
| Output  | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| Input | Player count, color choice, language selection |
| Output | `StartView:updateDisplay()`, `StartView:updateCountButtons()`, and `StartView:updateColorSelections()` are called |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | Player count is changed from 3 to 4 | `StartView:updateDisplay()`, `StartView:updateCountButtons()`, and `StartView:updateColorSelections()` are called | :x: |
| Test Case 2  | Player 1 selects color Red | `StartView:updateDisplay()`, `StartView:updateCountButtons()`, and `StartView:updateColorSelections()` are called | :x: |
| Test Case 3  | Language is changed from English | `StartView:updateDisplay()`, `StartView:updateCountButtons()`, and `StartView:updateColorSelections()` are called | :x: |

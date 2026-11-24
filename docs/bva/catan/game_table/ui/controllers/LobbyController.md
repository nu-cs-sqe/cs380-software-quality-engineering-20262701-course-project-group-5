# Class LobbyController

Classpath: catan.game_table.ui.controllers.LobbyController

## `public class LobbyController`

The `LobbyController` links the `LobbyView`, `Main`, and `GameSetup` classes. When changes to player number, color selection, or language are made in `LobbyView`, the `LobbyController` triggers the view to update. Once the players enter all the relevant information and hit start, `LobbyView` prompts `LobbyController` to route it on to `GameSetup` to create a `GameSession` before calling back to `Main` to return the newly instantiated session.

### `private void onStart(List<PlayerData> players)`

When a player hits the start button in the `LobbyView` after entering the number of players and what colors they chose, this information is routed through `onStart()` so it can be passed to `GameSetup` to trigger the creation of a new `GameSession` that is then passed back to `LobbyController` and then to `Main`.

### `private void onDataEntry()`

When a player enters data such as choosing a number of player, a language, or colors for player, `StartView` is updated for the players to see.

## Boundary-Value Analysis

### Method under test: `onStart(List<PlayerData> players)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `players : List<PlayerData>` | `GameSetup:createGame()` and `Main:showGame()` are called  |

**Step 2: Data types**
| Variable | Type |
|---|---|
| Input  | Cases |
| Output  | Cases |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| Input | List of size 3, list of size 4 |
| Output | `GameSetup:createGame()` and `Main:showGame()` are called |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|-------|-----------------|--------------|
| Test Case 1  | `players` list of size 3 | `GameSetup:createGame()` and `Main:showGame()` are called  | :x: |
| Test Case 2  | `players` list of size 4 | `GameSetup:createGame()` and `Main:showGame()` are called  | :x: |


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

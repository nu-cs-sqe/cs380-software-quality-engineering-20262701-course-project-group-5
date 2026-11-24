# Abstract Class LobbyController

Classpath: catan.game_table.ui.controllers.LobbyController

## `public class LobbyController`

The `LobbyController` links the `LobbyView`, `Main`, and `GameSetup` classes. The controller triggers the `LobbyView` to populate when given the signal from `Main` and allows users to input what color player they want to be. Once the players enter this information, `LobbyView` prompts `LobbyController` to route it back to `Main` and on to `GameSetup`.

### `public void open()`

When a player hits application run in Gradle, `Main` should build the stage environment and `showLobby()` is called to cause `LobbyController` to populate `LobbyView` and refresh this view to `Main`.

### `private void onStart(List<PlayerData> players)`

When a player hits the start button in the `LobbyView` after entering the number of players and what colors they chose, this information is routed through `onStart()` so it can be passed to `GameSetup` to trigger the creation of a new `GameSession` that is then passed back to `LobbyController` and then to `Main`.

## Boundary-Value Analysis

N/A for this class.
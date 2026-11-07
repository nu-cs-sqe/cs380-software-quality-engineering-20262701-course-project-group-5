# Class PlacementHelper
Classpath: catan.game_table.domain.board.PlacementHelper

## `class PlacementHelper`
Package-private helper that decides where pieces may legally be placed and performs placement on the `GameBoard`. Uses `GamePhaseHolder` to know whether the game is in `SETUP` or `REGULAR` phase.

### `public List<Vertex> getValidBuildingSpots()`
Returns vertices where the active player may place a building. A vertex is valid if it is unoccupied and no adjacent vertex has a building (using the distance rule). In `REGULAR` phase it must also connect to one of the player's own roads. In `SETUP` phase no road is required.

### `public List<Edge> getValidRoadSpots()`
Returns unoccupied edges where the active player may place a road. In `REGULAR` phase the edge must connect to the player's own pieces. In `SETUP` phase the edge must touch the settlement just placed.

### `public void place(Piece piece, Location location)`
Places `piece` at `location` via the board. Throws `IllegalArgumentException` if `location` is not currently a valid spot for `piece`. A `City` is valid only on a vertex holding the active player's own `Settlement`.

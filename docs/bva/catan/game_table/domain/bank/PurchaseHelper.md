# Class PurchaseHelper

Classpath: `catan.game_table.domain.bank.PurchaseHelper`

## `public class PurchaseHelper`

Handles paid purchases and builds for configured players. The buyer's color selects the inventory to charge. The controller supplies the current player and decides when paid actions can be taken.

### `public PurchaseHelper(Bank bank, Map<Color, PlayerInventory> playerInventories, PlacementHelper placementHelper)`

Uses the shared bank, player inventories, and placement helper.

### `public void buyDevelopment(Color buyerColor) throws NotEnoughInventoryException`

Checks that the buyer has 1 grain, 1 wool, and 1 ore and that the deck has a card. On success, transfers those resources to the bank and adds the card drawn from the deck to the buyer's inventory. An unaffordable purchase or an empty deck throws `NotEnoughInventoryException` before changing either inventory or the deck.

### `public <T extends Piece & Buyable> void build(Color buyerColor, T piece, Location location) throws NotEnoughInventoryException, InvalidPlacementException`

Checks that the piece has the buyer's color, is available in the buyer's piece inventory, and that the buyer can pay its cost. Uses `PlacementHelper` to validate and place the piece before collecting payment. On success, transfers the cost from the buyer to the bank. Insufficient resources or an unavailable piece throws `NotEnoughInventoryException`. A piece of another player's color or an illegal placement throws `InvalidPlacementException`. A rejected build leaves the board and inventories as they were.

### `private boolean canAfford(PlayerInventory inventory, List<ResourceStack> cost)`

Checks whether the buyer can cover all required resource quantities. Covered through `buyDevelopment()` and `build()`.

## Boundary-Value Analysis

### Test: `buyDevelopment(Color buyerColor)`

**Step 1: Inputs / outputs**

| Inputs                                                                                     | Outputs                                                                                                                              |
|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Buyer color, the buyer's stock of each required resource, and the bank's development deck. | A card moves to the buyer and the cost moves to the bank, or the purchase is rejected without changing the deck or either inventory. |

**Step 2: Data types**

| Variable                                | Type   |
|-----------------------------------------|--------|
| buyer color used to select an inventory | Cases  |
| cards held of each required resource    | Counts |
| development cards remaining             | Count  |
| purchase result                         | Cases  |

**Step 3: Concrete values**

| Variable                              | Values                                                                                            |
|---------------------------------------|---------------------------------------------------------------------------------------------------|
| buyer color                           | A configured buyer with another configured player present                                         |
| resources held compared with the cost | Exactly 1 grain, 1 wool, and 1 ore, or 0 ore with grain and wool sufficient                       |
| development cards remaining           | One card, zero cards                                                                              |
| purchase result                       | Card delivered with exact payment, rejection for insufficient resources, rejection for empty deck |

**Step 4: Each choice**

Isolate the two rejection reasons by making the other requirement sufficient.

|             | Inputs                                                                                                                      | Expected output                                                                                                                                               | Implemented? |
|-------------|-----------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Red has exactly 1 grain, 1 wool, and 1 ore. Blue also has the required resources. The deck has one Knight. Red buys a card. | Red loses 1 grain, 1 wool, and 1 ore and gains the Knight. The bank gains those three resources and its deck becomes empty. Blue's inventory stays as it was. | :x:          |
| Test Case 2 | Red has 1 grain, 1 wool, 0 ore, and 1 brick. The deck has one Knight. Red tries to buy a card.                              | Throws `NotEnoughInventoryException`. Red and the bank retain their resource counts, and the Knight stays in the deck.                                        | :x:          |
| Test Case 3 | Red has 1 grain, 1 wool, and 1 ore, and the deck is empty. Red tries to buy a card.                                         | Throws `NotEnoughInventoryException`. Red and the bank retain their resource counts, and Red gains no card.                                                   | :x:          |

### Test: `build(Color buyerColor, T piece, Location location)`

**Step 1: Inputs / outputs**

| Inputs                                                                                             | Outputs                                                                                                                 |
|----------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| Buyer color, piece and its cost, buyer's piece inventory and resources, location, and board state. | The piece is placed and the cost moves to the bank, or the build is rejected without changing the board or inventories. |

**Step 2: Data types**

| Variable                                                | Type                          |
|---------------------------------------------------------|-------------------------------|
| piece type and its color relative to the buyer          | Cases                         |
| whether the piece is available in the buyer's inventory | Boolean                       |
| cost                                                    | Collection of resource stacks |
| cards held compared with the cost for each type         | Counts                        |
| whether the placement is legal                          | Boolean                       |
| build result                                            | Cases                         |

**Step 3: Concrete values**

| Variable                                                | Values                                                                                                                                |
|---------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| piece and cost                                          | Road costs 1 brick and 1 lumber. City costs 2 grain and 3 ore.                                                                        |
| piece ownership                                         | Piece belongs to the buyer, piece belongs to another player                                                                           |
| whether the piece is available in the buyer's inventory | Red has a road left, all of Red's roads are already on the board                                                                      |
| resources held compared with cost                       | Exact city cost, road cost with 1 extra brick, city cost short by 1 ore                                                               |
| whether the placement is legal                          | Legal road edge or city upgrade, occupied edge that rejects the road                                                                  |
| build result                                            | Piece placed and payment collected, rejection for insufficient resources or an unavailable piece, rejection for ownership or location |

**Step 4: Each choice**

Use two different piece costs to verify that payment follows the supplied piece.

|             | Inputs                                                                                                                                          | Expected output                                                                                                                                | Implemented? |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Red has 2 brick and 1 lumber and an available Red road. Red builds it at a legal edge.                                                          | The road is placed and leaves Red's piece inventory. Red retains 1 brick and 0 lumber. The bank gains 1 brick and 1 lumber.                    | :x:          |
| Test Case 2 | Red has exactly 2 grain and 3 ore and an available Red city. Red builds it on a vertex with Red's settlement.                                   | The city replaces the settlement, which returns to Red's piece inventory. Red's grain and ore drop to 0, and the bank gains 2 grain and 3 ore. | :x:          |
| Test Case 3 | Red has 2 grain and 2 ore and an available Red city. Red tries to upgrade a legal vertex holding Red's settlement.                              | Throws `NotEnoughInventoryException`. The settlement stays on the board, and the piece and resource inventories stay as they were.             | :x:          |
| Test Case 4 | Red has the road cost and an available Red road. Red tries to build it on an edge occupied by another road.                                     | Throws `InvalidPlacementException`. The board and inventories stay as they were.                                                               | :x:          |
| Test Case 5 | Red and Blue can both afford a road. Red requests a build using an available Blue road at an edge where that Blue road could legally be placed. | Throws `InvalidPlacementException`. No piece moves and neither player nor the bank pays or receives any resources.                             | :x:          |
| Test Case 6 | Red can afford a road and has a legal empty edge, but all of Red's roads are already on the board. Red requests a build using a Red road.       | Throws `NotEnoughInventoryException`. The edge stays empty and the board and inventories stay as they were.                                    | :x:          |

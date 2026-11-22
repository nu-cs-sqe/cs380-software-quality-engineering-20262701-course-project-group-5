# Class BankResourceDistributor

Classpath: `catan.game_table.domain.bank.BankResourceDistributor`

## `public class BankResourceDistributor extends Observed`

Collects resource production claims for a roll and resolves them against the bank supply. A claim identifies a player, a resource type, and the quantity owed.

### `public BankResourceDistributor(Map<Color, ResourceInventory> playerInventories, Bank bank)`

Uses the supplied player inventories and bank.

### `public void produceResources(Color color, List<ResourceStack> products)`

Queues the resources owed to that player. Multiple entries for the same player and resource contribute to the same total. Inventories change when production is finalized.

### `public void finalizeProduction()`

For each resource, pays every claim if the bank has enough stock. If stock falls short, pays what remains when only one player is owed that resource. When multiple players are owed that resource, none receive it. Other resource types are resolved independently. Clears the claims after processing the roll.

### `public void abortProduction()`

Clears all queued claims without transferring cards.

## Boundary-Value Analysis

### Test: `produceResources(Color color, List<ResourceStack> products)` and `finalizeProduction()` **(lab 3 said we can combine tests)**

**Step 1: Inputs / outputs**

| Inputs                                                                                                                  | Outputs                                                                                                                       |
|-------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Which players are owed cards, the resources and quantities queued for the roll, and stock of each resource in the bank. | Inventories stay as they were while requests are queued. Finalization transfers the allowed quantities and clears the claims. |

**Step 2: Data types**

| Variable                                   | Type                               |
|--------------------------------------------|------------------------------------|
| resource stacks queued for the roll        | Collection                         |
| distinct players owed one resource         | Count                              |
| bank stock and total owed for one resource | Counts that interact at thresholds |
| quantities paid for each claim             | Counts                             |

**Step 3: Concrete values**

| Variable                            | Values                                                                                                                                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| resource stacks queued for the roll | Repeated stacks in one call, requests for the same resource in separate calls, and multiple resource types                                |
| distinct players owed one resource  | No players after the queue is cleared, one player, two players                                                                            |
| stock compared with total owed      | Exact `(4, 4)`, surplus `(3, 1)`, one short with a single claimant `(1, 2)`, one short with two claimants `(1, 2)`, and no stock `(0, 1)` |
| quantities paid                     | Full amount, remaining stock for one claimant, zero                                                                                       |

**Step 4: Each choice**

Pair the number of claimants with the shortage. The claimant count changes the payout when total demand exceeds stock.

|             | Inputs                                                                                                                                                                                      | Expected output                                                                                                                                                                                                 | Implemented? |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Bank stock is 4 wool and 3 grain. Queue `[1 wool, 1 grain, 1 wool]` for Red, then `[1 wool]` for Red and `[1 wool]` for Blue. Red starts with 2 wool. Finalize twice without queuing again. | Queuing leaves all inventories as they were. First finalization gives Red 3 wool and 1 grain and Blue 1 wool. Red ends with 5 wool, and the bank has 0 wool and 2 grain. Second finalization transfers nothing. | :x:          |
| Test Case 2 | Bank stock is 1 wool, 0 ore, and 3 grain. Queue `[2 wool, 1 ore]` for Red and `[1 grain]` for Blue.                                                                                         | Red receives the remaining 1 wool and no ore. Blue gets 1 grain. The bank has 0 wool, 0 ore, and 2 grain.                                                                                                       | :x:          |
| Test Case 3 | Bank stock is 1 wool and 3 grain. Queue `[1 wool, 1 grain]` for Red and `[1 wool]` for Blue.                                                                                                | Neither player gets wool, and the bank keeps the 1 wool. Red gets 1 grain, and the bank keeps 2 grain.                                                                                                          | :x:          |

### Test: `abortProduction()`

**Step 1: Inputs / outputs**

| Inputs                                                   | Outputs                                                                                        |
|----------------------------------------------------------|------------------------------------------------------------------------------------------------|
| Claims queued for a roll, with enough stock to pay them. | Aborting clears the claims and transfers nothing. The next roll processes only its own claims. |

**Step 2: Data types**

| Variable                      | Type       |
|-------------------------------|------------|
| queued claims                 | Collection |
| which roll a claim belongs to | Cases      |

**Step 3: Concrete values**

| Variable                      | Values                                                                           |
|-------------------------------|----------------------------------------------------------------------------------|
| queued claims                 | Red's wool and Blue's grain queued before abort, then a fresh ore claim for Blue |
| which roll a claim belongs to | Aborted roll, later roll that is finalized                                       |

**Step 4: Each choice**

|             | Inputs                                                                                                                             | Expected output                                                                                                | Implemented? |
|-------------|------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 4 | The bank has 3 wool, 2 grain, and 2 ore. Queue 1 wool for Red and 1 grain for Blue, then abort. Queue 1 ore for Blue and finalize. | Aborting leaves all inventories as they were. Later, only Blue gains 1 ore and only the bank's ore drops by 1. | :x:          |

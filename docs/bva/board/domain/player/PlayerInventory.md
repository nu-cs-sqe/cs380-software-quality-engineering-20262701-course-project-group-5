# Class PlayerInventory

Classpath: catan.game_table.domain.player.PlayerInventory

## class PlayerInventory

Represents one player's inventory, containing all their resources, pieces, and development cards.

### `public addPiece(piece : Piece)` : void

Adds piece to inventory.

### `public hasPiece(piece : Piece)` : boolean

Checks if equivalent piece exists in the inventory.

### `public removePiece(piece : Piece)` : void

Removes equivalent piece if able, throws `IllegalStateException` if empty.

### `public addResources(stack : ResourceStack)` : void

Adds resources to the inventory.

### `public hasResources(stack : ResourceStack)` : boolean

Checks if inventory has a greater than or equal resource stack to the one specified.

### `public removeResources(stack : ResourceStack)` : void

Remove resources from inventory if able, throws `IllegalStateException` if empty.

### `public addDevelopment(development : Development)` : void

Adds a development card.

### `public hasDevelopment(development : Development)` : boolean

Checks if equivalent development card exists in the inventory.

### `public removeDevelopment(development : Development)` : void

Removes development card from inventory if able, throws `IllegalStateException` if empty.

## Boundary-Value Analysis


### Method under test: `addPiece(piece : Piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|`piece: Piece`, state: list of pieces| state: list of pieces, IllegalArgumentException if it is a robber or a harbor |


**Step 2: Data types**
| Variable | Type |
|---|---|
|`piece`|Pointer, Case Piece is (Settlement, City, Road, Robber, Harbor)|
|interal list of pieces|Collection|
|Output|Cases (Void or IllegalArgumentException)|


**Operations:**
Appending a single element (to the internal list of pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|`piece`|Settlement, City, Road, Robber, Harbor|
|internal list of pieces| empty list, list with one element, list with one of each ownable type of piece |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `piece`: Harbor, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own a harbor`) | :x: |
| Test Case 2 | `piece`: Robber, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own the robber`) | :x: |
| Test Case 3 | `piece`: Settlement, internal list of pieces: empty list | internal list of pieces: {Settlement} | :x: |
| Test Case 4 | `piece`: City, internal list of pieces: empty list | internal list of pieces: {City} | :x: |
| Test Case 5 | `piece`: Road, internal list of pieces: empty list | internal list of pieces: {Road} | :x: |
| Test Case 6 | `piece`: Settlement, internal list of pieces: {Settlement, City, Road} | internal list of pieces: {Settlement, City, Road, Settlement} | :x: |
| Test Case 7 | `piece`: City, internal list of pieces: {Settlement, City, Road} | internal list of pieces: {Settlement, City, Road, City} | :x: |
| Test Case 8 | `piece`: Road, internal list of pieces: {Settlement, City, Road} | internal list of pieces: {Settlement, City, Road, Road} | :x: |
| Test Case 9 | `piece`: Settlement, internal list of pieces: {Road} | internal list of pieces: {Road, Settlement} | :x: |
| Test Case 10 | `piece`: City, internal list of pieces: {Road} | internal list of pieces: {Road, City} | :x: |
| Test Case 11 | `piece`: Road, internal list of pieces: {Road} | internal list of pieces: {Road, Road} | :x: |


### Method under test: `hasPiece(piece : Piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|`piece: Piece`, state: list of pieces| hasPiece: boolean|


**Step 2: Data types**
| Variable | Type |
|---|---|
|`piece`|Pointer, Case Piece is (Settlement, City, Road, Robber, Harbor)|
|interal list of pieces|Collection|
|Output| boolean |


**Operations:**
Equality comparisons

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|`piece`|Settlement, City, Road, Robber, Harbor|
|internal list of pieces| empty list, list with one element {repeat for each type}|


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `piece`: Harbor, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own a harbor`) | :x: |
| Test Case 2 | `piece`: Robber, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own the robber`) | :x: |
| Test Case 3 | `piece`: Settlement, internal list of pieces: empty list | false | :x: |
| Test Case 4 | `piece`: City, internal list of pieces: empty list | false | :x: |
| Test Case 5 | `piece`: Road, internal list of pieces: empty list | false | :x: |
| Test Case 6 | `piece`: Settlement, internal list of pieces: {Settlement} | true | :x: |
| Test Case 7 | `piece`: City, internal list of pieces: {Settlement} | false | :x: |
| Test Case 8 | `piece`: Road, internal list of pieces: {Settlement} | false | :x: |
| Test Case 9 | `piece`: Settlement, internal list of pieces: {Road} | False | :x: |
| Test Case 10 | `piece`: City, internal list of pieces: {Road} | false | :x: |
| Test Case 11 | `piece`: Road, internal list of pieces: {Road} | true | :x: |
| Test Case 12 | `piece`: Settlement, internal list of pieces: {City} | false | :x: |
| Test Case 13 | `piece`: City, internal list of pieces: {City} | true | :x: |
| Test Case 14 | `piece`: Road, internal list of pieces: {City} | false | :x: |



### Method under test: `removePiece(piece : Piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
|`piece: Piece`, state: list of pieces| state: list of pieces or `IllegalStateException`|


**Step 2: Data types**
| Variable | Type |
|---|---|
|`piece`|Pointer, Case Piece is (Settlement, City, Road, Robber, Harbor)|
|interal list of pieces|Collection|
|Output| Cases {Void, `IllegalStateException`} |


**Operations:**
Equality comparisons

**Step 3: Concrete values**
| Variable | Values |
|---|---|
|`piece`|Settlement, City, Road, Robber, Harbor|
|internal list of pieces| empty list, list with one element {repeat for each type}|


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `piece`: Road, internal list of pieces: empty list | `IllegalStateException` | :x: |
| Test Case 2| `piece`: Settlement, internal list of pieces: {Settlement} | internal list of pieces: empty list| :x: |
| Test Case 3| `piece`: Settlement, internal list of pieces: {Road} | internal list of pieces: {Road} | :x: |
| Test Case 4| `piece`: City, internal list of pieces: {City} | internal list of pieces: empty list| :x: |
| Test Case 5| `piece`: City, internal list of pieces: {Road} | internal list of pieces: {Road} | :x: |
| Test Case 6| `piece`: Road, internal list of pieces: {Road} | internal list of pieces: empty list| :x: |
| Test Case 7| `piece`: Road, internal list of pieces: {City} | internal list of pieces: {City} | :x: |
| Test Case 8 | `piece`: Harbor, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own a harbor`) | :x: |
| Test Case 9 | `piece`: Robber, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own the robber`) | :x: |
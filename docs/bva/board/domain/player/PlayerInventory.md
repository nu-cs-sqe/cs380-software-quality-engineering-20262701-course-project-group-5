# Class PlayerInventory

Classpath: catan.game_table.domain.player.PlayerInventory

## class PlayerInventory

Represents one player's inventory, containing all their resources, pieces, and development cards.

### `public addPiece(piece : Piece)` : void

Adds piece to inventory.

### `public hasPiece(piece : Piece)` : boolean

Checks if equivalent piece exists in the inventory.

### `public removePiece(piece : Piece)` : void

Removes equivalent piece if able, throws `NotEnoughInventoryException` if empty.

### `public addResources(stack : ResourceStack)` : void

Adds resources to the inventory.

### `public hasResources(stack : ResourceStack)` : boolean

Checks if inventory has a greater than or equal resource stack to the one specified.

### `public removeResources(stack : ResourceStack)` : void

Remove resources from inventory if able, throws `NotEnoughResourcesException` if empty.

### `public addDevelopment(development : Development)` : void

Adds a development card.

### `public hasDevelopment(development : Development)` : boolean

Checks if equivalent development card exists in the inventory.

### `public removeDevelopment(development : Development)` : void

Removes development card from inventory if able, throws `NotEnoughInventoryException` if empty.

## Boundary-Value Analysis


### Method under test: `addPiece(piece : Piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece: Piece`, state: list of pieces | state: list of pieces, IllegalArgumentException if it is a robber or a harbor |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece`| Case Piece is (Settlement, City, Road, Robber, Harbor) |
| interal list of pieces | Collection |
| Output |Cases (Void or IllegalArgumentException) |


**Operations:**
Appending a single element (to the internal list of pieces)

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` |Settlement, City, Road, Robber, Harbor |
| internal list of pieces | empty list, list with one element, list with one of each ownable type of piece |


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
| `piece: Piece`, state: list of pieces | hasPiece: boolean |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Case Piece is (Settlement, City, Road, Robber, Harbor) |
| interal list of pieces | Collection |
| Output | boolean |


**Operations:**
Equality comparisons

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | Settlement, City, Road, Robber, Harbor |
| internal list of pieces | empty list, list with one element {repeat for each type}, list with two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `piece`: Harbor, internal list of pieces: does not matter | `IllegalArgumentException('player cannot own a harbor')` | :x: |
| Test Case 2 | `piece`: Robber, internal list of pieces: does not matter | `IllegalArgumentException('player cannot own the robber')` | :x: |
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
| Test Case 14 | `piece`: Road, internal list of pieces: {City, Road} | true | :x: |
| Test Case 14 | `piece`: City, internal list of pieces: {Road, Settlement} | false | :x: |

### Method under test: `removePiece(piece : Piece)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `piece: Piece`, state: list of pieces | state: list of pieces or `NotEnoughInventoryException` |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `piece` | Case Piece is (Settlement, City, Road, Robber, Harbor) |
| interal list of pieces | Collection |
| Output | Cases {Void, `NotEnoughInventoryException`} |


**Operations:**
Equality comparisons, Removing a single element

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `piece` | Settlement, City, Road, Robber, Harbor |
| internal list of pieces | empty list, list with one element {repeat for each type}, list with two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `piece`: Road, internal list of pieces: empty list | `NotEnoughInventoryException` | :x: |
| Test Case 2 | `piece`: Settlement, internal list of pieces: {Settlement} | internal list of pieces: empty list| :x: |
| Test Case 3 | `piece`: Settlement, internal list of pieces: {Road} | internal list of pieces: {Road} | :x: |
| Test Case 4 | `piece`: City, internal list of pieces: {City} | internal list of pieces: empty list| :x: |
| Test Case 5 | `piece`: City, internal list of pieces: {Road} | internal list of pieces: {Road} | :x: |
| Test Case 6 | `piece`: Road, internal list of pieces: {Road} | internal list of pieces: empty list| :x: |
| Test Case 7 | `piece`: Road, internal list of pieces: {City} | internal list of pieces: {City} | :x: |
| Test Case 8 | `piece`: Harbor, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own a harbor`) | :x: |
| Test Case 9 | `piece`: Robber, internal list of pieces: does not matter | IllegalArgumentException(`player cannot own the robber`) | :x: |
| Test Case 10 | `piece`: Road, internal list of pieces: {City, Road} | internal list of pieces: {City} | :x: |
| Test Case 11 | `piece`: Settlement, internal list of pieces: {Settlement, Settlement} | internal list of pieces: {Settlement} | :x: |
| Test Case 12 | `piece`: Settlement, internal list of pieces: {City, Road} | internal list of pieces: {City, Road} | :x: |

### Method under test: `addResources(stack : ResourceStack)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `stack: ResourceStack`, state: list of resources | state: list of resources |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `stack` | Case Piece is (WoolResourceStack, LumberResourceStack, OreResourceStack, GrainResourceStack, BrickResourceStack) |
| interal list of resources|Collection |
| Output | internal list of resources |


**Operations:**
Appending a single element if no element of that type already exists

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `stack`|WoolResourceStack, LumberResourceStack, OreResourceStack, GrainResourceStack, BrickResourceStack, each with size 1 |
| internal list of pieces | empty list, list with one element, list with one of each ownable type of piece |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `stack`: LumberResourceStack{size:1}, internal list of resources: empty list | internal list of resources: {LumberResourceStack{size:1}} | :x: |
| Test Case 2 | `stack`: OreResourceStack{size:1}, internal list of resources: empty list | internal list of resources: {OreResourceStack{size:1}} | :x: |
| Test Case 3 | `stack`: WoolResourceStack{size:1}, internal list of resources: empty list | internal list of resources: {WoolResourceStack{size:1}} | :x: |
| Test Case 4 | `stack`: BrickResourceStack{size:1}, internal list of resources: empty list | internal list of resources: {BrickResourceStack{size:1}} | :x: |
| Test Case 5 | `stack`: GrainResourceStack{size:1}, internal list of resources: empty list | internal list of resources: {GrainResourceStack{size:1}} | :x: |
| Test Case 6 | `stack`: LumberResourceStack{size:1}, internal list of resources: {LumberResourceStack{size:1}} | internal list of resources: {LumberResourceStack{size:2}} | :x: |
| Test Case 7 | `stack`: WoolResourceStack{size:1}, internal list of resources: {WoolResourceStack{size:1}} | internal list of resources: {WoolResourceStack{size:2}} | :x: |
| Test Case 8 | `stack`: OreResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}} | internal list of resources: {OreResourceStack{size:2}} | :x: |
| Test Case 9 | `stack`: BrickResourceStack{size:1}, internal list of resources: {BrickResourceStack{size:1}} | internal list of resources: {BrickResourceStack{size:2}} | :x: |
| Test Case 10 | `stack`: GrainResourceStack{size:1}, internal list of resources: {GrainResourceStack{size:1}} | internal list of resources: {GrainResourceStack{size:2}} | :x: |
| Test Case 11 | `stack`: GrainResourceStack{size:1}, internal list of resources: {GrainResourceStack{size:1}, OreResourceStack{size:1}, WoolResourceStack{size:1}, LumberResourceStack{size:1}, BrickResourceStack{size:1}} | internal list of resources: {GrainResourceStack{size:2}, OreResourceStack{size:1}, WoolResourceStack{size:1}, LumberResourceStack{size:1}, BrickResourceStack{size:1}} | :x: |




### Method under test: `hasResources(stack : ResourceStack)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `stack: ResourceStack`, state: list of resources | hasResources: boolean |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `stack`| Case Piece is (WoolResourceStack, LumberResourceStack, OreResourceStack, GrainResourceStack, BrickResourceStack) |
| interal list of resources| Collection |
| Output | boolean |


**Operations:**
Equality comparisons

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `stack` | WoolResourceStack, LumberResourceStack, OreResourceStack, GrainResourceStack, BrickResourceStack, each with sizes {1, INT_MAX} |
| internal list of resources | empty list, list with one element {repeat for each type}, list with two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `stack`: LumberResourceStack{size:1}, internal list of resources: empty list | false | :x: |
| Test Case 2 | `stack`: OreResourceStack{size:1}, internal list of resources: empty list | false | :x: |
| Test Case 3 | `stack`: WoolResourceStack{size:1}, internal list of resources: empty list | false | :x: |
| Test Case 4 | `stack`: BrickResourceStack{size:1}, internal list of resources: empty list | false | :x: |
| Test Case 5 | `stack`: GrainResourceStack{size:1}, internal list of resources: empty list | false| :x: |
| Test Case 6 | `stack`: LumberResourceStack{size:1}, internal list of resources: {LumberResourceStack{size:1}} | true | :x: |
| Test Case 7 | `stack`: WoolResourceStack{size:1}, internal list of resources: {WoolResourceStack{size:1}} | true | :x: |
| Test Case 8 | `stack`: OreResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}} | true | :x: |
| Test Case 9 | `stack`: BrickResourceStack{size:1}, internal list of resources: {BrickResourceStack{size:1}} | true | :x: |
| Test Case 10 | `stack`: GrainResourceStack{size:1}, internal list of resources: {GrainResourceStack{size:1}} | true | :x: |
| Test Case 11 | `stack`: LumberResourceStack{size:INT_MAX}, internal list of resources: {LumberResourceStack{size:1}} | false | :x: |
| Test Case 12 | `stack`: WoolResourceStack{size:INT_MAX}, internal list of resources: {WoolResourceStack{size:1}} | false | :x: |
| Test Case 13 | `stack`: OreResourceStack{size:INT_MAX}, internal list of resources: {OreResourceStack{size:1}} | false | :x: |
| Test Case 14 | `stack`: BrickResourceStack{size:INT_MAX}, internal list of resources: {BrickResourceStack{size:1}} | false | :x: |
| Test Case 15 | `stack`: GrainResourceStack{size:INT_MAX}, internal list of resources: {GrainResourceStack{size:1}} | false | :x: |
| Test Case 16 | `stack`: GrainResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}} | false | :x: |
| Test Case 17 | `stack`: GrainResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}, GrainResourceStack{size:1}} | true | :x: |
| Test Case 18 | `stack`: GrainResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}, LumberResourceStack{size:1}} | false | :x: |



### Method under test: `removeResources(stack : ResourceStack)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `stack: ResourceStack`, state: list of resources | Case {state: list of resources, `NotEnoughResourcesException`} |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `stack` | Case Piece is (WoolResourceStack, LumberResourceStack, OreResourceStack, GrainResourceStack, BrickResourceStack) |
| interal list of resources | Collection |
| Output | Case(void, `NotEnoughResourcesException`) |


**Operations:**
Removing one element from a list

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `stack` | WoolResourceStack, LumberResourceStack, OreResourceStack, GrainResourceStack, BrickResourceStack, each with sizes {1, INT_MAX} |
| internal list of resources | empty list, list with one element {repeat for each type}, list with two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `stack`: LumberResourceStack{size:1}, internal list of resources: empty list | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 2 | `stack`: OreResourceStack{size:1}, internal list of resources: empty list | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 3 | `stack`: WoolResourceStack{size:1}, internal list of resources: empty list | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 4 | `stack`: BrickResourceStack{size:1}, internal list of resources: empty list | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 5 | `stack`: GrainResourceStack{size:1}, internal list of resources: empty list | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 6 | `stack`: LumberResourceStack{size:1}, internal list of resources: {LumberResourceStack{size:1}} | internal list of resources: empty list | :x: |
| Test Case 7 | `stack`: WoolResourceStack{size:1}, internal list of resources: {WoolResourceStack{size:1}} | internal list of resources: empty list | :x: |
| Test Case 8 | `stack`: OreResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}} | internal list of resources: empty list | :x: |
| Test Case 9 | `stack`: BrickResourceStack{size:1}, internal list of resources: {BrickResourceStack{size:1}} | internal list of resources: empty list | :x: |
| Test Case 10 | `stack`: GrainResourceStack{size:1}, internal list of resources: {GrainResourceStack{size:1}} | internal list of resources: empty list | :x: |
| Test Case 11 | `stack`: LumberResourceStack{size:INT_MAX}, internal list of resources: {LumberResourceStack{size:1}} | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 12 | `stack`: WoolResourceStack{size:INT_MAX}, internal list of resources: {WoolResourceStack{size:1}} | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 13 | `stack`: OreResourceStack{size:INT_MAX}, internal list of resources: {OreResourceStack{size:1}} | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 14 | `stack`: BrickResourceStack{size:INT_MAX}, internal list of resources: {BrickResourceStack{size:1}} | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 15 | `stack`: GrainResourceStack{size:INT_MAX}, internal list of resources: {GrainResourceStack{size:1}} | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 16 | `stack`: GrainResourceStack{size:1}, internal list of resources: {OreResourceStack{size:1}} | `NotEnoughResourcesException("not enough of that resource type")` | :x: |
| Test Case 17 | `stack`: WoolResourceStack{size:1}, internal list of resources: {WoolResourceStack{size:1}, OreResourceStack{size:1}} | internal list of resources: {OreResourceStack{size:1}} | :x: |
| Test Case 18 | `stack`: BrickResourceStack{size:1}, internal list of resources: {WoolResourceStack{size:1}, OreResourceStack{size:1}} |`NotEnoughResourcesException("not enough of that resource type")` | :x: |


### Method under test: `addDevelopment(development : Development)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `dev: Development`, state: list of dev cards | state: list of dev cards |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `dev` | Development{Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty} |
| interal list of dev cards | Collection |



**Operations:**
Adding one element

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `dev`| one of each card type: Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty |
| internal list of dev cards | empty list, list with one element, list with two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `dev`: Knight, internal list: empty | internal list: {Knight} | :x: |
| Test Case 2 | `dev`: RoadBuilder, internal list: empty | internal list: {RoadBuilder} | :x: |
| Test Case 3 | `dev`: VictoryPoint, internal list: empty | internal list: {VictoryPoint} | :x: |
| Test Case 4 | `dev`: Monopoly, internal list: empty | internal list: {Monopoly} | :x: |
| Test Case 5 | `dev`: YearOfPlenty, internal list: empty | internal list: {YearOfPlenty} | :x: |
| Test Case 6 | `dev`: YearOfPlenty, internal list: {Knight} | internal list: {Knight, YearOfPlenty} | :x: |
| Test Case 7 | `dev`: YearOfPlenty, internal list: {Knight, RoadBuilder} | internal list: {Knight, RoadBuilder, YearOfPlenty} | :x: |


### Method under test: `hasDevelopment(development : Development)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `dev: Development`, state: list of dev cards | hadDev: boolean |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `dev` | Development{Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty} |
| interal list of dev cards | Collection |



**Operations:**
Adding one element

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `dev` | one of each card type: Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty |
| internal list of dev cards | empty list, list with one element (repeat for each type), list with two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `dev`: Knight, internal list: empty | false | :x: |
| Test Case 2 | `dev`: RoadBuilder, internal list: empty | false| :x: |
| Test Case 3 | `dev`: VictoryPoint, internal list: empty | false | :x: |
| Test Case 4 | `dev`: Monopoly, internal list: empty | false | :x: |
| Test Case 5 | `dev`: YearOfPlenty, internal list: empty | false | :x: |
| Test Case 6 | `dev`: Knight, internal list: {Knight} | true | :x: |
| Test Case 7 | `dev`: RoadBuilder, internal list: {RoadBuilder} | true | :x: |
| Test Case 8 | `dev`: VictoryPoint, internal list: {VictoryPoint} | true | :x: |
| Test Case 9 | `dev`: Monopoly, internal list: {Monopoly}  | true | :x: |
| Test Case 10 | `dev`: YearOfPlenty, internal list: {YearOfPlenty} | true | :x: |
| Test Case 11 | `dev`: YearOfPlenty, internal list: {RoadBuilder} | false | :x: |
| Test Case 12 | `dev`: YearOfPlenty, internal list: {RoadBuilder, Knight} | false | :x: |
| Test Case 13 | `dev`: YearOfPlenty, internal list: {Knight, YearOfPlenty} | true | :x: |


### Method under test: `removeDevelopment(development : Development)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `dev: Development`, state: list of dev cards | Case (state: list of dev cards, `NotEnoughInventoryException`) |


**Step 2: Data types**
| Variable | Type |
|---|---|
| `dev` | Development{Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty} |
| interal list of dev cards| Collection |



**Operations:**
Adding one element

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `dev` | one of each card type: Knight, RoadBuilder, VictoryPoint, Monopoly, YearOfPlenty |
| internal list of dev cards | empty list, list with one element, list of two elements |


**Step 4: Each choice** 

|              | Inputs                                                                                                         | Expected output                 | Implemented? |
|--------------|----------------------------------------------------------------------------------------------------------------|---------------------------------|--------------|
| Test Case 1 | `dev`: doesn't matter, internal list: empty | `NotEnoughInventoryException` | :x: |
| Test Case 2 | `dev`: Knight, internal list: {RoadBuilder} | `NotEnoughInventoryException` | :x: |
| Test Case 3 | `dev`: VictoryPoint, internal list: {VictoryPoint} | internal list: empty | :x: |
| Test Case 4 | `dev`: Knight, internal list: {Knight} | internal list: empty | :x: |
| Test Case 5 | `dev`: RoadBuilder, internal list: {RoadBuilder} | internal list: empty | :x: |
| Test Case 6 | `dev`: Monopoly, internal list: {Monopoly} | internal list: empty | :x: |
| Test Case 7 | `dev`: YearOfPlenty, internal list: {YearOfPlenty} | internal list: empty | :x: |
| Test Case 8 | `dev`: YearOfPlenty, internal list: {YearOfPlenty, Knight} | internal list: {knight} | :x: |
| Test Case 9 | `dev`: YearOfPlenty, internal list: {Monopoly, Knight} | `NotEnoughInventoryException` | :x: |
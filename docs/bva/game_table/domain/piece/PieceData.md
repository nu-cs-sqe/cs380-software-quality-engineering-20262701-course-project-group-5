# Class PieceData

Classpath: catan.game_table.domain.piece.PieceData

## `public class PieceData`

A holder for a `Piece` objects color and type data, the former referencing an Enum and the latter being a string.

### `@Override public boolean equals(o : Object)`

Returns true if the `color` and `type` of a given `Piece` is equal to the color and type of o.

### `@Override public int hashCode()`

Returns an integer based on the color and the subclass of a given `Piece`. If two `Piece`s have the same color and subclass type, then their hash codes will be equal.

## Boundary-Value Analysis

### Method under test: `equals(o : Object)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `o : Object` | Whether or not the `PieceData` of a `Piece` is equal to a given object |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `o` colors | Cases |
| `o` types | Cases |
| Output | Boolean |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `o` colors | Cases: Blue, Orange, Red, White |
| `o` types | Cases: `Building` (`City` and `Settlement` subclasses), `Harbor`, `Road`, `Robber` |
| Output | True, False |

**Step 4: Each choice**
|              | Input | Expected output | Implemented? |
|--------------|----------------------------------------|----------------------------|--------------|
| Test Case 1 | Let this be a City owned by Blue, input a City owned by Blue | True | :x: |
| Test Case 2 | Let this be a Settlement owned by Orange, input a Settlement owned by Orange | True | :x: |
| Test Case 3 | Let this be a Harbor owned by Red, input a Harbor owned by Red | True | :x: |
| Test Case 4 | Let this be a Road owned by White, input a Road owned by White | True | :x: |
| Test Case 5 | Let this be a Robber with No Color, input a Road owned by White | False | :x: |
| Test Case 6 | Let this be a City owned by White, input a Settlement owned by Red | False | :x: |
| Test Case 7 | Let this be a Road owned by Orange, input a Harbor owned by Blue | False | :x: |
| Test Case 8 | Let this be a Settlement owned by Red, input a Road owned by Red | False | :x: |
| Test Case 9 | Let this be a Harbor owned by Orange, input a Harbor owned by Blue | False | :x: |


### Method under test: `public int hashCode()`

|             | State of the System                                        | Expected output      | Implemented? |
|-------------|------------------------------------------------------------|----------------------|--------------|
| Test Case 1 | Two different `Piece` instances of `type` City and `color` Blue | same value from each | :x:          |

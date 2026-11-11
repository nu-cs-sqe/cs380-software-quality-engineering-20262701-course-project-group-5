# Class ResourceStack
Classpath: catan.game_table.domain.resource.ResourceStack 

Direct known subclasses: `BrickResourceStack`, `LumberResourceStack`, `OreResourceStack`, `GrainResourceStack`, `WoolResourceStack`

## `public abstract class ResourceStack`

A holder for a specified number of resources of a type (Brick, Lumber, Ore, etc.) encoded by corresponding subclasses.

### `public static ResourceStack combine(ResourceStack stack1, ResourceStack stack2)`
Returns a new `ResourceStack` with the sum of the sizes of `stack1` and `stack2`. Throws an `IllegalArgumentException` if `stack1` and `stack2` are not the same type.

### `public ResourceStack(int size)`
Initializes a `ResourceStack` with the given `size`.

### `public final int getSize()`
Returns the size of this `ResourceStack`.

### `@Override public boolean equals(Object o)`
Returns `true` if the size and type of this `ResourceStack` is equal to the size and type of `o`.

### `@Override public int hashCode()`
Returns an integer dependent on the size and the concrete subclass of this `ResourceStack`. If two `ResourceStack` instances have the same type and size, then their hash codes will be equal.

## Boundary-Value Analysis

### Test: `public static ResourceStack combine(ResourceStack stack1, ResourceStack stack2)`

|             | State of the System                                                                           | Expected output                                                          | Implemented? |
|-------------|-----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|--------------|
| Test Case 1 | `BrickResourceStack` with size `1` and `BrickResourceStack` with size `INT_MAX - 1`           | `BrickResourceStack` with size `INT_MAX`                                 | :white_check_mark: |
| Test Case 2 | `BrickResourceStack` with size `0` and `BrickResourceStack` with size `0`                     | `BrickResourceStack` with size `0`                                       | :white_check_mark: |
| Test Case 3 | Let this be a `LumberResourceStack` with size `0`, input a `BrickResourceStack` with size `0` | `IllegalArgumentException` "Incompatible ResourceStack types: %s and %s" | :x:          |


### Test: `public boolean equals(Object o)`

|             | State of the System                                                                           | Expected output | Implemented? |
|-------------|-----------------------------------------------------------------------------------------------|-----------------|--------------|
| Test Case 1 | Let this be a `BrickResourceStack` with size `1`, input a `BrickResourceStack` with size `1`  | true            | :white_check_mark: |
| Test Case 2 | Let this be a `BrickResourceStack` with size `1`, input a `BrickResourceStack` with size `2`  | false           | :white_check_mark: |
| Test Case 3 | Let this be a `LumberResourceStack` with size `1`, input a `BrickResourceStack` with size `1` | false           | :x:          |
| Test Case 4 | Let this be a `BrickResourceStack` with size `1`, input a `LumberResourceStack` with size `1` | false           | :x:          |
| Test Case 5 | Let this be a `BrickResourceStack` with size `1`, input `this`                                | true            | :x:          |

### Test: `public int hashCode()`

|             | State of the System                                        | Expected output      | Implemented? |
|-------------|------------------------------------------------------------|----------------------|--------------|
| Test Case 1 | Two different `BrickResourceStack` instances with size `1` | same value from each | :white_check_mark: |
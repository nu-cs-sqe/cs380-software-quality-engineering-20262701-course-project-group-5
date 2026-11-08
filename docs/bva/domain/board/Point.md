# Class Point
Classpath: catan.game_table.domain.board.Point

## `public class Point`

An immutable `(x, y)` coordinate with `public final int x` and `public final int y`.

### `public boolean equals(Object o)`
Returns true if `o` is a `Point` with the same `x` and the same `y` as this one. Otherwise returns false.

### `public int hashCode()`
Returns a hash code consistent with `equals`: two equal points have the same hash code.

## Boundary-Value Analysis

### Test: `public boolean equals(Object o)`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| `o : Object`; state: this point's `x` and `y` | `boolean` |

**Step 2: Data types**
| Variable | Type |
|---|---|
| `o` | Cases (a `Point`, an object that is not a `Point`) |
| `o` vs this point | Pair of References (object equality) |
| `x` of `o` vs `x` of this | Boolean (equal, different) |
| `y` of `o` vs `y` of this | Boolean (equal, different) |
| returned value (output) | Boolean |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| `o` | a `Point`, a `String` (not a `Point`) |
| `o` vs this point | same object, different object with equal x and y |
| `x` of `o` vs `x` of this | equal, different |
| `y` of `o` vs `y` of this | equal, different |
| returned value (output) | true, false |

**Step 4: Each choice**

|             | Input                                              | Expected output | Implemented? |
|-------------|----------------------------------------------------|-----------------|--------------|
| Test Case 1 | `Point(1, 2)` compared to itself                   | true            | :x:          |
| Test Case 2 | `Point(1, 2)` and a different `Point(1, 2)`        | true            | :x:          |
| Test Case 3 | `Point(1, 2)` and `Point(3, 2)` (only `x` differs) | false           | :x:          |
| Test Case 4 | `Point(1, 2)` and `Point(1, 3)` (only `y` differs) | false           | :x:          |
| Test Case 5 | `Point(1, 2)` and a `String`                       | false           | :x:          |

### Test: `public int hashCode()`

**Step 1: Inputs / outputs**
| Inputs | Outputs |
|---|---|
| state: this point's `x` and `y` | `int` |

**Step 2: Data types**
| Variable | Type |
|---|---|
| point vs another point | Pair of References (object equality) |
| returned value (output) | Number (compared for equality) |

**Step 3: Concrete values**
| Variable | Values |
|---|---|
| point vs another point | same object, different object with equal x and y |
| returned value (output) | same value, same value |

**Step 4: Each choice**

|             | Input                                         | Expected output                  | Implemented? |
|-------------|-----------------------------------------------|----------------------------------|--------------|
| Test Case 1 | `Point(1, 2)`, `hashCode()` called twice      | the same value both times        | :x:          |
| Test Case 2 | `Point(1, 2)` and a different `Point(1, 2)`   | the same hash code for both      | :x:          |
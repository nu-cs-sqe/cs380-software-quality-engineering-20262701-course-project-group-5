![Gradle Build](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/actions/workflows/main.yml/badge.svg)

![Missed complexity](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/raw/badges/complexity.svg)

![Mutants killed](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/raw/badges/mutants-killed.svg)

# Settlers of Catan (by team Toyota Camry)

## Contributors
- Anthony Alvarez
- Breanna Lu
- Hayley McCormack
- Xavier Khan
- Daniel Murphy

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10

## Exceptions
### Code Coverage
- `PieceData`: `equals()` requires null checks but our functions should not take null inputs so the branch is never covered.
- 11/21: The current tests do not exercise `getLocationType()` on `Vertex`, `Edge`, or `Hex`, or the enum's display labels. Placement validation has yet to use this API.
### Mutation Testing
- `PieceData`: `hashCode()` adds hash of PieceData's fields and an equivalent mutant where the fields are subtracted will not be killed.
- 11/21: PIT reports four uncovered mutants for the location-type API, one in each of the three `getLocationType()` implementations and one in `LocationType.toString()`.
### Others
N/A
## Spotbugs
URF_UNREAD_FIELD — "Field is written but never read"
EI_EXPOSE_REP2 — "May expose internal representation by incorporating reference to mutable object"

Files:
src/main/java/catan/game_table/domain/piece/Settlement.java (class-level)
Why: The Settlement class has a ScoreTracker field of the player that owns it. The Settlement class initializes the field so that when placed on the board, the PlacementHelper can trigger an update to the owning players points. Because the write and the read are in different methods, SpotBugs treats the field as written but never read.

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
- Updated BVA and tests to ensure that ResourceStack.hashCode() generates different hash codes when used with two instances of ResourceStack which would not be considered equal, in addition to testing the case where the hash codes are the same. This allows the full method to be justified under TDD, whereas before it could have simply always returned the same code, and all test cases where the hash codes must be the same would have passed.
- Added test cases for every child of ResourceStack to test their overrides of makeNewStackOfSameType, to justify actually creating those overrides under TDD. Previous production code did not qualify as "bare minimum", so code coverage left gaps. In this instance, test cases were documented and added after production code was written to resolve these gaps, we will be careful to avoid such violations going forward.
- `PieceData`: `equals()` requires null checks but our functions should not take null inputs so the branch is never covered.
- 11/21: The current tests do not exercise `getLocationType()` on `Vertex`, `Edge`, or `Hex`, or the enum's display labels. Placement validation has yet to use this API.
### Mutation Testing
- `PieceData`: `hashCode()` adds hash of PieceData's fields and an equivalent mutant where the fields are subtracted will not be killed.
- 11/21: PIT reports four uncovered mutants for the location-type API, one in each of the three `getLocationType()` implementations and one in `LocationType.toString()`.
### Others
Note: the Resource package should be integration tested with the bank and player packages
## Spotbugs
URF_UNREAD_FIELD — "Field is written but never read"

Files:
src/main/java/catan/game_table/domain/piece/City.java (class-level)
Why: The City class has a ScoreTracker field of the player that owns it. The City class initializes the field so that when placed on the board, the PlacementHelper can trigger an update to the owning players points. Because the write and the read are in different methods, SpotBugs treats the field as written but never read.
src/main/java/catan/game_table/domain/piece/Settlement.java (class-level)
Why: The Settlement class has a ScoreTracker field of the player that owns it. The Settlement class initializes the field so that when placed on the board, the PlacementHelper can trigger an update to the owning players points. Because the write and the read are in different methods, SpotBugs treats the field as written but never read.

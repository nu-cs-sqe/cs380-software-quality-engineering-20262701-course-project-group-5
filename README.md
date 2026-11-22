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
- Updated BVA and tests to cover the case where ResourceStack.hashCode() is different for two instances of ResourceStack
- Added test cases for every child of ResourceStack to test their overrides of makeNewStackOfSameType, to justify actually creating those overrides under BVA. Previous production code did not qualify as "bare minimum", so code coverage left gaps. In this instance, test cases were documented and added after production code was written to resolve these gaps, we will be careful to avoid such violations going forward.
- `PieceData`: `equals()` requires null checks but our functions should not take null inputs so the branch is never covered.
### Mutation Testing
- `PieceData`: `hashCode()` adds hash of PieceData's fields and an equivalent mutant where the fields are subtracted will not be killed.
### Others
Note: the Resource package should be integration tested with the bank and player packages

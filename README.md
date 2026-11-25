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
N/A
### Mutation Testing
N/A

### SpotBugs

**`EI_EXPOSE_REP2` — Stores a reference to an externally mutable object**

The filter applies to the `board` and `bank` assignments in the `BankTradeHelper` constructor. The helper shares the live `GameBoard` so harbor access reflects current placements, and it shares the live `Bank` so trades update the game's resource supply. [BankTradeHelper.java](src/main/java/catan/game_table/domain/trade/BankTradeHelper.java) · [SpotBugs filter](config/spotbugs/exclude.xml)

### Others

Integration testing in Trade package:

- Proposal and counteroffer tests use real `ResourceStack` objects to exercise resource-type and quantity validation.
- Offer lifecycle tests use real `TradeOffer` objects to verify that identical terms can belong to distinct offers and that acceptance updates only the selected offer.


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
### Others

Integration testing in Trade package:

- Proposal and counteroffer tests use real `ResourceStack` objects to exercise resource-type and quantity validation.
- Offer lifecycle tests use real `TradeOffer` objects to verify that identical terms can belong to distinct offers and that acceptance updates only the selected offer.


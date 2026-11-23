# Week 3 (10/12/2026-10/18/2026)
**Planning and Progress Tracking**:
1. [done] Anthony: Add contributors to README ([links to pr](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/4)
2. [done] Breanna: build badge to readme ([Links to pr](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/5))
3. [done] Anthony: Github actions ([Links to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/1))
4. [done] Breanna: add branch protection rules

# Week 4 (10/19/2026-10/25/2026)
**Planning and Progress Tracking**:


Breanna and Xavier:
[In progress] software design + uml diagrams
- Breanna: design goes more in depth on the Trading and Building functionalities, more enums used [link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/23)]
- Breanna: updated Hayley's UI with uml diagram [link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/22)]
- Xavier: developing the MVC, classes used for terrain and resource type [link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/18)]

Hayley:
started on UI [link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/22)]
refactored TradeManager on Breanna's PR [[link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/24)]
[in progress] review software design

Anthony:
[ready to merge] make all assets [[link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/21)]
[in progress] review software design

decided to add additional team meeting on wednesday
TODO: 
- team organization docs (Anthony will pic it up)
- make meeting minutes for Wednesday (everyone)

# Week 5 (10/26/2026-11/01/2026)
**Planning and Progress Tracking**:
1. Xavier, Hayley, Anthony, Breanna: Reviewed and finalized board design, merged PR ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/18))
2. Anthony: Merged assets PR ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/21))
3. Breanna: Contacted Daniel on behalf of the team
4. Xavier, Hayley, Anthony, Breanna: Split up BVA for board setup (see Projects tab)

TODO:
- onboard Daniel
- additional in-person meeting this week to confirm TDD process (if needed)


# Week 6 (11/02/2026-11/09/2026)
**Planning and Progress Tracking**:
1. Xavier, Hayley, Anthony, Breanna, Daniel: Reviewed board design for refactoring 
2. [in review] Anthony: Performed BVA on the Player package: ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/51))
3. [in review] Breanna: Performed BVA on the Board package: ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/49))
4. [in progress] Hayley: Performed BVA on the Piece package: ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/50))
5. [in review] Hayley: UI Static Page implementation: ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/50))
6. [merged] Xavier: Performed BVA on the Resource package
7. [in review] Xavier: Implemented board design refactor based on team discussions: ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/56))

TODO:
- Anyone: debug linter setup
- Anthony Finalize Player BVA
- Breanna: Finalize Board BVA
- Daniel: Begin TDD on Resource package
- Hayley: Finish Piece BVA in light of design refactor
- Tentative TODOs: Following BVA completion, begin TDD on all relevant packages

# Week 7 (11/09/2026 - 11/15/2026)
**Planning and Progress Tracking:**

1. [merged] Daniel: TDD on Resource package (all its classes)
2. [in review] Breanna: Completed TDD on Hex class and its subclasses ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/77))
3. [in review] Breanna: Completed TDD on GameBoard class, barring the "neighbors" functionality needed for PlacementHelper ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/73))
4. [in progress, will be done by 11/16] Breanna: TDD on Location, Point, Edge, Vertex ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/82))
5. [in progress, will be done by 11/18] Hayley: TDD on Piece package ([link to PR for PieceData class](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/86))
6. [in progress, will be done by 11/15] Xavier: TDD on PlacementHelper class ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/71))
7. [in progress] Anthony: TDD on Player package
8. [in progress]: Daniel: BVA on Bank package ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/87))

TODO:
- Anyone: Review Hayley's PieceData TDD
- All: Decide on Checkstyle warnings or violations
- Breanna: Review comments by Daniel on GameBoard TDD
- Breanna: TDD for added "neighbors" functionality in GameBoard
- Check and merge Breanna's completed TDDs (2 reviews done on each)
- TDD on remaining packages / classes

# Week 8 (11/15/2026 - 11/22/2026)
**Planning and Progress Tracking:**

1. [merged] Breanna: TDD on Hex, GameBoard, Location and its inheritors
2. [in review, approved] Daniel: Revised TDD on Resource package ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/120))
3. [in review] Anthony: TDD on GamePlayer ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/96))
4. [in progress] Breanna & Daniel: BVA and TDD on Bank, BVA done by Breanna, TDD will be done by Daniel ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/123))
5. [in progress] Breanna: BVA and TDD on trade package ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/125))
6. [in progress] Xavier: TDD on PlacementHelper class ([link to PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/71))
7. [in progress] Hayley: TDD on City and Settlement ([link to city PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/122/changes))
   ([link to settlement PR](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/99))
8. [merged] Hayley: TDD on PieceData class

TODO:
- Xavier and Anthony: Design for Development, Robber, Award
- Daniel: TDD on Bank package (done by Monday / early Tuesday)
- Breanna and Hayley: Controller design
- Breanna: TDD on Trade package (done by Tuesday)
- Anyone: BVA & TDD for Development, Robber, Award


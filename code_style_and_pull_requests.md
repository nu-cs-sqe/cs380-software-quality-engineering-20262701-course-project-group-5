# Code Style and Review guidelines proposal
this will be loosely based on the code review rules I (Anthony) had at my co-op this year

## PR guidelines

1. Every commit has to be working according to TDD
    1. There should not be any "fixed \<thing I introduced in this PR\>" commits
    2. See "rewriting history" on the git documentation: https://git-scm.com/book/en/v2/Git-Tools-Rewriting-History
    3. every commit should compile, all tests should pass
    4. commits should have meaningful titles, no "wip", no "fixed this one function". I will not enforce an extra commit description (this is separate from the commit -m "title")
    5. Run all linters before each commit
    6. commits generally should be limited in scope to 1 source file + corresponding tests file
2. PR contents
    1. Descriptive title, and should be similar to its corresponding issue
        1. For a canned format you could use "TYPE: \<short description\>". See https://github.com/py-pdf/pypdf/pulls for examples
    2.  at least one issue must be associated with each pull request
        4. Can use "closing patterns". In the PR description write "Closes \<insert link to issue here\>" to automatically close an issue upon merging the PR 
    3. Any new/updated design decisions should be documented and a rationale should be given
        1. e.g. changing function signature
3. Reviewers
    1. I think one is enough, for big decisions we should have 2 though
    2. PR author should randomly pick a reviewer with a random number generator to distribute workload evenly
4. Review comments
    1. Each reviewer should comment with clarifying questions and proposed changes (if any), highlighting specific lines of code when needed
    2. PR author should reply to each comment.
        1. this is done regardless of whether the assignee argues back for their design or submitted a new commit to address the comment. This is so the reviewer gets a notification for it
    3. Only comment authors may resolve comments. PR authors do not resolve comments themselves, ever
5. Merging Checks:
    1. All review comments are resolved
    2. All test cases/other CI components pass
6. Branch names:
    1. use format `feature/feature-area/feature-name` for new features, and `bugfix/description` for bug fixes. See: https://learn.microsoft.com/en-us/azure/devops/repos/git/git-branching-guidance?view=azure-devops


## Other code style things

1. I guess we can just use the google one: https://google.github.io/styleguide/javaguide.html
2. Run all linters before each commit

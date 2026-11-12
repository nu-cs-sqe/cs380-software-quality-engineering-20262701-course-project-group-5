# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

## Week 7 Code Review
Since not much has been pushed into main yet, I looked at some of your bigger PRs:
PR [#67](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/67), [#63](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/63),[#55](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-5/pull/55)

Good work overall! Two minor comments:
1. In #63, code reviewer's comment regarding not doing null check is indeed what Uncle Bob and we have covered in this class so far. Good that you point it out! The `equals()` method here, however, is an acceptable exception because the established convention for a good `equals` check always begins with the null check.

2. The Main class has Javadoc comments but not the other classes. You would want to unify the decision across the project.
   
## Week 6 Code Review
I have read every line of production code currently in the main branch. It doesn't seem that there is much pushed into main yet. Knowing your team's progress, this is very reasonable! I will do code review again next week.

---
title: "CMPT 370 Group A3"
subtitle: "Project Summary"
date: "December 8, 2015"
toc: true
---

-------------------------------------------------------------

## Programmer Documentation
...Javadocs

## User Manual
...

## Overall Summary
...

## Testing Summary
### Unit testing

We have used the JUnit testing library to iteratively test and in some cases
TDD our implementation of the more programmatically testable parts of our
application. This includes the Robot language Parser and robots themselves which
each have a small suite of unit tests.

This has been invaluable in preventing regressions in our code as we continued
to build these portions of our application. It has also helped us to iterate
quickly because after significant code changes we can simply run the tests for
a given module and ensure that it is still behaving as we expect. We've used
JUnit for this.

### Integration Testing
TDD and JUnit was not used in implementing the Views because they are largely
dependent on user interaction and GUI components.

...

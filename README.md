# Order Book Coding Assignment

## Configuration and Build

**Pre-reqs:** have Java on path and JDK installed in `$JAVA_HOME` directory.

To build the executable .jar file, invoke `./gradlew jar`.
The .jar file will be in `OrderBookAssignment/build/libs/OrderBookAssignment-1.0.jar`

The application builds with Kotlin 1.9 compiler targeting language version 2.0.
Tested on OpenJDK-21.

## Run

* Windows: `type test2.txt | .\exchange.cmd > test2-output.txt`
* Linux: `./exchange < test2.txt > test2-output.txt`

The application reads standard input line by line. A blank line or EOF
indicate the end of the input.

## Assumptions and notes

* The order id is represented as a char array as requested in the assignment.
  Potentially, it could be a (very long) integer, based on the test data provided.
  A char array offers a minor space optimization compared to `String`.

* I used this assignment as an opportunity to learn more about Kotlin coroutines.
  The coroutines are found in the Main file. They add a bit of extra complexity
  that would not be there otherwise.

* The code formatter was KtLint.

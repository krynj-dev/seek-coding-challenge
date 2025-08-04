# Seek Coding Challenge
This application takes in an input file consisting of various lines of timestamped car counts of the form `2024-11-30T23:00:00 10` and prints out
various summary statistics about it, including:
- The total cars recorded in the data
- The total cars recorded grouped by day
- The three periods with the highest number of cars recorded
- The three consecutive periods with the smallest total cars recorded

## Technologies Used
- Java 17
- Gradle (Kotlin)
- JUnit5

## Running the project
### Option 1: the IntelliJ way
1. Navigate to `com.hbaker.Main.java` within IntelliJ.
2. Click the `Run` button beside the `main` method.
3. Click `Modify Run Configuration...`.
4. Enter the path to the desired text file.
   1. For example: `src\test\resources\sample.txt`
5. Click `OK`.
6. Click the `Run` button beside the `main` method then select `Run 'Main.main()'` from the context menu.
### Option 2: the CLI way
1. Run `./gradlew build`.
2. Run `java -jar build/libs/seek-coding-challenge-1.0-SNAPSHOT.jar <file-path>`.
   1. For example: `java -jar build/libs/seek-coding-challenge-1.0-SNAPSHOT.jar src/test/resources/sample.txt`.

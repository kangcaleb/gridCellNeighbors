## GridCellNeighbors

Utility class for computing the number of cells within a given Manhattan distance from any positive-valued cell in a 2D grid.

### Requirements
* Java17+


### Usage
#### Compile
From project root run
```bash
javac -d target/classes src/main/java/org/ga/GridCellNeighbors.java
````

#### Run
```bash
java -cp target/classes org.ga.GridCellNeighbors <distance> <path_to_csv>
```

### Arguments
#### distance (int)
* The maximum Manhattan distance from any positive-valued cell

#### path_to_csv_file (String)
* Path to a CSV file representing a 2D grid of signed integers

### Example
#### grid.csv

```text
0, 1, 0
-1, 0, 2
0, 0, 0
```

#### Commands
```bash
javac -d target/classes src/main/java/org/ga/GridCellNeighbors.java
java -cp target/classes org.ga.GridCellNeighbors 1 grid.csv
```
#### Output
````text
Grid Successfully Parsed:
[0, 1, 0]
[-1, 0, 2]
[0, 0, 0]
Number of Neighbors with distance 1: 6
````

#### Explanation
There are two positive numbers 
* "1" at `grid[0][1]`
* "2" at `grid[1][2]`. 

Each has 3 unique neighbors:
* 1's neighbors: left `grid[0][0]`, right `grid[0][2]`, down `grid[1][1]`
* 2's neighbors: left `grid[1][1]`, up `grid[0][2]`, down `grid[2][2]`

After removing duplicates, the set of affected cells is:
`grid[0][0], grid[0][1], grid[0][2], grid[1][1], grid[1][2], grid[2][2]`

Total = 6 unique cells


### Notes
#### Grid
* The grid file must contain only integers, with rows on separate lines and numbers separated by commas.
* Whitespace is ignored when parsing.

* If any validation fails, a clear error message is printed and the program exits.
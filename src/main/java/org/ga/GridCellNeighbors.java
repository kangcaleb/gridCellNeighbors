package org.ga;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


abstract class GridCellNeighbors {

    public static void main(String[] args) {
        System.out.println("DEBUG: main method entered");

        if (args.length != 2) {
            throw new IllegalArgumentException("Expected exactly 2 Arguments: " +
                    "An integer N representing the max Manhattan distance from any positive cell and " +
                    "A path to a csv file with a 2D grid of signed integers");
        }

        try {
            // Validate Distance is Positive
            int distanceThreshold = Integer.parseInt(args[0]);
            if (distanceThreshold < 0) {
                throw new IllegalArgumentException("Integer N representing the max Manhattan distance from any positive cell cannot be negative");
            }

            // Validate CSV
            String gridPath = args[1];
            validateCsvFilepath(gridPath);

            try (BufferedReader reader = new BufferedReader(new FileReader(gridPath))) {
                List<int[]> rows = new ArrayList<>();
                String line;

                // Parse grid one row at a time, removing whitespace and blank values
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.replaceAll("\\s", "").split(",");

                    int[] row = Arrays.stream(tokens)
                            .filter(s -> !s.isBlank())
                            .mapToInt(Integer::parseInt)
                            .toArray();

                    if (row.length > 0) rows.add(row);
                }

                // Validate Grid
                int[][] grid = rows.toArray(new int[rows.size()][]);
                validateGrid(grid);

                // Print output
                System.out.println("Grid Successfully Parsed:");
                Arrays.stream(grid).map(Arrays::toString).forEach(System.out::println);
                System.out.println("Number of Neighbors with distance " + distanceThreshold + ": " + findNeighborCountOfPositives(grid, distanceThreshold));

            } catch (FileNotFoundException fnfe) {
                throw new IllegalArgumentException(fnfe.getMessage());
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Expected csv with only numbers separated by commas but failed to parse: " + nfe.getMessage());
            } catch (IOException ioe) {
                System.out.println("Error Parsing grid. Make sure your csv has only numbers separated by commas. Make sure each row is on a separated line");
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Expected max Manhattan distance as an integer but failed to parse: " + nfe.getMessage());
        }
    }

    /**
     *
     * Validates that path to csv isn't blank and that the file is a csv file
     * Throws Illegal Argument Exceptions if validation fails
     *
     * @param path to csv as a String
     */
    private static void validateCsvFilepath(String path) {
        if (path.isBlank()) throw new IllegalArgumentException("Path to csv grid mustn't be blank");
        if (!path.endsWith(".csv")) throw new IllegalArgumentException("Expected a csv file but found path given was " + path);
    }

    /**
     *
     * Validates grid parsed from specified csv has at least 1 row and that all rows
     * have the same length. Throws Illegal Argument Exceptions if validation fails
     *
     * @param grid as 2D array of integers, parsed from the specified csv file
     */
    private static void validateGrid(int[][] grid) {
        if (grid == null || grid.length == 0) throw new IllegalArgumentException("Expected a grid in csv but found none");

        int length = grid[0].length;
        if (!Arrays.stream(grid).allMatch(row -> row.length == length)) {
            throw new IllegalArgumentException("All rows in grid need to have the same length");
        }
    }

    /**
     * Computes the number of unique cells in the grid that are within a given Manhattan
     * distance of any positive value in the grid. Positive cells include themselves in their neighborhood.
     * Uses a Breadth first approach to explore valid neighbors up to the given distance.
     *
     * @param grid 2D grid of signed integers
     * @param n Maximum Manhattan distance from any positive cell
     * @return Count of unique cells within distance n of any positive cell
     */

    static int findNeighborCountOfPositives(int[][] grid, int n) {
        PriorityQueue<GridCoordinate> distanceQueue = new PriorityQueue<>(
                Comparator.comparingDouble(GridCellNeighbors::getDistance)
        );

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] > 0) {
                    GridCoordinate positiveCell = new GridCoordinate.GridCoordinateBuilder()
                            .forGrid(grid)
                            .withCoordinates(y, x)
                            .withDeltaFromPositive(0, 0)
                            .build();
                    distanceQueue.add(positiveCell);
                }
            }
        }

        Set<GridCoordinate> foundNeighbors = new HashSet<>();

        while (!distanceQueue.isEmpty()) {
            GridCoordinate currentCell = distanceQueue.poll();

            if (foundNeighbors.contains(currentCell) || getDistance(currentCell) > n) continue;

            foundNeighbors.add(currentCell);

            addAdjacentNeighbors(grid, currentCell, distanceQueue);
        }

        return foundNeighbors.size();
    }

    /**
     * Adds all valid adjacent neighbors (up/down/left/right) of the current cell
     * to the queue with incremented distance.
     *
     * @param grid The original grid
     * @param currentCell The cell currently being processed
     * @param distanceQueue The queue used to track cells to visit
     */
    private static void addAdjacentNeighbors(int[][] grid, GridCoordinate currentCell, PriorityQueue<GridCoordinate> distanceQueue) {
        GridCoordinate.GridCoordinateBuilder gridCoordinateBuilder = new GridCoordinate.GridCoordinateBuilder()
                .forGrid(grid);
        GridCoordinate up = gridCoordinateBuilder
                .withCoordinates(currentCell.getY()-1, currentCell.getX())
                .withDeltaFromPositive(currentCell.getDeltaY()-1, currentCell.getDeltaX())
                .build();
        if (up != null) distanceQueue.add(up);

        GridCoordinate left = gridCoordinateBuilder
                .withCoordinates(currentCell.getY(), currentCell.getX() - 1)
                .withDeltaFromPositive(currentCell.getDeltaY(), currentCell.getDeltaX() - 1)
                .build();
        if (left != null) distanceQueue.add(left);

        GridCoordinate right = gridCoordinateBuilder
                .withCoordinates(currentCell.getY(), currentCell.getX() + 1)
                .withDeltaFromPositive(currentCell.getDeltaY(), currentCell.getDeltaX() + 1)
                .build();
        if (right != null) distanceQueue.add(right);

        GridCoordinate down = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() + 1, currentCell.getX())
                .withDeltaFromPositive(currentCell.getDeltaY() + 1, currentCell.getDeltaX())
                .build();
        if (down != null) distanceQueue.add(down);
    }

    protected static double getDistance(GridCoordinate coordinate) {
        //return calculateEuclideanDistance(coordinate);
        return calculateManhattanDistance(coordinate);
    }

    private static double calculateManhattanDistance(GridCoordinate coordinate) {
        return Math.abs(coordinate.getDeltaY()) + Math.abs(coordinate.getDeltaX());
    }

    private static double calculateEuclideanDistance(GridCoordinate coordinate) {
        double sumOfSquaredDifferences = Math.pow(coordinate.getDeltaY(), 2) + Math.pow(coordinate.getDeltaX(), 2);
        return Math.sqrt(sumOfSquaredDifferences);
    }
}

/**
 * Represents a coordinate in the 2D grid, along with its distance from a source cell.
 * Equality and hashing are based only on coordinates (not distance)
 */
class GridCoordinate {
    private final int y;
    private final int x;
    private final int deltaY;
    private final int deltaX;

    private GridCoordinate(GridCoordinateBuilder gridCoordinateBuilder) {
        this.y = gridCoordinateBuilder.y;
        this.x = gridCoordinateBuilder.x;
        this.deltaY = gridCoordinateBuilder.deltaY;
        this.deltaX = gridCoordinateBuilder.deltaX;
    }

    int getY() {
        return y;
    }

    int getX() {
        return x;
    }

    int getDeltaY() {
        return deltaY;
    }

    int getDeltaX() {
        return deltaX;
    }

    @Override
    public String toString() {
        return y+","+x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y,x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridCoordinate otherCoordinate) {
            return this.y == otherCoordinate.y && this.x == otherCoordinate.x;
        }

        return false;
    }

    /**
     * Builder class for creating valid GridCoordinate instances, with bounds checking
     * based on the grid size.
     */
    static class GridCoordinateBuilder {
        int[][] grid;
        int y;
        int x;
        int deltaY;
        int deltaX;
        double distance;

        GridCoordinateBuilder forGrid(int[][] grid) {
            this.grid = grid;
            return this;
        }
        GridCoordinateBuilder withCoordinates(int y, int x) {
            this.y = y;
            this.x = x;
            return this;
        }

        GridCoordinateBuilder withDeltaFromPositive(int deltaY, int deltaX) {
            this.deltaY = deltaY; this.deltaX = deltaX;
            return this;
        }

        GridCoordinate build() {
            if (y < 0 || y >= grid.length) return null;
            if (x < 0 || x >= grid[0].length) return null;
            if (distance < 0) return null;

            return new GridCoordinate(this);
        }
    }
}

package org.ga;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Goal: Given a 2D Grid and a distance threshold N, find the number of neighboring
 * cells within distance N af any positive integer in the grid.
 */
public class GridCellNeighbors {

    /**
     * Steps:
     *
     * 1. Validate arguments
     * 2. Parse file line by line, turning each line into a row of integers to form grid
     * 3. Validate grid
     * 4. Call findTotalCellCountWithinRange to find neighbors of positives
     *
     * @param args distance threshold 'n', path to csv file
     */
    public static void main(String[] args) {

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

            // Validate CSV isn't blank and has correct ending
            String gridPath = args[1];
            validateCsvFilepath(gridPath);

            try (BufferedReader reader = new BufferedReader(new FileReader(gridPath))) {
                List<int[]> rows = new ArrayList<>();
                String line;

                // Parse grid one row at a time, removing whitespace and blank values
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.replaceAll("\\s", "").split(",");

                    // Filter out blank values and map line into int array
                    int[] row = Arrays.stream(tokens)
                            .filter(s -> !s.isBlank())
                            .mapToInt(Integer::parseInt)
                            .toArray();

                    if (row.length > 0) rows.add(row); // Only add rows that have non-blank values
                }

                // Validate Grid isn't empty and has equal row lengths
                int[][] grid = rows.toArray(new int[rows.size()][]);
                validateGrid(grid);

                // Print output
                System.out.println("Grid Successfully Parsed:");
                Arrays.stream(grid).map(Arrays::toString).forEach(System.out::println);

                // Call findTotalCellCountWithinRange, entry point of program
                System.out.println(findTotalCellCountWithinRange(grid, distanceThreshold) + " Neighbors within a manhattan distance of " + distanceThreshold);

            } catch (FileNotFoundException fnfe) {
                throw new IllegalArgumentException(fnfe.getMessage());
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Expected csv with only numbers separated by commas but failed to parse: " + nfe.getMessage());
            } catch (IOException ioe) {
                throw new RuntimeException("Error Parsing grid. Make sure your csv has only numbers separated by commas. Make sure each row is on a separated line");
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
    public static int findTotalCellCountWithinRange(int[][] grid, int n) {
        Set<GridCoordinate> positives = findPositiveCellsForGrid(grid);

        // foundNeighborSet keeps track of neighboring cells found and processed
        Set<GridCoordinate> foundNeighborSet = new HashSet<>();

        // NeighboringCells is queue used to store and process neighbors of positive cells.
        // Positive cells include themselves in their neighborhood.
        Queue<GridCoordinate> neighboringCells = new LinkedList<>(positives);

        // Add neighboring cells to set and its own neighbors to queue for processing
        while (!neighboringCells.isEmpty()) {
            GridCoordinate neighbor = neighboringCells.poll();

            // Neighbors already in foundNeighborSet OR lie outside distance threshold 'n' should be skipped
            if (foundNeighborSet.contains(neighbor) || neighbor.getDistance() > n) continue;

            // Mark we've processed current cell and it's direct neighbors for processing
            foundNeighborSet.add(neighbor);
            addAdjacentNeighbors(grid, neighbor, neighboringCells);
        }

        return foundNeighborSet.size(); // For now, we only want the count
    }

    /**
     * Helper function that finds all positive numbers in a 2D array of integers
     *
     * @param grid: a 2D array of integers
     * @return a Set of GridCoordinates representing the locations of the positives
     */
    private static Set<GridCoordinate> findPositiveCellsForGrid(int[][] grid) {
        Set<GridCoordinate> positiveCells = new HashSet<>();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] > 0) { // Count element at y,x because its positive
                    GridCoordinate positiveCell = new GridCoordinate.GridCoordinateBuilder()
                            .forGrid(grid)
                            .withCoordinates(y, x)
                            .withDistance(0) // Positive cell is 0 distance away from positive cell
                            .build();
                    positiveCells.add(positiveCell);
                }
            }
        }

        return positiveCells;
    }

    /**
     * Adds all valid adjacent neighbors (up/down/left/right) of the current cell
     * to the queue with incremented distance.
     *
     * @param grid The original grid
     * @param currentCell The cell currently being processed
     * @param neighboringCells The queue used to track cells to visit
     */
    private static void addAdjacentNeighbors(int[][] grid, GridCoordinate currentCell, Queue<GridCoordinate> neighboringCells) {
        GridCoordinate.GridCoordinateBuilder gridCoordinateBuilder = new GridCoordinate.GridCoordinateBuilder()
                .forGrid(grid);

        GridCoordinate up = gridCoordinateBuilder
                .withCoordinates(currentCell.getY()-1, currentCell.getX())
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (up != null) neighboringCells.add(up); // Null means the "above" does not exist so don't add it

        GridCoordinate left = gridCoordinateBuilder
                .withCoordinates(currentCell.getY(), currentCell.getX() - 1)
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (left != null) neighboringCells.add(left);

        GridCoordinate right = gridCoordinateBuilder
                .withCoordinates(currentCell.getY(), currentCell.getX() + 1)
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (right != null) neighboringCells.add(right);

        GridCoordinate down = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() + 1, currentCell.getX())
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (down != null) neighboringCells.add(down);

        GridCoordinate upperLeft = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() - 1, currentCell.getX()-1)
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (upperLeft != null) neighboringCells.add(upperLeft);

        GridCoordinate upperRight = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() - 1, currentCell.getX()+1)
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (upperRight != null) neighboringCells.add(upperRight);

        GridCoordinate lowerRight = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() + 1, currentCell.getX()+1)
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (lowerRight != null) neighboringCells.add(lowerRight);

        GridCoordinate lowerLeft = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() + 1, currentCell.getX()-1)
                .withDistance(currentCell.getDistance() + 1)
                .build();
        if (lowerLeft != null) neighboringCells.add(lowerLeft);
    }
}

/**
 * Represents a coordinate in the 2D grid, along with its distance from a source cell.
 * Equality and hashing are based only on coordinates (not distance). Distance
 * represents the distance from a positive cell. This is used when processing neighbors
 * to ensure we don't count neighboring cells whose distance are greater than the
 * distance threshold 'n' (GridCoordinate.getDistance() > n)
 */
class GridCoordinate {
    private final int y;
    private final int x;
    private final int distance; // Distance away from positive cell

    private GridCoordinate(int y, int x, int distance) {
        this.y = y;
        this.x = x;
        this.distance = distance;
    }

    int getY() {
        return y;
    }

    int getX() {
        return x;
    }

    /**
     * The distance from a positive cell. Used to check we don't
     * count neighboring cells whose distance is greater than
     * the distance threshold 'n'
     *
     * @return an integer representing distance from positive
     */
    int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return y+","+x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y,x);
    }

    /**
     * GridCoordinates are equal if they share the same y and x values
     *
     * @param obj is the object with which to compare to this one
     * @return true if the other obj is a GridCoordinate and it shares the same
     * values for y and x
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridCoordinate otherCoordinate) {
            return this.y == otherCoordinate.y && this.x == otherCoordinate.x;
        }

        return false;
    }

    /**
     * Builder class for creating valid GridCoordinate instances, with bounds checking
     * based on the grid size. If built with coordinates out of bounds for the specified
     * grid, a null coordinate is returned
     */
    static class GridCoordinateBuilder {
        int[][] grid;
        int y;
        int x;
        int distance;

        GridCoordinateBuilder forGrid(int[][] grid) {
            this.grid = grid;
            return this;
        }
        GridCoordinateBuilder withCoordinates(int y, int x) {
            this.y = y;
            this.x = x;
            return this;
        }

        GridCoordinateBuilder withDistance(int distance) {
            this.distance = distance;
            return this;
        }

        /**
         *
         * Instantiate a GridCoordinate using give values
         *
         * @return a GridCoordinate instance for specified grid
         * distance and y and x values. Returns null if
         * coordinates are out of bounds for the grid
         */
        GridCoordinate build() {
            if (y < 0 || y >= grid.length) return null;
            if (x < 0 || x >= grid[0].length) return null;
            if (distance < 0) return null;

            return new GridCoordinate(y, x, distance);
        }
    }
}

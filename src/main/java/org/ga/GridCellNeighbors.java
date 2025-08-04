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


public class GridCellNeighbors {

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
    public static int findNeighborCountOfPositives(int[][] grid, int n) {
        Queue<GridCoordinate> neighboringCells = new LinkedList<>();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] > 0) {
                    GridCoordinate positiveCell = new GridCoordinate.GridCoordinateBuilder()
                            .forGrid(grid)
                            .withCoordinates(y, x)
                            .withDistance(0)
                            .build();
                    neighboringCells.add(positiveCell);
                }
            }
        }

        Set<GridCoordinate> foundNeighborSet = new HashSet<>();

        while (!neighboringCells.isEmpty()) {
            GridCoordinate currentCell = neighboringCells.poll();

            if (foundNeighborSet.contains(currentCell) || currentCell.getDistance() > n) continue;

            foundNeighborSet.add(currentCell);

            addAdjacentNeighbors(grid, currentCell, neighboringCells);
        }

        return foundNeighborSet.size();
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
        if (up != null) neighboringCells.add(up);

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
    }
}

/**
 * Represents a coordinate in the 2D grid, along with its distance from a source cell.
 * Equality and hashing are based only on coordinates (not distance)
 */
class GridCoordinate {
    private final int y;
    private final int x;
    private final int distance;

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

        GridCoordinate build() {
            if (y < 0 || y >= grid.length) return null;
            if (x < 0 || x >= grid[0].length) return null;
            if (distance < 0) return null;

            return new GridCoordinate(y, x, distance);
        }
    }
}

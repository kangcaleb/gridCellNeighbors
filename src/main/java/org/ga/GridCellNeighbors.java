package org.ga;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


abstract class GridCellNeighbors {

    public static void main(String[] args) {

        if (args.length != 3) {
            throw new IllegalArgumentException("Expected exactly 2 Arguments: " +
                    "-m for manhattan distance, -e for euclidean distance and " +
                    "an integer N representing the max distance from any positive cell and " +
                    "a path to a csv file with a 2D grid of signed integers");
        }

        try {
            // Validate Distance Calculation
            String distanceCalc = args[0];
            DistanceNeighborFinder distanceNeighborFinder = DistanceNeighborFinder.createDistanceFinder(distanceCalc);

            // Validate Distance is Positive
            int distanceThreshold = Integer.parseInt(args[1]);
            if (distanceThreshold < 0) {
                throw new IllegalArgumentException("Integer N representing the max distance from any positive cell cannot be negative");
            }

            // Validate CSV
            String gridPath = args[2];
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
                System.out.println(distanceNeighborFinder.findNeighborCountOfPositives(grid, distanceThreshold) + " Neighbors within a manhattan distance of " + distanceThreshold);

            } catch (FileNotFoundException fnfe) {
                throw new IllegalArgumentException(fnfe.getMessage());
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Expected csv with only numbers separated by commas but failed to parse: " + nfe.getMessage());
            } catch (IOException ioe) {
                System.out.println("Error Parsing grid. Make sure your csv has only numbers separated by commas. Make sure each row is on a separated line");
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Expected max distance as an integer but failed to parse: " + nfe.getMessage());
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
}

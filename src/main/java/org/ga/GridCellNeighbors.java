package org.ga;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Goal: Given a 2D Grid and a distance threshold N, find the number of neighboring
 * cells within distance N af any positive integer in the grid.
 */
abstract class GridCellNeighbors {

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

        if (args.length != 3) {
            throw new IllegalArgumentException("Expected exactly 2 Arguments: " +
                    "-m for manhattan distance, -e for euclidean distance and " +
                    "an integer N representing the max distance from any positive cell and " +
                    "a path to a csv file with a 2D grid of signed integers");
        }

        try {
            // Validate Distance Calculation
            String distanceCalc = args[0];
            DistanceNeighborFinder distanceNeighborFinder = DistanceNeighborFinder.createDistanceNeighborFinder(distanceCalc);

            // Validate Distance is Positive
            int distanceThreshold = Integer.parseInt(args[1]);
            if (distanceThreshold < 0) {
                throw new IllegalArgumentException("Integer N representing the max distance from any positive cell cannot be negative");
            }

            // Validate CSV isn't blank and has correct ending
            String gridPath = args[2];
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
                System.out.println(distanceNeighborFinder.findTotalCellCountWithinRange(grid, distanceThreshold) + " Neighbors within a distance of " + distanceThreshold);

            } catch (FileNotFoundException fnfe) {
                throw new IllegalArgumentException(fnfe.getMessage());
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Expected csv with only numbers separated by commas but failed to parse: " + nfe.getMessage());
            } catch (IOException ioe) {
                throw new RuntimeException("Error Parsing grid. Make sure your csv has only numbers separated by commas. Make sure each row is on a separated line");
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

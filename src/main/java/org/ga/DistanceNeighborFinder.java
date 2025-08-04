package org.ga;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class DistanceNeighborFinder {
    public enum Type {
        MANHATTAN("-m"),
        EUCLIDEAN("-e");
        final String flag;

        Type(String flag) {
            this.flag = flag;
        }
    }

    /**
     * Computes the number of unique cells in the grid that are within a given
     * distance of any positive value in the grid. Positive cells include themselves in their neighborhood.
     * Uses a Breadth first approach to explore valid neighbors up to the given distance.
     *
     * @param grid 2D grid of signed integers
     * @param n Maximum distance from any positive cell
     * @return Count of unique cells within distance n of any positive cell
     */
    public int findTotalCellCountWithinRange(int[][] grid, int n) {
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
            if (foundNeighborSet.contains(neighbor) || getDistance(neighbor) > n) continue;

            // Mark we've processed current cell and it's direct neighbors for processing
            foundNeighborSet.add(neighbor);
            addAdjacentNeighbors(grid, neighbor, neighboringCells);
        }

        return foundNeighborSet.size();
    }

    /**
     * Helper function that finds all positive numbers in a 2D array of integers
     *
     * @param grid: a 2D array of integers
     * @return a Set of GridCoordinates representing the locations of the positives
     */
    private static Set<GridCoordinate> findPositiveCellsForGrid(int[][] grid) {
        Set<GridCoordinate> positives = new HashSet<>();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] > 0) {
                    GridCoordinate positiveCell = new GridCoordinate.GridCoordinateBuilder()
                            .forGrid(grid)
                            .withCoordinates(y, x)
                            .withDeltaFromPositive(0, 0)
                            .build();
                    positives.add(positiveCell);
                }
            }
        }

        return positives;
    }

    /**
     * Adds all valid adjacent neighbors (up/down/left/right) of the current cell
     * to the queue with incremented distance.
     *
     * @param grid The original grid
     * @param currentCell The cell currently being processed
     * @param neighboringCells The queue used to track cells to visit
     */
    private void addAdjacentNeighbors(int[][] grid, GridCoordinate currentCell, Queue<GridCoordinate> neighboringCells) {
        GridCoordinate.GridCoordinateBuilder gridCoordinateBuilder = new GridCoordinate.GridCoordinateBuilder()
                .forGrid(grid);
        GridCoordinate up = gridCoordinateBuilder
                .withCoordinates(currentCell.getY()-1, currentCell.getX())
                .withDeltaFromPositive(currentCell.getDeltaY()-1, currentCell.getDeltaX())
                .build();
        if (up != null) neighboringCells.add(up); // Null means the "above" does not exist so don't add it

        GridCoordinate left = gridCoordinateBuilder
                .withCoordinates(currentCell.getY(), currentCell.getX() - 1)
                .withDeltaFromPositive(currentCell.getDeltaY(), currentCell.getDeltaX() - 1)
                .build();
        if (left != null) neighboringCells.add(left);

        GridCoordinate right = gridCoordinateBuilder
                .withCoordinates(currentCell.getY(), currentCell.getX() + 1)
                .withDeltaFromPositive(currentCell.getDeltaY(), currentCell.getDeltaX() + 1)
                .build();
        if (right != null) neighboringCells.add(right);

        GridCoordinate down = gridCoordinateBuilder
                .withCoordinates(currentCell.getY() + 1, currentCell.getX())
                .withDeltaFromPositive(currentCell.getDeltaY() + 1, currentCell.getDeltaX())
                .build();
        if (down != null) neighboringCells.add(down);
    }

    /**
     * Gets distance from given GridCoordinate from positive cell. Used to determine
     * if that cell is within the distance threshold 'n' of positive cell. Distance
     * can be calculated in multiple ways which is why it's abstract.
     *
     * @param coordinate GridCoordinate of which to get distance from positive
     * @return double representing the distance value.
     */
    public abstract double getDistance(GridCoordinate coordinate);

    /**
     * Factory method to create proper DistanceNeighborFinder
     *
     * @param type of DistanceNeighborFinder to create
     * @return EuclideanDistanceNeighborhoodFinder or DistanceNeighborFinder
     */
    public static DistanceNeighborFinder createDistanceNeighborFinder(String type) {
        if (type.equals(Type.MANHATTAN.flag)) {
            return new ManhattanDistanceNeighborFinder();
        } else if (type.equals(Type.EUCLIDEAN.flag)) {
            return new EuclideanDistanceNeighborhoodFinder();
        }

        throw new IllegalArgumentException("Expected -m for manhattan distance or -e for euclidean distance");
    }
}

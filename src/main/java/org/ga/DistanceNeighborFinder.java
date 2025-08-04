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
    public int findNeighborCountOfPositives(int[][] grid, int n) {
        Queue<GridCoordinate> neighboringCells = new LinkedList<>();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] > 0) {
                    GridCoordinate positiveCell = new GridCoordinate.GridCoordinateBuilder()
                            .forGrid(grid)
                            .withCoordinates(y, x)
                            .withDeltaFromPositive(0, 0)
                            .build();
                    neighboringCells.add(positiveCell);
                }
            }
        }

        Set<GridCoordinate> foundNeighborSet = new HashSet<>();

        while (!neighboringCells.isEmpty()) {
            GridCoordinate currentCell = neighboringCells.poll();

            if (foundNeighborSet.contains(currentCell) || getDistance(currentCell) > n) continue;

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
    private void addAdjacentNeighbors(int[][] grid, GridCoordinate currentCell, Queue<GridCoordinate> neighboringCells) {
        GridCoordinate.GridCoordinateBuilder gridCoordinateBuilder = new GridCoordinate.GridCoordinateBuilder()
                .forGrid(grid);
        GridCoordinate up = gridCoordinateBuilder
                .withCoordinates(currentCell.getY()-1, currentCell.getX())
                .withDeltaFromPositive(currentCell.getDeltaY()-1, currentCell.getDeltaX())
                .build();
        if (up != null) neighboringCells.add(up);

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

    public abstract double getDistance(GridCoordinate coordinate);

    public static DistanceNeighborFinder createDistanceFinder(String type) {
        if (type.equals(Type.MANHATTAN.flag)) {
            return new ManhattanDistanceNeighborFinder();
        } else if (type.equals(Type.EUCLIDEAN.flag)) {
            return new EuclideanDistanceNeighborhoodFinder();
        }

        throw new IllegalArgumentException("Expected -m for manhattan distance or -e for euclidean distance");
    }
}

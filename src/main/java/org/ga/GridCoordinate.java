package org.ga;

import java.util.Objects;

/**
 * Represents a coordinate in the 2D grid, along with its distance from a source cell.
 * Equality and hashing are based only on coordinates (not distance)
 */
public class GridCoordinate {
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

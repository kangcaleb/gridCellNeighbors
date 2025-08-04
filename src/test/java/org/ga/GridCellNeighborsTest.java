package org.ga;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridCellNeighborsTest {

    @Test
    public void testDistance0Returns1() {
        int[][] grid = new int[5][5];
        grid[2][2] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 0);
        assertEquals(1, result);
    }

    @Test
    public void testDistanceCoversEntireGrid() {
        int[][] grid = new int[5][5];
        grid[2][2] = 10;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 10);
        assertEquals(25, result);
    }

    @Test
    public void testDistanceThresholdZero() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[6][5] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 0);
        assertEquals(2, result);
    }

    @Test
    public void testTallArray() {
        int[][] grid = new int[11][1];
        grid[5][0] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 3);
        assertEquals(7, result);
    }

    @Test
    public void testLongArray() {
        int[][] grid = new int[1][11];
        grid[0][5] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 3);
        assertEquals(7, result);
    }

    @Test
    public void testNoPositivesReturn0() {
        int[][] grid = new int[5][5];
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 3);
        assertEquals(0, result);
    }

    @Test
    public void testAllPositives_ReturnsCountOfEveryCell() {
        int[][] grid = new int[5][5];
        for (int y = 0; y<grid.length; y++) {
            for(int x = 0; x<grid[0].length; x++) {
                grid[y][x] = 1;
            }
        }

        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 1);
        assertEquals(25, result);
    }

    @Test
    public void testAllPositivesWithLargeDistance_ReturnsCountOfEveryCell() {
        int[][] grid = new int[5][5];
        for (int y = 0; y<grid.length; y++) {
            for(int x = 0; x<grid[0].length; x++) {
                grid[y][x] = 1;
            }
        }

        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 10);
        assertEquals(25, result);
    }

    @Test
    public void testMultiplePositivesDistanceZero() {
        int[][] grid = new int[5][5];
        grid[0][0] = 1;
        grid[4][4] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 0);
        assertEquals(2, result);
    }

    @Test
    public void testGridWithOnlyNegativesReturnsZero() {
        int[][] grid = {
                {-1, -2},
                {-3, -4}
        };
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 1);
        assertEquals(0, result);
    }

    @Test
    public void exampleOnePositiveCellFullyContained_() {
        int[][] grid = new int[11][11];
        grid[5][5] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 3);
        assertEquals(49, result);
    }

    @Test
    public void testCornerCellDistance1_() {
        int[][] grid = new int[3][3];
        grid[0][0] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 1);
        assertEquals(4, result); // now includes diagonal (0,1), (1,0), (1,1)
    }

    @Test
    public void testDistance1InSmallGrid_() {
        int[][] grid = new int[3][3];
        grid[1][1] = 1;
        int result = GridCellNeighbors.findTotalCellCountWithinRange(grid, 1);
        assertEquals(9, result); // all 8 adjacent cells + self
    }

}

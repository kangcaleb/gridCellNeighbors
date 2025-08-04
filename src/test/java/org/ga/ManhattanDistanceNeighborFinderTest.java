package org.ga;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * Default implementation of GridCellNeighbors uses
 * "calculateManhattanDistance" to measure distance
 */
public class ManhattanDistanceNeighborFinderTest {
    ManhattanDistanceNeighborFinder distanceNeighborFinder = Mockito.spy(ManhattanDistanceNeighborFinder.class);

    @Test
    public void testDistance1InSmallGrid() {
        int[][] grid = new int[3][3];
        grid[1][1] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 1);
        assertEquals(5, result);
    }

    @Test
    public void testDistance0Returns1() {
        int[][] grid = new int[5][5];
        grid[2][2] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 0);
        assertEquals(1, result);
    }

    @Test
    public void testCornerCellDistance1() {
        int[][] grid = new int[3][3];
        grid[0][0] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 1);
        assertEquals(3, result);
    }

    @Test
    public void testDistanceCoversEntireGrid() {
        int[][] grid = new int[5][5];
        grid[2][2] = 10;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 10);
        assertEquals(25, result);
    }

    @Test
    public void testDistance3In6x6Grid() {
        int[][] grid = new int[6][6];
        grid[2][3] = 11;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
        assertEquals(23, result);
    }

    @Test
    public void testDistance11In22x22Grid() {
        int[][] grid = new int[22][22];
        grid[10][11] = 11;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 11);
        assertEquals(263, result);
    }

    @Test
    public void exampleOnePositiveCellFullyContained() {
        int[][] grid = new int[11][11];
        grid[5][5] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
        assertEquals(25, result);
    }

    @Test
    public void exampleTwoPositiveCellNearEdge() {
        int[][] grid = new int[11][11];
        grid[7][1] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
        assertEquals(21, result);
    }

    @Test
    public void exampleThreeDisjointNeighborhoods() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[3][7] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 2);
        assertEquals(26, result);
    }

    @Test
    public void exampleFourOverLappingNeighborhoods() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[6][5] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 2);
        assertEquals(22, result);
    }

    @Test
    public void testDistanceThresholdZero() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[6][5] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 0);
        assertEquals(2, result);
    }

    @Test
    public void testTallArray() {
        int[][] grid = new int[11][1];
        grid[5][0] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
        assertEquals(7, result);
    }

    @Test
    public void testLongArray() {
        int[][] grid = new int[1][11];
        grid[0][5] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
        assertEquals(7, result);
    }

    @Test
    public void testAdjacentPositives() {
        int[][] grid = new int[5][5];
        grid[2][2] = 1;
        grid[2][3] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 2);
        assertEquals(17, result);
    }

    @Test
    public void testOverLappingIntervalsThatRunOff() {
        int[][] grid = new int[5][5];
        grid[0][1] = 1;
        grid[0][3] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 2);
        assertEquals(12, result);
    }

    @Test
    public void testTwoCornersOverlap() {
        int[][] grid = new int[5][5];
        grid[0][0] = 1;
        grid[0][4] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
        assertEquals(16, result);
    }

    @Test
    public void testNoPositivesReturn0() {
        int[][] grid = new int[5][5];
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 3);
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

        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 1);
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

        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 10);
        assertEquals(25, result);
    }

    @Test
    public void testMultiplePositivesDistanceZero() {
        int[][] grid = new int[5][5];
        grid[0][0] = 1;
        grid[4][4] = 1;
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 0);
        assertEquals(2, result);
    }

    @Test
    public void testGridWithOnlyNegativesReturnsZero() {
        int[][] grid = {
                {-1, -2},
                {-3, -4}
        };
        int result = distanceNeighborFinder.findTotalCellCountWithinRange(grid, 1);
        assertEquals(0, result);
    }
}

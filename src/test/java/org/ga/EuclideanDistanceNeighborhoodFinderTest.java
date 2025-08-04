package org.ga;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * When testing usage of Euclidean distance, make sure to use
 * "calculateEuclideanDistance" method is "getDistance" method
 */
public class EuclideanDistanceNeighborhoodFinderTest {
    EuclideanDistanceNeighborhoodFinder distanceNeighborFinder = Mockito.spy(EuclideanDistanceNeighborhoodFinder.class);

    @Test
    public void testEuclideanDistance5_coversWholeGrid() {
        int[][] grid = new int[5][4];
        grid[4][0] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 5);
        assertEquals(20, result);
    }

    @Test
    public void testEuclideanDistance5() {
        int[][] grid = new int[5][5];
        grid[4][0] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 5);
        assertEquals(24, result);
    }

    @Test
    public void testEuclideanDistance1InSmallGrid() {
        int[][] grid = new int[3][3];
        grid[1][1] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 1);
        assertEquals(5, result); // center + 4 adjacent
    }

    @Test
    public void testEuclideanDistance0Returns1() {
        int[][] grid = new int[5][5];
        grid[2][2] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 0);
        assertEquals(1, result);
    }

    @Test
    public void testEuclideanCornerCellDistance1() {
        int[][] grid = new int[3][3];
        grid[0][0] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 1);
        assertEquals(3, result); // (0,0), (0,1), (1,0)
    }

    @Test
    public void testEuclideanDistanceCoversEntireGrid() {
        int[][] grid = new int[5][5];
        grid[2][2] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 10);
        assertEquals(25, result);
    }

    @Test
    public void testEuclideanDistance3In6x6Grid() {
        int[][] grid = new int[6][6];
        grid[2][3] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(27, result); // Circle radius 3 on grid
    }

    @Test
    public void testEuclideanDistance11In22x22Grid() {
        int[][] grid = new int[22][22];
        grid[10][11] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 11);
        assertEquals(375, result);
    }

    @Test
    public void testEuclideanExampleOnePositiveCellFullyContained() {
        int[][] grid = new int[11][11];
        grid[5][5] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(29, result);
    }

    @Test
    public void testEuclideanExampleTwoPositiveCellNearEdge() {
        int[][] grid = new int[11][11];
        grid[7][1] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(23, result);
    }

    @Test
    public void testEuclideanExampleThreeDisjointNeighborhoods() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[3][7] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 2);
        assertEquals(26, result);
    }

    @Test
    public void testEuclideanExampleFourOverlappingNeighborhoods() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[6][5] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 2);
        assertEquals(22, result);
    }

    @Test
    public void testEuclideanDistanceThresholdZero() {
        int[][] grid = new int[11][11];
        grid[7][3] = 1;
        grid[6][5] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 0);
        assertEquals(2, result);
    }

    @Test
    public void testEuclideanTallArray() {
        int[][] grid = new int[11][1];
        grid[5][0] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(7, result);
    }

    @Test
    public void testEuclideanLongArray() {
        int[][] grid = new int[1][11];
        grid[0][5] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(7, result);
    }

    @Test
    public void testEuclideanAdjacentPositives() {
        int[][] grid = new int[5][5];
        grid[2][2] = 1;
        grid[2][3] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 2);
        assertEquals(17, result);
    }

    @Test
    public void testEuclideanOverlappingIntervalsThatRunOff() {
        int[][] grid = new int[5][5];
        grid[0][1] = 1;
        grid[0][3] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 2);
        assertEquals(12, result);
    }

    @Test
    public void testEuclideanTwoCornersOverlap() {
        int[][] grid = new int[5][5];
        grid[0][0] = 1;
        grid[0][4] = 1;
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(17, result);
    }

    @Test
    public void testEuclideanNoPositivesReturn0() {
        int[][] grid = new int[5][5];
        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 3);
        assertEquals(0, result);
    }

    @Test
    public void testEuclideanAllPositives_ReturnsCountOfEveryCell() {
        int[][] grid = new int[5][5];
        for (int y = 0; y<grid.length; y++) {
            for(int x = 0; x<grid[0].length; x++) {
                grid[y][x] = 1;
            }
        }

        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 1);
        assertEquals(25, result);
    }

    @Test
    public void testEuclideanAllPositivesWithLargeDistance_ReturnsCountOfEveryCell() {
        int[][] grid = new int[5][5];
        for (int y = 0; y<grid.length; y++) {
            for(int x = 0; x<grid[0].length; x++) {
                grid[y][x] = 1;
            }
        }

        int result = distanceNeighborFinder.findNeighborCountOfPositives(grid, 10);
        assertEquals(25, result);
    }
}

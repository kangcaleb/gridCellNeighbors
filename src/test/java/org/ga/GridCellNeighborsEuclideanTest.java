package org.ga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * When testing usage of Euclidean distance, make sure to use
 * "calculateEuclideanDistance" method is "getDistance" method
 */
public class GridCellNeighborsEuclideanTest {

    @Test
    public void testEuclideanDistance5_coversWholeGrid() {
        int[][] grid = new int[5][4];
        grid[4][0] = 1;
        int result = GridCellNeighbors.findNeighborCountOfPositives(grid, 5);
        assertEquals(20, result);
    }

    @Test
    public void testEuclideanDistance5() {
        int[][] grid = new int[5][5];
        grid[4][0] = 1;
        int result = GridCellNeighbors.findNeighborCountOfPositives(grid, 5);
        assertEquals(24, result);
    }
}

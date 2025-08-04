package org.ga;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GridCellNeighborsMainTest {

    static Path tempCsv;
    private static final PrintStream standardOut = System.out;
    private static final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    static void setupCsv() throws IOException {
        // Create a simple valid CSV grid
        tempCsv = Files.createTempFile("test-grid", ".csv");
        Files.writeString(tempCsv, "1,0,0\n0,1,0\n0,0,1");
    }

    @BeforeEach
    public void setUp() {
        outputStreamCaptor.reset();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut); // Restore original System.out
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(tempCsv);
    }

    @Test
    void testMain_validInput_printsExpectedOutput()  {
        int mockDistance = 2;

        try (MockedStatic<GridCellNeighbors> mocked = mockStatic(GridCellNeighbors.class)) {
            mocked.when(() -> GridCellNeighbors.findNeighborCountOfPositives(any(int[][].class), eq(mockDistance)))
                    .thenReturn(7);
            mocked.when(() -> GridCellNeighbors.main(any())).thenCallRealMethod();

            GridCellNeighbors.main(new String[]{String.valueOf(mockDistance), tempCsv.toString()});

            assertTrue(outputStreamCaptor.toString().contains("Grid Successfully Parsed"));
            assertTrue(outputStreamCaptor.toString().contains("7 Neighbors within a manhattan distance of 2"));
        }
    }

    @Test
    void testMain_invalidDistance_throwsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{"-1", tempCsv.toString()})
        );
        assertTrue(e.getMessage().contains("cannot be negative"));
    }

    @Test
    void testMain_invalidCsvPath_throwsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{"2", "nonexistent/path.csv"})
        );
        assertTrue(e.getMessage().contains("nonexistent/path.csv"));
    }

    @Test
    void testMain_invalidDistanceFormat_throwsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{"abc", tempCsv.toString()})
        );
        assertTrue(e.getMessage().contains("Expected max Manhattan distance as an integer"));
    }

    @Test
    void testMain_missingArguments_throwsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{})
        );
        assertTrue(e.getMessage().contains("Expected exactly 2 Arguments"));
    }

    @Test
    void testMain_invalidCsvRowsNotEqual_throwsException() throws IOException {
        Files.writeString(tempCsv, "1,0,0\n0,1,0\n0,0");

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{"2", tempCsv.toString()})
        );
        assertTrue(e.getMessage().contains("All rows in grid need to have the same length"));
    }

    @Test
    void testMain_invalidCsvRowsNotExists_throwsException() throws IOException {
        Files.writeString(tempCsv, "");

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{"2", tempCsv.toString()})
        );
        assertTrue(e.getMessage().contains("Expected a grid in csv but found none"));
    }

    @Test
    void testMain_validExtraLine_printsExpectedOutput() throws IOException {
        Files.writeString(tempCsv, "1,0,0\n0,1,0\n0,0,1\n");

        int mockDistance = 2;

        try (MockedStatic<GridCellNeighbors> mocked = mockStatic(GridCellNeighbors.class)) {
            mocked.when(() -> GridCellNeighbors.findNeighborCountOfPositives(any(int[][].class), eq(mockDistance)))
                    .thenReturn(7);
            mocked.when(() -> GridCellNeighbors.main(any())).thenCallRealMethod();

            GridCellNeighbors.main(new String[]{String.valueOf(mockDistance), tempCsv.toString()});

            assertTrue(outputStreamCaptor.toString().contains("Grid Successfully Parsed"));
            assertTrue(outputStreamCaptor.toString().contains("7 Neighbors within a manhattan distance of 2"));
        }
    }

    @Test
    void testMain_validWhitespace_printsExpectedOutput() throws IOException {
        Files.writeString(tempCsv, "1 ,0,0 \n0,1  ,0\n0     ,0,1\n");

        int mockDistance = 2;

        try (MockedStatic<GridCellNeighbors> mocked = mockStatic(GridCellNeighbors.class)) {
            mocked.when(() -> GridCellNeighbors.findNeighborCountOfPositives(any(int[][].class), eq(mockDistance)))
                    .thenReturn(7);
            mocked.when(() -> GridCellNeighbors.main(any())).thenCallRealMethod();

            GridCellNeighbors.main(new String[]{String.valueOf(mockDistance), tempCsv.toString()});

            assertTrue(outputStreamCaptor.toString().contains("Grid Successfully Parsed"));
            assertTrue(outputStreamCaptor.toString().contains("7 Neighbors within a manhattan distance of 2"));
        }
    }

    @Test
    void testMain_emptyCsv_printsExpectedOutput() throws IOException {
        Files.writeString(tempCsv, "");
        int mockDistance = 2;

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                GridCellNeighbors.main(new String[]{String.valueOf(mockDistance), tempCsv.toString()})
        );

        assertTrue(e.getMessage().contains("Expected a grid in csv but found none"));
    }
}

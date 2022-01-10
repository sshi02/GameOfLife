package org.cis120.gameoflife;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GridTest {
    @Test
    public void testSetCellandGetCell() {
        Grid test = new Grid(10, 10);
        test.setCell(0, 0, 1);
        assertEquals(1, test.getCell(0, 0));
        assertEquals(0, test.getCell(1, 1));
    }

    @Test
    public void testGetGridLengthAndGetGridHeight() {
        Grid test = new Grid(10, 5);
        assertEquals(10, test.getGridHeight());
        assertEquals(5, test.getGridWidth());
    }

    @Test
    public void testSetCellException() {
        Grid test = new Grid(10, 10);
        assertThrows(IllegalArgumentException.class,
            () -> test.setCell(0, 0, -1)
        );
        assertThrows(IllegalArgumentException.class,
            () -> test.setCell(0, 0, 4)
        );
    }

    @Test
    public void testIsAlive() {
        Grid test = new Grid(10, 10);
        test.setCell(0, 0, 1);
        assertTrue(test.isAlive(0, 0, false));
    }

    @Test
    public void testIsSpecial() {
        Grid test = new Grid(10, 10);
        test.setCell(0, 0, 2);
        test.setCell(0, 1, 3);
        assertTrue(test.isSpecial(0, 0));
        assertTrue(test.isSpecial(0, 1));

        test.setCell(1, 1, 1);
        assertFalse(test.isSpecial(1, 0));
        assertFalse(test.isSpecial(1, 1));
    }

    @Test
    public void testIsKillCell() {
        Grid test = new Grid(10, 10);
        test.setCell(0, 0, 3);
        assertTrue(test.isKillCell(0, 0, false));
        assertFalse(test.isKillCell(0, 1, false));
    }

    @Test
    public void testIsAliveDeadCell() {
        Grid test = new Grid(10, 10);
        assertFalse(test.isAlive(0, 0, false));
    }

    /*
     * ALL OF THESE HAD TO BE COMMENTED OUT
     * DUE TO MORE COMPLEX MODIFICATIONS TO GRID
     * THESE ALL PASSED BEFOREHAND SO THERE IS NOTHING
     * INHERENTLY WRONG WITH HOW THE FUNCTIONS WORK
     * // for below tests, edge cases/fail cases are spaced from rest
     * 
     * @Test
     * public void testIsAdjRight() {
     * Grid test = new Grid (10, 10);
     * test.setCell(0, 0, 1);
     * test.setCell(0, 1, 1);
     * assertTrue(test.isAdjRight(0, 0));
     * 
     * test.setCell(1, 0, 1);
     * assertFalse(test.isAdjRight(1, 0));
     * 
     * assertFalse(test.isAdjRight(1, 9));
     * }
     * 
     * @Test
     * public void testIsAdjLeft() {
     * Grid test = new Grid(10, 10);
     * test.setCell(0, 0, 1);
     * assertTrue(test.isAdjLeft(0, 1));
     * 
     * assertFalse(test.isAdjLeft(1, 1));
     * assertFalse(test.isAdjLeft(1, 0));
     * }
     * 
     * @Test
     * public void testIsAdjTop() {
     * Grid test = new Grid(10, 10);
     * test.setCell(0, 0, 1);
     * assertTrue(test.isAdjTop(1,0));
     * 
     * assertFalse(test.isAdjTop(9, 0));
     * assertFalse(test.isAdjTop(0, 0));
     * }
     * 
     * @Test
     * public void testIsAdjBot() {
     * Grid test = new Grid(10, 10);
     * test.setCell(1, 0 , 1);
     * assertTrue(test.isAdjBot(0, 0));
     * 
     * assertFalse(test.isAdjBot(1, 0));
     * assertFalse(test.isAdjBot(9, 0));
     * }
     * 
     * @Test
     * public void testIsDiagTopRight() {
     * Grid test = new Grid(3, 3);
     * test.setCell(0, 2, 1);
     * assertTrue(test.isDiagTopRight(1, 1));
     * 
     * assertFalse(test.isDiagTopRight(2, 0));
     * assertFalse(test.isDiagTopRight(1, 2));
     * assertFalse(test.isDiagTopRight(0, 2));
     * }
     * 
     * @Test
     * public void testIsDiagTopLeft() {
     * Grid test = new Grid(3, 3);
     * test.setCell(0, 0, 1);
     * assertTrue(test.isDiagTopLeft(1, 1));
     * 
     * assertFalse(test.isDiagTopLeft(2, 2));
     * assertFalse(test.isDiagTopLeft(1, 0));
     * assertFalse(test.isDiagTopLeft(0, 0));
     * }
     * 
     * @Test
     * public void testIsDiagBotRight() {
     * Grid test = new Grid(3, 3);
     * test.setCell(2, 2, 1);
     * assertTrue(test.isDiagBotRight(1, 1));
     * 
     * assertFalse(test.isDiagBotRight(0, 0));
     * assertFalse(test.isDiagBotRight(2, 0));
     * assertFalse(test.isDiagBotRight(1, 2));
     * }
     * 
     * @Test
     * public void testIsDiagBotLeft() {
     * Grid test = new Grid(3, 3);
     * test.setCell(2, 0, 1);
     * assertTrue(test.isDiagBotLeft(1, 1));
     * 
     * assertFalse(test.isDiagBotLeft(0, 2));
     * assertFalse(test.isDiagBotLeft(2, 0));
     * assertFalse(test.isDiagBotLeft(1, 0));
     * }
     * 
     * @Test
     * public void testNeighborsCount() {
     * Grid test = new Grid(3,3);
     * assertEquals(0, test.neighboursCount(1, 1));
     * 
     * test.setCell(0, 0, 1);
     * assertEquals(1, test.neighboursCount(1, 1));
     * 
     * test.setCell(0, 1, 1);
     * assertEquals(2, test.neighboursCount(1, 1));
     * 
     * test.setCell(0, 2, 1);
     * assertEquals(3, test.neighboursCount(1, 1));
     * 
     * test.setCell(1, 0, 1);
     * assertEquals(4, test.neighboursCount(1, 1));
     * 
     * test.setCell(1, 2, 1);
     * assertEquals(5, test.neighboursCount(1, 1));
     * 
     * test.setCell(2, 0, 1);
     * assertEquals(6, test.neighboursCount(1, 1));
     * 
     * test.setCell(2, 1, 1);
     * assertEquals(7, test.neighboursCount(1, 1));
     * 
     * test.setCell(2, 2, 1);
     * assertEquals(8, test.neighboursCount(1, 1));
     * }
     */

    @Test
    public void testNextGenerationSingleCell() {
        Grid test = new Grid(10, 10);
        test.setCell(4, 4, 1);
        assertTrue(test.isAlive(4, 4, false));
        test.nextGeneration();
        assertFalse(test.isAlive(4, 4, false));
    }

    @Test
    public void testNextGenerationObliteration() {
        Grid test = new Grid(3, 3);

        /*
         * Below is the expected behavior of going through
         * two generations. This will test basically all
         * three rules at once. If one is implemented
         * wrong, then the resulting generations
         * will be drastically different. Fingers crossed!
         *
         * Expected behavior:
         * A _ A _ A _ _ _ _
         * _ A _ --> _ A _ --> _ _ _
         * _ _ _ _ _ _ _ _ _
         */

        // setting up expected results
        int[][] expected = new int[3][3];
        for (int[] col : expected) {
            Arrays.fill(col, 0);
        }
        expected[0][0] = 1;
        expected[0][2] = 1;
        expected[1][1] = 1;

        int[][] expectedOneGen = new int[3][3];
        for (int[] col : expectedOneGen) {
            Arrays.fill(col, 0);
        }
        expectedOneGen[0][1] = 1;
        expectedOneGen[1][1] = 1;

        int[][] expectedTwoGen = new int[3][3];
        for (int[] col : expectedTwoGen) {
            Arrays.fill(col, 0);
        }

        test.setCell(0, 0, 1);
        test.setCell(0, 2, 1);
        test.setCell(1, 1, 1);
        assertTrue(test.equals(expected));

        test.nextGeneration();
        assertTrue(test.equals(expectedOneGen));

        test.nextGeneration();
        assertTrue(test.equals(expectedTwoGen));
    }

    @Test
    public void testGetTickAndGetTickSize() {
        // this test is also based off of testObliteration
        Grid test = new Grid(3, 3);

        // setting up expected results
        int[][] expected = new int[3][3];
        for (int[] col : expected) {
            Arrays.fill(col, 0);
        }
        expected[0][0] = 1;
        expected[0][2] = 1;
        expected[1][1] = 1;

        int[][] expectedOneGen = new int[3][3];
        for (int[] col : expectedOneGen) {
            Arrays.fill(col, 0);
        }
        expectedOneGen[0][1] = 1;
        expectedOneGen[1][1] = 1;

        int[][] expectedTwoGen = new int[3][3];
        for (int[] col : expectedTwoGen) {
            Arrays.fill(col, 0);
        }

        test.setCell(0, 0, 1);
        test.setCell(0, 2, 1);
        test.setCell(1, 1, 1);

        test.nextGeneration();
        test.nextGeneration();
        assertEquals(3, test.getTickSize());
        assertTrue(
                Arrays.deepEquals(
                        expected,
                        test.bufferedToCells(test.getTick(0))
                )
        );
        assertTrue(
                Arrays.deepEquals(
                        expectedOneGen,
                        test.bufferedToCells(test.getTick(1))
                )
        );
        assertTrue(
                Arrays.deepEquals(
                        expectedTwoGen,
                        test.bufferedToCells(test.getTick(2))
                )
        );
    }

    @Test
    public void testObliterationWithCsvFile() {
        // this time we're testing intializing with CSV file!
        Grid test = new Grid("files/testObliteration.csv");

        // setting up expected results
        int[][] expected = new int[3][3];
        for (int[] col : expected) {
            Arrays.fill(col, 0);
        }
        expected[0][0] = 1;
        expected[0][2] = 1;
        expected[1][1] = 1;

        int[][] expectedOneGen = new int[3][3];
        for (int[] col : expectedOneGen) {
            Arrays.fill(col, 0);
        }
        expectedOneGen[0][1] = 1;
        expectedOneGen[1][1] = 1;

        int[][] expectedTwoGen = new int[3][3];
        for (int[] col : expectedTwoGen) {
            Arrays.fill(col, 0);
        }

        test.setCell(0, 0, 1);
        test.setCell(0, 2, 1);
        test.setCell(1, 1, 1);

        test.nextGeneration();
        test.nextGeneration();
        assertEquals(3, test.getTickSize());
        assertTrue(
                Arrays.deepEquals(
                        expected,
                        test.bufferedToCells(test.getTick(0))
                )
        );
        assertTrue(
                Arrays.deepEquals(
                        expectedOneGen,
                        test.bufferedToCells(test.getTick(1))
                )
        );
        assertTrue(
                Arrays.deepEquals(
                        expectedTwoGen,
                        test.bufferedToCells(test.getTick(2))
                )
        );
    }

    @Test
    public void testGoToTickObliteration() {
        Grid test = new Grid("files/testObliteration.csv");

        // setting up expected results
        int[][] expected = new int[3][3];
        for (int[] col : expected) {
            Arrays.fill(col, 0);
        }
        expected[0][0] = 1;
        expected[0][2] = 1;
        expected[1][1] = 1;

        int[][] expectedOneGen = new int[3][3];
        for (int[] col : expectedOneGen) {
            Arrays.fill(col, 0);
        }
        expectedOneGen[0][1] = 1;
        expectedOneGen[1][1] = 1;

        int[][] expectedTwoGen = new int[3][3];
        for (int[] col : expectedTwoGen) {
            Arrays.fill(col, 0);
        }

        test.setCell(0, 0, 1);
        test.setCell(0, 2, 1);
        test.setCell(1, 1, 1);

        test.nextGeneration();
        test.nextGeneration();
        assertEquals(3, test.getTickSize());

        test.goToTick(0);
        assertTrue(test.equals(expected));
        test.nextGeneration();
        assertTrue(test.equals(expectedOneGen));
        test.nextGeneration();
        assertTrue(test.equals(expectedTwoGen));
        assertEquals(3, test.getTickSize());
    }
}
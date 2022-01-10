package org.cis120.gameoflife;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;

public class GridWriterTest {
    @Test
    public void testFileExists() {
        assertTrue(
                GridWriter
                        .fileExists("testObliteration.csv")
        );
    }

    @Test
    public void testFileExistsNot() {
        assertFalse(
                GridWriter
                        .fileExists("PASSING=CAP")
        );
    }

    @Test
    public void testCreateGridFileAndWriteToFile() {
        if (GridWriter.fileExists("test.csv")) {
            File toDelete = new File("files/test.csv");
            toDelete.delete();
        }
        assertFalse(GridWriter.fileExists("test.csv"));
        GridWriter.createGridFile("test.csv", 3, 3);
        File testFile = new File("files/test.csv");

        // expected
        int[][] expected = new int[3][3];
        for (int[] col : expected) {
            Arrays.fill(col, 0);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(testFile));
            int[][] testCells = GridParser.csvDataToCells(br);
            assertArrayEquals(expected, testCells);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

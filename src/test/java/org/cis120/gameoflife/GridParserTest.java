package org.cis120.gameoflife;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;

public class GridParserTest {

    @Test
    public void testParseLine() {
        String str = "0,1,0";
        int[] expected = new int[] { 0, 1, 0 };
        assertArrayEquals(expected, GridParser.parseLine(str));
    }

    @Test
    public void testCsvDataToCells() {
        String str = "0,1,0\n" + "1,0,1\n" + "0,0,0";
        StringReader sr = new StringReader(str);
        BufferedReader br = new BufferedReader(sr);

        int[][] expected = new int[3][3];
        for (int[] col : expected) {
            Arrays.fill(col, 0);
        }
        expected[0][1] = 1;
        expected[1][0] = 1;
        expected[1][2] = 1;

        assertArrayEquals(expected, GridParser.csvDataToCells(br));
    }
}

package org.cis120.gameoflife;

import java.util.*;
import java.io.*;

/**
 * GridParser.java takes in a buffered reader that contains
 * a grid layout for a Game of Life seed and iterates through
 * the reader, returning a file as a int[][].
 * How a file should be formatted:
 * .CSV file, integers ranging from 0-3 should represent cells.
 * The only things that will be checked is if there are any
 * staggered rows in the returned[][] or invalid cell values.
 * In those cases, it will throw an exceptions.
 */
public class GridParser {

    /**
     * Takes a str representation of a line from
     * a supposed CSV of a level and returns the int[] rep.
     * 
     * @param str
     * @return int[] rep of CSV line.
     */
    public static int[] parseLine(String str) {
        String[] line = str.split(",");
        int[] parsedLine = new int[line.length];
        for (int i = 0; i < line.length; i++) {
            parsedLine[i] = Integer.parseInt(line[i]);
        }
        return parsedLine;
    }

    /**
     * Transposes a given 2D int array. Will likely throw a hissy fit
     * if given staggered 2D arrays.
     * Doesn't modify original array but returns a new one.
     *
     * Wait... never mind i don't need this because of how i
     * set up the 2D arrays in Grid.
     *
     * I guess this is for admiration purposes.
     * Admire how memory inefficient it is. Now.
     * 
     * @param arr
     * @return transposed 2D int array.
     */
    public static int[][] transpose(int[][] arr) {
        int[][] transposedArr = new int[arr[0].length][arr.length];
        for (int i = 0; i < transposedArr.length; i++) {
            for (int j = 0; j < transposedArr[0].length; j++) {
                transposedArr[i][j] = arr[j][i];
            }
        }
        return transposedArr;
    }

    /**
     * Returns an int[][] representing cells derived from a CSV,
     * which is input as and iterated through using a BufferedReader
     * 
     * @param br - BufferedReader of CSV
     * @return int[][] rep cells
     */
    public static int[][] csvDataToCells(BufferedReader br) {
        List<int[]> lineList = new LinkedList<>();
        FileLineIterator iter = new FileLineIterator(br);
        while (iter.hasNext()) {
            lineList.add(parseLine(iter.next()));
        }

        int[][] cells = new int[lineList.size()][lineList.get(0).length];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = lineList.get(i);
        }

        // validation
        for (int[] col : cells) {
            // check for staggered rows
            if (col.length != cells[0].length) {
                throw new IllegalArgumentException("CSV grid rows are staggered");
            }

            // check for valid cell values
            for (int c : col) {
                if (c < 0 || c > 3) {
                    throw new IllegalArgumentException("CSV contains invalid values");
                }
            }
        }

        return cells;
    }
}
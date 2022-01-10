package org.cis120.gameoflife;

import java.util.*;
import java.io.*;

/**
 * This class is a general model for GameOfLife.
 *
 * This class controls grid information such as boundaries,
 * status of cells (alive/dead, restricted, etc.). It will also
 * be in charge of implementing the rules of John Conway's Game of Life
 * in addition to new rules necessary for level/puzzle design.
 *
 * Rules of John Conway's Game of Life are sourced from:
 * https://www.compadre.org/osp/EJSS/3577/12.htm
 * 2014, Francisco Esquembre; Wolfgang Christian, under CC License
 *
 * Updating to successive generations will also be handled in this class.
 */

public class Grid {

    /**
     * cell data stored in an 2D array
     * - goes from top -> bot for row (0 is top)
     * - goes from left->right for col (0 is left-most)
     * cell state is represented by int:
     * - 0 is empty/dead
     * - 1 is full/alive
     * - 2 is restricted
     * - 3 is kill cell
     */
    private int[][] cells;
    private int[][] buffered;
    private final int buffer = 10;
    private LinkedList<int[][]> ticks;
    private int currTick;

    /**
     * Constructs a Grid Object based off of the reset() method
     * 
     * @param row
     * @param col
     */
    public Grid(int row, int col) {
        this.ticks = new LinkedList<>();
        reset(row, col); // ripped off of TicTacToe because its p cool!
    }

    /**
     * Constructs a Grid Object based off of file data using
     * overloaded reset() method
     * 
     * @param filename
     */
    public Grid(String filename) {
        this.ticks = new LinkedList<>();
        reset(filename);
    }

    /**
     * Resets the grid to empty based on a set row and col
     * 
     * @param row
     * @param col
     */
    public void reset(int row, int col) {
        this.cells = new int[row][col];
        this.buffered = cellsToBuffered(this.cells);
        this.ticks.clear();
        this.currTick = 0;

        // set all of them to dead.
        for (int[] column : this.cells) {
            Arrays.fill(column, 0);
        }
    }

    /**
     * Resets the grid to a given int[][]
     * 
     * @param cells
     */
    public void reset(int[][] cells) {
        this.cells = cells;
        this.ticks.clear();
        this.currTick = 0;
        this.buffered = cellsToBuffered(this.cells);
    }

    /**
     * Resets the grid based off of given file
     * 
     * @param filename - String of filename
     */
    public void reset(String filename) {
        BufferedReader br = FileLineIterator
                .fileToReader("files/" + filename);
        this.cells = GridParser.csvDataToCells(br);
        this.ticks.clear();
        this.currTick = 0;
        this.buffered = cellsToBuffered(this.cells);
    }

    /**
     * converts current cell to buffered cell matrix
     */
    public int[][] cellsToBuffered(int[][] cells) {
        int h = getGridHeight() + 2 * buffer;
        int w = getGridWidth() + 2 * buffer;
        int[][] arr = new int[h][w];

        for (int[] column : arr) {
            Arrays.fill(column, 0);
        }

        for (int r = 0; r < getGridHeight(); r++) {
            for (int c = 0; c < getGridWidth(); c++) {
                arr[r + buffer][c + buffer] = cells[r][c];
            }
        }
        return arr;
    }

    /**
     * turns buffered matrix into proper cell matrix
     * 
     * @return int[][]
     */
    public int[][] bufferedToCells() {
        return bufferedToCells(buffered);
    }

    public int[][] bufferedToCells(int[][] x) {
        int[][] arr = new int[getGridHeight()][getGridWidth()];
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                arr[r][c] = x[r + buffer][c + buffer];
            }
        }
        return arr;
    }

    /**
     * Returns true if input int[][] is equal to cells
     * 
     * @param input - int[][] to compare
     * @return boolean indicating structural equality
     */
    public boolean equals(int[][] input) {
        if (input.length != cells.length) {
            return false;
        } else if (input[0].length != cells[0].length) {
            return false;
        }

        for (int i = 0; i < input.length; i++) {
            if (!Arrays.equals(input[i], cells[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets cell to certain value
     * Throws exception if is not between 0-2
     * 
     * @param row
     * @param col
     * @param val - value to be set
     * @exception IllegalArgumentException val is not valid
     */
    public void setCell(int row, int col, int val) {
        if (val < 0 || val > 3) {
            throw new IllegalArgumentException("input val is not valid");
        }
        cells[row][col] = val;
        buffered[row + buffer][col + buffer] = val;
    }

    /**
     * Returns cell value of given row and column
     * 
     * @param row
     * @param col
     * @return cell value
     */
    public int getCell(int row, int col) {
        return cells[row][col];
    }

    public int[][] getCells() {
        return cells;
    }

    /**
     * Below are pretty straightforward getter methods for
     * cells dimensions.
     * 
     * @return int representing either width or height of cell dimensions.
     */

    public int getGridWidth() {
        return cells[0].length;
    }

    public int getGridHeight() {
        return cells.length;
    }

    /**
     * Returns whether cell is alive in buffered matrix
     * 
     * @param row
     * @param col
     * @param isBuffered
     * @return boolean indicating alive status
     */
    public boolean isAlive(int row, int col, boolean isBuffered) {
        if (isBuffered) {
            return buffered[row][col] == 1;
        }
        return cells[row][col] == 1;
    }

    /**
     * Returns true if cell tested is a special cell
     * 
     * @param row
     * @param col
     * @return boolean indicating special status
     */
    public boolean isSpecial(int row, int col) {
        return cells[row][col] != 1
                && cells[row][col] != 0;
    }

    /**
     * Returns true if cell tested is a kill cell
     * 
     * @param row
     * @param col
     * @param isBuffered
     * @return boolean indicating kill cell (death!)
     */
    public boolean isKillCell(int row, int col, boolean isBuffered) {
        if (isBuffered) {
            return buffered[row][col] == 3;
        }
        return cells[row][col] == 3;
    }

    public boolean isRestrictCell(int row, int col) {
        return cells[row][col] == 2;
    }

    /**
     * Below is____() functions return whether an ALIVE cell:
     * - has an adjacent alive on its right, left, top, bottom
     * - has a diagonal alive on all its corners
     *
     * @param row --- of cell
     * @param col --- of cell
     * @return whether it has certain alive cell adj/diag to it
     */
    public boolean isAdjRight(int row, int col) {
        // first condition is for edge cases and prevents OutOfBounds Excp
        return col + 1 < buffered[0].length
                && isAlive(row, col + 1, true);
    }

    public boolean isAdjLeft(int row, int col) {
        return col > 0 && isAlive(row, col - 1, true);
    }

    public boolean isAdjTop(int row, int col) {
        return row > 0 && isAlive(row - 1, col, true);
    }

    public boolean isAdjBot(int row, int col) {
        return row + 1 < buffered.length
                && isAlive(row + 1, col, true);
    }

    public boolean isDiagTopRight(int row, int col) {
        return row > 0 && col + 1 < buffered[0].length
                && isAlive(row - 1, col + 1, true);
    }

    public boolean isDiagTopLeft(int row, int col) {
        return row > 0 && col > 0
                && isAlive(row - 1, col - 1, true);
    }

    public boolean isDiagBotRight(int row, int col) {
        return row + 1 < buffered.length && col + 1 < buffered[0].length
                && isAlive(row + 1, col + 1, true);
    }

    public boolean isDiagBotLeft(int row, int col) {
        return row + 1 < buffered.length && col > 0
                && isAlive(row + 1, col - 1, true);
    }

    /**
     * Returns number of alive neighbors
     * 
     * @param row
     * @param col
     * @return int representing number of alive neighbors
     */
    public int neighboursCount(int row, int col) {
        int sum = 0;

        // pain... idk a more elegant way to do this
        if (isAdjLeft(row, col)) {
            sum++;
        }
        if (isAdjRight(row, col)) {
            sum++;
        }
        if (isAdjBot(row, col)) {
            sum++;
        }
        if (isAdjTop(row, col)) {
            sum++;
        }
        if (isDiagTopLeft(row, col)) {
            sum++;
        }
        if (isDiagTopRight(row, col)) {
            sum++;
        }
        if (isDiagBotLeft(row, col)) {
            sum++;
        }
        if (isDiagBotRight(row, col)) {
            sum++;
        }
        return sum;
    }

    /**
     * Iterates to next generation of cells. Returns true once completed.
     * Returns false if a cell wants to become alive in a kill cell.
     */
    public boolean nextGeneration() {
        // establish inital tick if needed
        if (getTickSize() == 0) {
            ticks.add(buffered);
        }

        int nrow = buffered.length;
        int ncol = buffered[0].length;
        int[][] next = new int[nrow][ncol];

        if (currTick == ticks.size() - 1) {
            for (int r = 0; r < nrow; r++) {
                for (int c = 0; c < ncol; c++) {
                    if (isAlive(r, c, true)) {
                        if (neighboursCount(r, c) < 2) {
                            // Any live cell with fewer than two live neighbors dies,
                            // as if by loneliness --- :( poor cell
                            next[r][c] = 0;
                        } else if (neighboursCount(r, c) > 3) {
                            // Any live cell with more than three live neighbors dies,
                            // as if by overcrowding. oof owwie
                            next[r][c] = 0;
                        } else {
                            // Any live cell with two or three live neighbors lives,
                            // unchanged, to the next generation. :D
                            next[r][c] = 1;
                        }
                    } else if (!isAlive(r, c, true)) {
                        // Any dead cell with exactly three live neighbors comes to life.
                        if (neighboursCount(r, c) == 3) {
                            // kill protocol
                            if (isKillCell(r, c, true)) {
                                return false;
                            }
                            next[r][c] = 1;
                        } else {
                            next[r][c] = buffered[r][c];
                        }
                    }
                }
            }
            // Updates Cells and ticks
            buffered = next;
            cells = bufferedToCells();
            ticks.add(buffered);
            currTick++;
        } else {
            // iterative case if starting from a place inside ticks
            goToTick(currTick + 1);
        }
        return true;
    }

    /**
     * Returns cells at specific tick
     * 
     * @param index
     * @return int[][] representing cells at tick
     */
    public int[][] getTick(int index) {
        return ticks.get(index);
    }

    public int getTickSize() {
        return ticks.size();
    }

    /**
     * Updates model to certain tick
     * Throws IllegalArgumentException if tick index does not exist
     * 
     * @param i - tick index
     */
    public void goToTick(int i) {
        if (ticks.size() <= i) {
            throw new IllegalArgumentException("goToTick: out of bounds");
        }
        buffered = ticks.get(i);
        cells = bufferedToCells();
        currTick = i;
    }

    /**
     * Goes back a tick
     */
    public void backTick() {
        if (currTick - 1 < 0) {
            return;
        }
        goToTick(currTick - 1);
    }

    /**
     * Goes forward a tick
     */
    public void forwardTick() {
        if (currTick + 1 >= ticks.size()) {
            return;
        }
        goToTick(currTick + 1);
    }

    /**
     * Updates model to first tick
     */
    public void firstTick() {
        goToTick(0);
    }

    /**
     * Updates model to last tick
     */
    public void lastTick() {
        goToTick(ticks.size() - 1);
    }

    /**
     * Returns number of alive cells in scope of cells (not buffered)
     * 
     * @return int representing number of alive cells
     */
    public int getAliveCount() {
        int sum = 0;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                if (isAlive(r, c, false)) {
                    sum++;
                }
            }
        }
        return sum;
    }
}
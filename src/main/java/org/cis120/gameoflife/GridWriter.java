package org.cis120.gameoflife;

import java.io.*;

public class GridWriter {

    static final String FILE_DIR = "files/";

    public static boolean fileExists(String filename) {
        // the below is super convoluted... oops
        return new File(FILE_DIR + filename).isFile();
    }

    public static void createGridFile(String filename, int row, int col) {
        try {
            File gridFile = new File(FILE_DIR + filename);
            if (gridFile.createNewFile()) {
                Grid grid = new Grid(row, col);
                writeToFile(filename, grid.getCells());
            } else {
                throw new IllegalArgumentException("file already exists");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void writeToFile(String filename, int[][] cells) {
        File file = new File(FILE_DIR + filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int[] col : cells) {
                for (int i = 0; i < col.length; i++) {
                    bw.write(col[i] + ",");
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
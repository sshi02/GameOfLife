package org.cis120.gameoflife;

import java.util.*;
import java.io.*;

/**
 * Level extends Grid to serve as a model for
 * Game of Life Story levels
 *
 * Instance Variables:
 * levelID <- lynchpin for naming system and identification
 * cellLim <- limits cells users can place
 * cellMin <- minimum cells users are req to place
 * genLim <- limits gens model can run through
 * genMin <- minimum gens model is required to run through
 * winCells <- cells that will trigger a win
 * winMin <- minimum number of alive cells that trigger a win
 * winCount <- EXACT number of alive cells that trigger a win
 * objecMes <- objective message for user
 * hint <- message for user if they suck
 *
 * Special values that need to be caught
 * if either null or -1, special indication attached
 *
 * Construction of level:
 * Given a levelID (int likely), level call a super constructor
 * referring to a layout file (ex. level[levelId].csv).
 * Then, a method will use levelID to initialize the rest of the
 * instance variables.
 * Method should take in levelID, search for a
 * levelData[levelID].csv file and read it. Formatting
 * specified below.
 * Finally, the constructor checks for a progress file. If the
 * file does not exist, nothing happens. Else, it will load the
 * user's progress.
 *
 * levelData[levelID].csv structure:
 * objecMes, hint,
 * cellLim, cellMin,
 * genLim, genMin,
 * winCount, winMin,
 * r1, c1, r2, c2... <- winCells
 *
 * Checking for a win:
 * IMPORTANT INVARIANTS FOR A WIN CONDITION:
 * - cellLim MUST NOT be exceeded
 * - cellMin MUST be met or exceeded
 * - genLim MUST NOT be exceeded. If a genLim is exceeded, we
 * should expect the fail condition to be met.
 * - genMin MUST be met or exceeded
 * - winCells COULD be matching with some cells present
 * within the running model.
 * - winCount COULD be matched exactly and trigger a win IF AND
 * ONLY IF genMin is met.
 * - winMin COULD be matched or exceeded.
 * When writing a win condition checker, the MUSTs have to be
 * checked first before considering the possibility of a win
 * For winCount and winMin, the IF AND ONLY IF conditions only
 * matter when genMin is defined.
 *
 * Checking for a failure:
 * IMPORTANT INVARIANTS FOR A FAIL CONDITION:
 * - genLim COULD be exceeded.
 * - model find that an alive cell tries to generate in a kill
 * cell. To find this, the failure condition should
 * catch the return value of runGeneration().
 */

public class Level extends Grid {
    /**
     * Id of a level
     */
    private int levelId;

    /**
     * objective message for user
     */
    private String objecMes;

    /**
     * dum dum message for user
     */
    private String hint;

    /**
     * number of cells user is allowed to modify
     * if -1, no limit to number of modifications
     */
    private int cellLim;

    /**
     * minimum of cells user is required to modify
     * if -1, no minimum to number of modifications
     */
    private int cellMin;

    /**
     * number of generations allowed
     * if -1, no limit to generations
     */
    private int genLim;

    /**
     * minimum of generations required to go through
     * if -1, no minimum to required generations
     */
    private int genMin;

    /**
     * list of win condition cells contained in int[]
     */
    private LinkedList<int[]> winCells;

    /**
     * number of alive cells to exceed in order to win
     * if -1, this is not a win condition
     */
    private int winMin;

    /**
     * EXACT number of alive cells needed in order to win
     * if -1, this is not a win condition
     */
    private int winCount;

    private int[][] layout;

    private int layoutAliveCount;

    public Level(int levelIdInit) {
        super("level" + levelIdInit + ".csv");
        this.levelId = levelIdInit;
        layout = new int[getGridHeight()][getGridWidth()];
        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[0].length; col++) {
                layout[row][col] = getCells()[row][col];
            }
        }
        layoutAliveCount = getAliveCount();

        readAndSetupLevelData(levelId);
        if (isProgress()) {
            loadProgress();
        }
    }

    /**
     * reads specifically formatted level data given level id
     * This method will throw all the exceptions if given a poorly
     * formatted file.
     * 
     * @param levelId
     */
    public void readAndSetupLevelData(int levelId) {
        String filePath = "files/levelData" + levelId + ".csv";
        FileLineIterator iter = new FileLineIterator(filePath);

        // reading objectMes, hint
        String[] line = parseLine(iter);
        objecMes = line[0];
        hint = line[1];

        // reading cellLim, cellMin
        line = parseLine(iter);
        cellLim = Integer.parseInt(line[0]);
        cellMin = Integer.parseInt(line[1]);

        // reading genLim, genMin
        line = parseLine(iter);
        genLim = Integer.parseInt(line[0]);
        genMin = Integer.parseInt(line[1]);

        // reading winCount, winMin
        line = parseLine(iter);
        winCount = Integer.parseInt(line[0]);
        winMin = Integer.parseInt(line[1]);

        // reading winCells
        line = parseLine(iter);
        winCells = new LinkedList<>();
        for (int i = 0; i < line.length; i += 2) {
            int r = Integer.parseInt(line[i]);
            int c = Integer.parseInt(line[i + 1]);
            winCells.add(new int[] { r, c });
        }
    }

    public String[] parseLine(FileLineIterator iter) {
        return iter.next().split(",");
    }

    public boolean isProgress() {
        String filePath = "progress" + levelId + ".csv";
        return GridWriter.fileExists(filePath);
    }

    public void loadProgress() {
        String filePath = "progress" + levelId + ".csv";
        reset(filePath);
    }

    public void save() {
        String fileName = "progress" + levelId + ".csv";
        if (!isProgress()) {
            GridWriter.createGridFile(
                    fileName,
                    getGridWidth(),
                    getGridHeight()
            );
        }
        GridWriter.writeToFile(fileName, getCells());
    }

    public void reset() {
        reset("level" + levelId + ".csv");
        save();
    }

    public boolean isWon() {
        // bottom line constraints
        // == -1 is to check if the constraint matters
        if ((genLim == -1 || getTickSize() - 1 <= genLim)
                && (genMin == -1 || getTickSize() - 1 >= genMin)) {
            // win conditions
            // != -1 is to check if it is a win condition
            if (onWinCell()
                    || (winCount != -1 && getAliveCount() == winCount)
                    || (winMin != -1 && getAliveCount() >= winMin)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks for fails outside of kill cells
     * 
     * @return
     */
    public boolean isFailed() {
        return genLim != -1 && getTickSize() - 1 > genLim;
    }

    public boolean onWinCell() {
        for (int[] wc : winCells) {
            int r = wc[0];
            int c = wc[1];
            if (r == -1 || c == -1) {
                return false;
            }
            if (isAlive(r, c, false)) {
                return true;
            }
        }
        return false;
    }

    public String getMessage(boolean needHint) {
        if (needHint) {
            return objecMes + "\n \n Hint: \n" + hint;
        }
        return objecMes;
    }

    public int getCellLim() {
        return cellLim;
    }

    public int getCellMin() {
        return cellMin;
    }

    public int findCellsModif() {
        return Math.abs(getAliveCount() - layoutAliveCount);
    }

    public LinkedList<int[]> getWinCells() {
        return winCells;
    }
}
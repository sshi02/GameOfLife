package org.cis120.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelView extends JPanel {
    private int levelId;
    private Level level;
    private JTextArea message;
    private JLabel status;

    /**
     * represents whether the model is running
     * in this case, it is the level.
     */
    private boolean isRunning;

    /**
     * represents whether the model is currently
     * simulating generations
     */
    private boolean isSimulating;

    /**
     * represents whether the model is won
     */
    private boolean isWon;

    /**
     * represents whether the model is failed
     */
    private boolean isFailed;


    // Grid Dimensions
    public static final int CELL_LENGTH = 40;
    private static int gridWidth;
    private static int gridHeight;

    // COLOR PALLETTE
    public static final Color SOFT_BLACK = new Color(30, 30, 30);
    public static final Color SOFT_RED = new Color(244, 113, 116);
    public static final Color SOFT_GRAY = new Color(47, 79, 79);
    public static final Color SOFT_GREEN = new Color(172, 209, 175);

    public LevelView(int levelId, JTextArea message, JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);
        this.levelId = levelId;
        level = new Level(levelId);
        isRunning = false;
        isSimulating = false;
        isWon = false;
        isFailed = false;
        gridWidth = level.getGridWidth() * CELL_LENGTH;
        gridHeight = level.getGridHeight() * CELL_LENGTH;
        this.message = message;
        this.status = statusInit;
        this.message.setText(level.getMessage(false));
        this.status.setText("Level " + levelId);

        revalidate();
        repaint();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // stops writing cells if model is running
                if (isRunning) {
                    return;
                }

                // after failure action: restart on click
                if (isFailed) {
                    isFailed = false;
                    reset();
                    revalidate();
                    repaint();
                    return;
                }

                // after winning action: rewatch
                if (isWon) {
                    isWon = false;
                    level.firstTick();
                    level.reset(level.getCells());
                    revalidate();
                    repaint();
                    run();
                    return;
                }

                Point p = e.getPoint();
                int r = p.y / CELL_LENGTH;
                int c = p.x / CELL_LENGTH;
                if (!level.isSpecial(r, c)) {
                    if (level.isAlive(r, c, false)) {
                        level.setCell(r, c, 0);
                        if (level.getCellLim() != -1
                                && level.getCellLim() < level.findCellsModif()) {
                            level.setCell(r, c, 1);
                            return;
                        }
                    } else {
                        level.setCell(r, c, 1);
                        if (level.getCellLim() != -1
                                && level.getCellLim() < level.findCellsModif()) {
                            level.setCell(r, c, 0);
                            return;
                        }
                    }
                    updateModifCount();
                }
                revalidate();
                repaint();
            }
        });
    }

    /**
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // BLACK background
        g.setColor(SOFT_BLACK);
        g.fillRect(0, 0, gridWidth, gridHeight);

        // draws alive and special cells
        for (int r = 0; r < level.getGridHeight(); r++) {
            for (int c = 0; c < level.getGridWidth(); c++) {
                if (level.isAlive(r, c, false)) {
                    if (isWon) {
                        g.setColor(SOFT_GREEN);
                    } else if (isFailed) {
                        g.setColor(SOFT_RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(
                            c * CELL_LENGTH,
                            r * CELL_LENGTH,
                            CELL_LENGTH, CELL_LENGTH
                    );
                } else if (level.isRestrictCell(r, c) && !isRunning) {
                    g.setColor(SOFT_GRAY);
                    g.fillRect(
                            c * CELL_LENGTH,
                            r * CELL_LENGTH,
                            CELL_LENGTH, CELL_LENGTH
                    );
                }
                if (level.isKillCell(r, c, false)) {
                    g.setColor(SOFT_RED);
                    g.fillRect(
                            c * CELL_LENGTH,
                            r * CELL_LENGTH,
                            CELL_LENGTH, CELL_LENGTH
                    );
                }
            }
        }

        // draws win cells
        for (int[] cell : level.getWinCells()) {
            if (cell[0] == -1 || cell[1] == -1) {
                break;
            }

            // case of restricted while editing
            if (!isRunning &&
                    level.isRestrictCell(cell[0], cell[1])) {

                g.setColor(new Color(102,127, 102));
            } else {
                g.setColor(SOFT_GREEN);
            }
            g.fillRect(
                    cell[1] * CELL_LENGTH,
                    cell[0] * CELL_LENGTH,
                    CELL_LENGTH, CELL_LENGTH
            );
        }

        // draws grid when not simulating and is editing
        g.setColor(Color.WHITE);
        if (!isRunning && !isWon && !isFailed) {
            for (int i = CELL_LENGTH; i < gridWidth; i += CELL_LENGTH) {
                g.drawLine(i, 0, i, gridHeight);
            }
            for (int i = CELL_LENGTH; i < gridHeight; i += CELL_LENGTH) {
                g.drawLine(0, i, gridWidth, i);
            }
        }
    }

    /**
     * resets grid and all ticks
     */
    public void reset() {
        isRunning = false;
        isSimulating = false;
        isWon = false;
        isFailed = false;
        level.reset();
        status.setText("RESET");
        revalidate();
        repaint();
    }

    /**
     * saves progress in a file progress[level].csv
     */
    public void save() {
        isRunning = false;
        isSimulating = false;
        if (isWon) {
            level.reset();
        }
        level.save();
    }

    /**
     * runs the level
     */
    public void run() {
        // models does not run if cell min req is not fufilled
        if (level.getCellMin() != -1
                && level.getCellMin() < level.findCellsModif()) {
            return;
        }

        isRunning = true;
        status.setText("RUNNING");

        isSimulating = true;
        int delay = 300;
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    ((Timer) e.getSource()).stop();
                    isSimulating = false;
                }

                if (isSimulating) {
                    // iterate and check for conditions
                    boolean notKilled = level.nextGeneration();
                    revalidate();
                    repaint();
                    if (!notKilled || level.isFailed()) {
                        fail();
                    }
                    if (level.isWon()) {
                        win();
                    }
                }
            }
        });
        timer.start();
    }

    /**
     * Stops running the model and sets it to initial layout
     */
    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        isSimulating = false;
        level.firstTick();
        level.reset(level.getCells());
        status.setText("STOPPED");
        revalidate();
        repaint();
    }

    public void fail() {
        reset();
        isRunning = false;
        isSimulating = false;
        isFailed = true;
        status.setText("FAILED: Click to Retry");
    }

    public void win() {
        isRunning = false;
        isSimulating = false;
        isWon = true;
        status.setText("CLEARED: Click to Rewatch");
    }

    public void updateModifCount() {
        status.setText("Cells Modified: " + level.findCellsModif());
    }

    public boolean next() {
        int nextLevelId = levelId + 1;
        String filePath = "level" + nextLevelId + ".csv";
        if (GridWriter.fileExists(filePath)) {
            save();
            return true;
        }
        status.setText("No More Levels!");
        return false;
    }

    public boolean back() {
        int backLevelId = levelId - 1;
        String filePath = "level" + backLevelId + ".csv";
        if (GridWriter.fileExists(filePath)) {
            save();
            return true;
        }
        status.setText("You're on the first level!");
        return false;
    }

    public void exit() {
        save();
    }

    public String getMessage(boolean needHint) {
        return level.getMessage(needHint);
    }

    public Dimension getPreferredSize() {
        return new Dimension(gridWidth, gridHeight);
    }
}
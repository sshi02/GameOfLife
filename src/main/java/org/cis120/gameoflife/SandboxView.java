package org.cis120.gameoflife;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SandboxView extends JPanel {
    private Sandbox sandbox;
    private JLabel status;

    /**
     * represents whether the model is simply
     * just running
     */
    private Boolean isRunning;

    /**
     * represents whether the model is currently
     * simulating generations
     */
    private Boolean isSimulating;

    // Grid Dimensions
    public static final int GRID_WIDTH = 1280;
    public static final int GRID_HEIGHT = 720;
    public static final int CELL_LENGTH = 40;

    // COLOR PALETTE
    public static final Color SOFT_BLACK = new Color(30, 30, 30);

    public SandboxView(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);
        sandbox = new Sandbox();
        status = statusInit;
        isRunning = false;
        isSimulating = false;

        revalidate();
        repaint();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // stops writing cells if model isRunning
                if (isRunning) {
                    return;
                }

                Point p = e.getPoint();
                int r = p.y / CELL_LENGTH;
                int c = p.x / CELL_LENGTH;
                if (sandbox.isAlive(r, c, false)) {
                    sandbox.setCell(r, c, 0);
                } else {
                    sandbox.setCell(r, c, 1);
                }
                updateStatus("EDITING");
                revalidate();
                repaint();
            }
        });
    }

    /**
     * paints panel
     * paints alive cells, only draws grid lines when not running
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // BLACK Background
        g.setColor(SOFT_BLACK);
        g.fillRect(0, 0, GRID_WIDTH, GRID_HEIGHT);

        // draws grid when not simulating
        g.setColor(Color.WHITE);
        if (!isRunning) {
            for (int i = CELL_LENGTH; i < GRID_WIDTH; i += CELL_LENGTH) {
                g.drawLine(i, 0, i, GRID_HEIGHT);
            }
            for (int i = CELL_LENGTH; i < GRID_HEIGHT; i += CELL_LENGTH) {
                g.drawLine(0, i, GRID_WIDTH, i);
            }
        }

        // draws alive cells
        for (int r = 0; r < sandbox.getGridHeight(); r++) {
            for (int c = 0; c < sandbox.getGridWidth(); c++) {
                if (sandbox.isAlive(r, c, false)) {
                    g.fillRect(
                            c * CELL_LENGTH,
                            r * CELL_LENGTH,
                            CELL_LENGTH, CELL_LENGTH
                    );
                }
            }
        }
    }

    /**
     * resets grid and all ticks
     */
    public void reset() {
        isRunning = false;
        isSimulating = false;
        sandbox.reset(
                sandbox.getGridHeight(),
                sandbox.getGridWidth()
        );
        updateStatus("EDITING");
        revalidate();
        repaint();
    }

    /**
     * updates status of label...
     * leaving this here since i dont want to refactor
     * after making some changes
     */
    public void updateStatus(String str) {
        status.setText(str);
    }

    public void load(int saveId) {
        isRunning = false;
        isSimulating = false;
        sandbox.load(saveId);
        status.setText("LOADED SAVE  " + saveId);
        revalidate();
        repaint();
    }

    public void save(int saveId) {
        isRunning = false;
        isSimulating = false;
        sandbox.save(saveId);
        status.setText("SAVED -> SAVE " + saveId);
        revalidate();
        repaint();
    }

    public void run() {
        if (!isRunning) {
            isRunning = true;
            updateStatus("RUNNING");
        }

        isSimulating = true;
        int delay = 300;
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    ((Timer) e.getSource()).stop();
                    isSimulating = false;
                }

                // implementing pause functionality
                if (isSimulating) {
                    sandbox.nextGeneration();
                    repaint();
                }
            }
        });
        timer.start();
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        isSimulating = false;
        updateStatus("STOPPED");
        sandbox.reset(sandbox.getCells());
        revalidate();
        repaint();
    }

    public void pause() {
        isSimulating = false;
        updateStatus("PAUSED");
        revalidate();
        repaint();
    }

    public void goToTick(int i) {
        if (!isRunning || isSimulating) {
            return;
        }
        sandbox.goToTick(i);
        revalidate();
        repaint();
    }

    public void backTick() {
        if (!isRunning || isSimulating) {
            return;
        }
        sandbox.backTick();
        revalidate();
        repaint();
    }

    public void forwardTick() {
        if (!isRunning || isSimulating) {
            return;
        }
        sandbox.forwardTick();
        revalidate();
        repaint();
    }

    public void firstTick() {
        if (!isRunning || isSimulating) {
            return;
        }
        sandbox.firstTick();
        revalidate();
        repaint();
    }

    public void lastTick() {
        if (!isRunning || isSimulating) {
            return;
        }
        sandbox.lastTick();
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GRID_WIDTH, GRID_HEIGHT);
    }
}
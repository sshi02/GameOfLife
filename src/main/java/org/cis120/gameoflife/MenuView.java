package org.cis120.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuView extends JPanel {
    private Grid title;

    /**
     * represents whether the model/title should
     * be running
     */
    private Boolean isRunning;

    // Grid Dimensions
    public static final int GRID_WIDTH = 640;
    public static final int GRID_HEIGHT = 180;
    public static final int CELL_LENGTH = 10;

    // COLOR PALETTE
    public static final Color SOFT_BLACK = new Color(30, 30, 30);

    public MenuView() {
        setFocusable(true);
        title = new Grid("title.csv");
        isRunning = false;

        revalidate();
        repaint();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isRunning) {
                    stop();
                } else {
                    run();
                }
            }
        });
    }

    public void run() {
        isRunning = true;
        int delay = 300;
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    ((Timer) e.getSource()).stop();
                } else {
                    title.nextGeneration();
                    repaint();
                }
            }
        });
        timer.start();
    }

    public void stop() {
        isRunning = false;
        title.firstTick();
        revalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // BLACK Background
        g.setColor(SOFT_BLACK);
        g.fillRect(0, 0, GRID_WIDTH, GRID_HEIGHT);
        g.setColor(Color.WHITE);

        // draws alive cells
        for (int r = 0; r < title.getGridHeight(); r++) {
            for (int c = 0; c < title.getGridWidth(); c++) {
                if (title.isAlive(r, c, false)) {
                    g.fillRect(
                            c * CELL_LENGTH,
                            r * CELL_LENGTH,
                            CELL_LENGTH, CELL_LENGTH
                    );
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GRID_WIDTH, GRID_HEIGHT);
    }
}
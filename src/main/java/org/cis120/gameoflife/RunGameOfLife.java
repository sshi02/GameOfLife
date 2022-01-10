package org.cis120.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RunGameOfLife implements Runnable {

    public void clean(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setJMenuBar(null);
        frame.revalidate();
        frame.repaint();
    }

    public void startMenu(JFrame frame) {
        clean(frame);
        frame.setLocation(100, 100);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1));
        JLabel title = new JLabel(
                "        A Puzzle Leveler" +
                        "                                        " +
                        "                                        " +
                        "                                        " +
                        " hint: click the title"
        );
        JButton storyButton = new JButton("STORY MODE");
        JButton sandboxButton = new JButton("SANDBOX MODE");
        menuPanel.add(title);
        menuPanel.add(storyButton);
        menuPanel.add(sandboxButton);

        // Title
        MenuView titlePanel = new MenuView();

        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(menuPanel, BorderLayout.SOUTH);

        storyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                level(frame, 1);
            }
        });

        sandboxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox(frame);
            }
        });

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void sandbox(JFrame frame) {
        clean(frame);
        frame.setLocation(0, 0);

        // Controls Panel
        JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.NORTH);
        JLabel status = new JLabel("EDITING");
        controlPanel.add(status);
        controlPanel.setLayout(new GridLayout(1, 10));
        // Pause/Play controls
        JButton stopButton = new JButton("STOP");
        JButton playButton = new JButton("RUN");
        JButton pauseButton = new JButton("PAUSE");
        JButton tickForwardButton = new JButton(">");
        JButton tickBackwardButton = new JButton("<");
        JButton tickFirstButton = new JButton("<<");
        JButton tickLastButton = new JButton(">>");
        controlPanel.add(stopButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(tickForwardButton);
        controlPanel.add(tickBackwardButton);
        controlPanel.add(tickFirstButton);
        controlPanel.add(tickLastButton);

        /*
         * THIS IS BEING COMMENTED OUT FOR THE SAKE OF TESTING OUT NEW
         * WAYS OF RUNNING SANDBOX. MAY BE USED AGAIN LATER FOR LEVEL
         * // Steps Control
         * numSteps = 5;
         * JLabel stepsLabel = new JLabel(spacing + "FIVE STEPS");
         * JButton oneStepsButton = new JButton("1");
         * JButton fiveStepsButton = new JButton("5");
         * JButton tenStepsButton = new JButton("10");
         * control_panel.add(stepsLabel);
         * control_panel.add(oneStepsButton);
         * control_panel.add(fiveStepsButton);
         * control_panel.add(tenStepsButton);
         */

        // Canvas Control Panel
        JPanel canvasControlPanel = new JPanel();
        canvasControlPanel.setLayout(new GridLayout(6, 1));
        frame.add(canvasControlPanel, BorderLayout.WEST);

        JButton reset = new JButton("RESET");

        // Save, Load, Exit
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu saveMenu = new JMenu("SAVE");
        JMenuItem saveOne = new JMenuItem("SAVE ONE");
        JMenuItem saveTwo = new JMenuItem("SAVE TWO");
        JMenuItem saveThree = new JMenuItem("SAVE THREE");
        saveMenu.add(saveOne);
        saveMenu.add(saveTwo);
        saveMenu.add(saveThree);

        JMenu loadMenu = new JMenu("LOAD");
        JMenuItem loadOne = new JMenuItem("SAVE ONE");
        JMenuItem loadTwo = new JMenuItem("SAVE TWO");
        JMenuItem loadThree = new JMenuItem("SAVE THREE");
        loadMenu.add(loadOne);
        loadMenu.add(loadTwo);
        loadMenu.add(loadThree);

        JMenuItem exit = new JMenuItem("EXIT");

        canvasControlPanel.add(reset);
        menuBar.add(saveMenu);
        menuBar.add(loadMenu);
        menuBar.add(exit);

        // Sandbox
        SandboxView sandbox = new SandboxView(status);
        frame.add(sandbox, BorderLayout.CENTER);

        // all the button functionality ie where my sanity dies
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.stop();
            }
        });

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.run();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.pause();
            }
        });

        tickBackwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.backTick();
            }
        });

        tickForwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.forwardTick();
            }
        });

        tickFirstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.firstTick();
            }
        });

        tickLastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.lastTick();
            }
        });

        /*
         * See above mass commented out code
         * oneStepsButton.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * numSteps = 1;
         * stepsLabel.setText(spacing + "ONE STEPS");
         * }
         * });
         * 
         * fiveStepsButton.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * numSteps = 5;
         * stepsLabel.setText(spacing + "FIVE STEPS");
         * }
         * });
         * 
         * tenStepsButton.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * numSteps = 10;
         * stepsLabel.setText(spacing + "TEN STEPS");
         * }
         * });
         */

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.reset();
            }
        });

        saveOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.save(1);
            }
        });

        saveTwo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.save(2);
            }
        });

        saveThree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.save(3);
            }
        });

        loadOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.load(1);
            }
        });

        loadTwo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.load(2);
            }
        });

        loadThree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sandbox.load(3);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMenu(frame);
            }
        });

        frame.repaint();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void level(JFrame frame, int levelId) {
        clean(frame);
        frame.setLocation(0, 0);

        // Message Panel
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridLayout(3, 1));
        frame.add(messagePanel, BorderLayout.EAST);
        JLabel status = new JLabel();
        JTextArea message = new JTextArea();
        JScrollPane messageScrollpane = new JScrollPane(message);
        messageScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageScrollpane.setPreferredSize(new Dimension(300, 100));
        JCheckBox hintBox = new JCheckBox("Hint", false);
        messagePanel.add(status);
        messagePanel.add(messageScrollpane);
        messagePanel.add(hintBox);

        // Controls Panel
        JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.WEST);
        controlPanel.setLayout(new GridLayout(3, 1));

        JButton playButton = new JButton("RUN");
        JButton stopButton = new JButton("STOP");
        JButton resetButton = new JButton("RESET");
        controlPanel.add(playButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);

        // Next, Back, Story Select
        JPanel select = new JPanel();
        select.setLayout(new GridLayout(1, 3));
        frame.add(select, BorderLayout.SOUTH);
        JButton backButton = new JButton("LAST LEVEL");
        JButton exitButton = new JButton("BACK TO MENU");
        JButton nextButton = new JButton("NEXT LEVEL");
        select.add(nextButton);
        select.add(backButton);
        select.add(exitButton);

        LevelView level = new LevelView(levelId, message, status);
        frame.add(level, BorderLayout.CENTER);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                level.run();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                level.stop();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                level.reset();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (level.next()) {
                    level(frame, levelId + 1);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (level.back()) {
                    level(frame, levelId - 1);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                level.exit();
                startMenu(frame);
            }
        });

        message.setText(level.getMessage(false));
        message.setLineWrap(true);

        hintBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    message.setText(level.getMessage(true));
                } else {
                    message.setText(level.getMessage(false));
                }
            }
        });

        frame.repaint();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Game Of Life");
        frame.setLocation(200, 200);
        frame.validate();

        startMenu(frame);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
package org.cis120.gameoflife;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testLevel1() {
        Level level = new Level(1);
        for (int i = 0; i < 20; i++) {
            level.nextGeneration();
        }
        assertTrue(level.isWon());
    }

    /*
     * tests level2 and findCellsModif()
     */
    @Test
    public void testLevel2() {
        Level level = new Level(2);
        level.setCell(3, 3, 0);
        assertEquals(1, level.findCellsModif());
        level.setCell(4, 4, 0);
        assertEquals(2, level.findCellsModif());
        level.nextGeneration();
        level.isWon();
    }
}
package org.cis120.gameoflife;

/**
 * Sandbox is a subclass of Grid to serve
 * as a model for Sandbox mode. This will have
 * unique functionalities like loading saved layouts.
 * Saved layouts are indicated by save state id's (integer)
 * that represent part of the filename: sandbox[saveId].csv
 */
public class Sandbox extends Grid {

    /**
     * When a sandbox is opened, it will always open
     * an empty sandbox.
     * If a user wants to open a saved layout
     * they will need to load saved file.
     */
    public Sandbox() {
        super(18, 32);
    }

    /**
     * load() loads desired saveId file into sandbox
     * 
     * @param saveId
     */
    public void load(int saveId) {
        if (!existsSaveState(saveId)) {
            throw new IllegalArgumentException("File does not exist");
        }
        reset("sandbox" + saveId + ".csv");
    }

    /**
     * save() saves to desired saveId file, overwriting
     * all its contents
     * 
     * @param saveId - saveId of save file.
     */
    public void save(int saveId) {
        String filename = "sandbox" + saveId + ".csv";
        if (!existsSaveState(saveId)) {
            GridWriter.createGridFile(
                    filename,
                    getGridHeight(),
                    getGridWidth()
            );
        }
        GridWriter.writeToFile(filename, getCells());
    }

    public static boolean existsSaveState(int saveId) {
        return GridWriter.fileExists("sandbox" + saveId + ".csv");
    }
}
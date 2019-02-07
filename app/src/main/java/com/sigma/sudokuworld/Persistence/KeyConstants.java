package com.sigma.sudokuworld.Persistence;

public final class KeyConstants {

    /**
     * About Bundles:
     *
     * A "settings" Bundle should have:
     * Difficulty
     * GameMode
     * Native words
     * Foreign words
     *
     * A "save" Bundle should have:
     * Difficulty
     * GameMode
     * Cell values
     * Solution
     * Locked cells
     * Native words
     * Foreign words
     */

    //Public index keys for saving data in bundle
    public static final String DIFFICULTY_KEY = "difficulty";
    public static final String MODE_KEY = "mode";
    public static final String CELL_VALUES_KEY = "values";
    public static final String SOLUTION_VALUES_KEY = "solution";
    public static final String LOCKED_CELLS_KEY = "locked";
    public static final String NATIVE_WORDS_KEY = "native";
    public static final String FOREIGN_WORDS_KEY = "foreign";

    //Public index key for continue game
    public static final String CONTINUE_KEY = "continue";
}

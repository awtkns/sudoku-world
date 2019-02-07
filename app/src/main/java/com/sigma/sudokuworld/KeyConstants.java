package com.sigma.sudokuworld;

final class KeyConstants {

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
     *
     */

    //Public index keys for saving data in bundle
    static final String DIFFICULTY_KEY = "difficulty";
    static final String MODE_KEY = "mode";
    static final String SUDOKU_SIZE_KEY = "size";
    static final String CELL_VALUES_KEY = "values";
    static final String SOLUTION_VALUES_KEY = "solution";
    static final String LOCKED_CELLS_KEY = "locked";
    static final String NATIVE_WORDS_KEY = "native";
    static final String FOREIGN_WORDS_KEY = "foreign";
}

package com.sigma.sudokuworld.game;

public enum GameDifficulty {
    EASY,
    MEDIUM,
    HARD;

    public static GameDifficulty fromString(String string) {
        string = string.trim();
        string = string.toLowerCase();
        switch (string){
            case "easy": return EASY;
            case "medium": return MEDIUM;
            case "hard": return HARD;
            default:
                throw new IllegalArgumentException("Game difficulty not recognized");
        }
    }
}



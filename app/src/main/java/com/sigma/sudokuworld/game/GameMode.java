package com.sigma.sudokuworld.game;

public enum GameMode {
    NUMBERS,
    NATIVE,
    FOREIGN;

    public static GameMode opposite(GameMode gameMode) {
        if (gameMode == GameMode.NUMBERS) return GameMode.NUMBERS;
        if (gameMode == GameMode.FOREIGN) return GameMode.NATIVE;
        else return GameMode.FOREIGN;
    }

    public static GameMode fromString(String string) {
        string = string.trim();
        string = string.toLowerCase();
        switch (string){
            case "native": return NATIVE;
            case "foreign": return FOREIGN;
            case "numbers": return NUMBERS;
            default:
                throw new IllegalArgumentException("Game Mode not recognized");
        }
    }
}


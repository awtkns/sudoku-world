package com.sigma.sudokuworld;

class PracticeSet {

    private String nativeLanguage;
    private String foreignLanguage;
    private WordPair[] wordPairs;
    private int wordPairsStored;

    PracticeSet(String nativeLanguage, String foreignLanguage, int setSize) {
        this.nativeLanguage = nativeLanguage;
        this.foreignLanguage = foreignLanguage;
        this.wordPairs = new WordPair[setSize];
        this.wordPairsStored = 0;
    }

    boolean addWordPair(WordPair wordPair) {
        if (wordPairsStored < wordPairs.length) {
            wordPairs[wordPairsStored] = wordPair;
            wordPairsStored++;
            return true;
        }

        return false;
    }
}

class WordPair {

    private String nativeWord;
    private String foreignWord;

    WordPair(String nativeWord, String foreignWord) {
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
    }

    public String getNativeWord() {
        return nativeWord;
    }

    public void setNativeWord(String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public String getForeignWord() {
        return foreignWord;
    }

    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }
}


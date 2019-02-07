package com.sigma.sudokuworld;

import java.io.Serializable;

class VocabGameSaveData implements Serializable {
    int [] puzzle;
    int [] solution;
    boolean [] lockedCells;
    String [] fWords;
    String [] nWords;


}

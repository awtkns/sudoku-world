package com.sigma.sudokuworld.persistence.db.views

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import com.sigma.sudokuworld.persistence.db.entities.Language

data class WordPair (
        @ColumnInfo(name = "nativeWord") var nativeWord: String,
        @ColumnInfo(name = "foreignWord") var foreignWord: String,

        @Embedded(prefix = "n_") var nativeLanguage: Language,
        @Embedded(prefix = "f_") var foreignLanguage: Language
)
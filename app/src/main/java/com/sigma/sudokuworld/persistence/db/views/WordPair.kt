package com.sigma.sudokuworld.persistence.db.views

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import com.sigma.sudokuworld.persistence.db.entities.Word

data class WordPair (
        var pairID: Long,
        @Embedded(prefix = "n_") var nativeWord: Word,
        @Embedded(prefix = "f_") var foreignWord: Word,
        @ColumnInfo(name = "n_lang_name")var nativeLanguageName: String,
        @ColumnInfo(name = "f_lang_name")var foreignLanguageName: String
)
package com.sigma.sudokuworld.persistence.db.views

import android.arch.persistence.room.Embedded
import com.sigma.sudokuworld.persistence.db.entities.Set

data class WordSet (
        @Embedded var set: Set = Set(),
        var wordPairs: List<WordPair> = listOf(),
        var nativeLanguageCode: String = "",
        var foreignLanguageCode: String = ""
)
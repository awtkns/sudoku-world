package com.sigma.sudokuworld.persistence.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(foreignKeys = [
    ForeignKey(
            entity = WordPair::class,
            parentColumns = ["wordPairID"],
            childColumns = ["wordPairID"],
            onDelete = CASCADE),
    ForeignKey(
            entity = Set::class,
            parentColumns = ["setID"],
            childColumns = ["setID"],
            onDelete = CASCADE)],
    primaryKeys = ["setID", "wordPairID"],
    tableName = "word_set_cross_reference")
data class WordSet (
        var setID: Int,
        var wordPairID: Int
)
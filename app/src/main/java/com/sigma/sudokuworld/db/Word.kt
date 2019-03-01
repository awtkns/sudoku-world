package com.sigma.sudokuworld.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

/**
 * Each Word has a:
 *      ID
 *      Spelling
 *      An associated language
 */

@Entity(foreignKeys = [ForeignKey(
        entity = Language::class,
        parentColumns = ["languageID"],
        childColumns = ["lid"],
        onDelete = CASCADE)],
        tableName = "words"
)
data class Word (
    @PrimaryKey(autoGenerate = true) var wordID: Int,
    var lid: Int,
    var word:String
)

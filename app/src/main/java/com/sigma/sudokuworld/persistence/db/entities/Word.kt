package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * Each Word has a:
 *      ID
 *      Spelling
 *      An associated language
 */

@Entity(foreignKeys = [
    ForeignKey(
        entity = Language::class,
        parentColumns = ["languageID"],
        childColumns = ["languageID"],
        onDelete = CASCADE)],
    tableName = "words",
    indices = [
        Index(value = ["languageID"]),
        Index(value = ["languageID", "word"], unique = true)]
)
data class Word (
    @PrimaryKey(autoGenerate = true) var wordID: Long = -1,
    var languageID: Long = -1,
    var word:String = ""
)

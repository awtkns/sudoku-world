package com.sigma.sudokuworld.persistence.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Each Language has a:
 *      ID
 *      Name
 *      Code
 */

@Entity(tableName = "languages")
data class Language (
    @PrimaryKey(autoGenerate = true) var languageID: Int,
    var name: String,
    var code: String
)
package com.sigma.sudokuworld.persistence.db.entities

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
    @PrimaryKey(autoGenerate = true) var languageID: Long,
    var name: String,
    var code: String
)
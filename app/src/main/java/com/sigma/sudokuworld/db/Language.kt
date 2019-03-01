package com.sigma.sudokuworld.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "languages")
data class Language (
    @PrimaryKey(autoGenerate = true) var languageID: Int,
    var language: String,
    var code: String
)
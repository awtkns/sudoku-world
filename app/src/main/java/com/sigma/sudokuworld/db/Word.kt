package com.sigma.sudokuworld.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity
data class Word (
    @PrimaryKey(autoGenerate = true) var wordID: Int,
    var word:String,
    @ForeignKey(entity = Language::class, parentColumns = ["languageID"], childColumns = ["lid"]) var lid: Language
)
package com.sigma.sudokuworld.persistence.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sets")
data class Set (
    @PrimaryKey(autoGenerate = true) var setID: Int,
    var name: String,
    var description: String
)
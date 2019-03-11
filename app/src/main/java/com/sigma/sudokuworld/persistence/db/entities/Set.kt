package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sets")
data class Set (
    @PrimaryKey(autoGenerate = true) var setID: Long,
    var name: String,
    var description: String
)
package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Word::class,
        parentColumns = ["wordID"],
        childColumns = ["nativeWordID"],
        onDelete = CASCADE),
    ForeignKey(
        entity = Word::class,
        parentColumns = ["wordID"],
        childColumns = ["foreignWordID"],
        onDelete = CASCADE)],
    tableName = "word_pairs",
    indices = [Index(value = ["foreignWordID"]), Index(value = ["nativeWordID"])]
)
data class Pair (
    @PrimaryKey(autoGenerate = true) var pairID: Long,
    var nativeWordID: Long,
    var foreignWordID: Long
)
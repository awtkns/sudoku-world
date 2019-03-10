package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(foreignKeys = [
    ForeignKey(
            entity = Pair::class,
            parentColumns = ["pairID"],
            childColumns = ["pairID"],
            onDelete = CASCADE),
    ForeignKey(
            entity = Set::class,
            parentColumns = ["setID"],
            childColumns = ["setID"],
            onDelete = CASCADE)],
    primaryKeys = ["setID", "pairID"],
    tableName = "word_set_cross_reference")
data class PairWithSet (
        var setID: Long,
        var pairID: Long
)
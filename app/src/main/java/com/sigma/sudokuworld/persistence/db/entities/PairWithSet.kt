package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(foreignKeys = [
    ForeignKey(
            entity = Pair::class,
            parentColumns = ["wordPairID"],
            childColumns = ["wordPairID"],
            onDelete = CASCADE),
    ForeignKey(
            entity = Set::class,
            parentColumns = ["setID"],
            childColumns = ["setID"],
            onDelete = CASCADE)],
    primaryKeys = ["setID", "wordPairID"],
    tableName = "word_set_cross_reference")
data class PairWithSet (
        var setID: Int,
        var wordPairID: Int
)
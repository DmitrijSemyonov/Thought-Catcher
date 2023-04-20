package com.squalgamesstudio.thoughtcatcher.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thought")
data class ThoughtEntity(
    @PrimaryKey
    val thought: String
)

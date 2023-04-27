package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val tagId: Int = 0,

    @ColumnInfo(name = "tag_name")
    var tagName: String
)

package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tags", indices = [Index(value = ["tag_name"], unique = true)])
data class Tag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    val tagId: Int = 0,

    @ColumnInfo(name = "tag_name")
    var tagName: String
)

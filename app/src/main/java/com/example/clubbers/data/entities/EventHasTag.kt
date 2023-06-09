package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "event_has_tag",
    primaryKeys = ["event_id", "tag_id"],
    foreignKeys = [
        ForeignKey(entity = Event::class,
            parentColumns = arrayOf("event_id"),
            childColumns = arrayOf("event_id"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Tag::class,
            parentColumns = arrayOf("tag_id"),
            childColumns = arrayOf("tag_id"),
            onDelete = ForeignKey.CASCADE)
    ]
)
data class EventHasTag(
    @ColumnInfo(name = "event_id")
    var eventId: Int,

    @ColumnInfo(name = "tag_id")
    var tagId: Int
)

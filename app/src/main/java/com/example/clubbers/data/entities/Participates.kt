package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "participates", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Event::class,
        parentColumns = arrayOf("eventId"),
        childColumns = arrayOf("event_id"),
        onDelete = ForeignKey.CASCADE)
])
data class Participates(
    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "event_id")
    var eventId: Int
)

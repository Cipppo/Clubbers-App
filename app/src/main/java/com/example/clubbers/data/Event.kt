package com.example.clubbers.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    val eventId: String = UUID.randomUUID().toString(),
)

package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events", foreignKeys = [
    ForeignKey(entity = Admin::class,
        parentColumns = arrayOf("admin_id"),
        childColumns = arrayOf("event_admin_id"),
        onDelete = ForeignKey.CASCADE)
])
data class Event(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    val eventId: Int = 0,

    @ColumnInfo(name = "event_name")
    val eventName: String,

    @ColumnInfo(name = "event_image")
    val eventImage: String,

    @ColumnInfo(name = "event_location")
    val eventLocation: String,

    @ColumnInfo(name = "event_location_lat")
    val eventLocationLat: Double,

    @ColumnInfo(name = "event_location_lon")
    val eventLocationLon: Double,

    @ColumnInfo(name = "event_description")
    val eventDescription: String?,

    @ColumnInfo(name = "time_start")
    val timeStart: Date,

    @ColumnInfo(name = "time_end")
    val timeEnd: Date,

    @ColumnInfo(name = "max_participants")
    val maxParticipants: Int?,

    @ColumnInfo(name = "participants")
    var participants: Int = 0,

    @ColumnInfo(name = "event_admin_id")
    val eventAdminId: Int
)

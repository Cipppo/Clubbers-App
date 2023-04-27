package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.entities.Event
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface EventsDAO {
    // Get all events
    @Query("SELECT * FROM events ORDER BY event_name ASC")
    fun getEvents(): Flow<List<Event>>

    // Get event by id
    @Query("SELECT * FROM events WHERE eventId = :eventId")
    fun getEventById(eventId: String): Flow<Event>

    // Insert event
    @Query("INSERT INTO events (eventId, event_name, event_description, time_start, time_end, " +
            "event_location, event_image) " +
            "VALUES (:eventId, :eventName, :eventDescription, :timeStart, :timeEnd, " +
            ":eventLocation, :eventImage)")
    suspend fun insert(eventId: Int, eventName: String, eventDescription: String,
                       timeStart: Date, timeEnd: Date, eventLocation: String, eventImage: String)

    // Delete event
    @Query("DELETE FROM events WHERE eventId = :eventId")
    suspend fun delete(eventId: Int)
}
package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clubbers.data.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDAO {
    // Get all events
    @Query("SELECT * FROM events ORDER BY time_start ASC")
    fun getEvents(): Flow<List<Event>>

    // Get event by id
    @Query("SELECT * FROM events WHERE event_id = :eventId")
    fun getEventById(eventId: Int): Flow<Event>

    // Get events by event name
    @Query("SELECT * FROM events WHERE event_name LIKE '%' || :eventName || '%' ORDER BY time_start ASC")
    fun getEventsByName(eventName: String): Flow<List<Event>>

    // Insert event
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event)

    // Update event
    @Update
    suspend fun update(event: Event)

    // Delete event
    @Query("DELETE FROM events WHERE event_id = :eventId")
    suspend fun delete(eventId: Int)

    // Get future events
    @Query("SELECT * FROM events WHERE time_end > :currentTime ORDER BY time_start ASC")
    fun getFutureEvents(currentTime: Long): Flow<List<Event>>
}
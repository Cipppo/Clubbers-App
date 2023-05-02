package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.Participates
import com.example.clubbers.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipatesDAO {
    // get all participants of an event
    @Query("SELECT * FROM users " +
            "INNER JOIN participates ON users.user_id = participates.user_id " +
            "WHERE participates.event_id = :eventId")
    fun getParticipants(eventId: Int): Flow<List<User>>

    // get all events a user participates in
    @Query("SELECT * FROM events " +
            "INNER JOIN participates ON events.event_id = participates.event_id " +
            "WHERE participates.user_id = :userId")
    fun getEvents(userId: Int): Flow<List<Event>>

    // insert a user to an event
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(participant: Participates)

    // delete a user from an event
    @Delete
    suspend fun delete(participant: Participates)

    // delete all users from an event
    @Query("DELETE FROM participates WHERE event_id = :eventId")
    suspend fun deleteAll(eventId: Int)
}
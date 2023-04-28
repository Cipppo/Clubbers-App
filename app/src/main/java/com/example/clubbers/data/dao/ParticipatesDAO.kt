package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipatesDAO {
    // get all participants of an event
    @Query("SELECT * FROM participates WHERE event_id = :eventId")
    fun getParticipants(eventId: Int): Flow<List<ParticipatesDAO>>

    // get all events a user participates in
    @Query("SELECT * FROM participates WHERE user_id = :userId")
    fun getEvents(userId: Int): Flow<List<ParticipatesDAO>>

    // insert a user to an event
    @Query("INSERT INTO participates (user_id, event_id) VALUES (:userId, :eventId)")
    suspend fun insert(userId: Int, eventId: Int)

    // delete a user from an event
    @Query("DELETE FROM participates WHERE user_id = :userId AND event_id = :eventId")
    suspend fun delete(userId: Int, eventId: Int)

    // delete all users from an event
    @Query("DELETE FROM participates WHERE event_id = :eventId")
    suspend fun deleteAll(eventId: Int)
}
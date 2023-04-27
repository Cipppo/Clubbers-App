package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.clubbers.data.entities.EventHasTag
import kotlinx.coroutines.flow.Flow

@Dao
interface EventHasTagsDAO {
    // Get all tags for an event
    @Query("SELECT * FROM event_has_tag WHERE event_id = :eventId")
    fun getTagsForEvent(eventId: Int): Flow<List<EventHasTag>>

    // Get all events for a tag
    @Query("SELECT * FROM event_has_tag WHERE tag_id = :tagId")
    fun getEventsForTag(tagId: Int): Flow<List<EventHasTag>>

    // Insert tag for an event
    @Query("INSERT INTO event_has_tag (event_id, tag_id) VALUES (:eventId, :tagId)")
    suspend fun insert(eventId: Int, tagId: Int)

    // Insert multiple tags for an event
    @Transaction
    suspend fun insertMultiple(eventId: Int, tagIds: List<Int>) {
        tagIds.forEach { tagId ->
            insert(eventId, tagId)
        }
    }

    // Delete tag for an event
    @Query("DELETE FROM event_has_tag WHERE event_id = :eventId AND tag_id = :tagId")
    suspend fun delete(eventId: Int, tagId: Int)

    // Delete all tags for an event
    @Query("DELETE FROM event_has_tag WHERE event_id = :eventId")
    suspend fun deleteAll(eventId: Int)
}
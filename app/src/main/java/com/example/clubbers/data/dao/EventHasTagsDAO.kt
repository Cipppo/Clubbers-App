package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.data.entities.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface EventHasTagsDAO {
    // Get all tags for an event
    @Query("SELECT * FROM event_has_tag WHERE event_id = :eventId")
    fun getTagsForEvent(eventId: Int): Flow<List<Tag>>

    // Get all events for a tag
    @Query("SELECT * FROM event_has_tag WHERE tag_id = :tagId")
    fun getEventsForTag(tagId: Int): Flow<List<Event>>

    // Insert tag for an event
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(eventHasTag: EventHasTag)

    // Insert multiple tags for an event
    @Transaction
    suspend fun insertMultiple(eventHasTags: List<EventHasTag>) {
        eventHasTags.forEach { eventHasTag ->
            insert(eventHasTag)
        }
    }

    // Delete tag for an event
    @Query("DELETE FROM event_has_tag WHERE event_id = :eventId AND tag_id = :tagId")
    suspend fun delete(eventId: Int, tagId: Int)

    // Delete all tags for an event
    @Query("DELETE FROM event_has_tag WHERE event_id = :eventId")
    suspend fun deleteAll(eventId: Int)
}
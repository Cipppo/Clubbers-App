package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.EventHasTagsDAO
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.data.entities.Tag
import kotlinx.coroutines.flow.Flow

class EventHasTagsRepository(private val eventHasTagsDAO: EventHasTagsDAO) {

    fun getTagsByEventId(eventId: Int): Flow<List<Tag>> {
        return eventHasTagsDAO.getTagsForEvent(eventId)
    }

    fun getTagsForEvents(eventIds: List<Int>): Flow<List<Tag>> {
        return eventHasTagsDAO.getTagsForEvents(eventIds)
    }

    fun getEventsForTagName(tagName: String): Flow<List<Event>> {
        return eventHasTagsDAO.getEventsForTagName(tagName)
    }

    fun getEventsByTagId(tagId: Int): Flow<List<Event>> {
        return eventHasTagsDAO.getEventsForTag(tagId)
    }

    suspend fun insert(eventHasTag: EventHasTag) {
        eventHasTagsDAO.insert(eventHasTag)
    }

    suspend fun insertMultiple(eventHasTags: List<EventHasTag>) {
        eventHasTagsDAO.insertMultiple(eventHasTags)
    }

    suspend fun delete(eventHasTag: EventHasTag) {
        eventHasTagsDAO.delete(eventHasTag)
    }

    suspend fun deleteAll(eventId: Int) {
        eventHasTagsDAO.deleteAll(eventId)
    }
}
package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.EventHasTagsDAO
import com.example.clubbers.data.entities.EventHasTag
import kotlinx.coroutines.flow.Flow

class EventHasTagsRepository(private val eventHasTagsDAO: EventHasTagsDAO) {

    fun getTagsByEventId(eventId: Int): Flow<List<EventHasTag>> {
        return eventHasTagsDAO.getTagsForEvent(eventId)
    }

    fun getEventsByTagId(tagId: Int): Flow<List<EventHasTag>> {
        return eventHasTagsDAO.getEventsForTag(tagId)
    }
}
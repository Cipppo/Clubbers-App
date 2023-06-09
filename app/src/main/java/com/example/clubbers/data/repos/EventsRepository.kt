package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.EventsDAO
import com.example.clubbers.data.entities.Event
import kotlinx.coroutines.flow.Flow

class EventsRepository(private val eventsDAO: EventsDAO) {

    val events = eventsDAO.getEvents()

    suspend fun insertNewEvent(event: Event) {
        eventsDAO.insert(event)
    }

    suspend fun updateEvent(event: Event) {
        eventsDAO.update(event)
    }

    suspend fun deleteEvent(event: Event) {
        eventsDAO.delete(event.eventId)
    }

    fun getEventById(eventId: Int): Flow<Event> {
        return eventsDAO.getEventById(eventId)
    }
}
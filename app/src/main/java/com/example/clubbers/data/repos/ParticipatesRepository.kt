package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.ParticipatesDAO
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.Participates
import com.example.clubbers.data.entities.User
import kotlinx.coroutines.flow.Flow

class ParticipatesRepository(private val participatesDAO: ParticipatesDAO) {

    fun getParticipants(eventId: Int): Flow<List<User>> {
        return participatesDAO.getParticipants(eventId)
    }

    fun getEvents(userId: Int): Flow<List<Event>> {
        return participatesDAO.getEvents(userId)
    }

    suspend fun insert(participates: Participates) {
        participatesDAO.insert(participates)
    }

    suspend fun delete(participates: Participates) {
        participatesDAO.delete(participates)
    }

    suspend fun deleteAll(eventId: Int) {
        participatesDAO.deleteAll(eventId)
    }
}
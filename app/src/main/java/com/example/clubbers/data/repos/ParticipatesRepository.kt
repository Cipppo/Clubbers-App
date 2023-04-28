package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.ParticipatesDAO
import kotlinx.coroutines.flow.Flow

class ParticipatesRepository(private val participatesDAO: ParticipatesDAO) {

    fun getParticipants(eventId: Int): Flow<List<ParticipatesDAO>> {
        return participatesDAO.getParticipants(eventId)
    }

    fun getEvents(userId: Int): Flow<List<ParticipatesDAO>> {
        return participatesDAO.getEvents(userId)
    }

    suspend fun insert(userId: Int, eventId: Int) {
        participatesDAO.insert(userId, eventId)
    }

    suspend fun delete(userId: Int, eventId: Int) {
        participatesDAO.delete(userId, eventId)
    }

    suspend fun deleteAll(eventId: Int) {
        participatesDAO.deleteAll(eventId)
    }
}
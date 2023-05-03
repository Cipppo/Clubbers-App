package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.TagsDAO
import com.example.clubbers.data.entities.Tag
import kotlinx.coroutines.flow.Flow

class TagsRepository(private val tagsDAO: TagsDAO) {

    val tags = tagsDAO.getTags()

    suspend fun insertNewTag(tag: Tag) {
        tagsDAO.insert(tag)
    }

    suspend fun updateTag(tag: Tag) {
        tagsDAO.update(tag)
    }

    suspend fun deleteTag(tag: Tag) {
        tagsDAO.delete(tag.tagId)
    }

    fun getTagById(tagId: Int): Flow<Tag> {
        return tagsDAO.getTagById(tagId)
    }
}
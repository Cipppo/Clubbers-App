package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.entities.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDAO {
    // Get all tags
    @Query("SELECT * FROM tags ORDER BY tag_id ASC")
    fun getTags(): Flow<List<Tag>>

    // Get tag by id
    @Query("SELECT * FROM tags WHERE tag_id = :tagId")
    fun getTagById(tagId: Int): Flow<Tag>

    // Insert tag
    @Query("INSERT INTO tags (tag_id, tag_name) VALUES (:tagId, :tagName)")
    suspend fun insert(tagId: Int, tagName: String)

    // Delete tag
    @Query("DELETE FROM tags WHERE tag_id = :tagId")
    suspend fun delete(tagId: Int)
}
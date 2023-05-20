package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clubbers.data.entities.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDAO {
    // Get all tags
    @Query("SELECT * FROM tags ORDER BY tag_name ASC")
    fun getTags(): Flow<List<Tag>>

    // Get tag by id
    @Query("SELECT * FROM tags WHERE tag_id = :tagId")
    fun getTagById(tagId: Int): Flow<Tag>

    // Insert tag
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: Tag)

    // Update tag
    @Update
    suspend fun update(tag: Tag)

    // Delete tag
    @Query("DELETE FROM tags WHERE tag_id = :tagId")
    suspend fun delete(tagId: Int)
}
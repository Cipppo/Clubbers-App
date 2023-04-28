package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clubbers.data.entities.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDAO {
    // Get all posts
    @Query("SELECT * FROM posts ORDER BY post_id ASC")
    fun getPosts(): Flow<List<Post>>

    // Get post by id
    @Query("SELECT * FROM posts WHERE post_id = :postId")
    fun getPostById(postId: Int): Flow<Post>

    // Get posts by user id
    @Query("SELECT * FROM posts WHERE user_id = :userId")
    fun getPostsByUserId(userId: Int): Flow<List<Post>>

    // Insert post
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    // Update post
    @Update
    suspend fun update(post: Post)

    // Delete post
    @Query("DELETE FROM posts WHERE post_id = :postId")
    suspend fun delete(postId: Int)
}
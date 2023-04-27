package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.entities.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDAO {
    // Get all posts
    @Query("SELECT * FROM posts ORDER BY postId ASC")
    fun getPosts(): Flow<List<Post>>

    // Get post by id
    @Query("SELECT * FROM posts WHERE postId = :postId")
    fun getPostById(postId: Int): Flow<Post>

    // Insert post
    @Query("INSERT INTO posts (postId, post_title, post_image, post_caption, post_user_id, post_event_id) " +
            "VALUES (:postId, :postTitle, :postImage, :postCaption, :postUserId, :postEventId)")
    suspend fun insert(postId: Int, postTitle: String, postImage: String, postCaption: String,
                       postUserId: Int, postEventId: Int)

    // Delete post
    @Query("DELETE FROM posts WHERE postId = :postId")
    suspend fun delete(postId: Int)
}
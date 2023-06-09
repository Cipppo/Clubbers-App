package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.PostsDAO
import com.example.clubbers.data.entities.Post
import kotlinx.coroutines.flow.Flow

class PostsRepository(private val postsDAO: PostsDAO) {

    val posts = postsDAO.getPosts()

    suspend fun insertNewPost(post: Post) {
        postsDAO.insert(post)
    }

    suspend fun updatePost(post: Post) {
        postsDAO.update(post)
    }

    suspend fun deletePost(post: Post) {
        postsDAO.delete(post.postId)
    }

    fun getPostById(postId: Int): Flow<Post> {
        return postsDAO.getPostById(postId)
    }

    fun getPostsByUserId(userId: Int): Flow<List<Post>> {
        return postsDAO.getPostsByUserId(userId)
    }
}
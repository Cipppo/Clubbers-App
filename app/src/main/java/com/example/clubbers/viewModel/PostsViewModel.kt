package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Post
import com.example.clubbers.data.repos.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostsRepository
) : ViewModel() {

    val posts = repository.posts

    fun getPostsByUserId(userId: Int): Flow<List<Post>> = repository.getPostsByUserId(userId)

    fun getPostById(postId: Int): Flow<Post> = repository.getPostById(postId)

    fun addNewPost(post: Post) = viewModelScope.launch {
        repository.insertNewPost(post)
    }

    private var _postSelected: Post? = null
    val postSelected
        get() = _postSelected

    fun selectPost(post: Post) {
        _postSelected = post
    }

    fun updatePost(post: Post) = viewModelScope.launch {
        repository.updatePost(post)
    }

    fun deletePost(post: Post) = viewModelScope.launch {
        repository.deletePost(post)
    }
}
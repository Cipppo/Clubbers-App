package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Post
import com.example.clubbers.data.repos.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostsRepository
) : ViewModel() {

    private var _postSelected: Post? = null
    val postSelected
        get() = _postSelected

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts


    val allPosts: StateFlow<List<Post>> get () = _allPost

    val _allPost = MutableStateFlow<List<Post>>(emptyList())

    fun getAllPosts() = viewModelScope.launch {
        repository.getAllPosts()
            .collect{ posts ->
                _allPost.value = posts
            }
    }

    fun getPostsByUserId(userId: Int) = viewModelScope.launch {
        repository.getPostsByUserId(userId)
            .collect { posts ->
                _posts.value = posts
            }
    }



    fun getPostById(postId: Int) = viewModelScope.launch {
        repository.getPostById(postId)
            .collect { post ->
                _postSelected = post
            }
    }

    fun getPostsByEventId(eventId: Int) = viewModelScope.launch {
        repository.getPostsByEventId(eventId)
            .collect { posts ->
                _posts.value = posts
            }
    }

    fun addNewPost(post: Post) = viewModelScope.launch {
        repository.insertNewPost(post)
    }

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
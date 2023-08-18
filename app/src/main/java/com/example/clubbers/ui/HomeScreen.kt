package com.example.clubbers.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.clubbers.data.entities.Post
import com.example.clubbers.data.entities.User

import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    postsViewModel: PostsViewModel,
    eventsViewModel: EventsViewModel,
    usersViewModel: UsersViewModel,
    userFollowsUsersViewModel: UserFollowsUsersViewModel,
    user: User

) {
    val sharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
    val userMail = sharedPreferences.getString("USER_LOGGED", "None")

    usersViewModel.getUserIdByEmail(userMail.orEmpty())

    val userId = user.userId

    userFollowsUsersViewModel.getFollowed(userId)
    val userFollowed = userFollowsUsersViewModel.followed.collectAsState().value
    val userIdList = userFollowed.map { user -> user.userId }

    postsViewModel.getAllPosts()
    val posts = postsViewModel.allPost.collectAsState(initial = listOf()).value

    var postList = emptyList<Post>()

    for(post in posts){
        if(userIdList.contains(post.postUserId)){
            postList = postList + post
        }
    }

    Log.d("2", "2")
}
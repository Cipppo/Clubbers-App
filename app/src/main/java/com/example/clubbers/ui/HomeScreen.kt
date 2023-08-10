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
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    usersViewModel: UsersViewModel,
    usersFollowsUsersViewModel: UserFollowsUsersViewModel,
    postsViewModel: PostsViewModel,
    eventsViewModel: EventsViewModel,
) {
    val sharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
    val userLogged = sharedPreferences.getString("USER_LOGGED", "None")
    Scaffold { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            // Text at the center of the screen
            Text(text = "Home Screen Current User: $userLogged")
            postsViewModel.getAllPosts()
            var posts = postsViewModel.allPosts.collectAsState(initial = listOf()).value
            Log.d("sdjisjd", "ceicme")
        }
    }
}
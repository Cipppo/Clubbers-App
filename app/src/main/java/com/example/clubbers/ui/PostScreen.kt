package com.example.clubbers.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.PostItem
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    modifier: Modifier = Modifier,
    postsViewModel: PostsViewModel,
    usersViewModel: UsersViewModel
) {
    val post = postsViewModel.postSelected

    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                PostItem(
                    usersViewModel = usersViewModel,
                    postsViewModel = postsViewModel,
                    isSinglePost = true,
                    post = post!!,
                    onClickAction = { }
                )
            }
        }
    }
}
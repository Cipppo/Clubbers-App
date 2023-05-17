package com.example.clubbers.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.EventItem
import com.example.clubbers.utilities.PostItem
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    eventsViewModel: EventsViewModel,
    postsViewModel: PostsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel,
    onClickAction: () -> Unit
) {
    val event = eventsViewModel.eventSelected
    postsViewModel.getPostsByEventId(event!!.eventId)
    val posts by postsViewModel.posts.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                EventItem(
                    eventsViewModel = eventsViewModel,
                    adminsViewModel = adminsViewModel,
                    eventHasTagsViewModel = eventHasTagsViewModel,
                    event = event,
                    participatesViewModel = participatesViewModel,
                    usersViewModel = usersViewModel,
                    isSingleEvent = true,
                    onClickAction = { }
                )
            }
            items(posts) { post ->
                PostItem(
                    usersViewModel = usersViewModel,
                    postsViewModel = postsViewModel,
                    post = post,
                    isSinglePost = false,
                    onClickAction = onClickAction
                )
            }
        }
    }
}
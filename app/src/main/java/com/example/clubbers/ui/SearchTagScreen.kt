package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateSearchTimeLine
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.TagsViewModel
import com.example.clubbers.viewModel.UsersViewModel

@Composable
fun SearchTagScreen(
    modifier: Modifier = Modifier,
    onEventClicked: () -> Unit,
    onSearchAction: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    tagsViewModel: TagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel
) {
    CreateSearchTimeLine(
        modifier = modifier,
        onClickAction = onEventClicked,
        onSearchAction = onSearchAction,
        eventsViewModel = eventsViewModel,
        adminsViewModel = adminsViewModel,
        eventHasTagsViewModel = eventHasTagsViewModel,
        participatesViewModel = participatesViewModel,
        usersViewModel = usersViewModel,
        tagsViewModel = tagsViewModel,
        isTodayEvents = false,
        searchingTags = true
    )
}
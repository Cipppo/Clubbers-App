package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateSearchTimeLine
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel

@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    onEventClicked: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel
) {
    /**
     * TODO: The screen should display the events for today
     *  When there will be real posts, modify this function
     */
    CreateSearchTimeLine(
        modifier = modifier,
        onClickAction = onEventClicked,
        eventsViewModel = eventsViewModel,
        adminsViewModel = adminsViewModel,
        eventHasTagsViewModel = eventHasTagsViewModel
    )
}
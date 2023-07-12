package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateParticipatedEventTimeLine
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.NotificationsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.UsersViewModel

@Composable
fun FoundEventsScreen(
    modifier: Modifier = Modifier,
    onEventSelected: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel,
    notificationsViewModel: NotificationsViewModel
) {
    val events by eventsViewModel.foundEvents.collectAsState()
    CreateParticipatedEventTimeLine(
        modifier = modifier,
        onClickAction = onEventSelected,
        eventsViewModel = eventsViewModel,
        adminsViewModel = adminsViewModel,
        eventHasTagsViewModel = eventHasTagsViewModel,
        participatesViewModel = participatesViewModel,
        passedEvents = events,
        usersViewModel = usersViewModel,
        notificationsViewModel = notificationsViewModel
    )
}
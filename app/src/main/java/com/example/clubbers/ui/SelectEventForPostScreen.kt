package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateParticipatedEventTimeLine
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.UsersViewModel

@Composable
fun SelectEventForPostScreen(
    modifier: Modifier = Modifier,
    onEventSelected: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel
) {
    CreateParticipatedEventTimeLine(
        modifier = modifier,
        onClickAction = onEventSelected,
        eventsViewModel = eventsViewModel,
        adminsViewModel = adminsViewModel,
        eventHasTagsViewModel = eventHasTagsViewModel,
        participatesViewModel = participatesViewModel,
        usersViewModel = usersViewModel
    )
}
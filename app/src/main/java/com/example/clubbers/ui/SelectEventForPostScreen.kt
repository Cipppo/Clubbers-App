package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateParticipatedEventTimeLine
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.UsersViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SelectEventForPostScreen(
    modifier: Modifier = Modifier,
    onEventSelected: () -> Unit,
    userId: Int,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel
) {
    participatesViewModel.getEvents(userId)
    val events by participatesViewModel.events.collectAsState()

    val postEvents = events.filter { event ->
        val timeStart = event.timeStart
        val timeEnd = event.timeEnd
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val eventStart = sdf.format(timeStart)
        val eventEnd = sdf.format(timeEnd)
        val currentDate = sdf.format(System.currentTimeMillis())
        currentDate in eventStart..eventEnd
    }

    CreateParticipatedEventTimeLine(
        modifier = modifier,
        onClickAction = onEventSelected,
        eventsViewModel = eventsViewModel,
        adminsViewModel = adminsViewModel,
        eventHasTagsViewModel = eventHasTagsViewModel,
        participatesViewModel = participatesViewModel,
        usersViewModel = usersViewModel,
        passedEvents = postEvents
    )
}
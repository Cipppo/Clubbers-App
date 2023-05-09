package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateParticipatedEventTimeLine

@Composable
fun SelectEventForPostScreen(
    modifier: Modifier = Modifier,
    onEventSelected: () -> Unit
) {
    CreateParticipatedEventTimeLine(
        modifier = modifier,
        onClickAction = onEventSelected
    )
}
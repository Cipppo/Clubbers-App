package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateSearchTimeLine

@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    onEventClicked: () -> Unit
) {
    /**
     * TODO: The screen should display the events for today
     *  When there will be real posts, modify this function
     */
    CreateSearchTimeLine(
        modifier = modifier,
        onClickAction = onEventClicked
    )
}
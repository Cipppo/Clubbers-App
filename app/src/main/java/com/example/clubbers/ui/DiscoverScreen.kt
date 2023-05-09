package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateSearchTimeLine

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    onEventClicked: () -> Unit,
) {
    CreateSearchTimeLine(
        modifier = modifier,
        onClickAction = onEventClicked
    )
}
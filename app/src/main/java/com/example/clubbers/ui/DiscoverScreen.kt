package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.CreateSearchTimeLine
import com.example.clubbers.viewModel.UsersViewModel

@Composable
fun DiscoverScreen(
    usersViewModel: UsersViewModel,
    modifier: Modifier = Modifier
) {
    CreateSearchTimeLine(modifier = modifier)
}
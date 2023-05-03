package com.example.clubbers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubbers.utilities.TakePhoto

@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier
) {
    TakePhoto()
}
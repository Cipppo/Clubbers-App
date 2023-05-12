package com.example.clubbers.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.clubbers.viewModel.WarningViewModel


@Composable
fun ConnectivitySnackBarComposable(
    snackBarHostState: SnackbarHostState,
    applicationContext: Context,
    warningViewModel: WarningViewModel
) {
    LaunchedEffect(snackBarHostState) {
        val result = snackBarHostState.showSnackbar(
            message = "No Internet available",
            actionLabel = "Go to settings",
            duration = SnackbarDuration.Indefinite
        )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                if (intent.resolveActivity(applicationContext.packageManager) != null) {
                    applicationContext.startActivity(intent)
                }
            }
            SnackbarResult.Dismissed -> {
                warningViewModel.setConnectivitySnackBarVisibility(false)
            }
        }
    }
}

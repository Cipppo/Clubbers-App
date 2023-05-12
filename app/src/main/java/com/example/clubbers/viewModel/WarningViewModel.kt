package com.example.clubbers.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class WarningViewModel: ViewModel() {
    private var _showPermissionSnackBar = mutableStateOf(false)
    val showPermissionSnackBar
        get() = _showPermissionSnackBar

    private var _showGPSAlertDialog = mutableStateOf(false)
    val showGPSAlertDialog
        get() = _showGPSAlertDialog

    private var _showConnectivitySnackBar = mutableStateOf(false)
    val showConnectivitySnackBar
        get() = _showConnectivitySnackBar


    fun setPermissionSnackBarVisibility(visible: Boolean) {
        _showPermissionSnackBar.value = visible
    }

    fun setGPSAlertDialogVisibility(visible: Boolean) {
        _showGPSAlertDialog.value = visible
    }

    fun setConnectivitySnackBarVisibility(visible: Boolean) {
        _showConnectivitySnackBar.value = visible
    }
}

@Composable
internal fun PermissionSnackBarComposable(
    snackBarHostState: SnackbarHostState,
    applicationContext: Context,
    warningViewModel: WarningViewModel
) {
    LaunchedEffect(snackBarHostState) {
        val result = snackBarHostState.showSnackbar(
            message = "Permission are needed to get your position",
            actionLabel = "Go to settings"
        )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", applicationContext.packageName, null)
                }
                if (intent.resolveActivity(applicationContext.packageManager) != null) {
                    applicationContext.startActivity(intent)
                }
            }
            SnackbarResult.Dismissed -> {
                warningViewModel.setPermissionSnackBarVisibility(false)
            }
        }
    }
}
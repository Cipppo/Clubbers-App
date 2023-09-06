package com.example.clubbers.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.clubbers.R
import com.example.clubbers.utilities.PicOrGalleyChoicePopup
import com.example.clubbers.utilities.createImageFile
import com.example.clubbers.utilities.saveImage
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import java.util.Objects


@Composable
fun UserOptionScreen(
    onLogout : () -> Unit
){

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
    val photoType = "ProPic"

    val capturedImageUri = remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val file = LocalContext.current.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file)
    val takenPhotoState = rememberSheetState()
    val picOrGalleryState = rememberSheetState()

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri.value = uri
        } else {
            // Handle cancellation event
            Toast.makeText(context, "Camera cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { newUri: Uri? ->
        if (newUri != null) {
            capturedImageUri.value = newUri
        } else {
            // Handle cancellation event
            Toast.makeText(context, "Camera cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }





    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
            .clickable { picOrGalleryState.show()
                       changeProPic(context = context, capturedImageUri = capturedImageUri.value, photoType)
            },
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "Change Propic",
                modifier = Modifier.size(35.dp)
            )
            Text(
                "Change your profile picture",
                modifier = Modifier.padding(16.dp),
                fontSize = 25.sp
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
            .clickable { logout(onLogout, sharedPreferences) },
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.logout_icon),
                contentDescription = "Logout",
                modifier = Modifier.size(35.dp)
            )
            Text("Logout", modifier = Modifier.padding(16.dp), fontSize = 25.sp)
        }


        PicOrGalleyChoicePopup(
            context = context,
            uri = uri,
            galleryLauncher = galleryLauncher,
            cameraLauncher = cameraLauncher,
            permissionLauncher = permissionLauncher,
            title = "choose What to do",
            sheetState = picOrGalleryState
        )
    }
}


fun logout(onLogout: () -> Unit, sharedPreferences: SharedPreferences) {
    val editor = sharedPreferences.edit()
    editor.remove("USER_LOGGED")
    editor.apply()
    onLogout()
}

fun changeProPic(context: Context, capturedImageUri: Uri, photoType: String){
    saveImage(context = context, context.applicationContext.contentResolver, capturedImageUri = capturedImageUri, photoType)

}

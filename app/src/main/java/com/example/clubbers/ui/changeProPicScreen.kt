package com.example.clubbers.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.utilities.PicOrGalleyChoicePopup
import com.example.clubbers.utilities.createImageFile
import com.example.clubbers.utilities.saveImage
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import java.util.Objects

@Composable
fun changeProPicScreen(){

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



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(capturedImageUri.value.path?.isNotEmpty() == true){
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = capturedImageUri.value)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_foreground)
                                error(R.drawable.ic_launcher_foreground)
                            }).build()
                    ),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            )
                            if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                                cameraLauncher.launch(uri)
                            }else{
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.default_avatar),
                    contentDescription = "DefaultAvatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable(
                            onClick = {
                                val permissionCheckResult = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                                if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                                    picOrGalleryState.show()
                                }else{
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        )
                )
            }

            Text(text = "Scatta o scegli una foto")
            if(capturedImageUri.value.path?.isNotEmpty() == true){
                Button(onClick = { saveProPic(context = context, capturedImageUri = capturedImageUri.value, photoType) }){
                    Text("Ok")
                }
            }
            PicOrGalleyChoicePopup(
                context = context,
                uri = uri,
                galleryLauncher = galleryLauncher,
                cameraLauncher = cameraLauncher,
                permissionLauncher = permissionLauncher,
                title = "Choose what to do",
                sheetState = picOrGalleryState
            )
        }
    }
}


fun saveProPic(context: Context, capturedImageUri: Uri, photoType: String){
    saveImage(context = context, context.contentResolver, capturedImageUri = capturedImageUri, photoType)
    val sharedPreferences = context.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
    val userMail = sharedPreferences.getString("USER_LOGGED", "None")
    val propic = sharedPreferences.getString("${userMail}_ACTUAL_PROPIC", "None")
    Log.d("propic", propic.toString())
}
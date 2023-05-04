package com.example.clubbers.utilities

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@Composable
fun TakePhoto() {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri = uri
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

    LaunchedEffect(Unit) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    if (capturedImageUri.path?.isNotEmpty() == true) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(capturedImageUri)
                .crossfade(true)
                .build(),
            contentDescription = "image taken"
        )
        saveImage(context, context.applicationContext.contentResolver, capturedImageUri)
    }
}

// Debug function to get all files from the app directory
fun Context.getFilesFromAppDir(): Array<out File>? {
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return storageDir?.listFiles()
}

/**
 * TODO: When the login is implemented, save the image to the user's directory
 */
private fun saveImage(context: Context, contentResolver: ContentResolver, capturedImageUri: Uri) {
    val bitmap = getBitmap(capturedImageUri, contentResolver)

    // Save image to gallery
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DISPLAY_NAME, "Image_" + SystemClock.currentThreadTimeMillis())

    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    val outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream?.close()

    // Save image to app directory
    val appDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(appDir, "Image_" + SystemClock.currentThreadTimeMillis() + ".jpg")
    val fileOutputStream = file.outputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    fileOutputStream.close()
}

private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

private fun getBitmap(selectedPhotoUri: Uri, contentResolver: ContentResolver): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
            contentResolver,
            selectedPhotoUri
        )
        else -> {
            val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}
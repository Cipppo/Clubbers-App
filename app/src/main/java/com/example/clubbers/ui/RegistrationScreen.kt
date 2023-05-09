package com.example.clubbers.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Paint
import android.media.Image
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.entities.User
import com.example.clubbers.utilities.createImageFile
import com.example.clubbers.viewModel.UsersViewModel
import java.util.Objects
import android.Manifest
import com.example.clubbers.utilities.saveImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    usersViewModel: UsersViewModel,
    onRegister: () -> Unit,
){
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val firstName = remember { mutableStateOf(TextFieldValue()) }
        val secondName = remember { mutableStateOf(TextFieldValue()) }
        val email = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val passwordConf = remember { mutableStateOf(TextFieldValue()) }

        val sharedPreferences =
            LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)

        val step1 = remember { mutableStateOf(true) }
        val step2 = remember { mutableStateOf(false) }

        val bioText = remember { mutableStateOf(TextFieldValue("")) }
        val username = remember { mutableStateOf(TextFieldValue("")) }

        val MAX_BIO = 50

        val context = LocalContext.current

        val file = LocalContext.current.createImageFile()
        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(LocalContext.current),
            LocalContext.current.packageName + ".provider", file
        )

        val capturedImageUri = remember {
            mutableStateOf<Uri>(Uri.EMPTY)
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ){success ->
            if(success){
                capturedImageUri.value = uri
            }
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted ->
            if (isGranted) {
                capturedImageUri.value = uri
            }else{
                Toast.makeText(context, "Camera cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        if (step1.value) {
            Text(
                text = "Are you ready ?",
                style = TextStyle(
                    fontSize = 30.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    label = { Text(text = "First Name") },
                    value = firstName.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    onValueChange = { firstName.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                TextField(
                    label = { Text(text = "Second Name") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    value = secondName.value,
                    onValueChange = { secondName.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                value = email.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { email.value = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                value = password.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { password.value = it },
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                value = passwordConf.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { value ->
                    passwordConf.value = value
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    step1.value = false
                    step2.value = true
                },
                enabled = entriesCheck(
                    firstName.value.text,
                    secondName.value.text,
                    email.value.text,
                    password.value.text,
                    passwordConf.value.text
                )
            ) {
                Text("Continue")
            }
        }
        if(step2.value){
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
                                            cameraLauncher.launch(uri)
                                        }else{
                                            permissionLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                    }
                                )
                        )
                    }

                    Text(text = "ScattaUnaFoto")
                }
            }
            OutlinedTextField(
                label = { Text(text = "Tell us something about you !") },
                value = bioText.value,
                onValueChange = { newText ->
                    if (newText.text.length <= MAX_BIO) {
                        bioText.value = newText
                    }
                },
                trailingIcon = {
                    val charCount = bioText.value.text.length
                    val charCountLabel = "$charCount/$MAX_BIO"
                    IconButton(
                        onClick = {/*Nothing to do here*/},
                        content = {
                            Text(charCountLabel)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                label = { Text(text = "Choose your Username !") },
                value = username.value,
                onValueChange = {username.value = it},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { registerNewUser(
                    firstName.value.text,
                    secondName.value.text,
                    username.value.text,
                    email.value.text,
                    password.value.text,
                    capturedImageUri.value.path.orEmpty(),
                    bioText.value.text,
                    usersViewModel,
                    onRegister,
                    sharedPreferences
                )
                          if (capturedImageUri.value.path?.isNotEmpty() == true){
                              saveImage(context, context.applicationContext.contentResolver, capturedImageUri.value, "ProPic")
                          }},
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                ),
                enabled = secondStageEntriesCheck(bioText.value.text, username.value.text)
            ) {
                Text(text = "Register Now !")
            }
        }
    }
}

fun entriesCheck(firstname: String, secondName: String, email: String, password: String, confirmPassword: String): Boolean{
    if(firstname.isNotBlank() && secondName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword){
        return true
    }
    return false
}

fun secondStageEntriesCheck(bioText: String, usernameText: String): Boolean{
    return bioText.isNotBlank() && usernameText.isNotBlank()
}

fun registerNewUser(firstname: String, secondName: String, username: String, email: String, password: String,userProPicPath: String, userBio: String, usersViewModel: UsersViewModel, onRegister: () -> Unit, sharedPreferences: SharedPreferences): Unit{
    val newUser = User(
        0,
        firstname,
        secondName,
        username,
        email,
        password,
        userProPicPath,
        userBio,
        false
    )
    usersViewModel.addNewUser(newUser)
    with(sharedPreferences.edit()) {
        putString("USER_LOGGED", email)
        apply()
    }
    onRegister()
    Log.d("REGNEWUSER", "Nuovo utente registrato")
}
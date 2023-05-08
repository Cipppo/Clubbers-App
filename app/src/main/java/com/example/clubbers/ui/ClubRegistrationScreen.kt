@file:Suppress("DEPRECATION")

package com.example.clubbers.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.RestrictionsManager.RESULT_ERROR
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubbers.R
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.UsersViewModel
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import java.util.Locale




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubRegistrationScreen(
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        val username = remember { mutableStateOf(TextFieldValue("")) }
        val email = remember { mutableStateOf(TextFieldValue("")) }
        val password = remember { mutableStateOf(TextFieldValue("")) }
        val checkPassword = remember { mutableStateOf(TextFieldValue("")) }

        val step1 = remember { mutableStateOf(true) }
        val step2 = remember { mutableStateOf(false) }
        val step3 = remember { mutableStateOf(false) }

        val sharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)


        if (step1.value) {
            Text("A new dimension to Club!")
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "What's your club name ?") },
                value = username.value,
                onValueChange = {
                    username.value = it
                })
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "What's your club email ?") },
                value = email.value,
                onValueChange = {
                    email.value = it
                })
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Select a password ") },
                value = password.value,
                onValueChange = {
                    password.value = it
                })
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Confirm your password") },
                value = checkPassword.value,
                onValueChange = {
                    checkPassword.value = it
                })
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    step1.value = false
                    step2.value = true
                },
                enabled = entriesCheck(
                    username.value.text,
                    email.value.text,
                    password.value.text,
                    checkPassword.value.text
                )
            ) {
                Text("Continue")
            }
        }
        if (step2.value) {

            val city = remember { mutableStateOf(TextFieldValue("")) }
            val via = remember { mutableStateOf(TextFieldValue("")) }
            val number = remember { mutableStateOf(TextFieldValue("")) }
            val cap = remember { mutableStateOf(TextFieldValue("")) }

            Text("Provide us your club Location !")
            OutlinedTextField(
                label = { Text(text = "Your Street address") },
                value = via.value,
                onValueChange = {via.value = it},
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                OutlinedTextField(
                    label = { Text(text = "Building number") },
                    value = number.value,
                    onValueChange = {number.value = it},
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    label = { Text(text = "CAP") },
                    value = cap.value,
                    onValueChange = {cap.value = it},
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
            }
            OutlinedTextField(
                label = { Text(text = "City") },
                value = city.value,
                onValueChange = {city.value = it},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    step2.value = false
                    step3.value = true
                },
                enabled = entriesCheckPhase2(
                    city.value.text,
                    via.value.text,
                    number.value.text,
                    cap.value.text
                )
            ) {
                Text("Final Part !")
            }
        }
        if(step3.value){


            val bioText = remember { mutableStateOf(TextFieldValue("")) }

            val MAX_BIO = 50

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
                    Image(
                        painter = painterResource(R.drawable.default_avatar),
                        contentDescription = "DefaultAvatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { /* TODO Need to call the upload a logo feature*/ }
                    )
                    Text(text = "ScattaUnaFoto")
                }
            }
            OutlinedTextField(
                label = { Text(text = "Explain your club genre, purpose, ecc. ecc.") },
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
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


fun entriesCheck(username: String, email: String, password: String, checkPassword: String): Boolean{
    if(username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && checkPassword.isNotBlank() && password == checkPassword){
        return true
    }
    return false
}

fun entriesCheckPhase2(city: String, via: String, number: String, cap: String): Boolean{
    if(city.isNotBlank() && via.isNotBlank() && number.isNotBlank() && cap.isNotBlank()){
        return true
    }
    return false
}

fun registerNewAdmin(username: String, email: String, password: String, image: String, userBio: String, address: String, adminsViewModel: AdminsViewModel, onRegister: () -> Unit, sharedPreferences: SharedPreferences): Unit{
    val newAdmin = Admin(
        0,
        username,
        email,
        password,
        image,
        userBio,
        address,
        true
    )
    adminsViewModel.addNewAdmin(newAdmin)
    with(sharedPreferences.edit()) {
        putString("USER_LOGGED", email)
        apply()
    }
    onRegister()
    Log.d("REGNEWUSER", "Nuovo utente registrato")
}





package com.example.clubbers.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun RegistrationScreen(){
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val firstName = remember { mutableStateOf(TextFieldValue()) }
        val secondName = remember { mutableStateOf(TextFieldValue()) }
        val email = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val passwordConf = remember { mutableStateOf(TextFieldValue()) }
        var isButtonEnabled = remember { mutableStateOf(true) }

        LaunchedEffect(
            firstName,
            secondName,
            email,
            password,
            passwordConf,
        ) {
            isButtonEnabled = mutableStateOf(
                    firstName.value.text.isNotEmpty() &&
                            secondName.value.text.isNotEmpty() &&
                            email.value.text.isNotEmpty() &&
                            password.value.text.isNotEmpty() &&
                            passwordConf.value.text.isNotEmpty()
                    )
        }

        Text(
            text = "Are you ready ?",
            style = TextStyle(
                fontSize = 30.sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "First Name") },
            value = firstName.value,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            onValueChange = {firstName.value = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Second Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            value = secondName.value,
            onValueChange = {secondName.value = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Email")},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            value = email.value,
            onValueChange = {email.value = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Send),
            value = password.value,
            onValueChange = {password.value = it},
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = {Text(text = "Confirm Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Send),
            value = passwordConf.value,
            onValueChange = {passwordConf.value = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { Log.d("SENDBUTTON", firstName.value.text.toString()) },
            enabled = EntriesCheck(firstName.value.text, secondName.value.text, email.value.text, password.value.text, passwordConf.value.text)
        ) {
           Text("Register")
        }
    }
}

fun EntriesCheck(firstname: String, secondName: String, email: String, password: String, confirmPassword: String): Boolean{
    if(firstname.isNotBlank() && secondName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()){
        return true
    }
    return false
}
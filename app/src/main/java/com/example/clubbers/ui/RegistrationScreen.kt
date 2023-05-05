package com.example.clubbers.ui

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubbers.R


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
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(R.drawable.default_avatar),
                    contentDescription = "DefaultAvatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Text(text = "ScattaUnaFoto")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextField(
                label = { Text(text = "First Name") },
                value = firstName.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                onValueChange = {firstName.value = it},
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            TextField(
                label = { Text(text = "Second Name") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                value = secondName.value,
                onValueChange = {secondName.value = it},
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            label = { Text(text = "Email")},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            value = email.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {email.value = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Send),
            value = password.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {password.value = it},
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = {Text(text = "Confirm Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Send),
            value = passwordConf.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {value ->
                passwordConf.value = value
            }
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
package com.example.clubbers.ui

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.clubbers.R
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.UsersViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    switchToRegister: () -> Unit,
    switchToAdminRegister: () -> Unit,
    onLogin: () -> Unit,
    usersViewModel: UsersViewModel,
    adminsViewModel: AdminsViewModel
){

    val context = LocalContext.current

    var hasNotificationPermission by remember {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        }else mutableStateOf(true)

    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission() ,
        onResult = {isGranted ->
            hasNotificationPermission = isGranted
        })

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.backpic),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val usersList = usersViewModel.getAllUsers().collectAsState(initial = listOf()).value
        val adminList = adminsViewModel.getAllAdmins().collectAsState(initial = listOf()).value
        val sharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)



        Text(
            text = "Welcome Clubber!",
            style = TextStyle(
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.surface
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Email") },
            value = username.value,
            onValueChange = {
                username.value = it
            })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    attemptLogin(username.value.text, password.value.text, usersList, onLogin, sharedPreferences, adminList, context) },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("New to Clubbing ? Please, register yourself! "),
            onClick = {switchToRegister()},
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Are you a club ? Join the community!"),
            onClick = {switchToAdminRegister()},
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface,
            ),
        )
    }
}




fun attemptLogin(email: String, password: String, usersList: List<User>, navigateToHome: () -> Unit, sharedPreferences: SharedPreferences, adminList: List<Admin>, context: Context): Unit{
    var i = usersList.size
    for(user in usersList){
        if(user.userEmail == email && user.userPassword == password){
            with(sharedPreferences.edit()){
                putString("USER_LOGGED", email)
                putString("USER_TYPE", "USER")
                apply()
            }
            navigateToHome()

        }
    }
    for(admin in adminList){
        if(admin.adminEmail == email && admin.adminPassword == password){
            with(sharedPreferences.edit()){
                putString("USER_LOGGED", email)
                putString("USER_TYPE", "CLUB")
                apply()
            }
            navigateToHome()
        }
    }
}





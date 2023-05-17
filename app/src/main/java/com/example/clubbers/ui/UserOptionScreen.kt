package com.example.clubbers.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubbers.R


@Composable
fun UserOptionScreen(
    onLogout : () -> Unit
){
    val sharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)

    Column(modifier = Modifier.fillMaxSize()) {
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
    }
}


fun logout(onLogout: () -> Unit, sharedPreferences: SharedPreferences) {
    val editor = sharedPreferences.edit()
    editor.remove("USER_LOGGED")
    editor.apply()
    onLogout()
}
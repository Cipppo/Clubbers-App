package com.example.clubbers.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.clubbers.R


@Composable
fun userOptionScreen(
    onLogout : () -> Unit
){

    val SharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
            .clickable { logout(onLogout, SharedPreferences) },
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.logout_icon),
                contentDescription = "Logout",
                modifier = Modifier.size(35.dp)
            )
            Text("Logout", modifier = Modifier.padding(16.dp), fontSize = 25.sp)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)) {
            Text("Opzione2", modifier = Modifier.padding(16.dp), fontSize = 25.sp)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)) {
            Text("Opzione3", modifier = Modifier.padding(16.dp), fontSize = 25.sp)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)) {
            Text("Opzione4", modifier = Modifier.padding(16.dp), fontSize = 25.sp)
        }
    }
}


fun logout(onLogout: () -> Unit, sharedPreferences: SharedPreferences): Unit{
    val editor = sharedPreferences.edit()
    editor.putString("USER_LOGGED", "null")
    editor.apply()
    onLogout()
}
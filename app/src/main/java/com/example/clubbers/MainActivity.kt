package com.example.clubbers

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.clubbers.ui.theme.ClubbersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login);

        var username = findViewById<EditText>(R.id.emailEntry);
        var password = findViewById<EditText>(R.id.passwordEntry);
        var loginButton = findViewById<Button>(R.id.loginButton);
        var logtext = findViewById<TextView>(R.id.logfield);

        loginButton.setOnClickListener(){
            val username = username.text;
            val password = password.text;

            //Need to change this with the credentials control in the database
            //Just a try
            if(isEmpty(username) || isEmpty(password)){
                logtext.text = "Empty";
            }else{
                logtext.text = "NotEmpty";
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClubbersTheme {
        Greeting("Android")
    }
}
package com.team13.fooriend.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(){
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Log In Screen")
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = id, onValueChange = idValue)
        TextField(value = password, onValueChange = passwordValue)
        Button(onClick = {
            if(id.isNotEmpty() && password.isNotEmpty()){
                // TODO
            }
        }) {
            Text("Log In")
        }
        Button(onClick = {
            // TODO
        }) {
            Text("Sign Up")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogInScreenPreview(){
    LogInScreen()
}
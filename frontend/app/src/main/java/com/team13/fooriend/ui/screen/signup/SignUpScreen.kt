package com.team13.fooriend.ui.screen.signup

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(navController: NavController){
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }
    val (passwordCheck, passwordCheckValue) = remember { mutableStateOf("") }
    val (nickname, nicknameValue) = remember { mutableStateOf("") }
    val (phoneNumber, phoneNumberValue) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Sign Up Page")
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = id, onValueChange = idValue)
        TextField(value = password, onValueChange = passwordValue)
        TextField(value = passwordCheck, onValueChange = passwordCheckValue)
        TextField(value = nickname, onValueChange = nicknameValue)
        TextField(value = phoneNumber, onValueChange = phoneNumberValue)
        Button(onClick = {
            navController.navigate("login")
        }) {
            Text("Sign Up")
        }
    }
}
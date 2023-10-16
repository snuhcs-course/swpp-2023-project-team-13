package com.team13.fooriend.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.team13.fooriend.ui.component.ScreenChangeBar


@Composable
fun HomeScreen(navController: NavController, nickname : String){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Home Screen")
        Spacer(modifier = Modifier.height(20.dp))
        Text(nickname)
        Button(onClick = {
            navController.navigate("login")
        }) {
            Text("Log Out")
        }
    }
    ScreenChangeBar(navController)

}
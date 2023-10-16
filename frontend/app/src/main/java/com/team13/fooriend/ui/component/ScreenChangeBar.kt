package com.team13.fooriend.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ScreenChangeBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.weight(1f)
        ) {
            Text("Home")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { navController.navigate("social") },
            modifier = Modifier.weight(1f)
        ) {
            Text("Social")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { navController.navigate("myPage") },
            modifier = Modifier.weight(1f)
        ) {
            Text("My Page")
        }
    }
}

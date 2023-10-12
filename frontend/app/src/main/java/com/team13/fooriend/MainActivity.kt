package com.team13.fooriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team13.fooriend.ui.theme.FooriendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                composable("login") {
                    LogInPage(navController)
                }
                composable("signup") {
                    SignUpPage(navController)
                }
                composable("home/{nickname}") {
                        backStackEntry ->
                    HomePage(
                        navController = navController,
                        nickname = backStackEntry.arguments?.getString("nickname") ?: "",
                    )
                }
                composable("home") {
                    HomePage(navController, "admin")
                }
                composable("search") {
                    SearchPage(navController)
                }
                composable("my") {
                    MyPage(navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInPage(navController: NavController){
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Log In Page")
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = id, onValueChange = idValue)
        TextField(value = password, onValueChange = passwordValue)
        Button(onClick = {
            if(id.isNotEmpty() && password.isNotEmpty()){
                navController.navigate("home/$id")
            }
        }) {
            Text("Log In")
        }
        Button(onClick = {
            navController.navigate("signup")
        }) {
            Text("Sign Up")
        }
    }
}

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

@Composable
fun HomePage(navController: NavController, nickname : String){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Home Page")
        Spacer(modifier = Modifier.height(20.dp))
        Text(nickname)
        Button(onClick = {
            navController.navigate("login")
        }) {
            Text("Log Out")
        }
    }
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ){
        Button(onClick = {
            navController.navigate("home")
        }) {
            Text("Home")
        }
        Button(onClick = {
            navController.navigate("search")
        }) {
            Text("Search")
        }
        Button(onClick = {
            navController.navigate("my")
        }) {
            Text("My")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(navController: NavController){
    val (search, searchValue) = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = search, onValueChange = searchValue)
    }
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ){
        Button(onClick = {
            navController.navigate("home")
        }) {
            Text("Home")
        }
        Button(onClick = {
            navController.navigate("search")
        }) {
            Text("Search")
        }
        Button(onClick = {
            navController.navigate("my")
        }) {
            Text("My")
        }
    }
}

@Composable
fun MyPage(navController: NavController){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ){
        Button(onClick = {
            navController.navigate("home")
        }) {
            Text("Home")
        }
        Button(onClick = {
            navController.navigate("search")
        }) {
            Text("Search")
        }
        Button(onClick = {
            navController.navigate("my")
        }) {
            Text("My")
        }
    }
}
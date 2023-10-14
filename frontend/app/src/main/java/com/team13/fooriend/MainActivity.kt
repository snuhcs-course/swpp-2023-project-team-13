package com.team13.fooriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.engage.common.datamodel.Image
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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 섹션
        ProfileSection()

        Spacer(modifier = Modifier.height(16.dp))


        // 중앙의 음식 리스트
        MyPage_foodpicList()

        Spacer(modifier = Modifier.weight(1f))

        // 하단 버튼 섹션(이걸 나중에 component로 빼기)
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                onClick = { navController.navigate("search") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { navController.navigate("my") },
                modifier = Modifier.weight(1f)
            ) {
                Text("My")
            }
        }

    }
}

@Composable
fun ProfileSection() {
    val userProfileImageUrl = painterResource(id = R.drawable.profile_cat)
    val username = "ozeeeno"
    val followersCount = 100
    val followingCount = 50

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Image(
                painter = userProfileImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = username,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$followersCount Followers", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "$followingCount Following", color = MaterialTheme.colorScheme.primary)
        }
    }
}


@Composable
fun MyPage_foodpicList() {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(2) { rowIndex ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { columnIndex ->
                    val index = rowIndex * 3 + columnIndex
                    if (index < 15) {
                        Mypage_ImageListItem(index)
                    } else {
                        Spacer(modifier = Modifier.size(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Mypage_ImageListItem(index: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.hamburger),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


    }
}


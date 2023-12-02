package com.team13.fooriend.ui.screen.mypage

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.createRetrofit

@Composable
fun MyInformationScreen(
    context: Context,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
) {
    val retrofit = createRetrofit(context)
    val apiService = retrofit.create(ApiService::class.java)
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    LaunchedEffect(Unit){
        val user = apiService.getMyInfo()
        name = user.name
        username = user.username
        image = user.profileImage
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        ) {
        Column(
            modifier = Modifier
                .background(FooriendColor.FooriendGreen,)
                .fillMaxWidth()
                .fillMaxHeight(0.3f)

        ){
            IconButton(onClick = onBackClick, modifier = Modifier.padding(top = 7.dp).testTag("backButton")) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .background(Color.Transparent),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .background(FooriendColor.FooriendGreen)
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(170.dp)
                    .background(color = Color.Transparent)
                    .border(BorderStroke(2.dp, color = FooriendColor.FooriendGray), CircleShape)
                    .clip(CircleShape),
            ) {
                Image(
                    painter = rememberImagePainter(image),
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(170.dp)
                        .background(FooriendColor.FooriendLightGray, shape = RoundedCornerShape(15.dp))
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = name, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = username, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(35.dp))
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    FooriendColor.FooriendGreen,
                )) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "logout", modifier = Modifier.size(18.dp),)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "로그아웃")
            }
        }

    }
}

//@Composable
//@Preview(showSystemUi = true, showBackground = true)
//fun MyInformationScreenPreview() {
//    MyInformationScreen(
//        context = TODO(),
//        onBackClick = {},
//        onLogout = {}
//    )
//}

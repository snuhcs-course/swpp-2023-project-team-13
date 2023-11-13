package com.team13.fooriend.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.data.User
import com.team13.fooriend.ui.theme.BaseGray
import com.team13.fooriend.ui.theme.BaseGreen

@Composable
fun MyInformationScreen(
    onBackClick: () -> Unit,
    onChangePwd: () -> Unit,
) {
    // 실제로는 user data 저장한 내용에서 가져와야 함
    val user = User(
        id = 202114671,
        userID = "korean_penguin",
        name = "조용찬",
//        email = "jych1109@gmail.com",
        password = "******",
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

    ) {
        Column(
            modifier = Modifier
                .background(BaseGreen)
                .fillMaxWidth()
                .fillMaxHeight(0.3f)

        ){
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .background(BaseGreen, shape = RoundedCornerShape(bottomEnd = 80.dp, bottomStart = 80.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(50.dp))
                )
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(180.dp)
                        .background(BaseGray, shape = RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(50.dp))
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
            Text(text = user.name, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = user.userID, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(35.dp))
            Button(
                onClick = onChangePwd,
                colors = ButtonDefaults.buttonColors(
                    BaseGreen,//CMidGreen,
                )) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit", modifier = Modifier.size(18.dp),)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "비밀번호 변경하기")
            }
        }

    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun MyInformationScreenPreview() {
    MyInformationScreen(
        onBackClick = {},
        onChangePwd = {}
    )
}

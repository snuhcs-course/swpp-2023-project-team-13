package com.team13.fooriend.ui.screen.mypage

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.data.User

@Composable
fun MyInformationScreen(
    context: Context,
    onBackClick: () -> Unit,
    onChangePwd: () -> Unit,
) {
    // 실제로는 user data 저장한 내용에서 가져와야 함
    val user = User(
        id = 202114671,
        name = "조용찬",
        email = "jych1109@gmail.com",
        password = "******",
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Text(text = "Name : ${user.name}")
        Text(text = "ID : ${user.id}")
        Text(text = "Email : ${user.email}")
        Text(text = "Password : ${user.password}")
        Button(onClick = onChangePwd) {
            Text(text = "change pwd")
        }
    }


}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun MyInformationScreenPreview() {
    MyInformationScreen(
        context = TODO(),
        onBackClick = {},
        onChangePwd = {}
    )
}

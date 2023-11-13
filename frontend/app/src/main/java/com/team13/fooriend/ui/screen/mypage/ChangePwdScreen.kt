package com.team13.fooriend.ui.screen.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.theme.BaseGreen
import com.team13.fooriend.ui.theme.BaseGray

@Composable
fun ChangePwdScreen(
    onConfirmClick : () -> Unit = {}
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //textfield 로 바꾸기
        //현재 비번 맞는지 확인->맞아야 바뀌게
        //새 비번, 확인 필드 일치하는지 확인->맞아야 넘어가게
        Text(
            text = "현재 비밀번호 :",
            modifier = Modifier.align(Alignment.Start)
        )
        Text(text = "새 비밀번호 :", modifier = Modifier.align(Alignment.Start))
        Text(text = "비밀번호 확인 :", modifier = Modifier.align(Alignment.Start))
        Button(
            onClick = { onConfirmClick() },
            colors = ButtonDefaults.buttonColors(
                BaseGreen,//CMidGreen,
            )) {
            Text("Confirm")
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ChangePwdScreenPreview(){
    ChangePwdScreen(
        onConfirmClick = {}
    )
}
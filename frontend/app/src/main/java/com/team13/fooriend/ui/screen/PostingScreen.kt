package com.team13.fooriend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PostingScreen(
    onCloseClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
){
    val (content, contentValue) = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        // 나가기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ){
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
        // 음식점 이름
        Text(text = "용찬 반점")
        // 리뷰 작성 textfield (최대 140자)
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            value = content,
            onValueChange = contentValue
        )
        // 사진 등록 (최대 ?장)
        Text(text = "사진 등록")
        OutlinedButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = "사진 등록"
            )
        }
        // 영수증 사진 첨부
        Text(text = "영수증 사진 첨부")
        OutlinedButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = "영수증 등록"
            )
        }
        // 리뷰 등록
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onPostClick() }, modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)){
            Text(text = "리뷰 등록")
        }

    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PostingScreenPreview(){
    PostingScreen()
}
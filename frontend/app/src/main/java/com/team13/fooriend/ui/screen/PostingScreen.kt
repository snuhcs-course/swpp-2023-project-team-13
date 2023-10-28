package com.team13.fooriend.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PostingScreen(
    onCloseClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
){
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
        Text(text = "음식점 이름")
        // 리뷰 작성 textfield (최대 140자)
        Text(text = "리뷰 작성")
        // 사진 등록 (최대 ?장)
        Text(text = "사진 등록")
        // 영수증 사진 첨부
        Text(text = "영수증 사진 첨부")
        // 리뷰 등록
        Button(onClick = onPostClick) {
            Text(text = "리뷰 등록")
        }

    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PostingScreenPreview(){
    PostingScreen()
}
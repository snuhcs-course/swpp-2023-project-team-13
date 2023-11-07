package com.team13.fooriend.ui.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.team13.fooriend.data.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCard(
    uri: Uri, // 스마트폰 갤러리에 있는 사진들은 uri format으로 저장되어 있다
    onClick: () -> Unit = {  },
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
    ){
        Box(
            modifier = Modifier.height(150.dp),
        ){
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}
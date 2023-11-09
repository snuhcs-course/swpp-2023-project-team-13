package com.team13.fooriend.ui.screen

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.team13.fooriend.ui.component.ImageCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingScreen(
    restaurantPlaceId: String = "",
    onCloseClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
){

    val state = rememberScrollState()
    val (content, contentValue) = remember { mutableStateOf("") }
    var selectImages by remember { mutableStateOf(listOf<Uri>()) }
    var selectReceipt by remember { mutableStateOf(listOf<Uri>()) }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()){
            selectImages = it
        }
    val receiptLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()){
            selectReceipt = it
        }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(state)
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
            onClick = { galleryLauncher.launch("image/*") },
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(text = "Pick Image From Gallery")
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray)){
            LazyVerticalGrid(columns = GridCells.Fixed(3)){
                items(
                    items = selectImages,
                ){
                    ImageCard(uri = it, onClick = {/*TODO*/ })
                }
            }
        }
        // 영수증 사진 첨부
        Text(text = "영수증 사진 첨부")
        OutlinedButton(
            onClick = { receiptLauncher.launch("image/*") },
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(text = "Pick Receipt From Gallery")
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray)){
            LazyVerticalGrid(columns = GridCells.Fixed(3)){
                items(
                    items = selectReceipt,
                ){
                    ImageCard(uri = it, onClick = {/*TODO*/ })
                }
            }
        }
        // 리뷰 등록
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onPostClick() }, modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)){
            Text(text = "리뷰 등록")
        }

    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PostingScreenPreview(){
    PostingScreen()
}
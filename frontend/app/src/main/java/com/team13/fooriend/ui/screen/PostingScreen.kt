package com.team13.fooriend.ui.screen

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import coil.compose.rememberImagePainter
import com.team13.fooriend.ui.component.ImageCard
import com.team13.fooriend.ui.theme.FooriendColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingScreen(
    restaurantPlaceId : String,
    restaurantName: String,
    onCloseClick: () -> Unit = {},
    onPostClick: (String, String, List<Uri>, Uri?) -> Unit = { _, _, _, _ -> }
){
    val state = rememberScrollState()
    val context = LocalContext.current

    var selectedReviews by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val reviewphotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> selectedReviews = uris }
    )

    var selectedReceipt by remember {
        mutableStateOf<Uri?>(null)
    }

    val receiptphotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedReceipt = uri }
    )


    var contentState by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(state)
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close"
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Store",
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(
                    text = restaurantName,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            }
        }

        Text(
            text = "리뷰 작성",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(vertical = 8 .dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 130.dp),
            value = contentState,
            onValueChange = {
                if (it.text.length <= 200) {
                    contentState = it
                }
            }
        )

        Text(
            text = "${contentState.text.length}/200",
            color = if (contentState.text.length <= 200) Color.Gray else Color.Red,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Text(
            text = "사진 등록",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(110.dp)
                .clickable {
                    reviewphotoLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedReviews.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    for (imageUri in selectedReviews) {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxHeight()
                        ) {
                            Image(
                                painter = rememberImagePainter(imageUri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.Center
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "영수증 인증하기",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(110.dp)
                .clickable {
                    receiptphotoLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedReceipt == null) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxHeight()
                        ) {
                            Image(
                                painter = rememberImagePainter(selectedReceipt),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.Center
                            )
                        }

                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
                onPostClick(
                    restaurantPlaceId,
                    contentState.text,
                    selectedReviews,
                    selectedReceipt
                )




                         }, modifier = Modifier
            .height(50.dp)
            .width(200.dp)
            .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = FooriendColor.FooriendGreen,
                contentColor = Color.White
            )
            ){
            Text(text = "리뷰 등록")
        }

    }
}


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PostingScreenPreview(){
    PostingScreen(restaurantName = "Sample Restaurant", restaurantPlaceId = "ChIJN1t_tDeuEmsRUsoyG83frY4")
}
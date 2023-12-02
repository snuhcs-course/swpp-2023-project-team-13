package com.team13.fooriend.ui.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.team13.fooriend.ui.FooriendIcon
import com.team13.fooriend.ui.fooriendicon.Fooriendicon
import com.team13.fooriend.ui.screen.home.PlacesApiService
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.RestaurantInfo
import com.team13.fooriend.ui.util.ReviewPostBody
import com.team13.fooriend.ui.util.createRetrofit
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingScreen(
    context: Context,
    restaurantPlaceId: String = "",
    restaurantName: String = "",
    onCloseClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
){

    val state = rememberScrollState()
    val (content, contentValue) = remember { mutableStateOf("") }
    var selectImages by remember { mutableStateOf(listOf<Uri>()) }
    var selectReceipt by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { uris -> selectImages = uris }
        )
    val receiptLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectReceipt = uri }
        )

    val retrofit = createRetrofit(context)
    val apiService = retrofit.create(ApiService::class.java)

    val retrofit2 = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val placesApi = retrofit2.create(PlacesApiService::class.java)

    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    var contentState by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .verticalScroll(state)
            .testTag("postingScreen")
    ){
        // 나가기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCloseClick, modifier = Modifier.testTag("backButton")) {
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
                    imageVector = FooriendIcon.Fooriendicon,
                    contentDescription = "Store",
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 8.dp)
                )
                if (restaurantName != null) {
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
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ){
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
                    .heightIn(min = 130.dp)
                    .testTag("writeReviewField"),
                value = contentState,
                onValueChange = {
                    if (it.text.length <= 200) {
                        contentState = it
                    }
                },
                placeholder = {
                    Text(
                        text = "음식점 리뷰를 작성하면 리뷰의 긍정/부정을 AI가 자동으로 분류하여 등록합니다. \n * 보다 정확한 분류를 위해 리뷰 내용은 최소 20자 이상 작성해주시기 바랍니다 *",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
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

            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .background(Color.Gray)
                            .clickable {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("imageButton"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    for (imageUri in selectImages) {
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


            // 영수증 사진 첨부
            Text(
                text = "영수증을 등록하여 리뷰 인증",
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

            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .background(Color.Gray)
                            .clickable {
                                receiptLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("receiptButton"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = rememberImagePainter(selectReceipt),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            alignment = Alignment.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(onClick = {
                Log.d("PostingScreen", "restaurantPlaceId: $restaurantPlaceId")
                if(contentState.text.isEmpty()){
                    Toast.makeText(context, "리뷰 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                } else if(selectImages.size == 0){
                    Toast.makeText(context, "리뷰 사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isLoading = true
                coroutineScope.launch {
                    Log.d("PostingScreen", "restaurantPlaceId: $restaurantPlaceId")
                    val response = placesApi.getPlaceDetails(placeId = restaurantPlaceId, apiKey = "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0")
                    Log.d("PostingScreen", "restaurant: $response")
                    val restaurant = RestaurantInfo(
                        googleMapPlaceId = restaurantPlaceId,
                        name = response.result.name,
                        latitude = response.result.geometry.location["lat"]!!,
                        longitude = response.result.geometry.location["lng"]!!
                    )
                    var imageIds = mutableListOf<Int>()
                    for(uri in selectImages){
                        val inputStream = context.contentResolver.openInputStream(uri)
                        inputStream?.let { stream ->
                            val requestBody = stream.readBytes().toRequestBody(MultipartBody.FORM)
                            val multipartBody = MultipartBody.Part.createFormData("file", "filename.jpg", requestBody)

                            // API 호출
                            val response = apiService.uploadImage(multipartBody)
                            imageIds.add(response.id)
                        }
//                     val file = File(uri.path)
//                     Log.d("PostingScreen", "file: $file")
//                     val requestbody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//                     val body = MultipartBody.Part.createFormData("file",file.name, requestbody)
//                     val imageresponse = apiService.uploadImage(body)

                    }
                    Log.d("PostingScreen", "imageIds: $imageIds")
                    val receiptImageIds = mutableListOf<Int>()
                    val inputStream = selectReceipt?.let { context.contentResolver.openInputStream(it) }
                    inputStream?.let { stream ->
                        val requestBody = stream.readBytes().toRequestBody(MultipartBody.FORM)
                        val multipartBody = MultipartBody.Part.createFormData("file", "filename.jpg", requestBody)

                        // API 호출
                        val response = apiService.uploadImage(multipartBody)
                        receiptImageIds.add(response.id)
                    }
                    Log.d("PostingScreen", "receiptImageIds: $receiptImageIds")
                    var receiptImageId = 0
                    if(receiptImageIds.size > 0) {
                        receiptImageId = receiptImageIds[0]
                    }

                    apiService.postReview(
                        ReviewPostBody(
                            content = contentState.text,
                            imageIds = imageIds,
                            receiptImageId = receiptImageId,
                            restaurant = restaurant
                        )
                    )
                    Toast.makeText(context, "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    onPostClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .testTag("postingButton"),
                colors = ButtonDefaults.buttonColors(containerColor = FooriendColor.FooriendGreen, contentColor = Color.White)){
                Text(text = "리뷰 등록")
            }
        }



    }
    if (isLoading) {
        LoadingScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PostingScreenPreview(){
//    PostingScreen()
}
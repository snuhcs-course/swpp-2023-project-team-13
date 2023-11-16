package com.team13.fooriend.ui.screen

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOfhttps://github.com/snuhcs-course/swpp-2023-project-team-13/pull/19/conflict?name=frontend%252Fapp%252Fsrc%252Fmain%252Fjava%252Fcom%252Fteam13%252Ffooriend%252Fui%252Fscreen%252FPostingScreen.kt&ancestor_oid=b6e8a2d814eef2bfc744b665c6c1e7f72740cd28&base_oid=3b9b8c0399bc80db27f42dff5cfebbae2dc417b0&head_oid=82240c1de2b395d2b9c79700df60b0c3b40ffcb5
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.net.toFile
import com.team13.fooriend.ui.component.ImageCard
import com.team13.fooriend.ui.screen.home.PlacesApiService
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.RestaurantInfo
import com.team13.fooriend.ui.util.ReviewPostBody
import com.team13.fooriend.ui.util.createRetrofit
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingScreen(
    context: Context,
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

    val retrofit = createRetrofit(context)
    val apiService = retrofit.create(ApiService::class.java)

    val retrofit2 = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val placesApi = retrofit2.create(PlacesApiService::class.java)

    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

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
        Text(text = "리뷰 작성")
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
        Button(onClick = {
            Log.d("PostingScreen", "restaurantPlaceId: $restaurantPlaceId")
            if(content == ""){
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
                for(uri in selectReceipt){
                    val inputStream = context.contentResolver.openInputStream(uri)
                    inputStream?.let { stream ->
                        val requestBody = stream.readBytes().toRequestBody(MultipartBody.FORM)
                        val multipartBody = MultipartBody.Part.createFormData("file", "filename.jpg", requestBody)

                        // API 호출
                        val response = apiService.uploadImage(multipartBody)
                        imageIds.add(response.id)
                    }
                }
                Log.d("PostingScreen", "receiptImageIds: $receiptImageIds")
                var receiptImageId = 0
                if(receiptImageIds.size > 0) {
                    receiptImageId = receiptImageIds[0]
                }

                 apiService.postReview(
                     ReviewPostBody(
                         content = content,
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
            .align(Alignment.CenterHorizontally)){
            Text(text = "리뷰 등록")
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